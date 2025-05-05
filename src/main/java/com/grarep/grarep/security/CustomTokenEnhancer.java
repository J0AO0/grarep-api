package com.grarep.grarep.security;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CustomTokenEnhancer implements TokenEnhancer {

	private static final Logger LOGGER = Logger.getLogger(CustomTokenEnhancer.class.getName());

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();

		// Log para depuração
		LOGGER.info("Enhancing token for user: " + usuarioSistema.getUsername());
		LOGGER.info("Usuario: " + usuarioSistema.getUsuario());

		Map<String, Object> addInfo = new HashMap<>();
		try {
			addInfo.put("nome", usuarioSistema.getUsuario().getNome());
			addInfo.put("id", usuarioSistema.getUsuario().getId());
			addInfo.put("tenant", usuarioSistema.getUsuario().getTenant());

			LOGGER.info("Nome: " + usuarioSistema.getUsuario().getNome());
			LOGGER.info("ID: " + usuarioSistema.getUsuario().getId());
			LOGGER.info("Tenant: " + usuarioSistema.getUsuario().getTenant());
		} catch (Exception e) {
			LOGGER.severe("Erro ao adicionar informações ao token: " + e.getMessage());
		}

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		return accessToken;
	}
}