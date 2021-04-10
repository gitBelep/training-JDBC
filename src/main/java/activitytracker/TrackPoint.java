package activitytracker;

import java.time.LocalDateTime;

public class TrackPoint {
    private int id;
    private LocalDateTime time;
    private double lat;
    private double lon;

    public TrackPoint(LocalDateTime time, double lat, double lon) {
        this.time = time;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public double getLat() {
        return lat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return id +" "+ time +" "+ lat +" "+ lon;
    }
}
