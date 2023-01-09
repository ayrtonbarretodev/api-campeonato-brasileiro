package br.com.cbf.campeonatobrasileiro.rest;

import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.cbf.campeonatobrasileiro.service.JogoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok().body(jogoServico.listarJogos());
    }

    @GetMapping("/time/{time}")
    public ResponseEntity<List<JogoDTO>> obterTodosJogosDeUmTime(@PathVariable(value = "time") String time){
        return ResponseEntity.ok().body(jogoServico.obterTodosJogosDeUmTime(time));
    }

    @GetMapping("quantidade-rodadas")
    public ResponseEntity<Long> obterQuantidadeDeRodadas(){
        return ResponseEntity.ok().body(jogoServico.quantidadeRodadas());
    }

    @PostMapping("/finalizar/{id}")
    public ResponseEntity<JogoDTO> finalizar(@PathVariable Integer id, @RequestBody JogoFinalizadoDTO jogoFinalizadoDto) throws Exception {
        return ResponseEntity.ok().body(jogoServico.finalizar(id,jogoFinalizadoDto));
    }

    @GetMapping("/classificacao")
    public ResponseEntity<ClassificacaoDTO> obterClassificacao(){
        return ResponseEntity.ok().body(jogoServico.obterClassificacao());
    }

    @GetMapping("/jogo/{id}")
    public ResponseEntity<JogoDTO> buscarJogo(@PathVariable (value = "id") Integer id){
        return ResponseEntity.ok().body(jogoServico.buscarJogo(id));
    }
}
