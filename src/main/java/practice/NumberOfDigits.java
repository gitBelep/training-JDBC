package practice;

public class NumberOfDigits {

    public int getNumberOfDigits(int income){
        int lengthOfNumber = getLengthOfNumber(income);

        int fullPart = 0;
        int rounds = lengthOfNumber;
        while (rounds > 1) {
            fullPart += 9 * (int)Math.pow(10, rounds - 2.0) * (rounds - 1);
//            System.out.println("9 * "+ (int)Math.pow(10, rounds - 2.0) +" * "+ (rounds-1) +" = "+ fullPart);
            rounds--;
        }
        int rest = (income + 1 - (int)Math.pow(10, lengthOfNumber - 1.0) ) * lengthOfNumber;
//        System.out.println("+ (1 + "+ income +" - "+ (int)Math.pow(10, lengthOfNumber - 1.0) +") *"+ lengthOfNumber +" = "+ rest);
        return fullPart + rest;
    }

    private int getLengthOfNumber(int income){
        int digit = 1;
        for( ; income >= 10; digit++){
            income /= 10;
        }
        return digit;
    }

}
