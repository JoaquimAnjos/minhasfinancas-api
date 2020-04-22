package com.gestaofinanceira.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gestaofinanceira.minhasfinancas.exception.ErroAutenticacao;
import com.gestaofinanceira.minhasfinancas.exception.RegraNegocioException;
import com.gestaofinanceira.minhasfinancas.model.entity.Usuario;
import com.gestaofinanceira.minhasfinancas.model.repository.UsuarioRepository;
import com.gestaofinanceira.minhasfinancas.service.impl.UsuarioServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	//por fazer testes unitários não é colocar o @Autowired
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	/*@BeforeEach
	public void setUp() {
		
		service = Mockito.spy(UsuarioServiceImpl.class);
		//repository = Mockito.mock(UsuarioRepository.class);
		//service = new UsuarioServiceImpl(repository);
	}*/
	
	@Test
	public void deveSalvarUmUsuario() {
		//cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		//cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//ação
		org.junit.jupiter.api.Assertions
		.assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario));
		
		//verificação
		
		 Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		//Assertions.assertDoesNotThrow(() -> {
			//cenário
			String email = "email@email.com";
			String senha = "senha";
			
			Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
			Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
			
			//ação
			service.autenticar(email, senha);
		//});
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
			//cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
			
			//ação
			//org.junit.jupiter.api.Assertions.assertThrows(ErroAutenticacao.class, () -> service.autenticar("email@email.com", "senha"));
			Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
			
			//verificação
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
 			// cenário
			String senha = "senha";
			Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

			// ação
			// o mail poderia ser outro mas por coerência está igual porque o teste é para a
			// senha por isso email não é importante neste caso
			Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "123"));
			
			//verificação
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida!");
	}
	
	@Test
	public void deveValidarEmail() {
		// cenario
		//repository.deleteAll();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("email@email.com");
	}
	
	@Test
	public void deveLancarErroAoValidaEmailQuandoExistirEmailCadastrado() {
			// cenario
			//Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
			//repository.save(usuario);
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
			//ação
			org.junit.jupiter.api.Assertions
			.assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
	}

}
