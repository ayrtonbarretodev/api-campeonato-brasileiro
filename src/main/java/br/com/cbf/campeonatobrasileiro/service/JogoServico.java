package br.com.cbf.campeonatobrasileiro.service;

import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoDTO;
import br.com.cbf.campeonatobrasileiro.dto.ClassificacaoTimeDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoDTO;
import br.com.cbf.campeonatobrasileiro.dto.JogoFinalizadoDTO;
import br.com.cbf.campeonatobrasileiro.entity.Jogo;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.repository.JogoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
        jogos.forEach(System.out::println);

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

    public List<JogoDTO> listarJogos() {
        return jogoRepository.findAll().stream().map(jogo -> toDto(jogo)).collect(Collectors.toList());
    }

    public List<JogoDTO> obterTodosJogosDeUmTime(String time) {
        return jogoRepository.findAll().stream()
                .filter(jogo -> jogo.getTimeMandante().getNome().equals(time) || jogo.getTimeVisitante().getNome().equals(time))
                .map(jogo -> toDto(jogo)).collect(Collectors.toList());
    }

    public Long quantidadeRodadas() {
        return (long) jogoRepository.findAll().size();
    }

    public JogoDTO toDto(Jogo jogo){
        return modelMapper.map(jogo,JogoDTO.class);
    }

    public JogoDTO buscarJogo(Integer id) {
        return toDto(jogoRepository.findById(id).get());
    }

    public JogoDTO finalizar(Integer id, JogoFinalizadoDTO jogoFinalizadoDTO) throws Exception {
        Optional<Jogo> jogo = jogoRepository.findById(id);
        if (!jogo.isPresent()){
         throw new Exception("Jogo não existe");
        }
        jogo.get().setGolsTimeMandante(jogoFinalizadoDTO.getGolsTimeMandante());
        jogo.get().setGolsTimeVisitante(jogoFinalizadoDTO.getGolsTimeVisitante());
        jogo.get().setEncerrado(true);
        jogo.get().setPublico(jogoFinalizadoDTO.getPublico());
        return toDto(jogoRepository.save(jogo.get()));
    }


    public ClassificacaoDTO obterClassificacao() {

        ClassificacaoDTO classificacaoDTO = new ClassificacaoDTO();
        final List<Time> times = timeServico.findAll();

        times.forEach(time -> {
           final List<Jogo> jogosMandante =  jogoRepository.findByTimeMandanteAndEncerrado(time,true);
           final List<Jogo> jogosVisitante =  jogoRepository.findByTimeVisitanteAndEncerrado(time,true);

            AtomicReference<Integer> vitorias = new AtomicReference<>(0);
            AtomicReference<Integer> empates = new AtomicReference<>(0);
            AtomicReference<Integer> derrotas = new AtomicReference<>(0);
            AtomicReference<Integer> golsSofridos = new AtomicReference<>(0);
            AtomicReference<Integer> golsMarcados = new AtomicReference<>(0);

            jogosMandante.forEach(jogo -> {
                if (jogo.getGolsTimeMandante() > jogo.getGolsTimeVisitante()){
                    vitorias.getAndSet(vitorias.get() + 1);
                }else if(jogo.getGolsTimeMandante() < jogo.getGolsTimeVisitante()){
                    derrotas.getAndSet(derrotas.get() + 1);
                }else{
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.updateAndGet(v -> v + jogo.getGolsTimeMandante());
                golsSofridos.updateAndGet(v -> v + jogo.getGolsTimeVisitante());
            });

            jogosVisitante.forEach(jogo -> {
                if (jogo.getGolsTimeVisitante() > jogo.getGolsTimeMandante()){
                    vitorias.getAndSet(vitorias.get() + 1);
                }else if(jogo.getGolsTimeVisitante() < jogo.getGolsTimeMandante()){
                    derrotas.getAndSet(derrotas.get() + 1);
                }else{
                    empates.getAndSet(empates.get() + 1);
                }
                golsMarcados.updateAndGet(v -> v + jogo.getGolsTimeVisitante());
                golsSofridos.updateAndGet(v -> v + jogo.getGolsTimeMandante());
            });
            ClassificacaoTimeDTO classificacaoTimeDTO = new ClassificacaoTimeDTO();
            classificacaoTimeDTO.setIdTime(time.getId());
            classificacaoTimeDTO.setTime(time.getNome());
            classificacaoTimeDTO.setPontos((vitorias.get() * 3) + empates.get());
            classificacaoTimeDTO.setDerrotas(derrotas.get());
            classificacaoTimeDTO.setEmpates(empates.get());
            classificacaoTimeDTO.setVitorias(vitorias.get());
            classificacaoTimeDTO.setGolsMarcados(golsMarcados.get());
            classificacaoTimeDTO.setGolsSofridos(golsSofridos.get());
            classificacaoTimeDTO.setJogos(derrotas.get() + empates.get() + vitorias.get());
            classificacaoDTO.getTimes().add(classificacaoTimeDTO);
        });

        Collections.sort(classificacaoDTO.getTimes(),Collections.reverseOrder());
        int posicao = 1;
        for (ClassificacaoTimeDTO time: classificacaoDTO.getTimes()) {
            time.setPosicao(posicao++);
        }
        return classificacaoDTO;
    }

}
