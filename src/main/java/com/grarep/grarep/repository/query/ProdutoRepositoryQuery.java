package com.grarep.grarep.repository.query;

import com.grarep.grarep.domain.Produto;
import com.grarep.grarep.repository.filter.ProdutoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoRepositoryQuery {
	public Page<Produto> filtrar(ProdutoFilter produtoFilter, Pageable pageable);
}
