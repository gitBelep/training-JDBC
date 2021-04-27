package practice.KovEni3.ladybird;

import java.util.Objects;

public class Ladybug {
    private long id;
    private String hungarianName;
    private String latinName;
    private String genus;
    private int nrOfPoints;

    public Ladybug(String hungarianName, String latinName, String genus, int nrOfPoints) {
        this.hungarianName = hungarianName;
        this.latinName = latinName;
        this.genus = genus;
        this.nrOfPoints = nrOfPoints;
    }

    public Ladybug(long id, String hungarianName, String latinName, String genus, int nrOfPoints) {
        this.id = id;
        this.hungarianName = hungarianName;
        this.latinName = latinName;
        this.genus = genus;
        this.nrOfPoints = nrOfPoints;
    }

    public long getId() {
        return id;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public String getLatinName() {
        return latinName;
    }

    public String getGenus() {
        return genus;
    }

    public int getNrOfPoints() {
        return nrOfPoints;
    }

//    @Override
//    public String toString() {
//        return  hungarianName + " " + latinName + " " + genus + " " + nrOfPoints;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ladybug ladybug = (Ladybug) o;
        return nrOfPoints == ladybug.nrOfPoints && Objects.equals(hungarianName, ladybug.hungarianName) && Objects.equals(latinName, ladybug.latinName) && Objects.equals(genus, ladybug.genus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hungarianName, latinName, genus, nrOfPoints);
    }
}
