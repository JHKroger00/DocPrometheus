package br.com.rnp.gsc.sisacis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rnp.gsc.sisacis.model.Usuario;
import br.com.rnp.gsc.sisacis.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	public List<Usuario> findUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@Override
	public Usuario findUsuario(long id) {
		return usuarioRepository.findById(id);
	}
	
	@Override
	public Usuario addUsuario (Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@Override
	public Usuario updateUsuario (Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@Override
	public String deleteUsuario (Usuario usuario) {
		Usuario userToDelete = usuarioRepository.findById(usuario.getId());
		usuarioRepository.delete(userToDelete);
		return "Usuario deletado";
		
	}
	
}
