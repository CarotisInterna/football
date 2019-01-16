package ru.vsu.football.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import ru.vsu.football.domain.ActionTypeId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity @DynamicUpdate
@Table(name = "action_type")
public class ActionTypeEntity extends FootballEntity<ActionTypeId> {
    @Id
    private ActionTypeId id;
    private String name;
}
