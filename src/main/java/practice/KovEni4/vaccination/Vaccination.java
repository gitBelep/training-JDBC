package practice.KovEni4.vaccination;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Vaccination {
    private Map<LocalTime, Person> vaccinations = new LinkedHashMap<>();
    private MetaData metaData;

    public void readFromFile(BufferedReader br) {
        try {
            getMetaDataFromHeader(br);
            getPersons(br);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot Read Datafile", e);
        }
    }

    private void getMetaDataFromHeader(BufferedReader br) throws IOException {
        String[] row1 = br.readLine().split(" ");
        String zip = row1[2].substring(0, 4);
        String city = row1[3];
        String[] row2 = br.readLine().split("-");
        int year = Integer.parseInt(row2[0].substring(row2[0].length() - 4));
        LocalDate day = LocalDate.of(year, Integer.parseInt(row2[1]), Integer.parseInt(row2[2]));
        metaData = new MetaData(zip, city, day);
        br.readLine();
        br.readLine();
    }

    private void getPersons(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            String[] temp = data[0].split(":");
            LocalTime t = LocalTime.of(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            int zip = Integer.parseInt(data[2]);
            int age = Integer.parseInt(data[3]);
            String taj = data[5]; //don't validate!
            Person p = new Person(t, data[1], zip, age, data[4], taj);
            vaccinations.put(t, p);
            if (data.length == 7) {
                p.setType(VaccinationType.valueOf(data[6]));
            } else {
                p.setType(VaccinationType.NONE);
            }
        }
    }

    public void validateTaj() {
        StringBuilder sb = new StringBuilder();
        boolean thereIsMore = false;
        for(Map.Entry<LocalTime, Person> e : vaccinations.entrySet()){
            String s = e.getValue().getTaj();
            int sum = 0;
            for (int i = 0; i <= 7; i++) {
                if (i % 2 == 0) {
                    sum += (Integer.parseInt(String.valueOf(s.charAt(i)))) * 3;
                } else {
                    sum += (Integer.parseInt(String.valueOf(s.charAt(i)))) * 7;
                }
            }
            String nineth = "" + sum % 10;
            if( !nineth.equals(s.substring(8))){
                if(thereIsMore){
                    sb.append(", ");
                }
                sb.append(s);
                thereIsMore = true;
            }
        }
        if(sb.length() > 0) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public String inviteExactPerson(LocalTime time){
        String groundText = "Kedves *&*! Ön következik. Kérem, fáradjon be!";
        String name ="";
        for(Map.Entry<LocalTime, Person> e : vaccinations.entrySet()){
            if(e.getValue().getTime().equals(time)){
                name = e.getValue().getName();
                break;
            }
        }
        return groundText.replace("*&*", name);
    }

    public LocalDate getDateOfVaccination(){
        return metaData.getDate();
    }

    public Map<VaccinationType, Integer> getVaccinationStatistics(){
        Map<VaccinationType, Integer> result = new HashMap<>();
        for( Person p : vaccinations.values()){
            if(!result.containsKey( p.getType())){
                result.put(p.getType(), 0);
            }
            result.put(p.getType(), result.get(p.getType()) + 1);
        }
        return result;
    }

    public Map<LocalTime, Person> getVaccinations() {
        return vaccinations;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public List<Person> getPersonsMoreThanHundredYearsOld(){
        List<Person> result = new ArrayList<>();
        for(Map.Entry<LocalTime, Person> e : vaccinations.entrySet()){
            if(e.getValue().getAge() > 100){
                result.add(e.getValue());
            }
        }
        return result;
    }

    public List<Person> getAfternoonPersons(){
        List<Person> result = new ArrayList<>();
        for(Map.Entry<LocalTime, Person> e : vaccinations.entrySet()){
            if(e.getValue().getTime().isAfter(LocalTime.of(12,0))){
                result.add(e.getValue());
            }
        }
        return result;
    }

}
