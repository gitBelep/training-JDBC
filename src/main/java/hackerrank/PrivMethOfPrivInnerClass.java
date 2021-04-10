package hackerrank;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PrivMethOfPrivInnerClass {

    public static void main(String[] args) throws Exception {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int num = Integer.parseInt(br.readLine().trim());
            Object o;// Must be used to hold the reference of the instance of the class Solution.Inner.Private
//Till this point the code was given
//1. megoldás)
//            o = new PrivMethOfPrivInnerClass.Inner().new Private();
//            System.out.println(num + " is " + ( (PrivMethOfPrivInnerClass.Inner.Private) o ).powerof2(num));
//            System.out.println("An instance of class: " + o.getClass().getCanonicalName() + " has been created");

// kifejtve)
            PrivMethOfPrivInnerClass.Inner.Private ip = new PrivMethOfPrivInnerClass.Inner().new Private();
            String s = ip.powerof2(num);
            System.out.println(num +" is "+ s);
            o = ip;
//Also given code:
            System.out.println("An instance of class: " + o.getClass().getCanonicalName() + " has been created");

////2)
//            System.out.println(num +" is "+ ((Inner.Private)(o = (Object) new PrivMethOfPrivInnerClass.Inner().new Private())).powerof2(num));
//            System.out.println("An instance of class: " + o.getClass().getCanonicalName() + " has been created");
////3)
//            System.out.println(num +" is "+ ((Inner.Private) (o = new Inner().new Private())).powerof2(num));
//            System.out.println("An instance of class: " + o.getClass().getCanonicalName() + " has been created");
    }//end of main

    static class Inner{

        private class Private{

            private String powerof2(int num){
                return ((num&num-1)==0)?"power of 2":"not a power of 2";
            }
        }
    }//end of Inner

}//end of aa
