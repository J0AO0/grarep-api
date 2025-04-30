package com.grarep.grarep.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.grarep.grarep.domain.dto.flat.PermissaoFront;
import com.grarep.grarep.domain.dto.flat.UsuarioFlat;
import com.grarep.grarep.domain.dto.viewretorno.UsuarioView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity

public class Usuario implements Serializable {
    @Override
    public String toString() {
        return "Usuario [id=" + id + ", status=" + status + ", nome=" + nome + ", login=" + login + ", email=" + email
                + ", permissoes=" + permissoes + ", senha=" + senha + "]";
    }
    private static final long serialVersionUID = 1L;
    @JsonView(UsuarioView.Resumo1.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonView(UsuarioView.Resumo1.class)
    private Boolean status = Boolean.TRUE;
    @JsonView(UsuarioView.Resumo1.class)
    private String nome;
    @JsonView(UsuarioView.Resumo1.class)
    private String login;
    @JsonView(UsuarioView.Resumo1.class)
    private String email;
    private String telefone;


    private Integer tenantativo;
    private Integer gtenantativo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_permissao", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_permissao"))
    private List<Permissao> permissoes= new ArrayList<Permissao>();

    @JsonIgnore
    private String senha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getTenantativo() {
        return tenantativo;
    }

    public void setTenantativo(Integer tenantativo) {
        this.tenantativo = tenantativo;
    }

    public Integer getGtenantAtivo() {
        return gtenantativo;
    }

    public void setGtenantativo(Integer gtenantativo) {
        this.gtenantativo = gtenantativo;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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

    public Usuario(Integer id, Boolean status, String nome, String login, String email, List<Permissao> permissoes,
                   List<LogSistema> logs, String senha) {
        this.id = id;
        this.status = status;
        this.nome = nome;
        this.login = login;
        this.email = email;
        this.permissoes = permissoes;
        this.logs = logs;
        this.senha = senha;
    }

    public Usuario() {
    }

    public Usuario(Usuario usuAtual, UsuarioFlat obj) {
        this.id = usuAtual.getId();
        this.status = obj.getStatus();
        this.nome = obj.getNome();
        this.login = obj.getLogin();
        this.email = obj.getEmail();
        this.senha = usuAtual.getSenha();
        transformarPermisaoFlat(obj.getPermissoes());

    }
    public Usuario(Usuario usuAtual, UsuarioFlat obj, Tenant t) {
        this.id = usuAtual.getId();
        this.status = obj.getStatus();
        this.nome = obj.getNome();
        this.login = obj.getLogin();
        this.email = obj.getEmail();
        this.senha = usuAtual.getSenha();
        transformarPermisaoFlat(obj.getPermissoes());

    }

    private void transformarPermisaoFlat(List<PermissaoFront> permissoes2) {
        for (PermissaoFront pf : permissoes2) {
            switch (pf.getNome()) {
                case "Pedido":classePermissaoFlatPedido(pf);break;
                case "Produto":classePermissaoFlatProduto(pf);break;
                case "Usuario":classePermissaoFlatUsuario(pf); break;
                case "Empresa":classePermissaoFlatEmpresa(pf); break;
               // case "Relatório":classePermissaoFlatRelatorio(pf);break;
            }

        }

    }

    private void classePermissaoFlatEmpresa(PermissaoFront pf) {
        if (pf.getPermission().getCreate()) {	Permissao c  = new Permissao(32,"C_EMP");this.permissoes.add(c);};
        if (pf.getPermission().getUpdate()) {	Permissao u  = new Permissao(33,"U_EMP");this.permissoes.add(u);};
        if (pf.getPermission().getDelete()) {	Permissao d  = new Permissao(34,"D_EMP");this.permissoes.add(d);};
        if (pf.getPermission().getRead())   {	Permissao r  = new Permissao(35,"R_EMP");this.permissoes.add(r);};
        if (pf.getPermission().getStatus()) {	Permissao s  = new Permissao(36,"S_EMP");this.permissoes.add(s);};

    }

    private void classePermissaoFlatPedido(PermissaoFront pf) {
        if (pf.getPermission().getCreate()) {	Permissao c  = new Permissao(6,"C_PED");this.permissoes.add(c);};
        if (pf.getPermission().getUpdate()) {	Permissao u  = new Permissao(7,"U_PED");this.permissoes.add(u);};
        if (pf.getPermission().getDelete()) {	Permissao d  = new Permissao(8,"D_PED");this.permissoes.add(d);};
        if (pf.getPermission().getRead())   {	Permissao r  = new Permissao(9,"R_PED");this.permissoes.add(r);};
        if (pf.getPermission().getStatus()) {	Permissao s  = new Permissao(10,"S_PED");this.permissoes.add(s);};

    }

    private void classePermissaoFlatProduto(PermissaoFront pf) {

        if (pf.getPermission().getCreate()) {	Permissao c  = new Permissao(11,"C_PROD");this.permissoes.add(c);};
        if (pf.getPermission().getUpdate()) {	Permissao u  = new Permissao(12,"U_PROD");this.permissoes.add(u);};
        if (pf.getPermission().getDelete()) {	Permissao d  = new Permissao(13,"D_PROD");this.permissoes.add(d);};
        if (pf.getPermission().getRead())   {	Permissao r  = new Permissao(14,"R_PROD");this.permissoes.add(r);};
        if (pf.getPermission().getStatus()) {	Permissao s  = new Permissao(15,"S_PROD");this.permissoes.add(s);};
    }

    private void classePermissaoFlatUsuario(PermissaoFront pf) {

        if (pf.getPermission().getCreate()) {	Permissao c  = new Permissao(21,"C_USU");this.permissoes.add(c);};
        if (pf.getPermission().getUpdate()) {	Permissao u  = new Permissao(22,"U_USU");this.permissoes.add(u);};
        if (pf.getPermission().getDelete()) {	Permissao d  = new Permissao(23,"D_USU");this.permissoes.add(d);};
        if (pf.getPermission().getRead())   {	Permissao r  = new Permissao(24,"R_USU");this.permissoes.add(r);};
        if (pf.getPermission().getStatus()) {	Permissao s  = new Permissao(25,"S_USU");this.permissoes.add(s);};
    }
//    private void classePermissaoFlatRelatorio(PermissaoFront pf) {
//
//        if (pf.getPermission().getRead())   {	Permissao r  = new Permissao(31,"R_REL");this.permissoes.add(r);};
//
//    }

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private List<LogSistema> logs = new ArrayList<LogSistema>();

    public void addLogs(LogSistema log) {
        logs.add(log);
    }

    public LogSistema getLogs() {
        Integer codigo = 0;
        Integer indice = -1;
        LogSistema ultimo = new LogSistema();
        for (int i = 0; i < logs.size(); i++) {
            if (codigo < logs.get(i).getId()) {
                codigo = logs.get(i).getId();
                indice = i;
            }
        }
        if (indice==-1) {
            return ultimo;
        }else {
            return ultimo = logs.get(indice);
        }

    }

    public void setLogs(List<LogSistema> logs) {
        this.logs = logs;
    }

    @ManyToOne
    private Tenant tenant;
    public Tenant getTenant() {
        return tenant;
    }
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }


}
