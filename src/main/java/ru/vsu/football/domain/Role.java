package ru.vsu.football.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Role extends FootballDomain<RoleId> {
    private RoleId id;
    private String name;

    @Override
    public String toString() {
        return getName();
    }
}
