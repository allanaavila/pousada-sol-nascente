package pousada.solnascente.apiPousada.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;
import pousada.solnascente.apiPousada.model.Cliente;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    EntityManager entityManager;

    private Cliente criarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente(clienteDTO);
        this.entityManager.persist(cliente);
        this.entityManager.flush();
        return cliente;
    }

    @Test
    @DisplayName("Deve salvar e buscar cliente por ID")
    void deveSalvarEBuscarClientePorId() {
        // Arrange - cria e persiste um cliente
        ClienteDTO clienteDTO = new ClienteDTO("Maria", "12345678900", "maria@email.com", "11999990000", true);
        Cliente clientePersistido = criarCliente(clienteDTO);

        // Act - busca cliente pelo ID gerado
        Optional<Cliente> encontrada = clienteRepository.findById(clientePersistido.getId());

        // Assert - verifica se encontrou e se os dados conferem
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getEmail()).isEqualTo("maria@email.com");
    }

    @Test
    @DisplayName("Deve retornar cliente por email")
    void deveRetornarClientePorEmail() {
        //Arrange
        ClienteDTO clienteDTO = new ClienteDTO("João", "11122233344", "joao@email.com", "11999998888", true);
        criarCliente(clienteDTO);

        //ACT
        Optional<Cliente> encontrada = clienteRepository.findByEmail("joao@email.com");

        //Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve retornar cliente por CPF")
    void deveRetornarClientePorCpf() {
        //Arrange
        ClienteDTO clienteDTO = new ClienteDTO("Ana", "99988877766", "ana@email.com", "11555554444", false);
        criarCliente(clienteDTO);

        //Act
        Optional<Cliente> encontrada = clienteRepository.findByCpf("99988877766");

        //Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getEmail()).isEqualTo("ana@email.com");
        assertThat(encontrada.get().isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar todos os clientes")
    void testeEncontrarTodosClientes() {
        //Arrange
        ClienteDTO cliente1 = new ClienteDTO("Carlos", "12312312312", "carlos@email.com", "11911112222", true);
        ClienteDTO cliente2 = new ClienteDTO("Luana", "32132132132", "luana@email.com", "11933334444", true);

        clienteRepository.save(criarCliente(cliente1));
        clienteRepository.save(criarCliente(cliente2));

        //Act
        List<Cliente> clientes = clienteRepository.findAll();

        //Assert
        assertThat(clientes).hasSize(2);
    }

    @Test
    @DisplayName("Deve deletar cliente por ID")
    void testeExcluirPorID() {

        //Arrange
        ClienteDTO clienteDTO = new ClienteDTO("Bruno", "11111111111", "bruno@email.com", "11900001111", true);
        Cliente cliente = clienteRepository.save(criarCliente(clienteDTO));

        //Act
        clienteRepository.deleteById(cliente.getId());
        Optional<Cliente> deletar = clienteRepository.findById(cliente.getId());

        //Assert
        assertThat(deletar).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar uma lista de clientes e fazer flush")
    void testeSalvarTodosELiberar() {
        //Arrange
        List<Cliente> clientes = List.of(
                criarCliente(new ClienteDTO("Joana", "22222222222", "joana@email.com", "11922223333", true)),
                criarCliente(new ClienteDTO("Pedro", "33333333333", "pedro@email.com", "11933334444", true))
        );

        //Act
        List<Cliente> salvo = clienteRepository.saveAllAndFlush(clientes);

        //Assert
        assertThat(salvo).hasSize(2);
    }

    @Test
    @DisplayName("Deve deletar todos os clientes em batch")
    void testeDeletarPorLote() {
        //Arrange
        clienteRepository.saveAllAndFlush(List.of(
                criarCliente(new ClienteDTO("A", "44444444444", "a@email.com", "11944445555", true)),
                criarCliente(new ClienteDTO("B", "55555555555", "b@email.com", "11955556666", true))
        ));

        //Act
        clienteRepository.deleteAllInBatch();

        //Assert
        assertThat(clienteRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cliente por email inexistente")
    void deveRetornarVazioAoBuscarClientePorEmailInexistente() {
        //Act
        Optional<Cliente> cliente = clienteRepository.findByEmail("naoexiste@email.com");

        //Assert
        assertThat(cliente).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cliente com email duplicado")
    void deveLancarExcecaoAoSalvarClienteComEmailDuplicado() {
        // Arrange
        ClienteDTO clienteDTO1 = new ClienteDTO("João", "11111111111", "joao@email.com", "11999998888", true);
        clienteRepository.saveAndFlush(criarCliente(clienteDTO1));

        ClienteDTO clienteDTO2 = new ClienteDTO("João2", "22222222222", "joao@email.com", "11900001111", true);

        // Act & Assert
        assertThrows(
                org.hibernate.exception.ConstraintViolationException.class,
                () -> clienteRepository.saveAndFlush(criarCliente(clienteDTO2))
        );
    }

    @Test
    @DisplayName("Deve retornar apenas clientes ativos")
    void testeEncontrarClientesAtivos() {
        //Arrange
        ClienteDTO clienteAtivoDTO = new ClienteDTO("Ativo", "12312312399", "ativo@email.com", "11911112222", true);
        ClienteDTO clienteInativoDTO = new ClienteDTO("Inativo", "32132132188", "inativo@email.com", "11933334444", false);
        criarCliente(clienteAtivoDTO);
        criarCliente(clienteInativoDTO);


        //Act
        List<Cliente> ativos = clienteRepository.findByAtivoTrue();

        //Assert
        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getNome()).isEqualTo("Ativo");
    }

    @Test
    @DisplayName("Deve atualizar dados de cliente")
    void testeAtualizarDadosDosClientes() {
        //Arrange
        ClienteDTO clienteDTO = new ClienteDTO("Rafa", "99999999999", "rafa@email.com", "11900000000", true);
        Cliente cliente = criarCliente(clienteDTO);

        cliente.setTelefone("11911110000");
        cliente.setAtivo(false);

        //Act
        Cliente atualizado = clienteRepository.saveAndFlush(cliente);

        //Assert
        assertThat(atualizado.getTelefone()).isEqualTo("11911110000");
        assertThat(atualizado.isAtivo()).isFalse();
    }
}
