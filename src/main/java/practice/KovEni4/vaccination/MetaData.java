package practice.KovEni4.vaccination;

import java.time.LocalDate;
import java.util.Objects;

public class MetaData {
    private final String postalCode;
    private final String townName;
    private final LocalDate date;

    public MetaData(String postalCode, String townName, LocalDate date) {
        this.postalCode = postalCode;
        this.townName = townName;
        this.date = date;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTownName() {
        return townName;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaData metaData = (MetaData) o;
        return postalCode == metaData.postalCode && Objects.equals(townName, metaData.townName) && Objects.equals(date, metaData.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postalCode, townName, date);
    }
}
