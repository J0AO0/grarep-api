package com.grarep.grarep.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.grarep.grarep.domain.LogSistema;
import com.grarep.grarep.domain.Produto;
import com.grarep.grarep.domain.Tenant;
import com.grarep.grarep.domain.dto.ProdutoDTO;
import com.grarep.grarep.domain.dto.ProdutoNewDTO;
import com.grarep.grarep.domain.dto.flat.ProdutoFlat;
import com.grarep.grarep.repository.LogSistemaRepository;
import com.grarep.grarep.repository.ProdutoRepository;
import com.grarep.grarep.service.exception.EntidadeEmUsoException;
import com.grarep.grarep.service.exception.EntidadeNaoEncontradaExcepition;
import com.grarep.grarep.service.util.Tenantuser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repo;

    @Autowired
    private Tenantuser tenantUsuario;

    @Autowired
    private LogSistemaRepository repolog;

    @Autowired
    private LogSistemaService log;


    public Page<Produto> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public Produto findPorId(Integer id) {
        Produto cat = repo.findPorId(id);
        return cat;
    }

    @Transactional
    public Produto insert(ProdutoNewDTO obj){
        obj.setId(null);
        Produto resEst = new Produto();
        resEst.setNome(obj.getNome());
        resEst.setSku(obj.getSku());

        // Recuperando o Tenant do usuário logado
        Tenant tenant = tenantUsuario.buscarOuFalhar();
        resEst.setTenant(tenant); // Associando ao Produto antes de salvar

        repo.save(resEst); // Agora salvando corretamente com o Tenant associado
        logProduto(resEst, "Insert");

        return resEst;
    }

    public Page<Produto> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return repo.findAll(pageRequest);
    }

    public Produto atualiza(Produto obj) {
        Produto resEst =  repo.findPorId(obj.getId());
        resEst.setTenant(tenantUsuario.buscarOuFalhar());
        BeanUtils.copyProperties(obj, resEst, "id");
        resEst.setTenant(tenantUsuario.buscarOuFalhar());
        repo.save(resEst);
        logProduto(resEst, "Update");
        return resEst;
    }

    public void delete (Integer id) {
        try {
            if(!repo.existsById(id)) {
                throw new EntidadeNaoEncontradaExcepition(String.format("Produto não encontrado", id));
            }
            repo.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format("Produto não pode ser deletado", id));
        }
    }

    public List<ProdutoFlat> findAllSql() {
        List<Produto> operadores = repo.findAllSql(tenantUsuario.buscarOuFalharInt());
        List<ProdutoFlat> operadorFlat = new ArrayList<>();
        for (Produto obj : operadores) {
            ProdutoFlat opeFlat = new ProdutoFlat(obj);
            operadorFlat.add(opeFlat);
        }
        return operadorFlat;
    }

    public Produto buscarOuFalhar(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaExcepition(String.format("Produto  não encontrado", id)));
    }


    public Page<ProdutoFlat> mudarProdutoParaFlat(Page<Produto> pacs) {
        List<ProdutoFlat> cFlats = new ArrayList<ProdutoFlat>();
        for (Produto p : pacs.getContent()) {
            ProdutoFlat cFlat = new ProdutoFlat(p);
            cFlats.add(cFlat);

        }
        Page<ProdutoFlat> page = new PageImpl<>(cFlats, pacs.getPageable(),
                pacs.getTotalElements());

        return page;
    }

    public ProdutoDTO getProdutoById(Integer id) {
        // Busca o produto no repositório
        Produto produto = repo.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaExcepition("Produto não encontrado"));
        // Cria o ProdutoDTO e preenche os dados do produto
        ProdutoDTO produtoDto = new ProdutoDTO();
        produtoDto.setId(produto.getId());
        produtoDto.setNome(produto.getNome());
        produtoDto.setSku(produto.getSku());
        return produtoDto;
    }

    private void logProduto(Produto obj, String string) {
        LogSistema logsistema = log.insert(obj, string);
        logsistema.setProduto(obj);
        repolog.save(logsistema);

    }
}

