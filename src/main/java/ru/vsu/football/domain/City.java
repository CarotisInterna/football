package ru.vsu.football.domain;


import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class City extends FootballDomain<Integer> {
    private Integer id;
    private String name;

    @Override
    public String toString() {
        return getName();
    }
}
