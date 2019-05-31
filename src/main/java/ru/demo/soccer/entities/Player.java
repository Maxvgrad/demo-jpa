package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
public class Player {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "PLAYER_ID_SEQ", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    /**
     * www.api-football.com
     */
    @Column(name = "API_FOOTBALL_COM_PLAYER_ID")
    @Deprecated
    private Long playerId;

    @Column(name = "PLAYER_NAME")
    private String playerName;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Team team;

    private Integer number;

    private Short age;

    private Boolean injured;

    private String position;

    private String captain;

    @Embedded
    private Statistics statistics;

}