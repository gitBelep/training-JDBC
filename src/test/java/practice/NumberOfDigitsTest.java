package practice;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumberOfDigitsTest {

    @Test
    void getNumberOfDigits() {
        int e1 = new NumberOfDigits().getNumberOfDigits(11);
        System.out.println("11 ~ "+ e1 +" digits");System.out.println();
        assertEquals(13, e1);

        int e2 = new NumberOfDigits().getNumberOfDigits(102);
        System.out.println("102 ~ "+ e2 +" digits");System.out.println();
            //full = 9*10*2 + 9*1*1 = 189
            //rest = (102+1 -100) *3 = 9
        assertEquals(198, e2);

        int e3 = new NumberOfDigits().getNumberOfDigits(1002);
        System.out.println("1002 ~ "+ e3 +" digits");System.out.println();
            //2700+180+9   +  3*3
        assertEquals(2901, e3);

        int e4 = new NumberOfDigits().getNumberOfDigits(1223);
        System.out.println("1223 ~ "+ e4 +" digits");System.out.println();
        assertEquals(3785, e4);

        int e5 = new NumberOfDigits().getNumberOfDigits(15522);
        System.out.println("15522 ~ "+ e5 +" digits");System.out.println();
        assertEquals(66504, e5);
    }

}
