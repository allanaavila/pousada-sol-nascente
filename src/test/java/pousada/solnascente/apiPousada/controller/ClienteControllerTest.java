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
import pousada.solnascente.apiPousada.service.ClienteService;

import java.util.Collections;
import java.util.List;

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
}