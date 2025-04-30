package com.grarep.grarep.service;

import com.grarep.grarep.domain.Estoque;
import com.grarep.grarep.repository.EstoqueRepository;
import com.grarep.grarep.service.exception.EstoqueInsuficienteException;
import com.grarep.grarep.service.exception.EntidadeNaoEncontradaExcepition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Transactional
    public void subtrairEstoque(String nf, String produtoSku, BigDecimal quantidade) {
        Estoque estoque = estoqueRepository.findByNfAndProduto(nf, produtoSku)
                .orElseThrow(() -> new EntidadeNaoEncontradaExcepition(
                        String.format("Estoque nao encontrado para NF %s e produto %s", nf, produtoSku)));
        if (estoque.getQuantidade().compareTo(quantidade) < 0) {
            throw new EstoqueInsuficienteException(
                    String.format("Quantidade insuficiente no estoque para NF %s e produto %s", nf, produtoSku));
        }
        estoque.setQuantidade(estoque.getQuantidade().subtract(quantidade));
        estoqueRepository.save(estoque);
    }
}