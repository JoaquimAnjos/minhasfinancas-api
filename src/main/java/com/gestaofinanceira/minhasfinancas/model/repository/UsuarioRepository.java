package com.gestaofinanceira.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestaofinanceira.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	boolean existsByEmail(String email);
	// já tem implementação padrão devido à convenção da forma com está escrito
	
	Optional<Usuario> findByEmail(String email);
	//Optional é melhor para o tratamento de null pointer exceptions

}
