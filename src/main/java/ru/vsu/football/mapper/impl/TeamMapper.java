package ru.vsu.football.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Coach;
import ru.vsu.football.domain.Player;
import ru.vsu.football.domain.Team;
import ru.vsu.football.entity.CoachEntity;
import ru.vsu.football.entity.PlayerEntity;
import ru.vsu.football.entity.TeamEntity;
import ru.vsu.football.mapper.AbstractMapper;

import java.util.stream.Collectors;

@Service
public class TeamMapper implements AbstractMapper<TeamEntity, Team> {

    private CityMapper cityMapper;
    private RoleMapper roleMapper;

    @Override
    public TeamEntity toEntity(Team domain) {
        if (domain == null) {
            return null;
        }

        TeamEntity entity = new TeamEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setPrevPlace(domain.getPrevPlace());
        entity.setCity(cityMapper.toEntity(domain.getCity()));
        return entity;
    }

    @Override
    public Team toDomain(TeamEntity entity) {
        if (entity == null) {
            return null;
        }

        Team domain = new Team();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setPrevPlace(entity.getPrevPlace());
        domain.setCity(cityMapper.toDomain(entity.getCity()));
        domain.setPlayers(entity.getPlayers().stream().map(this::toDomain).collect(Collectors.toList()));
        domain.setCoach(toDomain(entity.getCoach()));
        return domain;
    }

    private Coach toDomain(CoachEntity entity) {
        if (entity == null) {
            return null;
        }

        Coach coach = Coach.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .build();
        return coach;
    }

    public Player toDomain(PlayerEntity entity) {
        if (entity == null) {
            return null;
        }

        Player domain = Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .age(entity.getAge())
                .role(roleMapper.toDomain(entity.getRole()))
                .city(cityMapper.toDomain(entity.getCity()))
                .build();

        return domain;
    }


    @Autowired
    public TeamMapper(CityMapper cityMapper, RoleMapper roleMapper) {
        this.cityMapper = cityMapper;
        this.roleMapper = roleMapper;
    }
}
