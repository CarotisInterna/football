package ru.vsu.football.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ru.vsu.football.domain.RoleId;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity @DynamicUpdate
@Table(name = "role")
public class RoleEntity extends FootballEntity<RoleId> {
    @Id
    @Enumerated(EnumType.ORDINAL)
    private RoleId id;
    private String name;
}
