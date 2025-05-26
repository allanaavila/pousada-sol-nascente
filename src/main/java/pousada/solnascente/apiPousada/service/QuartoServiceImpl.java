package pousada.solnascente.apiPousada.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pousada.solnascente.apiPousada.controller.dto.QuartoDTO;
import pousada.solnascente.apiPousada.model.Quarto;
import pousada.solnascente.apiPousada.repository.QuartoRepository;

import java.util.List;

@Service
@Transactional
public class QuartoServiceImpl implements QuartoService {

    private final QuartoRepository quartoRepository;

    public QuartoServiceImpl(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }


    @Override
    public QuartoDTO cadastrarQuarto(QuartoDTO quartoDTO) {
        Quarto  quarto = new Quarto(quartoDTO);
        Quarto salvo = quartoRepository.save(quarto);
        return salvo.toDTO();
    }

    @Override
    public QuartoDTO buscarPorId(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado com ID: " + id));
        return quarto.toDTO();
    }

    @Override
    public List<QuartoDTO> listarTodos() {
        return quartoRepository.findAll().stream()
                .map(Quarto::toDTO)
                .toList();
    }

    @Override
    public QuartoDTO alterarQuarto(Long id, QuartoDTO quartoDTO) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado com ID: " + id));

        quarto.setNumeroQuarto(quartoDTO.numeroQuarto());
        quarto.setTipoQuarto(quartoDTO.tipoQuarto());
        quarto.setCapacidadeMaxima(quartoDTO.capacidadeMaxima());
        quarto.setValorDiaria(quartoDTO.valorDiaria());
        quarto.setDescricao(quartoDTO.descricao());
        quarto.setDisponivel(quartoDTO.disponivel());

        Quarto atualizado = quartoRepository.save(quarto);
        return atualizado.toDTO();
    }

    @Override
    public void desativarQuarto(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado com ID: " + id));

        quarto.setDisponivel(false);
        quartoRepository.save(quarto);
    }
}
