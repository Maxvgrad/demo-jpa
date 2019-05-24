package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.sql.Date;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Season {

    private Integer season;

    @Column(name = "season_start")
    private Date seasonStart;

    @Column(name = "season_end")
    private Date seasonEnd;

}
