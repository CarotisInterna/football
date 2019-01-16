package ru.vsu.football.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vsu.football.domain.Match;
import ru.vsu.football.domain.Ticket;
import ru.vsu.football.entity.MatchEntity;
import ru.vsu.football.entity.TicketEntity;
import ru.vsu.football.mapper.AbstractMapper;

@Service
public class MatchMapper implements AbstractMapper<MatchEntity, Match> {

    private StadiumMapper stadiumMapper;
    private TeamMapper teamMapper;

    @Override
    public MatchEntity toEntity(Match domain) {
        if (domain == null) {
            return null;
        }

        MatchEntity entity = new MatchEntity();
        entity.setId(domain.getId());
        entity.setDate(domain.getDate());
        entity.setTeam1(teamMapper.toEntity(domain.getTeam1()));
        entity.setTeam2(teamMapper.toEntity(domain.getTeam2()));
        entity.setStadium(stadiumMapper.toEntity(domain.getStadium()));
        entity.setTeam1Res(domain.getTeam1Res());
        entity.setTeam2Res(domain.getTeam2Res());
        entity.setTicket(toEntity(domain.getTicket(), entity));
        return entity;
    }

    @Override
    public Match toDomain(MatchEntity entity) {
        if (entity == null) {
            return null;
        }

        Match domain = new Match();
        domain.setId(entity.getId());
        domain.setDate(entity.getDate());
        domain.setTeam1Res(entity.getTeam1Res());
        domain.setTeam2Res(entity.getTeam2Res());
        domain.setStadium(stadiumMapper.toDomain(entity.getStadium()));
        domain.setTeam1(teamMapper.toDomain(entity.getTeam1()));
        domain.setTeam2(teamMapper.toDomain(entity.getTeam2()));
        domain.setTicket(toDomain(entity.getTicket()));
        return domain;
    }

    public TicketEntity toEntity(Ticket domain, MatchEntity matchEntity) {
        if (domain == null) {
            return null;
        }

        TicketEntity entity = new TicketEntity();
        entity.setId(domain.getId());
        entity.setPrice(domain.getPrice());
        entity.setMatch(matchEntity);
        return entity;
    }

    public Ticket toDomain(TicketEntity entity) {
        if (entity == null) {
            return null;
        }

        Ticket domain = new Ticket();
        domain.setId(entity.getId());
        domain.setPrice(entity.getPrice());
        return domain;
    }

    @Autowired
    public MatchMapper(StadiumMapper stadiumMapper, TeamMapper teamMapper) {
        this.stadiumMapper = stadiumMapper;
        this.teamMapper = teamMapper;
    }

}
