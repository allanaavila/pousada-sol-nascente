package pousada.solnascente.apiPousada.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pousada.solnascente.apiPousada.model.Quarto;

import java.util.Optional;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {
    Optional<Quarto> findByNumeroQuarto(String numeroQuarto);
}
