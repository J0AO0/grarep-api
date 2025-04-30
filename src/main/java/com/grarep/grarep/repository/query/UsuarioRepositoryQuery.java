package com.grarep.grarep.repository.query;


import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.repository.filter.UsuarioFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioRepositoryQuery {
    public Page<Usuario> filtrar(UsuarioFilter usuarioFilter, Pageable pageable);
}
