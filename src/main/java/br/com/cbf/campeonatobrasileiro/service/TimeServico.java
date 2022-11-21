package br.com.cbf.campeonatobrasileiro.service;

import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeServico {

    @Autowired
    private TimeRepository timeRepository;

    public Time cadastrarTime(Time time){
        return timeRepository.save(time);
    }

    public List<Time> listarTimes(){
        return timeRepository.findAll();
    }

    public Time buscarTime(Integer id){
        return timeRepository.findById(id).get();
    }
}
