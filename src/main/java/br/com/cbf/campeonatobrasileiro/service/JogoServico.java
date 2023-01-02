package br.com.cbf.campeonatobrasileiro.service;

import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.entity.Jogo;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.repository.JogoRepository;
import br.com.cbf.campeonatobrasileiro.repository.TimeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JogoServico {

    @Autowired
    JogoRepository jogoRepository;

    @Autowired
    TimeServico timeServico;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * @param primeiraRodada Data da primeira rodada
     */

    public void gerarJogos(LocalDateTime primeiraRodada){
        final List<Time> times = timeServico.findAll();
        List<Time> all1 = new ArrayList<>();
        List<Time> all2 = new ArrayList<>();
        all1.addAll(times);
        all2.addAll(times);

        jogoRepository.deleteAll();

        List<Jogo> jogos = new ArrayList<>();

        int t = times.size();
        int m = times.size()/2;

        LocalDateTime dataJogo = primeiraRodada;
        Integer rodada = 0;

        for (int i = 0; i< t-1; i++){
            rodada = i+1;
            for (int j = 0; j < m; j++){
                //Teste para ajustar o mando de campo
                Time time1;
                Time time2;
                if (j%2 ==1 || i%2 == 1 && j==0){
                    time1 = times.get(t - j - 1);
                    time2 = times.get(j);
                } else{
                    time1 = times.get(j);
                    time2 = times.get(t - j - 1);
                }
                if (time1 == null){
                    System.out.println("Time 1 null");
                }
                jogos.add(gerarJogo(dataJogo,rodada,time1,time2));
                dataJogo = dataJogo.plusDays(7);
            }
            //Gira os times no sentido horário, mantendo o primeiro lugar
            times.add(1,times.remove(times.size()-1));
        }
        jogos.forEach(jogo -> System.out.println(jogo));

        jogoRepository.saveAll(jogos);
        List<Jogo> jogos2 = new ArrayList<>();

        jogos.forEach(jogo -> {
            Time time1 = jogo.getTimeVisitante();
            Time time2 = jogo.getTimeMandante();
            jogos2.add(gerarJogo(jogo.getData().plusDays(7* jogos.size()), jogo.getRodada() + jogos.size(),time1,time2));
        });
        jogoRepository.saveAll(jogos2);
    }

    private Jogo gerarJogo(LocalDateTime dataJogo, Integer rodada, Time time1, Time time2) {
        Jogo jogo = new Jogo();
        jogo.setTimeMandante(time1);
        jogo.setTimeVisitante(time2);
        jogo.setRodada(rodada);
        jogo.setData(dataJogo);
        jogo.setEncerrado(false);
        jogo.setGolsTimeMandante(0);
        jogo.setGolsTimeVisitante(0);
        jogo.setPublico(0);
        return jogo;
    }

    public List<JogoDTO> obterJogos() {
        return jogoRepository.findAll().stream().map(jogo -> toDto(jogo)).collect(Collectors.toList());
    }

//    public Jogo toJogo(JogoDTO jogoDTO){
//        return modelMapper.map(jogoDTO,Jogo.class);
//    }

    public JogoDTO toDto(Jogo jogo){
        return modelMapper.map(jogo,JogoDTO.class);
    }
}
