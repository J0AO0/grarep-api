package com.grarep.grarep.domain.dto;

import java.io.Serializable;
import java.util.Set;

public class PedidoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String representante;
    private String arquiteto;
    private Integer nf;
    private Set<ProdutoPedidoDTO> produtos;

    public PedidoDTO() {
    }

    public PedidoDTO(Integer id, String representante, String arquiteto, Integer nf, Set<ProdutoPedidoDTO> produtos) {
        this.id = id;
        this.representante = representante;
        this.arquiteto = arquiteto;
        this.nf = nf;
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

    public Set<ProdutoPedidoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<ProdutoPedidoDTO> produtos) {
        this.produtos = produtos;
    }
}