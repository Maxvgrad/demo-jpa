package ru.demo.jpa.entities.acl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "acl_entry")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "employees")
@ToString(exclude = "employees")
@Builder
public class AclEntry {

    @Id
    @SequenceGenerator(name = "seq", sequenceName = "acl_entry_id_seq", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "acl_object_identity")
    private AclObjectIdentity aclObjectIdentity;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private AclSid sid;

    private Integer mask;
}
