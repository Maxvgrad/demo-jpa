package ru.demo.jpa.apps;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class QueryRunnerApp extends BaseApp {

    public QueryRunnerApp(String persistenceUnitName) {
        super(persistenceUnitName);
    }

    @Override
    protected void compute() throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            while(true) {

                log.info("Sql query: ");
                String sql = reader.readLine();

                if (sql == null || sql.isEmpty()) {
                    break;
                }

                List result = getEntityManager().createQuery(sql).getResultList();
                result.forEach(o -> log.info("{}", o));

                log.info("Result size: {}", result.size());
            }

        }
    }
}
