package ru.vsu.football.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@Entity @DynamicUpdate
@Table(name = "player_team_history")
public class PlayerTeamHistoryEntity extends AbstractHistoryEntity {

    @ManyToOne
    @MapsId("personId")
    private PlayerEntity player;
}
