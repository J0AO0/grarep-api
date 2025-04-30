package com.grarep.grarep.domain.dto;

import com.grarep.grarep.validation.produto.ProdutoInsert;
import com.grarep.grarep.validation.produto.ProdutoUpdate;

@ProdutoInsert
public class ProdutoNewDTO {
    private Integer id;
    private String nome;
    private String sku;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ProdutoNewDTO(Integer id, String nome, String sku) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
    }

    public ProdutoNewDTO() {
    }
}
