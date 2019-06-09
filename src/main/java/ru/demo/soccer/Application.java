package ru.demo.soccer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.demo.soccer.config.AppConfig;

@Slf4j
@Component
public class Application {

    public static void main(String[] args) {
        Application application = new Application();
        application.run();
    }

    private void run() {
        try {

            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

            //Application syncRepository = applicationContext.getBean(Application.class);

            //syncRepository.syncRepository.findById(1);

            //if (true) return;

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
