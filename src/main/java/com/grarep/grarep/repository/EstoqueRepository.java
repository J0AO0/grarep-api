package com.grarep.grarep.repository;

import com.grarep.grarep.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Estoque.EstoqueId> {

    Optional<Estoque> findByNfAndProduto(String nf, String produto);
}