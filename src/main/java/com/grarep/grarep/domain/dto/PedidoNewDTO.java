package com.grarep.grarep.domain.dto;

import java.util.List;

public class PedidoNewDTO {
    private Integer id;
    private String representante;
    private String arquiteto;
    private Integer nf;
    private List<ProdutoPedidoDTO> produtos;

    public List<ProdutoPedidoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoPedidoDTO> produtos) {
        this.produtos = produtos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getArquiteto() {
        return arquiteto;
    }

    public void setArquiteto(String arquiteto) {
        this.arquiteto = arquiteto;
    }

    public Integer getNf() {
        return nf;
    }

    public void setNf(Integer nf) {
        this.nf = nf;
    }

    public PedidoNewDTO(Integer id, String representante, String arquiteto, Integer nf) {
        this.id = id;
        this.representante = representante;
        this.arquiteto = arquiteto;
        this.nf = nf;
    }

    public PedidoNewDTO() {
    }
}