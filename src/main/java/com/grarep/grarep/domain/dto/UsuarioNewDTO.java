package com.grarep.grarep.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grarep.grarep.domain.Permissao;
import com.grarep.grarep.validation.usuario.UsuarioInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@UsuarioInsert
public class UsuarioNewDTO implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    private Integer status;//enum ok
    private String nome;
    private String login;
    private String email;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_permissao", joinColumns = @JoinColumn(name = "id_usuario")
            , inverseJoinColumns = @JoinColumn(name = "id_permissao"))
    private List<Permissao> permissoes;
//    @JsonIgnore
//    @OneToMany(mappedBy = "usuario")
//    private List<LogSistema> logs = new ArrayList<LogSistema>();

    @JsonIgnore
    private String senha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Permissao> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioNewDTO(Integer id, Integer status, String nome, String login, String email, List<Permissao> permissoes,
                          String senha) {
        this.id = id;
        this.status = status;
        this.nome = nome;
        this.login = login;
        this.email = email;
        this.permissoes = permissoes;
        this.senha = senha;
    }

    public UsuarioNewDTO() {
    }

}
