package com.grarep.grarep.service;

import com.grarep.grarep.domain.ProdutoPedido;
import com.grarep.grarep.repository.ProdutoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoPedidoService {

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    public void salvarTodos(List<ProdutoPedido> produtos) {
        produtoPedidoRepository.saveAll(produtos);
    }
}
