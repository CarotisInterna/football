package ru.vsu.football.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Player;
import ru.vsu.football.domain.Team;
import ru.vsu.football.domain.TeamHistory;
import ru.vsu.football.entity.AbstractHistoryEntity;
import ru.vsu.football.entity.PlayerEntity;
import ru.vsu.football.entity.PlayerTeamHistoryEntity;
import ru.vsu.football.entity.TeamEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.repository.TeamEntityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerMapper implements AbstractMapper<PlayerEntity, Player> {

    private RoleMapper roleMapper;
    private CityMapper cityMapper;
    private TeamEntityRepository teamEntityRepository;

    @Override
    public PlayerEntity toEntity(Player domain) {
        if (domain == null) {
            return null;
        }

        PlayerEntity entity = new PlayerEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setSurname(domain.getSurname());
        entity.setRole(roleMapper.toEntity(domain.getRole()));
        entity.setCity(cityMapper.toEntity(domain.getCity()));
        entity.setAge(domain.getAge());
        entity.setTeam(teamToEntity(domain, entity));

        return entity;
    }

    @Override
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
                .team(toDomain(entity.getTeam()))
                .teamHistory(convertTeamHistoryList(entity.getTeamHistory()))
                .build();

        return domain;
    }

    private List<TeamHistory> convertTeamHistoryList(List<PlayerTeamHistoryEntity> teamHistory) {
        if (teamHistory == null) {
            return null;
        }
        return teamHistory.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Team toDomain(TeamEntity entity) {
        if (entity == null) {
            return null;
        }
        Team team = Team.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
        return team;
    }

    private TeamEntity teamToEntity(Player player, PlayerEntity entity) {
        if (player == null || player.getTeam() == null) {
            return null;
        }

        TeamEntity teamEntity = teamEntityRepository.findOne(player.getTeam().getId());
        teamEntity.getPlayers().add(entity);
        return teamEntity;

    }


    public TeamHistory toDomain(AbstractHistoryEntity entity) {
        if (entity == null) {
            return null;
        }
        TeamHistory teamHistory = new TeamHistory();
        teamHistory.setActionDate(entity.getActionDate());
        teamHistory.setActionType(entity.getActionType());
        teamHistory.setTeam(toDomain(entity.getTeam()));
        return teamHistory;
    }


    @Autowired
    public PlayerMapper(RoleMapper roleMapper,
                        CityMapper cityMapper, TeamEntityRepository teamEntityRepository) {
        this.roleMapper = roleMapper;
        this.cityMapper = cityMapper;
        this.teamEntityRepository = teamEntityRepository;
    }
}
