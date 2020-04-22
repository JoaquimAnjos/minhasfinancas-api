package com.gestaofinanceira.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gestaofinanceira.minhasfinancas.model.entity.Usuario;

/*@SpringBootTest 
 * ela faz com que todo o contexto da aplicação SpringBoot suba para a realização dos testes de uma classe(UsuarioRepository)
 * e não é interessante porque junto com o contexto SpringBoot vêm classes de configuração, classes de serviço e etc que não é necessário
*/
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
/*cria uma instância do BD na memória e ao finalizar a bateria de testes ela deleta da memória, encerrou o teste encerrou a BD
 * sempre faz uma transação ao iniciar o teste e ao concluir o teste ela dá um rollback, ou seja desfaz tudo o que foi feito na transação
*/
@AutoConfigureTestDatabase(replace = Replace.NONE)
//para o @DataJpaTest não cria uma instancia própria do BD de memória e assim não vai subscrever as configurações feitas do h2
public class UsuarioRepositoryTest { 

	//Teste de integração porque necessita de recursos externos à aplicação porque como estamos acessando a base de dados é necessário ter a BD toda configurada
	//Teste unitário seria se não fosse necessário usar recursos externos
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		//repository.save(usuario); não é bom estar aqui porque vamos testar esse metodo no metodo devePersistirUmUsuarioNaBaseDeDados()
		entityManager.persist(usuario);
		
		// ação/execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenário
		//repository.deleteAll();
		
		//ação
		boolean result = repository.existsByEmail("usuario.email.com");
		
		//verificação
		Assertions.assertThat(result).isFalse();
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//ação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		//cenário
		
		//ação
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");
		
		//verificação
		Assertions.assertThat(result.isPresent()).isFalse();
	}
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}
}
