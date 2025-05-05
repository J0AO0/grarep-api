package com.grarep.grarep.resource;

import com.grarep.grarep.domain.Pedido;
import com.grarep.grarep.domain.dto.PedidoDTO;
import com.grarep.grarep.domain.dto.PedidoNewDTO;
import com.grarep.grarep.domain.dto.flat.PedidoFlat;
import com.grarep.grarep.repository.PedidoRepository;
import com.grarep.grarep.security.resource.CheckSecurity;
import com.grarep.grarep.service.PedidoService;
import com.grarep.grarep.service.exception.EntidadeNaoEncontradaExcepition;
import com.grarep.grarep.service.rels.RelatorioPedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoRepository pedRepo;

    @Autowired
    private RelatorioPedidoService relRepo;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PedidoFlat>> lista(Pageable pageable) {
        Page<Pedido> pedidos = pedidoService.findAll(pageable);
        Page<PedidoFlat> pedidosFlat = pedidoService.mudarProdutoParaFlat(pedidos);
        pedidosFlat.forEach(pedidoFlat ->
                System.out.println("Enviando PedidoFlat ID: " + pedidoFlat.getId() +
                        ", nf=" + pedidoFlat.getNf() +
                        ", datagravacao=" + pedidoFlat.getDatagravacao() +
                        ", emailusuario=" + pedidoFlat.getEmailusuario()));
        return ResponseEntity.ok(pedidosFlat.getContent());
    }

    @CheckSecurity.Pedido.PodeConsultar
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            Pedido obj = pedidoService.buscarOuFalhar(id);
            PedidoDTO dto = modelMapper.map(obj, PedidoDTO.class);
            return ResponseEntity.ok(dto);
        } catch (EntidadeNaoEncontradaExcepition e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado: " + e.getMessage());
        }
    }

    @CheckSecurity.Pedido.PodeCadastrar
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Pedido> criarPedido(@RequestBody PedidoNewDTO objNewDTO) {
        Pedido novoObj = modelMapper.map(objNewDTO, Pedido.class);
        Pedido objNovo = pedidoService.insert(objNewDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(objNovo.getId()).toUri();
        return ResponseEntity.created(uri).body(novoObj);
    }

    @CheckSecurity.Pedido.PodeAtualizar
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Pedido> update(@Valid @RequestBody PedidoDTO obj, @PathVariable Integer id) {
        obj.setId(id);
        Pedido obj1 = pedidoService.atualiza(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj1.getId()).toUri();
        return ResponseEntity.created(uri).body(obj1);
    }

    @CheckSecurity.Pedido.PodeExcluir
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Pedido.PodeConsultar
    @RequestMapping(value = "/relatorios/mensalUsuario", method = RequestMethod.GET)
    public ResponseEntity<byte[]> mensalUsuario(
            @RequestParam(required = false) String mes,
            @RequestParam(required = false) String ano) {
        try {
            // Log dos parâmetros recebidos
            System.out.println("Parâmetros recebidos (String): mes=" + mes + ", ano=" + ano);

            // Validação de parâmetros
            if (mes == null || mes.trim().isEmpty() || ano == null || ano.trim().isEmpty()) {
                System.out.println("Erro: Parâmetros mes ou ano não fornecidos ou vazios");
                return ResponseEntity.badRequest().body("Mês e ano são obrigatórios".getBytes());
            }

            // Conversão para Integer
            Integer mesInt;
            Integer anoInt;
            try {
                mesInt = Integer.parseInt(mes.trim());
                anoInt = Integer.parseInt(ano.trim());
            } catch (NumberFormatException e) {
                System.out.println("Erro: Parâmetros não são números válidos - mes=" + mes + ", ano=" + ano);
                return ResponseEntity.badRequest().body("Mês e ano devem ser números válidos".getBytes());
            }

            // Log dos parâmetros convertidos
            System.out.println("Parâmetros convertidos (Integer): mes=" + mesInt + ", ano=" + anoInt);

            // Validação de intervalo
            if (mesInt < 1 || mesInt > 12 || anoInt < 2000) {
                System.out.println("Validação falhou: mês=" + mesInt + ", ano=" + anoInt);
                return ResponseEntity.badRequest().body("Mês (1-12) ou ano (>=2000) inválidos".getBytes());
            }

            // Gerar relatório
            byte[] relatorio = relRepo.pedidosPorRepresentante(anoInt, mesInt);
            System.out.println("Relatório gerado com sucesso, tamanho: " + relatorio.length + " bytes");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_pedidos.pdf")
                    .body(relatorio);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar relatório: " + e.getMessage()).getBytes());
        }
    }
}