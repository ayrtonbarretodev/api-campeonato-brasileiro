package br.com.cbf.campeonatobrasileiro.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
//@Entity
public class Jogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Time timeMandante;
    private Time timeVisitante;
    private Integer golsTimeMandante;
    private Integer golsTimeVisitante;
    private Integer publico;
}
