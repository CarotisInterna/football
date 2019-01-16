package ru.vsu.football.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ticket extends FootballDomain<Integer> {

    private Integer id;
    private Double price;
}
