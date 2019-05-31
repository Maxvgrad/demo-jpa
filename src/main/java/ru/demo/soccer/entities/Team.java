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
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "seasons")
@ToString(exclude = "seasons")
public class Team {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "TEAM_ID_NAME", allocationSize = 1, initialValue = 10_000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    /**
     * www api-football com
     */
    @Column(name = "API_FOOTBALL_COM_TEAM_ID")
    @Deprecated
    private Long teamId;

    private String name;

    private String code;

    private String logo;

    private Integer founded;

    @ManyToMany(mappedBy = "teams", cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<Season> seasons = new HashSet<>();

    private Boolean checked;
}
