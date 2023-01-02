package br.com.cbf.campeonatobrasileiro.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Jogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "timeMandante")
    private Time timeMandante;
    @ManyToOne
    @JoinColumn(name = "timeVisitante")
    private Time timeVisitante;
    private Integer golsTimeMandante;
    private Integer golsTimeVisitante;
    private Integer publico;
    private LocalDateTime data;
    private Integer rodada;
    private Boolean encerrado;
}
