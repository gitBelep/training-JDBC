package practice;

import java.io.*;

public class UpperChar {

    public int countUpperChars(){
        try (BufferedReader br = new BufferedReader(new InputStreamReader(UpperChar.class
                .getResourceAsStream("/upperChar.txt")))) {
            String line;
            int sum = 0;
            while((line = br.readLine()) != null) {
                sum += uppercaseCounter(line);
            }
            return sum;
        }catch (IOException ioe) {
            throw new IllegalStateException("Cannot read file", ioe);
        }
    }

    private int uppercaseCounter(String line){
        char[] chars = line.toCharArray();
        int nrOfUppercases = 0;
        for (Character c : chars){
            Character up = Character.toUpperCase(c); //Uppercase of Number or Sign equals Number or Sign!
            if( Character.isLetter(c) && up.equals(c) ){
                nrOfUppercases++;
            }
        }
        return nrOfUppercases;
    }
}
