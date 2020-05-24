package br.com.rnp.gsc.sisacis.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rnp.gsc.sisacis.dto.UsuarioDTO;
import br.com.rnp.gsc.sisacis.dto.UsuarioRespostaDTO;
import br.com.rnp.gsc.sisacis.model.Usuario;
import br.com.rnp.gsc.sisacis.service.UsuarioService;

@RestController()
@RequestMapping("/usuario")
public class UsuarioController {
	
	private UsuarioService usuarioService;
	
	@Autowired
	UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("/get")
	public List<UsuarioRespostaDTO> getUsuarios() { 
		List<Usuario> usuarios = usuarioService.findUsuarios();
		return usuarios.stream()
				.map(u -> UsuarioRespostaDTO.mapToDTO(u))
				.collect(Collectors.toList());
	}
	
	@GetMapping("/get/{id}")
	public UsuarioRespostaDTO getUsuario(@PathVariable("id") long id) { 
		Usuario usuario = usuarioService.findUsuario(id);
		return UsuarioRespostaDTO.mapToDTO(usuario);
	}
	
	@PostMapping("/post")
	public UsuarioRespostaDTO addUsuario(@RequestBody UsuarioDTO dto) {
		Usuario usuario = usuarioService.addUsuario(dto.mapToUsuario());
		return UsuarioRespostaDTO.mapToDTO(usuario);
	}
	
	@PutMapping("/put")
	public UsuarioRespostaDTO updateUsuario(@RequestBody UsuarioDTO dto) {
		Usuario usuario = usuarioService.updateUsuario(dto.mapToUsuario());
		return UsuarioRespostaDTO.mapToDTO(usuario);
	}
	
	@DeleteMapping("/delete")
	public String deleteUsuario(@RequestBody UsuarioDTO dto) {
		return usuarioService.deleteUsuario(dto.mapToUsuario());
	}
}
