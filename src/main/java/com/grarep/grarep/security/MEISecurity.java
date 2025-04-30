package com.grarep.grarep.security;


import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MEISecurity {
	
	@Autowired
	private UsuarioRepository usurepo;
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public String getUsuario() {
		String nome = (String) getAuthentication().getPrincipal().toString();		
		return nome;
	}
	public Integer getIdUsuario() {
		String nome = (String) getAuthentication().getPrincipal().toString();
		Usuario idusuario = usurepo.buscarUsuario(nome);
		return idusuario.getId();
	}
	
	public String getEmailsuario() {
		String nome = (String) getAuthentication().getPrincipal().toString();
		Usuario idusuario = usurepo.buscarUsuario(nome);
		return idusuario.getEmail();
	}
	
	public Usuario getUsu() {
		String nome = (String) getAuthentication().getPrincipal().toString();
		Usuario usuario = usurepo.buscarUsuario(nome);
		return usuario;
	}
	
	
}
