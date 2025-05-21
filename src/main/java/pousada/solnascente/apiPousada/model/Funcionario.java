package pousada.solnascente.apiPousada.model;

import jakarta.persistence.*;
import lombok.*;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.model.enums.Perfil;

@Entity(name = "funcionario")
@Table(name = "funcionario", schema = "public")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(nullable = false, length = 50)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Perfil perfil;

    @Column(nullable = false)
    private boolean ativo;

    public Funcionario(FuncionarioDTO funcionarioDTO) {
        this.nome = funcionarioDTO.nome();
        this.cpf = funcionarioDTO.cpf();
        this.email = funcionarioDTO.email();
        this.telefone = funcionarioDTO.telefone();
        this.cargo = funcionarioDTO.cargo();
        this.perfil = funcionarioDTO.perfil();
        this.ativo = funcionarioDTO.ativo();
    }

    public FuncionarioDTO toDTO() {
        return new FuncionarioDTO(
                this.nome,
                this.cpf,
                this.email,
                this.telefone,
                this.cargo,
                this.perfil,
                this.ativo);
    }

}
