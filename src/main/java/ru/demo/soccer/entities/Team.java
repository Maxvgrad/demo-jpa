package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "team_id_name", allocationSize = 1, initialValue = 10_000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    /**
     * www api-football com
     */
    @Column(name = "api_football_com_team_id")
    private Long teamId;

    private String name;

    private String code;

    private String logo;

    private Integer founded;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private League league;
}
