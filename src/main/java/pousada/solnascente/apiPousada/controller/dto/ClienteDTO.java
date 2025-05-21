package pousada.solnascente.apiPousada.controller.dto;


public record ClienteDTO(
        String nome,
        String cpf,
        String email,
        String telefone,
        boolean ativo) {
}
