package com.grarep.grarep.repository.query;


import com.grarep.grarep.domain.Empresa;
import com.grarep.grarep.repository.filter.EmpresaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpresaRepositoryQuery {
    public Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable);
}
