package br.com.cbf.campeonatobrasileiro.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JogoDTO {
    private Integer id;
    private LocalDateTime data;
    private Integer golsTimeMandante;
    private Integer golsTimeVisitante;
    private Integer publicoPagante;
    private Boolean encerrado;
    private Integer rodada;
    private TimeDTO timeMandante;
    private TimeDTO timeVisitante;
}
