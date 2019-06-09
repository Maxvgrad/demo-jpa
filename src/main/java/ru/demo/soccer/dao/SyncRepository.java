package ru.demo.soccer.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.demo.soccer.entities.Sync;

@Slf4j
@Repository
public class SyncRepository extends JpaGenericDao<Sync, Long> {
    public SyncRepository() {
        super(Sync.class);
    }
}
