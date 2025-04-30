package com.grarep.grarep.domain.dto.rel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class PedidoRelatorioDTO {
    private String representante;
    private Integer pedidoId;
    private Integer nf;
    private String arquiteto;
    private BigDecimal quantidade;
    private String nomeProduto;
    private String skuProduto;
    private Timestamp dataGravacao;

    // Construtores
    public PedidoRelatorioDTO() {}

    public PedidoRelatorioDTO(String representante, Integer pedidoId, Integer nf, String arquiteto,
                              BigDecimal quantidade, String nomeProduto, String skuProduto, Timestamp dataGravacao) {
        this.representante = representante;
        this.pedidoId = pedidoId;
        this.nf = nf;
        this.arquiteto = arquiteto;
        this.quantidade = quantidade;
        this.nomeProduto = nomeProduto;
        this.skuProduto = skuProduto;
        this.dataGravacao = dataGravacao;
    }

    // Getters e Setters
    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getNf() {
        return nf;
    }

    public void setNf(Integer nf) {
        this.nf = nf;
    }

    public String getArquiteto() {
        return arquiteto;
    }

    public void setArquiteto(String arquiteto) {
        this.arquiteto = arquiteto;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getSkuProduto() {
        return skuProduto;
    }

    public void setSkuProduto(String skuProduto) {
        this.skuProduto = skuProduto;
    }

    public Timestamp getDataGravacao() {
        return dataGravacao;
    }

    public void setDataGravacao(Timestamp dataGravacao) {
        this.dataGravacao = dataGravacao;
    }
}