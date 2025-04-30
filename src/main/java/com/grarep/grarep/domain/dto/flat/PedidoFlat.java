package com.grarep.grarep.domain.dto.flat;

import com.grarep.grarep.domain.Pedido;
import com.grarep.grarep.domain.LogSistema;

import java.time.OffsetDateTime;

public class PedidoFlat {
    private Integer id;
    private String representante;
    private String arquiteto;
    private Integer nf;
    private OffsetDateTime datagravacao;
    private String emailusuario;

    public PedidoFlat() {
    }

    public PedidoFlat(Integer id, String representante, String arquiteto, Integer nf,
                      OffsetDateTime datagravacao, String emailusuario) {
        this.id = id;
        this.representante = representante;
        this.arquiteto = arquiteto;
        this.nf = nf;
        this.datagravacao = datagravacao;
        this.emailusuario = emailusuario;
    }

    public PedidoFlat(Pedido ped) {
        this.id = ped.getId();
        this.nf = ped.getNf();
        this.representante = ped.getRepresentante();
        this.arquiteto = ped.getArquiteto();
        LogSistema log = ped.getLogs();
        this.datagravacao = log != null ? log.getDatagravacao() : null;
        this.emailusuario = log != null ? log.getEmailUsuario() : null;
    }

    public PedidoFlat(Pedido ped, String ok) {
        this.id = ped.getId();
        this.nf = ped.getNf();
        this.representante = ped.getRepresentante();
        this.arquiteto = ped.getArquiteto();
        LogSistema log = ped.getLogs();
        this.datagravacao = log != null ? log.getDatagravacao() : null;
        this.emailusuario = log != null ? log.getEmailUsuario() : null;
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
}