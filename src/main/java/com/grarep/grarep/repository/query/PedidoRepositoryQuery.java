package com.grarep.grarep.repository.query;


import com.grarep.grarep.domain.Pedido;
import com.grarep.grarep.repository.filter.PedidoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoRepositoryQuery {
    public Page<Pedido> filtrar(PedidoFilter pedidoFilter, Pageable pageable);
}
