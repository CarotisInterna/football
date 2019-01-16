package ru.vsu.football.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractHistoryEntity extends FootballEntity<HistoryEntityId> {

    @EmbeddedId
    private HistoryEntityId id;

    @ManyToOne
    @MapsId("teamId")
    private TeamEntity team;

    @ManyToOne
    @MapsId("actionType")
    @JoinColumn(name = "action_type")
    private ActionTypeEntity actionType;

    private Date actionDate;
}
