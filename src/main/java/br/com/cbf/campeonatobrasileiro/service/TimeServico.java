package br.com.cbf.campeonatobrasileiro.service;

import br.com.cbf.campeonatobrasileiro.dto.TimeDTO;
import br.com.cbf.campeonatobrasileiro.entity.Time;
import br.com.cbf.campeonatobrasileiro.repository.TimeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeServico {

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void cadastrarTime(TimeDTO timeDto){
        Time time = toTime(timeDto);
        timeRepository.save(time);
    }

    public List<TimeDTO> listarTimes(){
        return timeRepository.findAll().stream().map(entity -> toDto(entity)).collect(Collectors.toList());
    }

    private TimeDTO toDto(Time time) {
        return modelMapper.map(time,TimeDTO.class);
    }

    private Time toTime(TimeDTO timeDTO){
        return modelMapper.map(timeDTO,Time.class);
    }

    public TimeDTO buscarTime(Integer id){
        return toDto(timeRepository.findById(id).get());
    }


}
