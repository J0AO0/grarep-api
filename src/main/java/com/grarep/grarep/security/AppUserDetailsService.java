package com.grarep.grarep.security;

import com.grarep.grarep.domain.Usuario;
import com.grarep.grarep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Service
public class AppUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = Logger.getLogger(AppUserDetailsService.class.getName());

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		LOGGER.info("Carregando usuário com email: " + email);

		Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));

		// Log para depuração
		LOGGER.info("Usuário encontrado: " + usuario.getEmail());
		LOGGER.info("ID: " + usuario.getId());
		LOGGER.info("Tenant: " + usuario.getTenant());
		LOGGER.info("Nome: " + usuario.getNome());

		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissoes().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
		return authorities;
	}
}