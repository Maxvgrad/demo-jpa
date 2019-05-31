package ru.demo.soccer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.demo.soccer.config.AppConfig;

@Slf4j
public class Application {

    public static void main(String[] args) {
        Application application = new Application();
        application.run();
    }

    private void run() {
        try {

            ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

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
