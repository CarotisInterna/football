package ru.vsu.football.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity @DynamicUpdate
@Table(name = "match")
public class MatchEntity extends FootballEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    @Column(name = "team1_res")
    private Integer team1Res;
    @Column(name = "team2_res")
    private Integer team2Res;
    @ManyToOne
    @JoinColumn(name = "stadium_id", referencedColumnName = "id")
    private StadiumEntity stadium;
    @ManyToOne
    @JoinColumn(name = "team_1_id", referencedColumnName = "id")
    private TeamEntity team1;
    @ManyToOne
    @JoinColumn(name = "team_2_id", referencedColumnName = "id")
    private TeamEntity team2;
    @OneToOne(mappedBy = "match", cascade = CascadeType.ALL)
    private TicketEntity ticket;

}
