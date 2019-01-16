package ru.vsu.football.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Stadium extends FootballDomain<Integer>{
    private Integer id;
    private String name;
    private Long capacity;
    private City city;

    @Override
    public String toString() {
        return getName();
    }
}
