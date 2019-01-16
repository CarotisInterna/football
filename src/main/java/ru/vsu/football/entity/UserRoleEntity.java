package ru.vsu.football.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ru.vsu.football.domain.UserRoleId;

import javax.persistence.*;

@Getter
@Setter
@Entity @DynamicUpdate
@Table(name = "user_role")
public class UserRoleEntity extends FootballEntity<UserRoleId> {
    @Id
    @Enumerated(EnumType.ORDINAL)
    private UserRoleId id;
    private String name;
}
