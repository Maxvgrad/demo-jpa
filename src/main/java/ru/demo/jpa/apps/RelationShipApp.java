package ru.demo.jpa.apps;

import lombok.RequiredArgsConstructor;
import ru.demo.jpa.entities.Account;
import ru.demo.jpa.entities.Address;
import ru.demo.jpa.entities.Book;
import ru.demo.jpa.entities.ParkingLot;
import ru.demo.jpa.entities.Pattern;
import ru.demo.jpa.entities.Provider;
import ru.demo.jpa.entities.Role;
import ru.demo.jpa.entities.Student;
import ru.demo.jpa.entities.University;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class RelationShipApp implements Runnable {

    private final String persistenceUnitName;

    @Override
    public void run() {

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {

            emf = Persistence.createEntityManagerFactory(persistenceUnitName);

            em = emf.createEntityManager();

            EntityTransaction transaction = em.getTransaction();

            transaction.begin();

            Book book = Book.builder().title("Fathers and sons").releaseDate(Date.valueOf(LocalDate.now())).build();
            Role role = Role.builder().comment("comment").sid("role").build();
            Account account = Account.builder().serialNumber("num").currency("USD").build();


            Address address = Address.builder().city("Tomks").state("Tomsk obl.").street("Lenin").zip("634049").build();
            Address studentAddr = Address.builder().city("Saint-Petersburg").state("Leningradskaya obl.").street("Ilushina")
                                         .zip("731919").build();

            University university = University.builder().name("MIT").address(address).build();
            ParkingLot parkingLot = ParkingLot.builder().lot(50L).build();
            Student student = Student.builder().address(studentAddr).parkingLot(parkingLot).university(university)
                                     .firstName("fName").lastName("lName").email("test@mail.com").build();
            em.persist(book);
            em.persist(role);
            //em.persist(account);

            Collection<ParkingLot> parkingLots = Arrays.asList(ParkingLot.builder().lot(3L).owner(student).build(),
                                                               ParkingLot.builder().lot(4L).build());
            parkingLots.forEach(em::persist);

            university.setParkingLots(parkingLots);

            em.persist(university);
            em.persist(parkingLot);
            System.out.println(university);
            em.persist(student);


            Collection<Pattern> patterns = Arrays
                    .asList(Pattern.builder().name("invoice").build(), Pattern.builder().name("act").build());
            patterns.forEach(em::persist);


            Collection<Provider> providers = Arrays
                    .asList(Provider.builder().name("sim-provider").patterns(patterns).build(),
                            Provider.builder().name("modem-provider").patterns(patterns).build());

            providers.forEach(em::persist);

            System.out.println(em.find(Student.class, student.getId()));
            System.out.println(em.find(ParkingLot.class, parkingLot.getId()));
            System.out.println(em.find(University.class, university.getId()));

            transaction.commit();

        } finally {
            Optional.ofNullable(em).ifPresent(EntityManager::close);
            Optional.ofNullable(emf).ifPresent(EntityManagerFactory::close);
        }


    }
}
