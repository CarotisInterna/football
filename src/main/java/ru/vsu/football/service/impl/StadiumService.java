package ru.vsu.football.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Stadium;
import ru.vsu.football.entity.StadiumEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.service.AbstractService;

@Service
public class StadiumService extends AbstractService<StadiumEntity, Stadium, Integer> {

    public StadiumService(JpaRepository<StadiumEntity, Integer> repository, AbstractMapper<StadiumEntity, Stadium> mapper) {
        super(repository, mapper);
    }
}
