package pousada.solnascente.apiPousada.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pousada.solnascente.apiPousada.model.Cliente;
import pousada.solnascente.apiPousada.model.Funcionario;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    Optional<Funcionario> findByEmail(String email);
    Optional<Funcionario> findByCpf(String cpf);
    List<Funcionario> findByAtivoTrue();
}
