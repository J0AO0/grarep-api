package com.grarep.grarep.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "estoque")
@IdClass(Estoque.EstoqueId.class)
public class Estoque {

    @Id
    @Column(name = "nf")
    private String nf;

    @Id
    @Column(name = "produto")
    private String produto; // SKU do produto

    @Column(name = "quantidade")
    private BigDecimal quantidade;

    // Classe interna para a chave composta
    public static class EstoqueId implements Serializable {
        private String nf;
        private String produto;

        public EstoqueId() {
        }

        public EstoqueId(String nf, String produto) {
            this.nf = nf;
            this.produto = produto;
        }

        public String getNf() {
            return nf;
        }

        public void setNf(String nf) {
            this.nf = nf;
        }

        public String getProduto() {
            return produto;
        }

        public void setProduto(String produto) {
            this.produto = produto;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EstoqueId estoqueId = (EstoqueId) o;
            return Objects.equals(nf, estoqueId.nf) && Objects.equals(produto, estoqueId.produto);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nf, produto);
        }
    }

    // Construtores
    public Estoque() {
    }

    public Estoque(String nf, String produto, BigDecimal quantidade) {
        this.nf = nf;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    // Getters e Setters
    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }
}