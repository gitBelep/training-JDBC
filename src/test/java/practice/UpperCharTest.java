package practice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpperCharTest {

    @Test
    void countUpperChars() {
        UpperChar u = new UpperChar();
        assertEquals(8, u.countUpperChars());
    }
}