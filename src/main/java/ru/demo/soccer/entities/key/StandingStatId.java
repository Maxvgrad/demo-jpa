package ru.demo.soccer.entities.key;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.demo.soccer.entities.Standing;
import ru.demo.soccer.entities.Team;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StandingStatId implements Serializable {

    private Standing standing;

    private Team team;
}
