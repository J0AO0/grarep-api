package com.grarep.grarep.service.exception;

public class FotoProdutoNaoEncontradaException extends EntidadeNaoEncontradaExcepition {

	private static final long serialVersionUID = 1L;

	public FotoProdutoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public FotoProdutoNaoEncontradaException( Integer produtoId) {
		this(String.format("Não existe um cadastro de foto do produto com código %d",
				produtoId));
	}

}