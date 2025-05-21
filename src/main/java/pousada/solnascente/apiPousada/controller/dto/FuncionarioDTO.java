package pousada.solnascente.apiPousada.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import pousada.solnascente.apiPousada.model.enums.Perfil;

public record FuncionarioDTO(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotBlank(message = "O e-mail é obrigatório")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "O cargo é obrigatório")
        String cargo,

        @NotNull(message = "O perfil é obrigatório")
        Perfil perfil,

        boolean ativo
) {
}
