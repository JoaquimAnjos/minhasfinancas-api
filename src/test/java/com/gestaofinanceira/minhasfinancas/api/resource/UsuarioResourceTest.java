package com.gestaofinanceira.minhasfinancas.api.resource;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestaofinanceira.minhasfinancas.api.dto.UsuarioDTO;
import com.gestaofinanceira.minhasfinancas.exception.ErroAutenticacao;
import com.gestaofinanceira.minhasfinancas.exception.RegraNegocioException;
import com.gestaofinanceira.minhasfinancas.model.entity.Usuario;
import com.gestaofinanceira.minhasfinancas.service.LancamentoService;
import com.gestaofinanceira.minhasfinancas.service.UsuarioService;

@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioResource.class)
@AutoConfigureMockMvc /* vai servir para ter acesso ao objecto MockMvc 
							que vai ajudar na execução dos testes para a API*/
public class UsuarioResourceTest {
	
	public static final String API = "/api/usuarios";
	public static final MediaType JSON = MediaType.APPLICATION_JSON;
	
	@Autowired
	MockMvc mvc; //Vai servir para mockar todas as chamadas à Rest API
	
	@MockBean
	UsuarioService service;
	
	@MockBean
	LancamentoService lancamentoService;
	
	@Test
	public void deveAutenticarUmUsuario() throws Exception {
		// cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();// representa o Json que vamos mandar
		Usuario usuario = Usuario.builder().email(email).senha(senha).build();// vai ser retornado quando chamar o metodo autenticar do UsuarioService
		
		Mockito.when(service.autenticar(email, senha)).thenReturn(usuario);
		
		// precisa de um objecto json para mandar na requisição - transforma um objecto qualquer para String
		String json = new ObjectMapper().writeValueAsString(dto);
		
		// execução e verificação
		//serve para criar uma requisição do tipo post
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON) // mandando JSON
													.contentType(JSON) //recebendo JSON
													.content(json);
		
		mvc.perform(request)// executa a requisição
			.andExpect(MockMvcResultMatchers.status().isOk())// espera o estado OK
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
			
	}
	
	@Test
	public void deveRetornarBadRequestAoObterErroDeAutenticacao() throws Exception {
		//cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Mockito.when(service.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API.concat("/autenticar"))
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void deveCriarUmNovoUsuario() throws Exception {
		//cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		Usuario usuario = Usuario.builder().id(1l).email(email).senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isCreated())
		   .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
		   .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
		   .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
	}
	
	@Test
	public void deveRetornarBadRequestAoTentarCriarUmNovoUsuario() throws Exception {
		//cenário
		String email = "usuario@email.com";
		String senha = "123";
		
		UsuarioDTO dto = UsuarioDTO.builder().email(email).senha(senha).build();
		
		Mockito.when(service.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		//execucao e verificacao
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(API)
													.accept(JSON)
													.contentType(JSON)
													.content(json);
		
		mvc.perform(request)
		   .andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
