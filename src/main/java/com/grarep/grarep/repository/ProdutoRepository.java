package com.grarep.grarep.repository;


import com.grarep.grarep.domain.Produto;
import com.grarep.grarep.repository.query.ProdutoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>, ProdutoRepositoryQuery {
    @Query(value = "SELECT * FROM produto", nativeQuery = true)
    List<Produto> findAllCat();

    @Query(value = "SELECT * FROM produto where id = ?", nativeQuery = true)
    Produto findPorId(Integer id);

    Produto findByNome(String name);

    Produto findBySku(String name);

    @Query(value = "select * from produto where status=1 and tenant_id = ?", nativeQuery = true)
    List<Produto> findAllSql(Integer buscarOuFalharInt);

}
