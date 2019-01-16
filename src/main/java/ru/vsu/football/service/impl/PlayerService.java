package ru.vsu.football.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Player;
import ru.vsu.football.entity.PlayerEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.service.AbstractService;

@Service
public class PlayerService extends AbstractService<PlayerEntity, Player, Integer> {

    public PlayerService(JpaRepository<PlayerEntity, Integer> repository, AbstractMapper<PlayerEntity, Player> mapper) {
        super(repository, mapper);
    }
}
