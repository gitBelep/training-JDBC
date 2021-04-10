package practice.week14d02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingTest {
    Shopping sh = new Shopping();

    @BeforeEach
    void setUp() {
        sh.readFile();
    }

    @Test
    public void readFile() {
//        Shopping sh = new Shopping();
//        sh.readFile();
        assertEquals(9, sh.getBills().size());
    }

    @Test
    public void testSum() {
        assertEquals(200, sh.getBills().get(0).getItems().get(0).getPrice());
    }

    @Test
    void sumOfABill() {
        assertEquals(850, sh.sumOfABill("111"));
    }

    @Test
    void sumPerCustomer() {
        assertEquals(1250 ,sh.sumPerCustomer("RA22"));
    }

    @Test
    void sortingItemsOfABill() {
        assertEquals(200, sh.sortingItemsOfABill("1145", Sorter.PRICE).get(0).getPrice());
        assertEquals(1300, sh.sortingItemsOfABill("1145", Sorter.PRICE).get(4).getPrice());

        assertEquals("bread",sh.sortingItemsOfABill("1145", Sorter.PRODUCT_NAME).get(0).getProduct());
    }

    @Test
    void quantityOfOrderedProduct() {
        assertEquals(3, sh.quantityOfOrderedProduct("bread") );
    }

    @Test
    void statisticOrderedProducts() {
        Map<String, Integer> result = sh.statisticOrderedProducts();
        assertEquals(27, result.size());

        System.out.println(result.toString());
    }

}
