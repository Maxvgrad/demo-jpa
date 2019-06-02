package ru.demo.springframework.until;

import org.junit.jupiter.api.Test;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClassUtilsTests {

    @Test
    void getAllInterfaces() {
        // when
        Class[] interfaces = ClassUtils.getAllInterfaces(new PgDataBase());
        // then
        assertTrue(Arrays.stream(interfaces).anyMatch(Serializable.class::equals));
        assertTrue(Arrays.stream(interfaces).anyMatch(AutoCloseable.class::equals));
        assertFalse(Arrays.stream(interfaces).anyMatch(DataBase.class::equals)); // DataBase is not an interface
    }

    abstract class DataBase implements AutoCloseable {

    }

    class PgDataBase extends DataBase implements Serializable {
        @Override
        public void close() throws Exception {

        }
    }

}
