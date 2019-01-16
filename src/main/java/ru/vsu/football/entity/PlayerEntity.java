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
@Table(name = "player")
public class PlayerEntity extends FootballEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String surname;
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private RoleEntity role;
    @ManyToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private CityEntity city;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;
    @OneToMany(mappedBy = "player")
    private List<PlayerTeamHistoryEntity> teamHistory;
}
