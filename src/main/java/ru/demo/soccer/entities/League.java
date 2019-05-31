package ru.demo.soccer.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "teams")
@ToString(exclude = "teams")
public class League {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "LEAGUE_ID_SEQ", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    /**
     * www.api-football.com
     */
    @Column(name = "API_FOOTBALL_COM_LEAGUE_ID")
    @Deprecated
    private Long leagueId;

    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Country country;

    private String logo;

    @OneToMany(mappedBy = "league")
    @Builder.Default
    private Set<Season> season = new HashSet<>();
}
