package com.grarep.grarep.repository;

import com.grarep.grarep.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {

    @Query(value= "select * from tenant where id = ?", nativeQuery = true)
    Tenant findPorId(int codigoTenant);

    @Query(value= "select razao from empresa where tenant_id = ?", nativeQuery = true)
    String findEmpresaTenant(int codigoTenant);
    @Modifying
    @Transactional
    @Query(value= "select id from tenant where descricao = ?", nativeQuery = true)
    Integer buscarIdTenant(String descricao);



}
