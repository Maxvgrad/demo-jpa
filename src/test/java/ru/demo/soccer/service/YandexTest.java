package ru.demo.soccer.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ru.demo.jpa.common.BaseJpaTests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class YandexTest extends BaseJpaTests {


    /**
     * OneEditApart("cat", "cat") -> true
     * OneEditApart("cat", "dog") -> false
     * OneEditApart("cat", "cats") -> true
     * OneEditApart("cat", "cut") -> true
     * OneEditApart("cat", "cast") -> true
     * OneEditApart("cat", "at") -> true
     * OneEditApart("cat", "acts") -> false
     *
     */

    @Test
    void t1() {
        assertTrue(OneEditApart("cat", "cat"));
    }
    @Test
    void t2() {
        assertFalse(OneEditApart("cat", "dog"));
    }
    @Test
    void t3() {
        assertTrue(OneEditApart("cat", "cats"));
    }
    @Test
    void t4() {
        assertTrue(OneEditApart("cat", "cut"));
    }
    @Test
    void t5() {
        assertTrue(OneEditApart("cat", "cast"));
    }
    @Test
    void t6() {
        assertTrue(OneEditApart("cat", "at"));
    }
    @Test
    void t7() {
        assertFalse(OneEditApart("cat", "acts"));
    }


    public boolean OneEditApart(String str1, String str2) {


        for (int i = 0, j = 0, c = 0; i < str1.length() && j < str2.length(); i++, j++) {

            if (str1.charAt(i) != str2.charAt(j)) {

                if (str1.length() >= str2.length()) i++;

                if (str1.length() <= str2.length()) j++;

                if (++c == 2) return false;
            }

        }

        return true;
    }


}