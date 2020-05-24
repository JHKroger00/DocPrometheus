package br.com.rnp.gsc.sisacis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.rnp.gsc.sisacis.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	public Usuario findById(long id);
}
