package ru.vsu.football.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Match extends FootballDomain<Integer> {

    private Integer id;
    private Stadium stadium;
    private Date date;
    private Team team1;
    private Team team2;
    private Integer team1Res;
    private Integer team2Res;
    private Ticket ticket;

    public boolean hasResults() {
        return team1Res != null && team2Res != null;
    }
}
