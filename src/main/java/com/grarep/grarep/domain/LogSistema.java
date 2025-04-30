package com.grarep.grarep.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
public class LogSistema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String comando;

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss:SSS")
	@Column(columnDefinition = "datetime")
	private OffsetDateTime datagravacao;

	private String emailUsuario;
	private Boolean status;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "tenant_id", nullable = true)
	private Tenant tenant;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "produto_id", nullable = true)
	private Produto produto;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "pedido_id", nullable = true)
	private Pedido pedido;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Usuario usuario;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "empresa_id", nullable = true)
	private Empresa empresa;

	public LogSistema() {
	}

	public LogSistema(String comando, OffsetDateTime datagravacao, String emailUsuario, Pedido pedido) {
		this.comando = comando;
		this.datagravacao = datagravacao;
		this.emailUsuario = emailUsuario;
		this.pedido = pedido;
		this.status = true;
	}

	public LogSistema(Integer id, String comando, OffsetDateTime datagravacao, String emailUsuario) {
		this.id = id;
		this.comando = comando;
		this.datagravacao = datagravacao;
		this.emailUsuario = emailUsuario;
		this.status = true;
	}

	public LogSistema(Integer id, String usuarioLogado, String comando, OffsetDateTime datagravacao, Produto produto) {
		this.id = id;
		this.emailUsuario = usuarioLogado;
		this.comando = comando;
		this.datagravacao = datagravacao;
		this.produto = produto;
		this.status = true;
	}

	public LogSistema(Integer id, String usuarioLogado, String comando, OffsetDateTime datagravacao, Pedido pedido) {
		this.id = id;
		this.emailUsuario = usuarioLogado;
		this.comando = comando;
		this.datagravacao = datagravacao;
		this.pedido = pedido;
		this.status = true;
	}

	public LogSistema(Integer id, String usuarioLogado, String comando, OffsetDateTime datagravacao, Empresa empresa) {
		this.id = id;
		this.emailUsuario = usuarioLogado;
		this.comando = comando;
		this.datagravacao = datagravacao;
		this.empresa = empresa;
		this.status = true;
	}

	public LogSistema(Integer id, String usuarioLogado, String comando, OffsetDateTime datagravacao, Usuario usu) {
		this.id = id;
		this.emailUsuario = usuarioLogado;
		this.comando = comando;
		this.datagravacao = datagravacao;
		this.usuario = usu;
		this.status = true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public OffsetDateTime getDatagravacao() {
		return datagravacao;
	}

	public void setDatagravacao(OffsetDateTime datagravacao) {
		this.datagravacao = datagravacao;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}