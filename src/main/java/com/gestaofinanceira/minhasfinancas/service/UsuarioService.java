package com.gestaofinanceira.minhasfinancas.service;

import java.util.Optional;

import com.gestaofinanceira.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {
	
	Usuario autenticar (String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);// convenção do Spring Data usa o exist e o nome da propriedade para obter o boolean se existe ou não
	
	Optional<Usuario> obterPorId(Long id);
}
