package practice.week14d02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Shopping {
    private List<Bill> bills = new ArrayList<>();

    public void readFile(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Shopping.class
                .getResourceAsStream("/week14d02_shoppinglist.txt")))) {
            String line;
            while((line = br.readLine()) != null) {
                makeBills(line);
            }  //System.out.println("Kész. Bills: "+ bills.size());
        }catch (IOException ioe) {
            throw new IllegalStateException("Cannot read file", ioe);}
    }

    private void makeBills(String line){
        String[] temp = line.split(": ");
        String[] tempItems = temp[1].split(",");
        String[] customerAndBasket = temp[0].split("-");
        List<Item> items = makeItems(tempItems);
        bills.add(new Bill(customerAndBasket[0], customerAndBasket[1], items));
    }

    private List<Item> makeItems(String[] items){
        List<Item> result = new ArrayList<>();
        for(String s : items){
            String[] data = s.split("\\(");
            String product = data[0];
            int price = Integer.parseInt(data[1].substring(0, data[1].length()-1 ));
            result.add(new Item(product, price));
        }
        return result;
    }

    public List<Bill> getBills() {
        return new ArrayList<>(bills);
    }

    public int sumOfABill(String basket){
        int sumOfBill = 0;
        for(Bill b : bills){
            if (b.getBasket().equals(basket)) {
                for(Item i : b.getItems()){
                    sumOfBill += i.getPrice();
                }
                break;
            }
        }
        return sumOfBill;
    }

    public int sumPerCustomer(String customer){
        int sum = 0;
        for(Bill b : bills){
            if(b.getCustomer().equals(customer)){
                sum += sumOfABill(b.getBasket());
            }
        }
        return sum;
    }

    public List<Item> sortingItemsOfABill(String basket, Sorter sorter){
        List<Item> result = new ArrayList<>();
        for(Bill b : bills){
            if (b.getBasket().equals(basket)) {
                result = new ArrayList<>(b.getItems());
                break;
            }
        }
        return sortingItemsList(sorter, result);
    }

    private List<Item> sortingItemsList(Sorter sorter, List<Item> result) {
        Comparator<Item> priceComparator = (o1, o2) -> o1.getPrice() - o2.getPrice();
//        Comparator<Item> priceComparator = new Comparator<Item>() {
//            @Override
//            public int compare(Item o1, Item o2) {
//                return o1.getPrice() - o2.getPrice();
//            }
//        };
        Comparator<Item> abcComparator = (o1, o2) -> o1.getProduct().compareTo(o2.getProduct());
//        Comparator<Item> abcComparator = new Comparator<Item>() {
//            @Override
//            public int compare(Item o1, Item o2) {
//                return o1.getProduct().compareTo(o2.getProduct());
//            }
//        };

        if(sorter == Sorter.PRICE){
            result.sort(priceComparator);
        } else {
            result.sort(abcComparator);
        }
        return result;
    }

    public int quantityOfOrderedProduct(String product){
        int counter = 0;
        for(Bill b : bills){
            for (Item i : b.getItems()){
                if(i.getProduct().equals(product)){
                    counter++;
                }
            }
        }
        return counter;
    }

    public Map<String, Integer> statisticOrderedProducts(){
        Map<String, Integer> result = new TreeMap<>();
        for(Bill b : bills){
            for (Item i : b.getItems()){
                String product = i.getProduct();
                if(!result.containsKey(product)){
                    result.put(product, 1);
                } else {
                    result.put( product, result.get(product) + 1 );
                }
            }
        }
        return result;
    }

}

enum Sorter {
    PRODUCT_NAME, PRICE;
}
