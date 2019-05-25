package ru.demo.soccer.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "standing")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Standing {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "standing_id_seq", initialValue = 10_000, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private League league;

    @Column(name = "group_name")
    private String group;

    private String forme;

    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "standing_stat")
    @Column(name = "standing_stat")
    @Builder.Default
    private Set<StandingStat> standingStats = new HashSet<>();

    @Column(name = "last_update")
    private Date lastUpdate;
}
