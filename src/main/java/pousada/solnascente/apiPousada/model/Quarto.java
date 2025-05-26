package pousada.solnascente.apiPousada.model;

import jakarta.persistence.*;
import lombok.*;
import pousada.solnascente.apiPousada.controller.dto.FuncionarioDTO;
import pousada.solnascente.apiPousada.controller.dto.QuartoDTO;
import pousada.solnascente.apiPousada.model.enums.TipoQuarto;

import java.math.BigDecimal;

@Entity(name = "quarto")
@Table(name = "quartos", uniqueConstraints = {@UniqueConstraint(columnNames = "numeroQuarto")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroQuarto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoQuarto tipoQuarto;

    @Column(nullable = false)
    private int capacidadeMaxima;

    @Column(nullable = false)
    private BigDecimal valorDiaria;

    private String descricao;

    @Column(nullable = false)
    private boolean disponivel = true;

    public Quarto(QuartoDTO quartoDTO) {
        this.numeroQuarto = quartoDTO.numeroQuarto();
        this.tipoQuarto = quartoDTO.tipoQuarto();
        this.capacidadeMaxima = quartoDTO.capacidadeMaxima();
        this.valorDiaria = quartoDTO.valorDiaria();
        this.descricao = quartoDTO.descricao();
        this.disponivel = quartoDTO.disponivel();
    }

    public QuartoDTO toDTO() {
        return new QuartoDTO(
                this.numeroQuarto,
                this.tipoQuarto,
                this.capacidadeMaxima,
                this.valorDiaria,
                this.descricao,
                this.disponivel
        );
    }
}
