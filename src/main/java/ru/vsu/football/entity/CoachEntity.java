package ru.vsu.football.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity @DynamicUpdate
@Table(name = "coach")
public class CoachEntity extends FootballEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;


    @OneToMany(mappedBy = "coach")
    private List<CoachTeamHistoryEntity> teamHistory;
}
