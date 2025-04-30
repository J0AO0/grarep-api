package com.grarep.grarep.domain.dto.flat;

import com.grarep.grarep.domain.Produto;

import java.time.OffsetDateTime;

public class ProdutoFlat {
    private Integer id;
    private String nome;
    private String sku;
    private OffsetDateTime datagravacao;
    private String emailusuario;

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

    public OffsetDateTime getDatagravacao() {
        return datagravacao;
    }

    public void setDatagravacao(OffsetDateTime datagravacao) {
        this.datagravacao = datagravacao;
    }

    public String getEmailusuario() {
        return emailusuario;
    }

    public void setEmailusuario(String emailusuario) {
        this.emailusuario = emailusuario;
    }

    public ProdutoFlat(Integer id, String nome, String sku, OffsetDateTime datagravacao, String emailusuario) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.datagravacao = datagravacao;
        this.emailusuario = emailusuario;
    }

    public ProdutoFlat() {
    }

    public ProdutoFlat(Produto prod, String ok){
        this.id = prod.getId();
        this.nome = prod.getNome();
        this.datagravacao = prod.getLogs().getDatagravacao();
        this.emailusuario = prod.getLogs().getEmailUsuario();
        this.sku = prod.getSku();
    }

    public ProdutoFlat(Produto prod){
        this.id = prod.getId();
        this.nome = prod.getNome();
        this.datagravacao = prod.getLogs().getDatagravacao();
        this.emailusuario = prod.getLogs().getEmailUsuario();
        this.sku = prod.getSku();
    }
}
