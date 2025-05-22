package pousada.solnascente.apiPousada.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.expection.ValidacaoException;
import pousada.solnascente.apiPousada.model.Funcionario;
import pousada.solnascente.apiPousada.model.enums.Perfil;
import pousada.solnascente.apiPousada.repository.FuncionarioRepository;
import pousada.solnascente.apiPousada.util.CPFFormatter;
import pousada.solnascente.apiPousada.util.EmailFormatter;
import pousada.solnascente.apiPousada.util.TelefoneFormatter;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FuncionarioServiceImplTest {

    @Mock
    FuncionarioRepository funcionarioRepository;

    @Mock
    private CPFFormatter cpfFormatter;

    @Mock
    private TelefoneFormatter telefoneFormatter;

    @Mock
    private EmailFormatter emailFormatter;

    @InjectMocks
    private FuncionarioServiceImpl funcionarioService;

    private FuncionarioDTO funcionarioDTOValido;
    private Funcionario funcionarioSalvo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        funcionarioDTOValido = new FuncionarioDTO(
                "Theo de Ávila",
                "123.456.789-00",
                "theo@gmail.com",
                "(11) 98765-4321",
                "Recepcionista",
                Perfil.USUARIO,
                true
        );

        funcionarioSalvo = new Funcionario(funcionarioDTOValido);
        funcionarioSalvo.setId(1L);
    }

    @Test
    @DisplayName("Deve cadastrar um funcionário com sucesso quando os dados são válidos e únicos")
    void testeCadastrarFuncionarioCenario1() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(cpfFormatter.formatAndValidate(anyString())).thenReturn("123.456.789-00");
        when(funcionarioRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        when(telefoneFormatter.isCelularValido(anyString())).thenReturn(true);
        when(telefoneFormatter.formatCelular(anyString())).thenReturn("(11) 98765-4321");

        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionarioSalvo);

        Funcionario resultado = funcionarioService.cadastrarFuncionario(funcionarioDTOValido);

        assertNotNull(resultado);
        assertEquals("Theo de Ávila", resultado.getNome());
        assertEquals("theo@gmail.com", resultado.getEmail());
        assertEquals("123.456.789-00", resultado.getCpf());
        assertEquals("(11) 98765-4321", resultado.getTelefone());
        assertEquals("Recepcionista", resultado.getCargo());
        assertEquals(Perfil.USUARIO, resultado.getPerfil());
        assertTrue(resultado.isAtivo());

        verify(emailFormatter, times(1)).isEmailValido(anyString());
        verify(emailFormatter, times(1)).formatToLowercase(anyString());
        verify(funcionarioRepository, times(1)).findByEmail(anyString());
        verify(cpfFormatter, times(1)).formatAndValidate(anyString());
        verify(funcionarioRepository, times(1)).findByCpf(anyString());
        verify(telefoneFormatter, times(1)).isCelularValido(anyString());
        verify(telefoneFormatter, times(1)).formatCelular(anyString());
        verify(funcionarioRepository, times(1)).save(any(Funcionario.class));
    }

    @Test
    @DisplayName("Não deve cadastrar funcionário quando o e-mail já está cadastrado")
    void testeCadastrarFuncionarioCenario2() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.of(new Funcionario()));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> funcionarioService.cadastrarFuncionario(funcionarioDTOValido),
                "Deveria lançar IllegalArgumentException"
        );

        assertEquals("Email já cadastrado", thrown.getMessage());

        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    @DisplayName("Não deve cadastrar funcionário quando o formato do e-mail é inválido")
    void testeCadastrarFuncionarioCenario3() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            funcionarioService.cadastrarFuncionario(funcionarioDTOValido);
        });
        assertEquals("Erro de validação no campo 'email': Formato de email inválido.", exception.getMessage());
        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve cadastrar funcionário quando o CPF já está cadastrado")
    void testeCadastrarFuncionarioCenario4() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(cpfFormatter.formatAndValidate(anyString())).thenReturn("123.456.789-00");
        when(funcionarioRepository.findByCpf(anyString())).thenReturn(Optional.of(new Funcionario()));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> funcionarioService.cadastrarFuncionario(funcionarioDTOValido),
                "Deveria lançar IllegalArgumentException"
        );

        assertEquals("CPF já cadastrado", thrown.getMessage());
        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve cadastrar funcionário quando o formato do CPF é inválido")
    void testeCadastrarFuncionarioCenario5() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(cpfFormatter.formatAndValidate(anyString())).thenReturn(null);

        ValidacaoException thrown = assertThrows(
                ValidacaoException.class,
                () -> funcionarioService.cadastrarFuncionario(funcionarioDTOValido),
                "Deveria lançar ValidacaoException"
        );
        assertEquals("Erro de validação no campo 'cpf': CPF inválido.", thrown.getMessage());
        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve cadastrar funcionário quando o formato do telefone é inválido")
    void testeCadastrarFuncionarioCenario6() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(cpfFormatter.formatAndValidate(anyString())).thenReturn("123.456.789-00");
        when(funcionarioRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        when(telefoneFormatter.isCelularValido(anyString())).thenReturn(false);

        ValidacaoException thrown = assertThrows(
                ValidacaoException.class,
                () -> funcionarioService.cadastrarFuncionario(funcionarioDTOValido),
                "Deveria lançar ValidacaoException"
        );

        assertEquals("Erro de validação no campo 'telefone': Telefone celular inválido.", thrown.getMessage());
        verify(funcionarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve propagar exceção se ocorrer erro no repository")
    void testeCadastrarFuncionarioCenarioErroRepository() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(cpfFormatter.formatAndValidate(anyString())).thenReturn("123.456.789-00");
        when(funcionarioRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        when(telefoneFormatter.isCelularValido(anyString())).thenReturn(true);
        when(telefoneFormatter.formatCelular(anyString())).thenReturn("(11) 98765-4321");

        when(funcionarioRepository.save(any())).thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> funcionarioService.cadastrarFuncionario(funcionarioDTOValido)
        );

        assertEquals("Erro no banco", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve propagar exceção se ocorrer erro no repository ao salvar o funcionário")
    void testeCadastrarFuncionarioErroAoSalvar() {
        when(emailFormatter.isEmailValido(anyString())).thenReturn(true);
        when(emailFormatter.formatToLowercase(anyString())).thenReturn("theo@gmail.com");
        when(funcionarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        when(cpfFormatter.formatAndValidate(anyString())).thenReturn("123.456.789-00");
        when(funcionarioRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        when(telefoneFormatter.isCelularValido(anyString())).thenReturn(true);
        when(telefoneFormatter.formatCelular(anyString())).thenReturn("(11) 98765-4321");

        when(funcionarioRepository.save(any())).thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> funcionarioService.cadastrarFuncionario(funcionarioDTOValido)
        );

        assertEquals("Erro no banco", thrown.getMessage());
    }

    //Testes metodo buscarFuncionarioPorId
    @Test
    @DisplayName("Deve retornar um funcionário quando encontrado pelo ID")
    void testeBuscarFuncionarioPorIdCenario1() {
        Long id = 1L;
        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioSalvo));
        Funcionario resultado = funcionarioService.buscarFuncionarioPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals(funcionarioSalvo.getNome(), resultado.getNome());

        verify(funcionarioRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não encontrar funcionário pelo ID")
    void testeBuscarFuncionarioPorIdCenario2() {
        Long id = 2L;
        when(funcionarioRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> funcionarioService.buscarFuncionarioPorId(id),
                "Deveria lançar NoSuchElementException"
        );

        assertEquals("Funcionário não encontrado com o ID: " + id, thrown.getMessage());
        verify(funcionarioRepository, times(1)).findById(id);
    }
}
