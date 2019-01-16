package ru.vsu.football.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Team extends FootballDomain<Integer>{
    private Integer id;
    private String name;
    private City city;
    private Integer prevPlace;
    private List<Player> players = new ArrayList<>(0);
    private Coach coach;

    @Override
    public String toString() {
        return getName();
    }
}
