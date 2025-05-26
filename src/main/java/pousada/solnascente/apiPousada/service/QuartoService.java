package pousada.solnascente.apiPousada.service;

import pousada.solnascente.apiPousada.controller.dto.QuartoDTO;

import java.util.List;

public interface QuartoService {
    QuartoDTO cadastrarQuarto(QuartoDTO quartoDTO);
    QuartoDTO buscarPorId(Long id);
    List<QuartoDTO> listarTodos();
    QuartoDTO alterarQuarto(Long id, QuartoDTO quartoDTO);
    void desativarQuarto(Long id);
}
