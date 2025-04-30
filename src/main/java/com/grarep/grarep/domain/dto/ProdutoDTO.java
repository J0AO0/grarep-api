package com.grarep.grarep.domain.dto;

import com.grarep.grarep.validation.produto.ProdutoInsert;
import com.grarep.grarep.validation.produto.ProdutoUpdate;

@ProdutoUpdate
public class ProdutoDTO {
    private Integer id;
    private String nome;
    private String sku;

    public ProdutoDTO() {

    }

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

    public ProdutoDTO(Integer id, String nome, String sku) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
    }

    public ProdutoDTO(Integer id) {
        this.id = id;
    }
}
