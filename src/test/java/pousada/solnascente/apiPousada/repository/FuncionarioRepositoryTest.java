package pousada.solnascente.apiPousada.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.model.Funcionario;
import pousada.solnascente.apiPousada.model.enums.Perfil;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    EntityManager entityManager;

    private Funcionario criarFuncionario(FuncionarioDTO funcionarioDTO) {
        Funcionario funcionario = new Funcionario(funcionarioDTO);
        this.entityManager.persist(funcionario);
        this.entityManager.flush();
        return funcionario;
    }

    @Test
    @DisplayName("Deve salvar e buscar funcionario por ID")
    void testeSalvarEBuscarFuncionarioPorID() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO("João", "524.480.230-50", "joao@gmail.com", "11996665544", "Limpeza", Perfil.USUARIO, true);
        Funcionario funcionario = criarFuncionario(funcionarioDTO);

        Optional<Funcionario> encontrado = funcionarioRepository.findById(funcionario.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("joao@gmail.com");
    }


}
