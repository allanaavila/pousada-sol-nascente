package pousada.solnascente.apiPousada.model;

import jakarta.persistence.*;
import lombok.*;
import pousada.solnascente.apiPousada.controller.dto.ClienteDTO;

@Entity(name = "cliente")
@Table(name = "cliente", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false)
    private boolean ativo;

    public Cliente(ClienteDTO clienteDTO) {
        this.nome = clienteDTO.nome();
        this.cpf = clienteDTO.cpf();
        this.email = clienteDTO.email();
        this.telefone = clienteDTO.telefone();
        this.ativo = clienteDTO.ativo();
    }

    public ClienteDTO toDTO() {
        return new ClienteDTO(
                this.nome,
                this.cpf,
                this.email,
                this.telefone,
                this.ativo);
    }
}