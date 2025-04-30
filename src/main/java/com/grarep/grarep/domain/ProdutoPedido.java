package com.grarep.grarep.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ProdutoPedido implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProdutoPedidoPK id = new ProdutoPedidoPK(); // Chave composta

    private BigDecimal quantidade;

    // Getters e Setters
    public ProdutoPedidoPK getId() {
        return id;
    }

    public void setId(ProdutoPedidoPK id) {
        this.id = id;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutoPedido(ProdutoPedidoPK id, BigDecimal quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

    public ProdutoPedido() {
    }
}