package ru.demo.jpa.entities.acl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acl_object_identity")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "entries")
@ToString(exclude = "entries")
@Builder
public class AclObjectIdentity {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "acl_object_identity_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @OneToMany(mappedBy = "aclObjectIdentity")
    @Builder.Default
    private Set<AclEntry> entries = new HashSet<>();

    private String identifier;

}
