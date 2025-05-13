package pousada.solnascente.apiPousada.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pousada.solnascente.apiPousada.model.Cliente;

@Getter
@Setter
public class ClienteDTO {
    private Long id;


    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;


    @Size(min = 11, max = 14, message = "CPF inválido")
    private String cpf;


    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")
    private String email;


    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;

    private boolean ativo;

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String nome, String cpf, String email, String telefone, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.ativo = ativo;
    }

    public static ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.isAtivo()
        );
    }
}
