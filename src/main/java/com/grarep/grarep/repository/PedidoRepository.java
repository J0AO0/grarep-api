package com.grarep.grarep.repository;

import com.grarep.grarep.domain.Pedido;
import com.grarep.grarep.domain.dto.rel.PedidoRelatorioDTO;
import com.grarep.grarep.repository.query.PedidoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>, PedidoRepositoryQuery {
    @Query(value = "SELECT * FROM pedido", nativeQuery = true)
    List<Pedido> findAllCat();

    @Query(value = "SELECT * FROM pedido WHERE id = :id", nativeQuery = true)
    Pedido findPorId(@Param("id") Integer id);

    @Query(value = "SELECT " +
            "p.representante, " +
            "p.id AS pedidoId, " +
            "p.nf, " +
            "p.arquiteto, " +
            "pp.quantidade, " +
            "prod.nome AS nome_produto, " +
            "prod.sku AS sku_produto, " +
            "ls.datagravacao " +
            "FROM pedido p " +
            "LEFT JOIN produto_pedido pp ON p.id = pp.pedido_id " +
            "LEFT JOIN produto prod ON pp.produto_id = prod.id " +
            "LEFT JOIN log_sistema ls ON p.id = ls.pedido_id " +
            "WHERE EXTRACT(MONTH FROM ls.datagravacao) = :mes " +
            "AND EXTRACT(YEAR FROM ls.datagravacao) = :ano " +
            "ORDER BY p.representante, p.id, prod.nome", nativeQuery = true)
    List<PedidoRelatorioDTO> findByAnoMesForRelatorio(@Param("ano") Integer ano, @Param("mes") Integer mes);
}