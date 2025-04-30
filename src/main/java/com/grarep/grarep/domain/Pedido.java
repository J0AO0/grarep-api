package com.grarep.grarep.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grarep.grarep.domain.dto.PedidoDTO;
import com.grarep.grarep.domain.dto.PedidoNewDTO;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String representante;
    private String arquiteto;
    @OneToMany(mappedBy = "id.pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ProdutoPedido> produtos = new HashSet<>();
    private Integer nf;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    @JsonIgnore
    private Tenant tenant;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<LogSistema> logs = new ArrayList<>();

    public Pedido() {
    }

    public Pedido(Integer id, String representante, String arquiteto, Integer nf, Tenant tenant, Set<ProdutoPedido> produtos) {
        this.id = id;
        this.representante = representante;
        this.arquiteto = arquiteto;
        this.nf = nf;
        this.tenant = tenant;
        this.produtos = produtos != null ? produtos : new HashSet<>();
    }

    public Pedido(@Valid PedidoDTO obj) {
        this.id = obj.getId();
        this.representante = obj.getRepresentante();
        this.arquiteto = obj.getArquiteto();
        this.nf = obj.getNf();
    }

    public Pedido(@Valid PedidoNewDTO obj) {
        this.id = obj.getId();
        this.representante = obj.getRepresentante();
        this.arquiteto = obj.getArquiteto();
        this.nf = obj.getNf();
    }

    public Set<ProdutoPedido> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<ProdutoPedido> produtos) {
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void addLogs(LogSistema log) {
        log.setPedido(this);
        logs.add(log);
    }

    @JsonIgnore
    public LogSistema getLogs() {
        if (logs.isEmpty()) {
            return null;
        }
        LogSistema ultimo = logs.get(0);
        for (LogSistema log : logs) {
            if (log.getId() != null && (ultimo.getId() == null || log.getId() > ultimo.getId())) {
                ultimo = log;
            }
        }
        return ultimo;
    }

    public void setLogs(List<LogSistema> logs) {
        this.logs = logs;
    }
}