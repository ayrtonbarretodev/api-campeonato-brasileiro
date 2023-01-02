package br.com.cbf.campeonatobrasileiro.rest;

import br.com.cbf.campeonatobrasileiro.dto.TimeDTO;
import br.com.cbf.campeonatobrasileiro.service.TimeServico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeRestController {

    @Autowired
    private TimeServico timeServico;

    @Operation(summary = "Lista todos os times",description = "Listando todas os times",responses = {
            @ApiResponse(responseCode = "200", description = "Times listados com sucesso"),
            @ApiResponse(responseCode = "500", description = "Houve um erro ao listar os times")
    })

    @GetMapping()
    public ResponseEntity<List<TimeDTO>> getTimes(){
        return ResponseEntity.ok().body(timeServico.listarTimes());
    }

    @Operation(summary = "Obtem os dados de um time",description = "Buscando uma tarefa por id", responses = {
            @ApiResponse(responseCode = "200", description = "Time encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Não foi encontrado nenhum time com esse id")
    })

    @GetMapping("{id}")
    public ResponseEntity<TimeDTO> getTime(@PathVariable(value = "id") Integer id){
        return ResponseEntity.ok().body(timeServico.buscarTime(id));
    }

    @Operation(summary = "Criar Time",description = "Criando um novo time",responses = {
            @ApiResponse(responseCode = "201", description = "Time criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Houve um erro ao criar o time, verifique as informações")
    })

    @PostMapping()
    public ResponseEntity<TimeDTO> createTime(@RequestBody TimeDTO timeDTO) throws Exception {
        return ResponseEntity.ok().body(timeServico.cadastrarTime(timeDTO));
    }
}
