package ru.vsu.football.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity @DynamicUpdate
@Table(name = "ticket")
public class TicketEntity extends FootballEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double price;
    @OneToOne
    @JoinColumn(name = "match_id", referencedColumnName = "id")
    private MatchEntity match;

}
