package pousada.solnascente.api_pousada.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.repository.ClienteRepository;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

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
        Optional<Cliente> found = clienteRepository.findById(saved.getId());

        //Assert
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("maria@email.com");
    }

}
