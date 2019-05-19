package ru.demo.jpa.services;

import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;
import ru.demo.jpa.entities.acl.AclEntry;
import ru.demo.jpa.entities.acl.AclObjectIdentity;
import ru.demo.jpa.entities.acl.AclSid;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

class AclTests extends BaseJpaTests {

    @Test
    void mapTest() {
        // given
        AclObjectIdentity oid = AclObjectIdentity.builder().identifier("RESOURCE").build();
        AclSid sid = AclSid.builder().name("user").build();

        AclEntry entryRead = AclEntry.builder().aclObjectIdentity(oid).sid(sid).mask(1).build();
        AclEntry entryWrite = AclEntry.builder().aclObjectIdentity(oid).sid(sid).mask(2).build();
        AclEntry entryCreate = AclEntry.builder().aclObjectIdentity(oid).sid(sid).mask(4).build();
        AclEntry entryDelete = AclEntry.builder().aclObjectIdentity(oid).sid(sid).mask(8).build();

        Set<AclEntry> entries = Set.of(entryRead, entryWrite, entryDelete, entryCreate);
        oid.getEntries().addAll(entries);

        entries.forEach(getEntityManager()::persist);
        commit();
        begin();
        // when
        sid = getEntityManager().find(AclSid.class, sid.getId());

        //then
        assertFalse(sid.getEntries().isEmpty());
    }
}
