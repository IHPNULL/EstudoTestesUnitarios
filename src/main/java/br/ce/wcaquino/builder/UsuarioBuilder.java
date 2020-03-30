package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

	private Usuario user;

	private UsuarioBuilder() {
	}

	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.user = new Usuario();
		builder.user.setNome("Italao");
		return builder;
	}
	
	public UsuarioBuilder comNome(String nome) {
		user.setNome(nome);
		return this;
	}

	public Usuario agora() {
		return user;
	}

}