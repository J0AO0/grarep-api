package com.grarep.grarep.repository;

import com.grarep.grarep.domain.ProdutoPedido;
import com.grarep.grarep.domain.UsuarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido, Integer> {
}
