package br.com.cbf.campeonatobrasileiro.rest;

import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.entity.Jogo;
import br.com.cbf.campeonatobrasileiro.service.JogoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoRestController {
    @Autowired
    private JogoServico jogoServico;

    @PostMapping(value = "gerar-jogos")
    public ResponseEntity<Void> gerarJogos(){
        jogoServico.gerarJogos(LocalDateTime.now());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<JogoDTO>> obterJogos(){
        return ResponseEntity.ok().body(jogoServico.obterJogos());
    }
}
