package ru.vsu.football.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Coach;
import ru.vsu.football.domain.Team;
import ru.vsu.football.domain.TeamHistory;
import ru.vsu.football.entity.AbstractHistoryEntity;
import ru.vsu.football.entity.CoachEntity;
import ru.vsu.football.entity.CoachTeamHistoryEntity;
import ru.vsu.football.entity.TeamEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.repository.TeamEntityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachMapper implements AbstractMapper<CoachEntity, Coach> {

    private TeamEntityRepository teamEntityRepository;

    @Override
    public CoachEntity toEntity(Coach domain) {
        if (domain == null) {
            return null;
        }

        CoachEntity coachEntity = new CoachEntity();
        coachEntity.setId(domain.getId());
        coachEntity.setName(domain.getName());
        coachEntity.setSurname(domain.getSurname());
        coachEntity.setTeam(teamToEntity(domain, coachEntity));
        return coachEntity;
    }

    @Override
    public Coach toDomain(CoachEntity entity) {
        if (entity == null) {
            return null;
        }

        Coach domain = Coach.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .team(toDomain(entity.getTeam()))
                .teamHistory(convertTeamHistoryList(entity.getTeamHistory()))
                .build();

        return domain;
    }

    private List<TeamHistory> convertTeamHistoryList(List<CoachTeamHistoryEntity> teamHistory) {
        if (teamHistory == null) {
            return null;
        }
        return teamHistory.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
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

    private TeamEntity teamToEntity(Coach coach, CoachEntity entity) {
        if (coach == null || coach.getTeam() == null) {
            return null;
        }

        TeamEntity teamEntity = teamEntityRepository.findOne(coach.getTeam().getId());
        teamEntity.setCoach(entity);
        return teamEntity;

    }

    @Autowired
    public CoachMapper(TeamEntityRepository teamEntityRepository) {
        this.teamEntityRepository = teamEntityRepository;
    }
}
