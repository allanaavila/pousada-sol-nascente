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

import java.util.List;
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

    // teste metodo alterarFuncionario
    @Test
    @DisplayName("Deve alterar todos os campos do funcionário com sucesso")
    void testeAlterarFuncionarioCenario1(){
        Long id = 1L;
        FuncionarioDTO funcionarioDTOAlterado = new FuncionarioDTO(
                "Theo Mendes",
                "987.654.321-00",
                "theo.mendes@gmail.com",
                "(11) 99999-8888",
                "Gerente",
                Perfil.GERENTE,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.findByEmail(funcionarioDTOAlterado.email())).thenReturn(Optional.empty());
        when(emailFormatter.isEmailValido(funcionarioDTOAlterado.email())).thenReturn(true);
        when(emailFormatter.formatToLowercase(funcionarioDTOAlterado.email())).thenReturn("theo.mendes@gmail.com");

        when(funcionarioRepository.findByCpf(funcionarioDTOAlterado.cpf())).thenReturn(Optional.empty());
        when(cpfFormatter.formatAndValidate(funcionarioDTOAlterado.cpf())).thenReturn("987.654.321-00");

        when(telefoneFormatter.isCelularValido(funcionarioDTOAlterado.telefone())).thenReturn(true);
        when(telefoneFormatter.formatCelular(funcionarioDTOAlterado.telefone())).thenReturn("(11) 99999-8888");

        when(funcionarioRepository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario resultado = funcionarioService.alterarFuncionario(id, funcionarioDTOAlterado);

        assertEquals("Theo Mendes", resultado.getNome());
        assertEquals("theo.mendes@gmail.com", resultado.getEmail());
        assertEquals("987.654.321-00", resultado.getCpf());
        assertEquals("(11) 99999-8888", resultado.getTelefone());

    }

    @Test
    @DisplayName("Não deve alterar e-mail se for nulo")
    void testeAlterarFuncionarioEmailNulo(){
        Long id = 1L;

        FuncionarioDTO dtoAtualizado = new FuncionarioDTO(
                "Theo Mendes",
                null,
                null,
                null,
                null,
                Perfil.USUARIO,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Funcionario resultado = funcionarioService.alterarFuncionario(id, dtoAtualizado);

        assertEquals(funcionarioDTOValido.email(), resultado.getEmail());
    }


    @Test
    @DisplayName("Deve lançar exceção se novo e-mail já estiver cadastrado")
    void testeAlterarFuncionarioEmailJaCadastrado() {
        Long id = 1L;
        String novoEmail = "jaexistente@gmail.com";

        FuncionarioDTO dtoAtualizado = new FuncionarioDTO(
                null,
                null,
                novoEmail,
                null,
                null,
                Perfil.USUARIO,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.findByEmail(novoEmail)).thenReturn(Optional.of(new Funcionario()));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> funcionarioService.alterarFuncionario(id, dtoAtualizado)
        );

        assertEquals("Email já cadastrado", thrown.getMessage());
    }


    @Test
    @DisplayName("Deve lançar exceção se novo e-mail for inválido")
    void testeAlterarFuncionarioEmailInvalido(){
        Long id = 1L;
        String novoEmail = "emailinvalido";

        FuncionarioDTO dtoAtualizado = new FuncionarioDTO(
                null,
                null,
                novoEmail,
                null,
                null,
                Perfil.USUARIO,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.findByEmail(novoEmail)).thenReturn(Optional.empty());
        when(emailFormatter.isEmailValido(novoEmail)).thenReturn(false);

        ValidacaoException thrown = assertThrows(
                ValidacaoException.class,
                () -> funcionarioService.alterarFuncionario(id, dtoAtualizado)
        );

        assertEquals("Erro de validação no campo 'email': Formato de email inválido.", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se novo CPF já estiver cadastrado")
    void testeAlterarFuncionarioCpfJaCadastrado() {
        Long id = 1L;
        String novoCpf = "987.654.321-00";

        FuncionarioDTO dtoAtualizado = new FuncionarioDTO(
                null,
                novoCpf,
                null,
                null,
                null,
                Perfil.USUARIO,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.findByCpf(novoCpf)).thenReturn(Optional.of(new Funcionario()));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> funcionarioService.alterarFuncionario(id, dtoAtualizado)
        );


        assertEquals("CPF já cadastrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se novo CPF for inválido")
    void testeAlterarFuncionarioCpfInvalido() {
        Long id = 1L;
        String novoCpf = "111.111.111-11";

        FuncionarioDTO dtoAtualizado = new FuncionarioDTO(
                null,
                novoCpf,
                null,
                null,
                null,
                Perfil.USUARIO,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(funcionarioRepository.findByCpf(novoCpf)).thenReturn(Optional.empty());
        when(cpfFormatter.formatAndValidate(novoCpf)).thenReturn(null);

        ValidacaoException thrown = assertThrows(
                ValidacaoException.class,
                () -> funcionarioService.alterarFuncionario(id, dtoAtualizado)
        );

        assertEquals("Erro de validação no campo 'cpf': CPF inválido.", thrown.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se novo telefone for inválido")
    void testeAlterarFuncionarioTelefoneInvalido() {
        Long id = 1L;
        String novoTelefone = "123";

        FuncionarioDTO dtoAtualizado = new FuncionarioDTO(
                null,
                null,
                null,
                novoTelefone,
                null,
                Perfil.USUARIO,
                true
        );

        Funcionario funcionarioExistente = new Funcionario(funcionarioDTOValido);
        funcionarioExistente.setId(id);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioExistente));
        when(telefoneFormatter.isCelularValido(novoTelefone)).thenReturn(false);

        ValidacaoException thrown = assertThrows(
                ValidacaoException.class,
                () -> funcionarioService.alterarFuncionario(id, dtoAtualizado)
        );

        assertEquals("Erro de validação no campo 'telefone': Telefone celular inválido.", thrown.getMessage());
    }

    // teste metodo desativarFuncionario

    @Test
    @DisplayName("Deve desativar um funcionário ativo com sucesso")
    void testeDesativarFuncionarioCenario1() {
        Long id = 1L;
        funcionarioSalvo.setAtivo(true);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioSalvo));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionarioSalvo);

        funcionarioService.desativarFuncionario(id);

        assertFalse(funcionarioSalvo.isAtivo(), "Funcionário deveria estar inativo após desativação");
        verify(funcionarioRepository, times(1)).save(funcionarioSalvo);
    }

    @Test
    @DisplayName("Não deve desativar um funcionário que já está inativo")
    void testeDesativarFuncionarioCenario2() {
        Long id = 1L;
        funcionarioSalvo.setAtivo(false);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioSalvo));

        funcionarioService.desativarFuncionario(id);

        assertFalse(funcionarioSalvo.isAtivo(), "Funcionário já estava inativo");
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar desativar funcionário inexistente")
    void testeDesativarFuncionarioCenario3() {
        Long id = 999L;

        when(funcionarioRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> funcionarioService.desativarFuncionario(id),
                "Deveria lançar NoSuchElementException"
        );

        assertEquals("Funcionário não encontrado com o ID: " + id, thrown.getMessage());
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    // teste metodo reativar cadastro funcionario

    @Test
    @DisplayName("Deve reativar um funcionário inativo com sucesso")
    void testeReativarFuncionarioCenario1() {
        Long id = 1L;
        funcionarioSalvo.setAtivo(false);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioSalvo));
        when(funcionarioRepository.save(any(Funcionario.class))).thenReturn(funcionarioSalvo);

        funcionarioService.reativarFuncionario(id);

        assertTrue(funcionarioSalvo.isAtivo(), "Funcionário deveria estar ativo após reativação");
        verify(funcionarioRepository, times(1)).save(funcionarioSalvo);
    }

    @Test
    @DisplayName("Não deve reativar um funcionário que já está ativo")
    void testeReativarFuncionarioCenario2() {
        Long id = 1L;
        funcionarioSalvo.setAtivo(true);

        when(funcionarioRepository.findById(id)).thenReturn(Optional.of(funcionarioSalvo));

        funcionarioService.reativarFuncionario(id);

        assertTrue(funcionarioSalvo.isAtivo(), "Funcionário já estava ativo");
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar reativar funcionário inexistente")
    void testeReativarFuncionarioCenario3() {
        Long id = 999L;

        when(funcionarioRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException thrown = assertThrows(
                NoSuchElementException.class,
                () -> funcionarioService.reativarFuncionario(id),
                "Deveria lançar NoSuchElementException"
        );

        assertEquals("Funcionário não encontrado com o ID: " + id, thrown.getMessage());
        verify(funcionarioRepository, never()).save(any(Funcionario.class));
    }

    //testes para o metodo listar todos os funcionarios
    @Test
    @DisplayName("Deve retornar lista vazia quando não houver funcionários")
    void testeListarTodosFuncionariosCenario2() {
        when(funcionarioRepository.findAll()).thenReturn(List.of());

        List<FuncionarioDTO> resultado = funcionarioService.listarTodosFuncionarios();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(funcionarioRepository, times(1)).findAll();
        verifyNoInteractions(cpfFormatter, telefoneFormatter, emailFormatter);
    }

    @Test
    @DisplayName("Deve propagar exceção se ocorrer erro ao buscar funcionários")
    void testeListarTodosFuncionariosCenario3() {
        when(funcionarioRepository.findAll()).thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> funcionarioService.listarTodosFuncionarios()
        );

        assertEquals("Erro no banco", thrown.getMessage());

        verify(funcionarioRepository, times(1)).findAll();
    }
}
