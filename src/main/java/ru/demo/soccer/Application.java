package ru.demo.soccer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.demo.soccer.config.AppConfig;
import ru.demo.soccer.dao.GenericDao;
import ru.demo.soccer.entities.Sync;

@Slf4j
@Component
public class Application {

    @Autowired
    private GenericDao<Sync, Integer> syncRepository;

    public static void main(String[] args) {
        Application application = new Application();
        application.run();
    }

    private void run() {
        try {

            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);


            Application syncRepository = applicationContext.getBean(Application.class);

            syncRepository.syncRepository.findById(1);

            if (true) return;

            Runtime.getRuntime()
                   .addShutdownHook(new Thread(() -> {
                       log.info("#run: shutdown hook");
                       ((AnnotationConfigApplicationContext) applicationContext).close();
                   }));

            Thread.currentThread().join();

        } catch (Exception ex) {
            log.error("#run: ex({})", ex.getLocalizedMessage());
            throw new IllegalStateException(ex);
        }
    }
}
