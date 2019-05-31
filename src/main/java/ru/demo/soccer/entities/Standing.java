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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STANDING")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "standingStats")
@ToString(exclude = "standingStats")
@Builder
@Data
public class Standing {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "STANDING_ID_SEQ", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private League league;

    @Column(name = "GROUP_NAME")
    private String group;

    private String forme;

    private String description;

    @OneToMany(mappedBy = "standing", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<StandingStat> standingStats = new HashSet<>();

    @Column(name = "LAST_UPDATE")
    private Date lastUpdate;
}
