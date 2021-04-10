package activitytracker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Activity {
    private List<TrackPoint> trackPoint = new ArrayList<>();

    private long id;
    private final LocalDateTime startTime;
    private final String desc;
    private final Type type;

    public Activity(LocalDateTime startTime, String desc, Type type) {
        this.startTime = startTime;
        this.desc = desc;
        this.type = type;
    }

    public Activity(long id, LocalDateTime startTime, String desc, Type type) {
        this.id = id;
        this.startTime = startTime;
        this.desc = desc;
        this.type = type;
    }

    public List<TrackPoint> getTrackPoint() {
        return new ArrayList<>(trackPoint);
    }

    public void setTrackPoint(List<TrackPoint> trackPoint) {
        this.trackPoint = trackPoint;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getDesc() {
        return desc;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return id +" "+startTime +" "+ desc +" "+ type;
    }
}
