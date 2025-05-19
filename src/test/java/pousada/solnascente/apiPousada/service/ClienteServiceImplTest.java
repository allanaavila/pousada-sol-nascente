package pousada.solnascente.apiPousada.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;
import pousada.solnascente.apiPousada.expection.ValidacaoException;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.repository.ClienteRepository;
import pousada.solnascente.apiPousada.util.CPFFormatter;
import pousada.solnascente.apiPousada.util.EmailFormatter;
import pousada.solnascente.apiPousada.util.TelefoneFormatter;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CPFFormatter cpfFormatter;

    @Mock
    private TelefoneFormatter telefoneFormatter;

    @Mock
    private EmailFormatter emailFormatter;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setup() {
        //inicia os mocks
        MockitoAnnotations.openMocks(this);
    }

    //testes do método cadastrarCliente

    @Test
    @DisplayName("Deve cadastrar um cliente válido")
    void testeCadastroDoCliente() {
        //Arrange
        ClienteDTO dto = new ClienteDTO("Viviane", "12345678901", "viviane@email.com", "11999999999", true);

        //Simulando o comportamento dos Mocks
        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(clienteRepository.findByCpf(dto.cpf())).thenReturn(java.util.Optional.empty());

        when(emailFormatter.isEmailValido(dto.email())).thenReturn(true);
        when(emailFormatter.formatToLowercase(dto.email())).thenReturn(dto.email().toLowerCase());

        when(cpfFormatter.formatAndValidate(dto.cpf())).thenReturn(dto.cpf());

        when(telefoneFormatter.isCelularValido(dto.telefone())).thenReturn(true);
        when(telefoneFormatter.formatCelular(dto.telefone())).thenReturn(dto.telefone());

        //retornando o cliente criado
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        Cliente clienteSalvo = clienteService.cadastrarCliente(dto);

        //Assert
        assertNotNull(clienteSalvo);
        assertEquals(dto.nome(), clienteSalvo.getNome());
        assertEquals(dto.email().toLowerCase(), clienteSalvo.getEmail());
        assertEquals(dto.cpf(), clienteSalvo.getCpf());
        assertEquals(dto.telefone(), clienteSalvo.getTelefone());
        assertTrue(clienteSalvo.isAtivo());

        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao cadastrar cliente com email já existente")
    void testeCadastroComEmailExistente() {
        // Arrange
        ClienteDTO dto = new ClienteDTO("Vivian", "12345678901", "vivian@email.com", "11999999999", true);

        // Simula que o email já existe no banco
        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.of(new Cliente()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.cadastrarCliente(dto);
        });

        assertEquals("Email já cadastrado", exception.getMessage());

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ValidacaoException ao cadastrar cliente com CPF inválido")
    void testeCadastroComCpfInvalido() {
        // Arrange
        ClienteDTO dto = new ClienteDTO("Viviane", "cpf_invalido", "viviane@email.com", "11999999999", true);

        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(clienteRepository.findByCpf(dto.cpf())).thenReturn(java.util.Optional.empty());
        when(emailFormatter.isEmailValido(dto.email())).thenReturn(true);
        when(emailFormatter.formatToLowercase(dto.email())).thenReturn(dto.email().toLowerCase());

        // Simula CPF inválido, retornando null
        when(cpfFormatter.formatAndValidate(dto.cpf())).thenReturn(null);

        // Act & Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            clienteService.cadastrarCliente(dto);
        });

        assertEquals("Erro de validação no campo 'cpf': CPF inválido.", exception.getMessage());

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ValidacaoException ao cadastrar cliente com telefone inválido")
    void testeCadastroComTelefoneInvalido() {
        // Arrange
        ClienteDTO dto = new ClienteDTO("Viviane", "12345678901", "viviane@email.com", "telefone_invalido", true);

        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(clienteRepository.findByCpf(dto.cpf())).thenReturn(java.util.Optional.empty());
        when(emailFormatter.isEmailValido(dto.email())).thenReturn(true);
        when(emailFormatter.formatToLowercase(dto.email())).thenReturn(dto.email().toLowerCase());
        when(cpfFormatter.formatAndValidate(dto.cpf())).thenReturn(dto.cpf());
        when(telefoneFormatter.isCelularValido(dto.telefone())).thenReturn(false);

        // Act & Assert
        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            clienteService.cadastrarCliente(dto);
        });

        assertEquals("Erro de validação no campo 'telefone': Telefone celular inválido.", exception.getMessage());

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao cadastrar cliente com CPF já existente")
    void testeCadastroComCpfExistente() {
        ClienteDTO dto = new ClienteDTO("Viviane", "12345678901", "viviane@email.com", "11999999999", true);

        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(emailFormatter.isEmailValido(dto.email())).thenReturn(true);
        when(emailFormatter.formatToLowercase(dto.email())).thenReturn(dto.email().toLowerCase());

        when(clienteRepository.findByCpf(dto.cpf())).thenReturn(java.util.Optional.of(new Cliente()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.cadastrarCliente(dto);
        });

        assertEquals("CPF já cadastrado", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar ValidacaoException ao cadastrar cliente com email inválido")
    void testeCadastroComEmailInvalido() {
        ClienteDTO dto = new ClienteDTO("Viviane", "12345678901", "email_invalido", "11999999999", true);

        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(emailFormatter.isEmailValido(dto.email())).thenReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () -> {
            clienteService.cadastrarCliente(dto);
        });

        assertEquals("Erro de validação no campo 'email': Formato de email inválido.", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    //testes do método buscarClientePorId

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void testeRetornarClientePorIdExistente() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("Viviane");

        when(clienteRepository.findById(id)).thenReturn(java.util.Optional.of(cliente));

        Cliente resultado = clienteService.buscarClientePorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Viviane", resultado.getNome());
        verify(clienteRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar NoSuchElementException ao buscar cliente por ID inexistente")
    void testeLancarExecucaoQuandoIdNaoExistir() {
        Long id = 1L;

        when(clienteRepository.findById(id)).thenReturn(java.util.Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            clienteService.buscarClientePorId(id);
        });

        assertEquals("Cliente não encontrado com o ID: " + id, exception.getMessage());
        verify(clienteRepository).findById(id);
    }

    //testes do metodo alterar cliente

    @Test
    @DisplayName("Deve alterar todos os campos com dados válidos")
    void testeAlterarTodosOsCampos() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(id, "Ana", "ana@teste.com", "12345678901", "11911111111", true);
        ClienteDTO dto = new ClienteDTO("Ana Maria", "98765432100", "ana.maria@email.com", "11999999999", true);


        when(clienteRepository.findById(id)).thenReturn(java.util.Optional.of(clienteExistente));
        when(clienteRepository.findByEmail(dto.email())).thenReturn(java.util.Optional.empty());
        when(clienteRepository.findByCpf(dto.cpf())).thenReturn(java.util.Optional.empty());
        when(emailFormatter.isEmailValido(dto.email())).thenReturn(true);
        when(emailFormatter.formatToLowercase(dto.email())).thenReturn(dto.email().toLowerCase());
        when(cpfFormatter.formatAndValidate(dto.cpf())).thenReturn(dto.cpf());
        when(telefoneFormatter.isCelularValido(dto.telefone())).thenReturn(true);
        when(telefoneFormatter.formatCelular(dto.telefone())).thenReturn(dto.telefone());

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente atualizado = clienteService.alterarCliente(id, dto);

        assertEquals(dto.nome(), atualizado.getNome());
        assertEquals(dto.email().toLowerCase(), atualizado.getEmail());
        assertEquals(dto.cpf(), atualizado.getCpf());
        assertEquals(dto.telefone(), atualizado.getTelefone());
    }


    @Test
    @DisplayName("Deve alterar apenas o nome do cliente")
    void testeAlterarApenasONome() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(
                id,
                "João",
                "12345678901",
                "joao@email.com",
                "11999999999",
                true
        );
        ClienteDTO dto = new ClienteDTO("João da Silva", null, null, null, true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente atualizado = clienteService.alterarCliente(id, dto);

        assertEquals("João da Silva", atualizado.getNome());
        assertEquals("joao@email.com", atualizado.getEmail());
        assertEquals("12345678901", atualizado.getCpf());
    }

    @Test
    @DisplayName("Deve lançar exceção se e-mail novo já estiver em uso por outro cliente")
    void testeAlterarEmailJaExistente() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(id, "João", "joao@email.com", "12345678901", "11999999999", true);
        ClienteDTO dto = new ClienteDTO(null, null, "novo@email.com", null, true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.findByEmail("novo@email.com")).thenReturn(Optional.of(new Cliente())); // outro cliente

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clienteService.alterarCliente(id, dto)
        );

        assertEquals("Email já cadastrado", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se novo e-mail for inválido")
    void testeAlterarEmailInvalido() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(id, "João", "joao@email.com", "12345678901", "11999999999", true);
        ClienteDTO dto = new ClienteDTO(null, null, "email_invalido", null, true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.findByEmail("email_invalido")).thenReturn(Optional.empty());
        when(emailFormatter.isEmailValido("email_invalido")).thenReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                clienteService.alterarCliente(id, dto)
        );

        assertEquals("Erro de validação no campo 'email': Formato de email inválido.", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se novo CPF já estiver em uso por outro cliente")
    void testeAlterarCpfJaExistente() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(id, "João", "joao@email.com", "12345678901", "11999999999", true);
        ClienteDTO dto = new ClienteDTO(null, "98765432100", null, null, true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.findByCpf("98765432100")).thenReturn(Optional.of(new Cliente())); // outro cliente

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                clienteService.alterarCliente(id, dto)
        );

        assertEquals("CPF já cadastrado", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se novo CPF for inválido")
    void testeAlterarCpfInvalido() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(id, "João", "joao@email.com", "12345678901", "11999999999", true);
        ClienteDTO dto = new ClienteDTO(null, "cpf_invalido", null, null, true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.findByCpf("cpf_invalido")).thenReturn(Optional.empty());
        when(cpfFormatter.formatAndValidate("cpf_invalido")).thenReturn(null);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                clienteService.alterarCliente(id, dto)
        );

        assertEquals("Erro de validação no campo 'cpf': CPF inválido.", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção se telefone for inválido")
    void testeAlterarTelefoneInvalido() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente(id, "João", "joao@email.com", "12345678901", "11999999999", true);
        ClienteDTO dto = new ClienteDTO(null, null, null, "telefone_invalido", true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(telefoneFormatter.isCelularValido("telefone_invalido")).thenReturn(false);

        ValidacaoException exception = assertThrows(ValidacaoException.class, () ->
                clienteService.alterarCliente(id, dto)
        );

        assertEquals("Erro de validação no campo 'telefone': Telefone celular inválido.", exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    //Testes para o metodo desativarCliente

    @Test
    @DisplayName("Deve desativar cliente ativo")
    void testeDesativarClienteAtivo() {
        Long id = 1L;
        Cliente clienteAtivo = new Cliente();
        clienteAtivo.setId(id);
        clienteAtivo.setAtivo(true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteAtivo));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        clienteService.desativarCliente(id);

        assertFalse(clienteAtivo.isAtivo());
        verify(clienteRepository).save(clienteAtivo);
    }

    @Test
    @DisplayName("Não deve desativar cliente já desativado")
    void testeDesativarClienteJaDesativado() {
        Long id = 1L;
        Cliente clienteInativo = new Cliente();
        clienteInativo.setId(id);
        clienteInativo.setAtivo(false);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteInativo));

        clienteService.desativarCliente(id);

        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar NoSuchElementException se cliente não existir")
    void testeDesativarClienteInexistente() {
        Long id = 1L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            clienteService.desativarCliente(id);
        });

        assertEquals("Cliente não encontrado com o ID: " + id, exception.getMessage());
    }

    @Test
    @DisplayName("Deve chamar buscarClientePorId ao desativar cliente")
    void testeVerificarChamadaBuscarClientePorId() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setAtivo(true);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);

        clienteService.desativarCliente(id);

        verify(clienteRepository).findById(id);
    }

    //Testes do metodo listar todos os clientes











}
