package ru.demo.jpa.entities.elementcollection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
public class Reactor {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "reactor_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private Long power;

    @ElementCollection
    @CollectionTable(name = "TVS", joinColumns = @JoinColumn(name = "reactor_identifier"))
    @AttributeOverride(name = "position", column = @Column(name = "t_position"))
    private Set<Tvs> tvs;

    @ElementCollection
    @Column(name = "tvel")
    private Set<String> tvels;

    //@ElementCollection
    //private List<Suz> suz;

    @ManyToOne
    private NuclearPowerPlant npp;


}
