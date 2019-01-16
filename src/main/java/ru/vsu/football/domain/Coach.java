package ru.vsu.football.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Coach extends FootballDomain<Integer>{

    private Integer id;
    private String name;
    private String surname;
    private Team team;
    private List<TeamHistory> teamHistory;

    @Override
    public String toString() {
        return getName();
    }
}
