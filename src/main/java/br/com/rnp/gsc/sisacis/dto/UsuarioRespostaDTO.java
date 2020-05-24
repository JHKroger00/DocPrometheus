package br.com.rnp.gsc.sisacis.dto;

import br.com.rnp.gsc.sisacis.model.Usuario;

public class UsuarioRespostaDTO {
	private long id;
	private String nome;
	private String sobrenome;
	private String email;
	private String prefeitura;
	private String cargo;
	
	private UsuarioRespostaDTO(long id, String nome, String sobrenome, String email, String prefeitura, String cargo) {
		this.id = id;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.prefeitura = prefeitura;
		this.cargo = cargo;
	}
		
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

	public String getPrefeitura() {
		return prefeitura;
	}

	public String getCargo() {
		return cargo;
	}

	public static UsuarioRespostaDTO mapToDTO(Usuario usuario) {
		return new UsuarioRespostaDTO(usuario.getId(), usuario.getNome(), usuario.getSobrenome(), 
				usuario.getEmail(), usuario.getPrefeitura(), usuario.getCargo());
	}
	
}
