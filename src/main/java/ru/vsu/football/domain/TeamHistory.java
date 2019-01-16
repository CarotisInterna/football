package ru.vsu.football.domain;

import lombok.Getter;
import lombok.Setter;
import ru.vsu.football.entity.ActionTypeEntity;

import java.util.Date;

@Getter
@Setter
public class TeamHistory {

    private Team team;

    private ActionTypeEntity actionType;

    private Date actionDate;

}
