package com.grarep.grarep.repository;



import com.grarep.grarep.domain.LogSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogSistemaRepository extends JpaRepository<LogSistema, Integer> {
	
}