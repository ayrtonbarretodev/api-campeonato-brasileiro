package br.com.cbf.campeonatobrasileiro.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TimeDTO {
    private String nome;
    private String sigla;
    private String uf;
    private String estadio;
}
