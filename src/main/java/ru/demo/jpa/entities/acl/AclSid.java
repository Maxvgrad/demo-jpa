package ru.demo.jpa.entities.acl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import java.util.HashMap;
import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "entries")
@ToString(exclude = "entries")
@Builder
public class AclSid {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "acl_sid_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "acl_entry",
               joinColumns = @JoinColumn(name = "sid_id"),
               inverseJoinColumns = @JoinColumn(name = "acl_object_identity"))
    @MapKeyColumn(name = "mask")
    @Builder.Default
    private Map<Integer, AclObjectIdentity> entries = new HashMap<>();

}
