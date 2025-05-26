package pousada.solnascente.apiPousada.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.model.Funcionario;
import pousada.solnascente.apiPousada.model.enums.Perfil;
import pousada.solnascente.apiPousada.service.FuncionarioService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FuncionarioControllerTest {

    @InjectMocks
    private FuncionarioController funcionarioController;

    @Mock
    private FuncionarioService funcionarioService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(funcionarioController)
                .build();
    }


    @Test
    void testeRetornarListaDeFuncionariosComStatus200() throws Exception {
        FuncionarioDTO funcionario1 = new FuncionarioDTO("João", "joao@email.com", "123.456.789-00", "(11) 91234-5678", "Recepcionista", Perfil.USUARIO, true);
        FuncionarioDTO funcionario2 = new FuncionarioDTO("Maria", "maria@email.com", "987.654.321-00", "(21) 99876-5432", "Gerente", Perfil.GERENTE, true);

        List<FuncionarioDTO> funcionarios = Arrays.asList(funcionario1, funcionario2);

        when(funcionarioService.listarTodosFuncionarios()).thenReturn(funcionarios);

        mockMvc.perform(get("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(funcionarios)));
    }

    @Test
    void testeRetornarFuncionarioQuandoIdExistir() throws Exception {
        Long id = 1L;
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("João");
        funcionario.setEmail("joao@email.com");
        funcionario.setCpf("123.456.789-00");
        funcionario.setTelefone("(11) 91234-5678");
        funcionario.setCargo("Recepcionista");
        funcionario.setPerfil(Perfil.USUARIO);
        funcionario.setAtivo(true);

        when(funcionarioService.buscarFuncionarioPorId(id)).thenReturn(funcionario);
        FuncionarioDTO funcionarioDTO = funcionario.toDTO();

        mockMvc.perform(get("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(funcionarioDTO)));
    }

    @Test
    void testeRetornar404QuandoFuncionarioNaoExistir() throws Exception {
        Long id = 99L;
        when(funcionarioService.buscarFuncionarioPorId(id)).thenThrow(new NoSuchElementException("Funcionário não encontrado"));

        mockMvc.perform(get("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void testeCadastrarFuncionarioComSucesso() throws Exception {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
                "Maria",
                "maria@email.com",
                "12345678900",
                "11912345678",
                "Recepcionista",
                Perfil.USUARIO,
                true
        );

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.nome());
        funcionario.setEmail(funcionarioDTO.email());
        funcionario.setCpf(funcionarioDTO.cpf());
        funcionario.setTelefone(funcionarioDTO.telefone());
        funcionario.setCargo(funcionarioDTO.cargo());
        funcionario.setPerfil(funcionarioDTO.perfil());
        funcionario.setAtivo(funcionarioDTO.ativo());

        when(funcionarioService.cadastrarFuncionario(funcionarioDTO)).thenReturn(funcionario);

        mockMvc.perform(post("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(funcionario.toDTO())));
    }

    @Test
    void testeAlterarFuncionarioComSucesso() throws Exception {
        Long id = 1L;

        FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
                "Carlos Silva",
                "12345678900",
                "carlos@example.com",
                "11988887777",
                "Gerente",
                Perfil.GERENTE,
                true
        );

        Funcionario funcionario = new Funcionario(funcionarioDTO);

        when(funcionarioService.alterarFuncionario(eq(id), any(FuncionarioDTO.class))).thenReturn(funcionario);

        mockMvc.perform(put("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(funcionario.toDTO())));
    }

    @Test
    void testeAlterarFuncionarioNaoEncontrado() throws Exception {
        Long id = 99L;

        FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
                "Carlos Silva",
                "12345678900",
                "carlos@example.com",
                "11988887777",
                "Gerente",
                Perfil.GERENTE,
                true
        );

        when(funcionarioService.alterarFuncionario(eq(id), any(FuncionarioDTO.class)))
                .thenThrow(new NoSuchElementException());

        mockMvc.perform(put("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testeAlterarFuncionarioBadRequest() throws Exception {
        Long id = 1L;

        FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
                "Carlos Silva",
                "12345678900",
                "carlos@example.com",
                "11988887777",
                "Gerente",
                Perfil.GERENTE,
                true
        );

        when(funcionarioService.alterarFuncionario(eq(id), any(FuncionarioDTO.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(put("/v1/funcionarios/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(funcionarioDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testeRetornarListaVaziaDeFuncionarios() throws Exception {
        when(funcionarioService.listarTodosFuncionarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/funcionarios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testeDesativarFuncionarioSucesso() {
        Long id = 1L;

        doNothing().when(funcionarioService).desativarFuncionario(id);

        ResponseEntity<Void> response = funcionarioController.desativarFuncionario(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(funcionarioService, times(1)).desativarFuncionario(id);
    }

    @Test
    void testeDesativarFuncionarioNaoEncontrado() {
        Long id = 1L;

        doThrow(new NoSuchElementException()).when(funcionarioService).desativarFuncionario(id);

        ResponseEntity<Void> response = funcionarioController.desativarFuncionario(id);

        assertEquals(404, response.getStatusCodeValue());
        verify(funcionarioService, times(1)).desativarFuncionario(id);
    }

    @Test
    void testeReativarFuncionarioSucesso() {
        Long id = 1L;

        doNothing().when(funcionarioService).reativarFuncionario(id);

        ResponseEntity<Void> response = funcionarioController.reativarFuncionario(id);

        assertEquals(204, response.getStatusCodeValue());
        verify(funcionarioService, times(1)).reativarFuncionario(id);
    }

    @Test
    void testeReativarFuncionarioNaoEncontrado() {
        Long id = 1L;

        doThrow(new NoSuchElementException()).when(funcionarioService).reativarFuncionario(id);

        ResponseEntity<Void> response = funcionarioController.reativarFuncionario(id);

        assertEquals(404, response.getStatusCodeValue());
        verify(funcionarioService, times(1)).reativarFuncionario(id);
    }
}
