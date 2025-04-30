package com.grarep.grarep.repository;


import com.grarep.grarep.domain.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Integer> {

    @Modifying
    @Transactional
    @Query(value= "DELETE FROM usuario_permissao WHERE id_usuario = ?", nativeQuery = true)
    void deletaPorUsuario(int idusuario);


}
