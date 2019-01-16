package ru.vsu.football.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Coach;
import ru.vsu.football.entity.CoachEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.mapper.impl.CoachMapper;
import ru.vsu.football.repository.CoachEntityRepository;
import ru.vsu.football.service.AbstractService;

@Service
public class CoachService extends AbstractService<CoachEntity, Coach, Integer> {

    public CoachService(JpaRepository<CoachEntity, Integer> repository, AbstractMapper<CoachEntity, Coach> mapper) {
        super(repository, mapper);
    }
}
