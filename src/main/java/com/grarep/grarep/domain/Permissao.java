package com.grarep.grarep.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permissao")
public class Permissao implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    @JsonIgnore
    @ManyToOne
    private Classepermissao classepermissao;

    public Classepermissao getClassepermissao() {
        return classepermissao;
    }
    public void setClassepermissao(Classepermissao classepermissao) {
        this.classepermissao = classepermissao;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Permissao(Integer id, String descricao, Classepermissao classepermissao) {
        this.id = id;
        this.descricao = descricao;
        this.classepermissao = classepermissao;
    }
    public Permissao() {
    }
    public Permissao(Integer id) {
        this.id = id;
    }
    public Permissao(int id2, String descricao2) {
        this.id = id2;
        this.descricao = descricao2;
    }
    @Override
    public String toString() {
        return "PermissaoRepository [id=" + id + ", descricao=" + descricao + ", classepermissao=" + classepermissao + "]";
    }


}
