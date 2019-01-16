package ru.vsu.football.domain;

import lombok.Getter;
import lombok.Setter;
import ru.vsu.football.entity.FootballEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
public class ActionType extends FootballDomain<ActionTypeId> {
    private ActionTypeId id;
    private String name;
}
