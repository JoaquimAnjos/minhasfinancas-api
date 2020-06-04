package com.gestaofinanceira.minhasfinancas.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="usuario", schema="financas")
/*@Getter
@Setter
@EqualsAndHashCode
@ToString
*/
@Builder
@Data// substitui as notações que estão em comentário
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	
	@Column(name="email")
	//@Setter(AccessLevel.PRIVATE) para não criar setter
	private String email;
	
	@JsonIgnore // para não expor a senha do usuario
	@Column(name="senha")
	private String senha;
	
	/*Só para didática
	  public static void main(String[] args) {
		Usuario usuario = new Usuario();
		usuario.setEmail("hjkhkj");
		usuario.setNome("ghjbn");
		
		Usuario.builder().nome("usuario").email("ghjh").build();
	}*/
}
