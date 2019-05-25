package ru.demo.soccer.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.soccer.entities.Sync;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseSyncJob {

    private final EntityManager entityManager;
    private final String jobName;
    private final String seqName;
    private long initialSeqValue = 0L;

    public void execute() {
        entityManager.getTransaction().begin();
        Sync lastSync = retrieveSync();
        process(lastSync);
        entityManager.getTransaction().commit();
    }

    /**
     * Last processed synchronisation.
     */
    protected Sync retrieveSync() {

        Sync result;
        try {
            result = entityManager.createQuery("select j from Sync j where j.name=:name and j.key=:seqName", Sync.class)
                                  .setParameter("name", jobName)
                                  .setParameter("seqName", seqName)
                                  .getSingleResult();
        } catch (NoResultException ex) {
            result = Sync.builder().name(jobName).key(seqName).value(initialSeqValue).build();
            entityManager.persist(result);
        }

        return result;
    }

    protected abstract void process(Sync sync);
}
