package br.com.cbf.campeonatobrasileiro.dto;

import br.com.cbf.campeonatobrasileiro.entity.Time;
import lombok.Data;

import javax.persistence.Column;
@Data
public class TimeDTO {
    private String nome;
    private String sigla;
    private String uf;

    private String estadio;

    public Time toTime(){
        Time time = new Time();
        time.setNome(nome);
        time.setSigla(sigla);
        time.setUf(uf);
        time.setEstadio(estadio);
        return time;
    }
}
