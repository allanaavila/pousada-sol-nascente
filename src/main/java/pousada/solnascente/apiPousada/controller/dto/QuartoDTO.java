package pousada.solnascente.apiPousada.controller.dto;

import pousada.solnascente.apiPousada.model.enums.TipoQuarto;

import java.math.BigDecimal;

public record QuartoDTO(
    String numeroQuarto,
    TipoQuarto tipoQuarto,
    int capacidadeMaxima,
    BigDecimal valorDiaria,
    String descricao,
    boolean disponivel
) {
}
