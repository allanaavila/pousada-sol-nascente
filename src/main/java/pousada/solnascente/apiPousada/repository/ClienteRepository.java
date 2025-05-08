package pousada.solnascente.apiPousada.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pousada.solnascente.apiPousada.model.Cliente;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
}
