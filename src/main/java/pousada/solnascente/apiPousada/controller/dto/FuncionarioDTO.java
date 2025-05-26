package pousada.solnascente.apiPousada.controller.dto;

import pousada.solnascente.apiPousada.model.enums.Perfil;

public record FuncionarioDTO(
        String nome,
        String cpf,
        String email,
        String telefone,
        String cargo,
        Perfil perfil,
        boolean ativo) {
}
