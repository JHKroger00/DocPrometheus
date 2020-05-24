package br.com.rnp.gsc.sisacis.dto;

import br.com.rnp.gsc.sisacis.model.Usuario;

public class UsuarioDTO {
	private long id;
	private String nome;
	private String sobrenome;
	private String email;
	private String senha;
	private String prefeitura;
	private String cargo;
	
	
	public long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public String getPrefeitura() {
		return prefeitura;
	}

	public String getCargo() {
		return cargo;
	}

	public Usuario mapToUsuario() {
		return new Usuario(id, nome, sobrenome, email, senha, prefeitura, cargo);
	}
}
