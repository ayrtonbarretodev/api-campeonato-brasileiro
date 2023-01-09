package br.com.cbf.campeonatobrasileiro.repository;

import br.com.cbf.campeonatobrasileiro.entity.Jogo;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JogoRepository extends JpaRepository<Jogo,Integer> {
    List<Jogo> findByTimeMandanteAndEncerrado(Time time, boolean encerrado);

    List<Jogo> findByTimeVisitanteAndEncerrado(Time time, boolean encerrado);
}
