package ru.vsu.football.entity;

import lombok.*;
import ru.vsu.football.domain.ActionType;
import ru.vsu.football.domain.ActionTypeId;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"teamId", "personId", "actionType"})
public class HistoryEntityId implements Serializable {
    private Integer teamId;
    private Integer personId;
    private ActionTypeId actionType;
}
