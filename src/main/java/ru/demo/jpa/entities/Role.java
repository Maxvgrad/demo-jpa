package ru.demo.jpa.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "acl_sid")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Role { // real table name is "acl_sid"

    @Id
    @TableGenerator(name = "generator", table = "generators", pkColumnName = "gen_name", pkColumnValue = "role",
                    valueColumnName = "gen_value", initialValue = 10, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
    private Long id;

    private String sid;

    @Builder.Default
    private Boolean principal = Boolean.FALSE;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "comment")
    private String comment;

}
