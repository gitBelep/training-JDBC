package practice.week14d02;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private String customer;
    private String basket;
    List<Item> items = new ArrayList<>();

    public Bill(String customer, String basket, List<Item> items) {
        this.customer = customer;
        this.basket = basket;
        this.items = items;
    }

    public String getCustomer() {
        return customer;
    }

    public String getBasket() {
        return basket;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return customer +"-"+ basket + items.toString();
    }
}
