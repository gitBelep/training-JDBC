package practice.KovEni4.vaccination;

import java.time.LocalTime;

public class Person {
    private final LocalTime time;
    private final String name;
    private final int zip;
    private final int age;
    private final String email;
    private final String taj;
    private VaccinationType type;

    public Person(LocalTime time, String name, int zip, int age, String email, String taj) {
        this.time = time;
        this.name = name;
        this.zip = zip;
        this.age = age;
        this.email = email;
        this.taj = taj;
    }

    public void setType(VaccinationType type) {
        this.type = type;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getZip() {
        return zip;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getTaj() {
        return taj;
    }

    public VaccinationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return time +" "+ zip +" "+ name +" "+ age +" "+ type;
    }

}
