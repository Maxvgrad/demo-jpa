package ru.demo.soccer.entities;


import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "raw_response")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class)
public class RawResponse {

    @Id
    private String url;

    private Integer code;

    @Type(type = "jsonb-node" )
    @Column(columnDefinition = "jsonb not null")
    private JsonNode body;

    // TODO
    @Version
    private Integer version;
}
