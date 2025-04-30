package com.grarep.grarep.repository;

import com.grarep.grarep.domain.Classepermissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassepermissaoRepository extends JpaRepository<Classepermissao, Integer> {
    @Query(value= "select * from classepermissao ", nativeQuery = true)
    List<Classepermissao> findAllSql();
    @Query(value= "select * from classepermissao", nativeQuery = true)
    List<Classepermissao> findAllSqlInativo();

}
