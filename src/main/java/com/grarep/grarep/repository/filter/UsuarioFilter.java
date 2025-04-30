package com.grarep.grarep.repository.filter;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UsuarioFilter {
    private Integer id;
    private String nome;
    private String status;
    private String telefone;
    private String email;
    private String login;
    private String emailusuario;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datagravacaode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datagravacaoate;
    public UsuarioFilter() {
    }

    public UsuarioFilter(Integer id, String nome, String status, String telefone, String email, String login,
                         String emailusuario, Date datagravacaode, Date datagravacaoate) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.telefone = telefone;
        this.email = email;
        this.login = login;
        this.emailusuario = emailusuario;
        this.datagravacaode = datagravacaode;
        this.datagravacaoate = datagravacaoate;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmailusuario() {
        return emailusuario;
    }

    public void setEmailusuario(String emailusuario) {
        this.emailusuario = emailusuario;
    }

    public Date getDatagravacaode() {
        return datagravacaode;
    }

    public void setDatagravacaode(Date datagravacaode) {
        this.datagravacaode = datagravacaode;
    }

    public Date getDatagravacaoate() {
        return datagravacaoate;
    }

    public void setDatagravacaoate(Date datagravacaoate) {
        this.datagravacaoate = datagravacaoate;
    }
}
