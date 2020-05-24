package br.com.rnp.gsc.sisacis.service;

import java.util.List;

import br.com.rnp.gsc.sisacis.model.Usuario;

public interface UsuarioService {
	
	public List<Usuario> findUsuarios();
	
	public Usuario findUsuario(long id);
	
	public Usuario addUsuario (Usuario usuario);
	
	public Usuario updateUsuario (Usuario usuario);
	
	public String deleteUsuario (Usuario usuario);
	
	
}
