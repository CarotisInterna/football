package ru.vsu.football.service.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.football.domain.Team;
import ru.vsu.football.entity.TeamEntity;
import ru.vsu.football.mapper.AbstractMapper;
import ru.vsu.football.mapper.impl.CityMapper;
import ru.vsu.football.mapper.impl.CoachMapper;
import ru.vsu.football.mapper.impl.PlayerMapper;
import ru.vsu.football.service.AbstractService;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamService extends AbstractService<TeamEntity, Team, Integer> {
    private CityMapper cityMapper;
    private CoachMapper coachMapper;
    private PlayerMapper playerMapper;

    public TeamService(JpaRepository<TeamEntity, Integer> repository,
                       AbstractMapper<TeamEntity, Team> mapper,
                       CityMapper cityMapper,
                       CoachMapper coachMapper,
                       PlayerMapper playerMapper) {
        super(repository, mapper);
        this.cityMapper = cityMapper;
        this.coachMapper = coachMapper;
        this.playerMapper = playerMapper;
    }

//    @Override
//    public Team save(Team domain) {
//        TeamEntity entity = Optional.ofNullable(domain.getId())
//                .map(repository::findOne)
//                .orElse(null);
//        if (entity == null) {
//            entity = new TeamEntity();
//        }
//
//        if (!entity.getCity().getId().equals(domain.getCity().getId())) {
//
//        } else {
//
//        }
//        return mapper.toDomain(repository.save(entity));
//    }

    @Transactional(readOnly = true)
    public Team getById(Integer id) {
        return mapper.toDomain(repository.getOne(id));
    }
}
