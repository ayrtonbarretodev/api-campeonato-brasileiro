package br.com.cbf.campeonatobrasileiro.entity;

import lombok.Data;

import javax.persistence.*;

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
}
