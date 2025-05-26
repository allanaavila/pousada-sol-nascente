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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @Test
    @DisplayName("Deve retornar funcionario por email")
    void testRetornarFuncionarioPorEmail() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO("João", "11122233344", "joao@email.com", "11999998888", "Recepcionista", Perfil.USUARIO, true);
        criarFuncionario(funcionarioDTO);

        Optional<Funcionario> encontrado = funcionarioRepository.findByEmail("joao@email.com");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("João");
    }

    @Test
    @DisplayName("Deve retornar funcionario por CPF")
    void testeRetornarFuncionarioPorCpf() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO("Maria", "12345678901", "maria@gmail.com", "11998887766", "Gerente", Perfil.GERENTE, true);
        criarFuncionario(funcionarioDTO);

        Optional<Funcionario> encontrado = funcionarioRepository.findByCpf("12345678901");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("maria@gmail.com");
        assertThat(encontrado.get().isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar todos os funcionarios")
    void testeEncontrarTodosFuncionario() {
        FuncionarioDTO funcionarioGerente = new FuncionarioDTO("Paula", "13198565456", "paula@gmail.com", "11998887766", "Gerente", Perfil.GERENTE, true);
        FuncionarioDTO funcionarioRecepcionista = new FuncionarioDTO("Carlos", "13197846555", "carlos@gmail.com", "11998887766", "Recepcionista", Perfil.USUARIO, true);

        funcionarioRepository.save(criarFuncionario(funcionarioGerente));
        funcionarioRepository.save(criarFuncionario(funcionarioRecepcionista));

        List<Funcionario> funcionarios = funcionarioRepository.findAll();

        assertThat(funcionarios).hasSize(2);
    }

    @Test
    @DisplayName("Deve deletar funcionario por ID")
    void testeExcluirPorID() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO("Paula", "13198565456", "paula@gmail.com", "11998887766", "Gerente", Perfil.GERENTE, true);

        Funcionario funcionario = funcionarioRepository.save(criarFuncionario(funcionarioDTO));

        funcionarioRepository.deleteById(funcionario.getId());
        Optional<Funcionario> encontrado = funcionarioRepository.findById(funcionario.getId());

        assertThat(encontrado).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar funcionario por email inexistente")
    void testeRetornarVazioAoBuscarClientePorEmailInexistente() {
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail("naoexiste@email.com");
        assertThat(funcionario).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar funcionario com email duplicado")
    void testeLancarExcecaoAoSalvarFuncionarioComEmailDuplicado() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO("Paula", "13198565456", "paula@gmail.com", "11998887766", "Gerente", Perfil.GERENTE, true);
        funcionarioRepository.saveAndFlush(criarFuncionario(funcionarioDTO));

        FuncionarioDTO funcionarioDTO1 = new FuncionarioDTO("Mariana", "12354687451", "paula@gmail.com", "35944226688", "Limpeza", Perfil.USUARIO, true);

        assertThrows(
                org.hibernate.exception.ConstraintViolationException.class,
                () -> funcionarioRepository.saveAndFlush(criarFuncionario(funcionarioDTO1))
        );
    }


    @Test
    @DisplayName("Deve retornar apenas funcionarios ativos")
    void testeEncontrarFuncionariosAtivos() {
        FuncionarioDTO funcionarioAtivo = new FuncionarioDTO("Paula", "13198565456", "paula@gmail.com", "11998887766", "Gerente", Perfil.GERENTE, true);
        FuncionarioDTO funcionarioInativo = new FuncionarioDTO("Stephanya", "13152647896", "ste@gmail.com", "13944226688", "Administração", Perfil.USUARIO, false);

        criarFuncionario(funcionarioAtivo);
        criarFuncionario(funcionarioInativo);

        List<Funcionario> ativos = funcionarioRepository.findByAtivoTrue();

        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getNome()).isEqualTo("Paula");
    }

    @Test
    @DisplayName("Deve atualizar dados de funcionarios")
    void testeAtualizarDadosDosFuncionarios() {
        FuncionarioDTO funcionarioDTO = new FuncionarioDTO("Stephanya", "13152647896", "ste@gmail.com", "13944226688", "Administração", Perfil.USUARIO, false);
        Funcionario funcionario = criarFuncionario(funcionarioDTO);

        funcionario.setCargo("Gerente");
        funcionario.setAtivo(true);

        Funcionario atualizar = funcionarioRepository.saveAndFlush(funcionario);
        assertThat(atualizar.getCargo()).isEqualTo("Gerente");
        assertThat(atualizar.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve retornar funcionarios por cargo")
    void testeRetornarFuncionariosPorCargo() {
        FuncionarioDTO funcionarioGerente1 = new FuncionarioDTO("Paula", "13198565456", "paula@gmail.com", "11998887766", "Gerente", Perfil.GERENTE, true);
        FuncionarioDTO funcionarioGerente2 = new FuncionarioDTO("Ana", "14198565456", "ana@gmail.com", "11998887767", "Gerente", Perfil.GERENTE, true);
        FuncionarioDTO funcionarioRecepcionista = new FuncionarioDTO("Carlos", "13197846555", "carlos@gmail.com", "11998887766", "Recepcionista", Perfil.USUARIO, true);

        criarFuncionario(funcionarioGerente1);
        criarFuncionario(funcionarioGerente2);
        criarFuncionario(funcionarioRecepcionista);

        List<Funcionario> gerentes = funcionarioRepository.findAllByCargo("Gerente");

        assertThat(gerentes).hasSize(2);
        assertThat(gerentes).extracting(Funcionario::getNome).containsExactlyInAnyOrder("Paula", "Ana");
    }








}
