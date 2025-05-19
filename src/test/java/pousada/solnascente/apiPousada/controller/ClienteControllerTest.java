package pousada.solnascente.apiPousada.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.service.ClienteService;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .build();
    }


    //testes do metodo GET -  listar Todos Clientes

    @Test
    @DisplayName("Deve listar todos os cliente com sucesso")
    void testeListarTodosOsClientesComSucesso() throws Exception {
        // Arrange
        var clienteDTO = new ClienteDTO("Maria", "12345678900", "maria@email.com", "11988773322", true);
        when(clienteService.listarTodosClientes()).thenReturn(List.of(clienteDTO));

        // Act & Assert
        mockMvc.perform(get("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Maria"))
                .andExpect(jsonPath("$[0].cpf").value("12345678900"))
                .andExpect(jsonPath("$[0].email").value("maria@email.com"))
                .andExpect(jsonPath("$[0].telefone").value("11988773322"))
                .andExpect(jsonPath("$[0].ativo").value(true));

        verify(clienteService).listarTodosClientes();
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar lista vazia e status 200 quando não há clientes")
    void testeListarTodosOsClientesSemClientes() throws Exception {
        when(clienteService.listarTodosClientes()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(clienteService).listarTodosClientes();
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar Content-Type application/json no GET listar todos")
    void testeContentTypeGetListarTodos() throws Exception {
        when(clienteService.listarTodosClientes()).thenReturn(List.of());

        mockMvc.perform(get("/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    //testes do metodo GET - listar cliente por id

    @Test
    @DisplayName("Deve retornar cliente por ID com status 200")
    void testeBuscarClientePorIdComSucesso() throws Exception {
        Long id = 1L;
        var clienteDTO = new ClienteDTO("João", "11122233344", "joao@email.com", "11999998888", true);
        when(clienteService.buscarClientePorId(id)).thenReturn(new Cliente(clienteDTO));

        mockMvc.perform(get("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.cpf").value("11122233344"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.telefone").value("11999998888"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).buscarClientePorId(id);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar status 404 para ID de cliente inexistente")
    void testeBuscarClientePorIdNaoEncontrado() throws Exception {
        Long id = 100L;
        when(clienteService.buscarClientePorId(id)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clienteService).buscarClientePorId(id);
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar 405 Method Not Allowed para POST em /v1/clientes/{id}")
    void testePostInvalidoEmEndpointPut() throws Exception {
        Long id = 1L;
        var clienteDTO = new ClienteDTO("João", "11122233344", "joao@email.com", "11999998888", true);

        mockMvc.perform(post("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isMethodNotAllowed());
    }


    //testes do metodo POST - cadastrar cliente

    @Test
    @DisplayName("Deve cadastrar um cliente com sucesso e retornar status 201")
    void testeCadastrarClienteComSucesso() throws Exception {
        var clienteDTO = new ClienteDTO("Maria", "12345678900", "maria@email.com", "11988773322", true);
        var cliente = mock(Cliente.class);
        when(cliente.toDTO()).thenReturn(clienteDTO);
        when(clienteService.cadastrarCliente(any(ClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.email").value("maria@email.com"))
                .andExpect(jsonPath("$.telefone").value("11988773322"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).cadastrarCliente(any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve chamar o service mesmo com dados inválidos, pois não há validação automática")
    void testeCadastrarClienteComCorpoInvalido() throws Exception {
        var clienteDTOInvalido = new ClienteDTO("", "invalid_cpf", "invalid_email", "invalid_phone", true);
        var clienteMock = mock(Cliente.class);
        when(clienteMock.toDTO()).thenReturn(clienteDTOInvalido);
        when(clienteService.cadastrarCliente(any(ClienteDTO.class))).thenReturn(clienteMock);

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTOInvalido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(""))
                .andExpect(jsonPath("$.cpf").value("invalid_cpf"))
                .andExpect(jsonPath("$.email").value("invalid_email"))
                .andExpect(jsonPath("$.telefone").value("invalid_phone"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).cadastrarCliente(any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }


    @Test
    @DisplayName("Deve chamar o service mesmo com dados nulos, pois não há validação automática")
    void testeCadastrarClienteComDadosNulos() throws Exception {
        var clienteDTOInvalido = new ClienteDTO(null, null, null, null, true);
        var clienteMock = mock(Cliente.class);
        when(clienteMock.toDTO()).thenReturn(clienteDTOInvalido);
        when(clienteService.cadastrarCliente(any(ClienteDTO.class))).thenReturn(clienteMock);

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTOInvalido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").doesNotExist())
                .andExpect(jsonPath("$.cpf").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.telefone").doesNotExist())
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).cadastrarCliente(any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve cadastrar cliente com email vazio, pois não há validação automática")
    void testeCadastrarClienteComEmailVazio() throws Exception {
        var clienteDTOInvalido = new ClienteDTO("Maria", "12345678900", "", "11988773322", true);
        var clienteMock = mock(Cliente.class);
        when(clienteMock.toDTO()).thenReturn(clienteDTOInvalido);
        when(clienteService.cadastrarCliente(any(ClienteDTO.class))).thenReturn(clienteMock);

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTOInvalido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.cpf").value("12345678900"))
                .andExpect(jsonPath("$.email").value(""))
                .andExpect(jsonPath("$.telefone").value("11988773322"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).cadastrarCliente(any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar Content-Type application/json no POST cadastrar")
    void testeContentTypePostCadastrar() throws Exception {
        var clienteDTO = new ClienteDTO("Maria", "12345678900", "maria@email.com", "11988773322", true);
        var clienteMock = mock(Cliente.class);
        when(clienteMock.toDTO()).thenReturn(clienteDTO);
        when(clienteService.cadastrarCliente(any())).thenReturn(clienteMock);

        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request para POST com corpo vazio")
    void testePostComCorpoVazio() throws Exception {
        mockMvc.perform(post("/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    //testes do metodo PUT - alterar cliente

    @Test
    @DisplayName("Deve alterar cliente com sucesso e retornar status 200")
    void testeAlterarClienteComSucesso() throws Exception {
        Long id = 1L;
        var clienteDTO = new ClienteDTO("João", "11122233344", "joao@email.com", "11999998888", true);
        var clienteMock = mock(Cliente.class);
        when(clienteMock.toDTO()).thenReturn(clienteDTO);
        when(clienteService.alterarCliente(eq(id), any(ClienteDTO.class))).thenReturn(clienteMock);

        mockMvc.perform(put("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.cpf").value("11122233344"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.telefone").value("11999998888"))
                .andExpect(jsonPath("$.ativo").value(true));

        verify(clienteService).alterarCliente(eq(id), any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }


    @Test
    @DisplayName("Deve retornar status 404 ao tentar alterar cliente inexistente")
    void testeAlterarClienteNaoEncontrado() throws Exception {
        Long id = 100L;
        var clienteDTO = new ClienteDTO("João", "11122233344", "joao@email.com", "11999998888", true);

        when(clienteService.alterarCliente(eq(id), any(ClienteDTO.class))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(put("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isNotFound());

        verify(clienteService).alterarCliente(eq(id), any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar status 400 ao tentar alterar cliente com dados inválidos")
    void testeAlterarClienteComDadosInvalidos() throws Exception {
        Long id = 1L;
        var clienteDTOInvalido = new ClienteDTO("", "", "invalid_email", "", true);

        when(clienteService.alterarCliente(eq(id), any(ClienteDTO.class))).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(put("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTOInvalido)))
                .andExpect(status().isBadRequest());

        verify(clienteService).alterarCliente(eq(id), any(ClienteDTO.class));
        verifyNoMoreInteractions(clienteService);
    }

    @Test
    @DisplayName("Deve retornar Content-Type application/json no PUT alterar")
    void testeContentTypePutAlterar() throws Exception {
        Long id = 1L;
        var clienteDTO = new ClienteDTO("João", "11122233344", "joao@email.com", "11999998888", true);
        var clienteMock = mock(Cliente.class);
        when(clienteMock.toDTO()).thenReturn(clienteDTO);
        when(clienteService.alterarCliente(eq(id), any())).thenReturn(clienteMock);

        mockMvc.perform(put("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request para PUT com corpo vazio")
    void testePutComCorpoVazio() throws Exception {
        Long id = 1L;
        mockMvc.perform(put("/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    //testes do metodo DELETE - desativar cliente

    @Test
    @DisplayName("Deve desativar cliente com sucesso e retornar status 204")
    void testeDesativarClienteComSucesso() throws Exception {
        Long id = 1L;
        doNothing().when(clienteService).desativarCliente(id);

        mockMvc.perform(delete("/v1/clientes/{id}", id))
                .andExpect(status().isNoContent());

        verify(clienteService).desativarCliente(id);
        verifyNoMoreInteractions(clienteService);
    }


    @Test
    @DisplayName("Deve retornar status 404 ao tentar desativar cliente inexistente")
    void testeDesativarClienteNaoEncontrado() throws Exception {
        Long id = 100L;
        doThrow(NoSuchElementException.class).when(clienteService).desativarCliente(id);

        mockMvc.perform(delete("/v1/clientes/{id}", id))
                .andExpect(status().isNotFound());

        verify(clienteService).desativarCliente(id);
        verifyNoMoreInteractions(clienteService);
    }




}