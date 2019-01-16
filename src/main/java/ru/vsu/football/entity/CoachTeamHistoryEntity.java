package ru.vsu.football.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Getter
@Setter
@Entity @DynamicUpdate
@Table(name = "coach_team_history")
public class CoachTeamHistoryEntity extends AbstractHistoryEntity {

    @ManyToOne
    @MapsId("personId")
    private CoachEntity coach;
}
