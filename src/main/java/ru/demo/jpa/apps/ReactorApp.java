package ru.demo.jpa.apps;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.demo.jpa.entities.elementcollection.Department;
import ru.demo.jpa.entities.elementcollection.MaintenanceEmployee;
import ru.demo.jpa.entities.elementcollection.NuclearPowerPlant;
import ru.demo.jpa.entities.elementcollection.PhoneType;
import ru.demo.jpa.entities.elementcollection.Reactor;
import ru.demo.jpa.entities.elementcollection.Tvs;
import ru.demo.jpa.services.ReactorService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ReactorApp implements Runnable {

    private final String persistenceUnitName;

    public void run() {

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName);
            em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();
            transaction.begin();

            ReactorService reactorService = new ReactorService(em);

            NuclearPowerPlant npp = NuclearPowerPlant.builder()
                                                     .type("VVER")
                                                     .build();

            em.persist(npp);


            if(true) {
                transaction.commit();
            }

            transaction = em.getTransaction();
            transaction.begin();

            Reactor reactor = reactorService
                    .create(npp, Set.of(Tvs.builder().position("L1").build(), Tvs.builder().position("L2").build()),
                                   Set.of("tvs1", "tvs2", "tvs3"));

            reactor = reactorService.findById(reactor.getId());

            log.info("#run: {}", reactor);


            List<Reactor> reactors = Arrays
                    .asList(reactor, reactorService.create(npp, Collections.emptySet(), Collections.emptySet()),
                                                   reactorService.create(npp,
                                                           Collections.emptySet(),
                                                           Collections.emptySet()),
                                                   reactorService.create(npp,
                                                           Collections.emptySet(),
                                                           Collections.emptySet()),
                                                   reactorService.create(npp,
                                                           Collections.emptySet(),
                                                           Collections.emptySet()));

            if(true) {
                transaction.commit();
            }

            transaction = em.getTransaction();
            transaction.begin();

            npp = em.find(NuclearPowerPlant.class, npp.getId());

            log.info("#run: --------------------------------------");
            Optional.ofNullable(npp.getReactors()).ifPresent(r -> r.forEach(System.out::println));

            transaction.commit();

            em.getTransaction().begin();

            log.info("#run: test Employee phones mapping");

            MaintenanceEmployee employee = MaintenanceEmployee.builder().name("Bob").phones(
                    Map.of(
                            PhoneType.WORK, "65",
                            PhoneType.HOME, "66",
                            PhoneType.MOBILE, "67"
                           )
            ).build();

            em.persist(employee);

            employee = em.find(MaintenanceEmployee.class, employee.getId());
            log.info("#run: {}", employee);

            em.getTransaction().commit();


            em.getTransaction().begin();

            log.info("#run: test Employee department map");

            NuclearPowerPlant nuclearPowerPlant = NuclearPowerPlant.builder().type("RBMK").build();

            em.persist(nuclearPowerPlant);

            List.of(MaintenanceEmployee.builder().name("Jon").department(Department.CC).nuclearPowerPlant(Collections.singleton(nuclearPowerPlant)).build(),
                    MaintenanceEmployee.builder().name("Don").department(Department.RC).nuclearPowerPlant(Collections.singleton(nuclearPowerPlant)).build(),
                    MaintenanceEmployee.builder().name("Kim").department(Department.TC).nuclearPowerPlant(Collections.singleton(nuclearPowerPlant)).build())
                .forEach(em::persist);

            em.getTransaction().commit();


            em.clear();


            log.info("#run: ###########################");

            nuclearPowerPlant = em.find(NuclearPowerPlant.class, nuclearPowerPlant.getId());
            log.info("#run: {}", nuclearPowerPlant.getEmployee());


        } catch (Throwable ex) {
            log.error("#run: ", ex);
            throw new IllegalArgumentException(ex);
        } finally {
            Optional.ofNullable(em).ifPresent(EntityManager::close);
            Optional.ofNullable(emf).ifPresent(EntityManagerFactory::close);
        }
    }

}
