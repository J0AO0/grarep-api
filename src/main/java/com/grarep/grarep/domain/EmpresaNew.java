package com.grarep.grarep.domain;

import com.grarep.grarep.validation.empresa.EmpresaInsert;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@EmpresaInsert
public class EmpresaNew implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cidade;
    private String razaosocial;
    @NotNull
    @Column(unique = true)
    private String cpfoucnpj;
    private String naturezapessoa;
    private String uf;
    private BigDecimal valor;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String nomecontato;
    private String telefone;
    private String whats;
    private String email;
    private Boolean status = Boolean.TRUE;


    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRazaosocial() {
        return razaosocial;
    }

    public void setRazaosocial(String razaosocial) {
        this.razaosocial = razaosocial;
    }

    public String getCpfoucnpj() {
        return cpfoucnpj;
    }

    public void setCpfOuCnpj(String cpfoucnpj) {
        this.cpfoucnpj = cpfoucnpj;
    }

    public String getNaturezapessoa() {
        return naturezapessoa;
    }

    public void setNaturezapessoa(String naturezapessoa) {
        this.naturezapessoa = naturezapessoa;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNomecontato() {
        return nomecontato;
    }

    public void setNomecontato(String nomecontato) {
        this.nomecontato = nomecontato;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getWhats() {
        return whats;
    }

    public void setWhats(String whats) {
        this.whats = whats;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public EmpresaNew(Integer id, String cidade, String razaosocial, String cpfoucnpj, String naturezapessoa, String uf,
                      BigDecimal valor, String cep, String logradouro, String numero, String complemento, String bairro,
                      String nomecontato, String telefone, String whats, String email, Boolean status) {
        super();
        this.id = id;
        this.cidade = cidade;
        this.razaosocial = razaosocial;
        this.cpfoucnpj = cpfoucnpj;
        this.naturezapessoa = naturezapessoa;
        this.uf = uf;
        this.valor = valor;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.nomecontato = nomecontato;
        this.telefone = telefone;
        this.whats = whats;
        this.email = email;
        this.status = status;
    }

    public EmpresaNew() {

    }

    @Override
    public String toString() {
        return "EmpresaFlat [id=" + id + ", cidade=" + cidade + ", razao=" + razaosocial + ", cpfOuCnpj=" + cpfoucnpj
                + ", naturezaPessoa=" + naturezapessoa + ", uf=" + uf + ", cep=" + cep + ", logradouro=" + logradouro
                + ", numero=" + numero + ", complemento=" + complemento + ", bairro=" + bairro + ", nomecontato="
                + nomecontato + ", telefone=" + telefone + ", whats=" + whats + ", email=" + email + ", status="
                + status + "]";
    }
}
