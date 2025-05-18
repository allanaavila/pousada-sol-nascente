package pousada.solnascente.apiPousada.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pousada.solnascente.apiPousada.model.Cliente;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente criarCliente(String nome, String email, String cpf, String telefone, boolean ativo) {
        return new Cliente(nome, email, cpf, telefone, ativo);
    }

    @Test
    @DisplayName("Deve salvar e buscar cliente por ID")
    void testSaveAndFindById() {
        // Arrange
        Cliente cliente = criarCliente("Maria", "maria@email.com", "12345678900", "11999990000", true);
        Cliente saved = clienteRepository.save(cliente);

        //ACT
        Optional<Cliente> encontrada = clienteRepository.findById(saved.getId());

        //Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getEmail()).isEqualTo("maria@email.com");
    }

    @Test
    @DisplayName("Deve retornar cliente por email")
    void testSaveAndFindByEmail() {
        //Arrange
        Cliente cliente = criarCliente("João", "joao@email.com", "11122233344", "11999998888", true);
        clienteRepository.save(cliente);

        //ACT
        Optional<Cliente> encontrada = clienteRepository.findByEmail("joao@email.com");

        //Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getNome()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve retornar cliente por CPF")
    void testSaveAndFindByCpf() {
        //Arrange
        Cliente cliente = criarCliente("Ana", "ana@email.com", "99988877766", "11555554444", false);
        clienteRepository.save(cliente);

        //Act
        Optional<Cliente> encontrada = clienteRepository.findByCpf("99988877766");

        //Assert
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getEmail()).isEqualTo("ana@email.com");
        assertThat(encontrada.get().isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar todos os clientes")
    void testFindAll() {
        //Arrange
        clienteRepository.save(criarCliente("Carlos", "carlos@email.com", "12312312312", "11911112222", true));
        clienteRepository.save(criarCliente("Luana", "luana@email.com", "32132132132", "11933334444", true));

        //Act
        List<Cliente> clientes = clienteRepository.findAll();

        //Assert
        assertThat(clientes).hasSize(2);
    }

    @Test
    @DisplayName("Deve deletar cliente por ID")
    void testDeleteById() {

        //Arrange
        Cliente cliente = clienteRepository.save(criarCliente("Bruno", "bruno@email.com", "11111111111", "11900001111", true));
        clienteRepository.deleteById(cliente.getId());

        //Act
        Optional<Cliente> deletar = clienteRepository.findById(cliente.getId());

        //Assert
        assertThat(deletar).isEmpty();
    }

    @Test
    @DisplayName("Deve salvar uma lista de clientes e fazer flush")
    void testSaveAllAndFlush() {
        //Arrange
        List<Cliente> clientes = List.of(
                criarCliente("Joana", "joana@email.com", "22222222222", "11922223333", true),
                criarCliente("Pedro", "pedro@email.com", "33333333333", "11933334444", true)
        );

        //Act
        List<Cliente> salvo = clienteRepository.saveAllAndFlush(clientes);

        //Assert
        assertThat(salvo).hasSize(2);
    }

    @Test
    @DisplayName("Deve deletar todos os clientes em batch")
    void testDeleteAllInBatch() {
        //Arrange
        clienteRepository.saveAllAndFlush(List.of(
                criarCliente("A", "a@email.com", "44444444444", "11944445555", true),
                criarCliente("B", "b@email.com", "55555555555", "11955556666", true)
        ));

        //Act
        clienteRepository.deleteAllInBatch();

        //Assert
        assertThat(clienteRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar cliente por email inexistente")
    void testFindByEmailInexistente() {
        //Act
        Optional<Cliente> cliente = clienteRepository.findByEmail("naoexiste@email.com");

        //Assert
        assertThat(cliente).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cliente com email duplicado")
    void testEmailDuplicado() {
        // Arrange
        clienteRepository.saveAndFlush(criarCliente("João", "joao@email.com", "11111111111", "11999998888", true));

        // Act & Assert
        assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class, // CORRIGIDO AQUI
                () -> clienteRepository.saveAndFlush(criarCliente("João2", "joao@email.com", "22222222222", "11900001111", true))
        );
    }

    @Test
    @DisplayName("Deve retornar apenas clientes ativos")
    void testFindByAtivoTrue() {
        //Arrange
        clienteRepository.save(criarCliente("Ativo", "ativo@email.com", "12312312399", "11911112222", true));
        clienteRepository.save(criarCliente("Inativo", "inativo@email.com", "32132132188", "11933334444", false));

        //Act
        List<Cliente> ativos = clienteRepository.findByAtivoTrue();

        //Assert
        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getNome()).isEqualTo("Ativo");
    }

    @Test
    @DisplayName("Deve atualizar dados de cliente")
    void testUpdateCliente() {
        //Arrange
        Cliente cliente = clienteRepository.save(criarCliente("Rafa", "rafa@email.com", "99999999999", "11900000000", true));
        cliente.setTelefone("11911110000");
        cliente.setAtivo(false);

        //Act
        Cliente atualizado = clienteRepository.saveAndFlush(cliente);

        //Assert
        assertThat(atualizado.getTelefone()).isEqualTo("11911110000");
        assertThat(atualizado.isAtivo()).isFalse();
    }
}
