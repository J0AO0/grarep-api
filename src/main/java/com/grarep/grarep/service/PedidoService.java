package com.grarep.grarep.service;

import com.grarep.grarep.domain.*;
import com.grarep.grarep.domain.dto.PedidoDTO;
import com.grarep.grarep.domain.dto.PedidoNewDTO;
import com.grarep.grarep.domain.dto.ProdutoPedidoDTO;
import com.grarep.grarep.domain.dto.flat.PedidoFlat;
import com.grarep.grarep.repository.PedidoRepository;
import com.grarep.grarep.repository.ProdutoRepository;
import com.grarep.grarep.repository.LogSistemaRepository; // Adicione este repositório
import com.grarep.grarep.service.exception.EntidadeEmUsoException;
import com.grarep.grarep.service.exception.EntidadeNaoEncontradaExcepition;
import com.grarep.grarep.service.util.Tenantuser;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private Tenantuser tenantUser;

    @Autowired
    private ProdutoRepository produtoRepo;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private LogSistemaService logSistemaService; // Injetar LogSistemaService

    @Autowired
    private LogSistemaRepository logSistemaRepository; // Injetar LogSistemaRepository

    public Page<Pedido> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Pedido findPorId(Integer id) {
        Pedido cat = repo.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaExcepition("Pedido com ID " + id + " nao encontrado"));
        Hibernate.initialize(cat.getProdutos());
        Hibernate.initialize(cat.getLogs()); // Inicializar logs para evitar LazyInitializationException
        return cat;
    }

    @Transactional
    public Pedido insert(PedidoNewDTO obj) {
        obj.setId(null);
        Pedido novoPedido = new Pedido(obj);

        Tenant tenant = tenantUser.buscarOuFalhar();
        novoPedido.setTenant(tenant);

        Set<ProdutoPedido> itens = new HashSet<>();
        for (ProdutoPedidoDTO item : obj.getProdutos()) {
            Produto produto = produtoRepo.findById(item.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));

            ProdutoPedidoPK produtoPedidoPK = new ProdutoPedidoPK(produto, novoPedido);
            ProdutoPedido produtoPedido = new ProdutoPedido(produtoPedidoPK, item.getQuantidade());

            itens.add(produtoPedido);
        }

        novoPedido.setProdutos(itens);
        novoPedido = repo.save(novoPedido);

        // Criar e salvar log
        logPedido(novoPedido, "Insert");

        // Subtrair do estoque para cada produto no pedido
        String nfAsString = obj.getNf().toString();
        for (ProdutoPedidoDTO produto : obj.getProdutos()) {
            Produto prod = produtoRepo.findById(produto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
            String sku = prod.getSku();
            estoqueService.subtrairEstoque(nfAsString, sku, produto.getQuantidade());
        }

        return novoPedido;
    }

    @Transactional
    public Pedido atualiza(PedidoDTO obj) {
        try {
            Pedido resEst = repo.findById(obj.getId())
                    .orElseThrow(() -> new EntidadeNaoEncontradaExcepition("Pedido com ID " + obj.getId() + " nao encontrado"));
            BeanUtils.copyProperties(obj, resEst, "id");
            resEst = repo.save(resEst);
            // Criar e salvar log
            logPedido(resEst, "Update");
            return resEst;
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeNaoEncontradaExcepition("Pedido nao encontrado com ID " + obj.getId());
        }
    }

    @Transactional
    public void delete(Integer id) {
        try {
            Pedido pedido = repo.findById(id)
                    .orElseThrow(() -> new EntidadeNaoEncontradaExcepition("Pedido nao encontrada com ID " + id));
            // Criar e salvar log antes de deletar
            logPedido(pedido, "Delete");
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException("Pedido nao pode ser deletado, possui relacionamento com Cliente, ID " + id);
        }
    }

    public List<Pedido> lista() {
        List<Pedido> buscarTodas = repo.findAll();
        buscarTodas.forEach(pedido -> {
            Hibernate.initialize(pedido.getProdutos());
            Hibernate.initialize(pedido.getLogs()); // Inicializar logs
        });
        return buscarTodas;
    }

    public Pedido buscarOuFalhar(int id) {
        Pedido pedido = repo.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaExcepition("Pedido nao encontrado com ID " + id));
        Hibernate.initialize(pedido.getProdutos());
        Hibernate.initialize(pedido.getLogs()); // Inicializar logs
        return pedido;
    }

    public Page<PedidoFlat> mudarProdutoParaFlat(Page<Pedido> pacs) {
        List<PedidoFlat> cFlats = new ArrayList<>();
        for (Pedido p : pacs.getContent()) {
            Hibernate.initialize(p.getLogs()); // Garantir que logs sejam carregados
            PedidoFlat cFlat = new PedidoFlat(p);
            cFlats.add(cFlat);
        }
        return new PageImpl<>(cFlats, pacs.getPageable(), pacs.getTotalElements());
    }

    private void logPedido(Pedido pedido, String acao) {
        LogSistema logSistema = logSistemaService.insert(pedido, acao);
        logSistema.setPedido(pedido); // Associar o pedido ao log
        logSistemaRepository.save(logSistema); // Persistir o log
    }
}