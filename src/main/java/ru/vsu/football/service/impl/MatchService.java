package ru.vsu.football.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Match;
import ru.vsu.football.entity.MatchEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.service.AbstractService;

@Service
public class MatchService extends AbstractService<MatchEntity, Match, Integer> {

    public MatchService(JpaRepository<MatchEntity, Integer> repository, AbstractMapper<MatchEntity, Match> mapper) {
        super(repository, mapper);
    }
}
