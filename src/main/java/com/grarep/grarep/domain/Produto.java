package com.grarep.grarep.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grarep.grarep.domain.dto.PedidoDTO;
import com.grarep.grarep.domain.dto.PedidoNewDTO;
import com.grarep.grarep.domain.dto.ProdutoDTO;
import com.grarep.grarep.domain.dto.ProdutoNewDTO;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String sku;
    @OneToMany(mappedBy = "id.produto")
    private Set<ProdutoPedido> pedidos = new HashSet<>();
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;


    public Set<ProdutoPedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<ProdutoPedido> pedidos) {
        this.pedidos = pedidos;
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Produto(Integer id, String nome, String sku, Tenant tenant) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.tenant = tenant;
        this.pedidos = new HashSet<>();
    }

    public Produto() {
    }

    public Produto(@Valid ProdutoDTO obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.sku = obj.getSku();
        this.pedidos = new HashSet<>();
    }

    public Produto(@Valid ProdutoNewDTO obj) {
        this.id = obj.getId();
        this.nome = obj.getNome();
        this.sku = obj.getSku();
        this.pedidos = new HashSet<>();
    }

    @JsonIgnore
    @OneToMany(mappedBy = "produto")
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

}
