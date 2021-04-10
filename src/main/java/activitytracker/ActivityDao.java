package activitytracker;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ActivityDao {
    private final DataSource ds;

    public ActivityDao(DataSource ds) {
        this.ds = ds;
    }

//Ha BÁRMI hiba adódik, semmit nem menthet! Tehát: Ugyanazzal a Connectionnal
//kell menteni Activity-t (megszerezni a kulcsát), és a TrackPointjait.
    public Activity saveActivity(Activity rowA) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Activity actualActivity = saveActivityToDB(conn, rowA);   //privát
                if (!actualActivity.getTrackPoint().isEmpty()) {
                    saveTrackPointsToDB(conn, actualActivity);            //privát
                }
                conn.commit();
                return actualActivity;
            } catch (IllegalArgumentException ie) {
                conn.rollback();
                throw new IllegalStateException("Cannot insert Activity, rollback");
            }
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot insert Activity", se);
        }
    }

    private Activity saveActivityToDB(Connection conn, Activity rowA) {
        Activity actualActivity = rowA;
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO activities(start_time, activity_desc, activity_type) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setTimestamp(1, Timestamp.valueOf(rowA.getStartTime()));
            stmt.setString(2, rowA.getDesc());
            stmt.setString(3, rowA.getType().toString());
            stmt.executeUpdate();

            long activityId = executeAndGetKey(stmt);                 //privát
            actualActivity = new Activity(activityId, rowA.getStartTime(), rowA.getDesc(), rowA.getType());
            actualActivity.setTrackPoint(rowA.getTrackPoint());
        } catch (SQLException sqle) {
            throw new IllegalArgumentException("Error by insert", sqle);
        }
        return actualActivity;
    }

    private long executeAndGetKey(PreparedStatement ps) {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getLong("id");
            }
            throw new IllegalArgumentException("Cannot get id");
        } catch (SQLException sqle) {
            throw new IllegalStateException("Error by insert", sqle);
        }
    }

    public void saveTrackPointsToDB(Connection conn, Activity a) {
        try (PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO track_point(tr_time, tr_lat, tr_lon, a_id) VALUES (?,?,?,?)")
        ) {
            for (TrackPoint tp : a.getTrackPoint()) {
                if (tp.getLat() < -90 || 90 < tp.getLat() || tp.getLon() < -180 || 180 < tp.getLon()) {
                    throw new IllegalArgumentException("Wrong Data");
                }
                stmt.setTimestamp(1, Timestamp.valueOf(tp.getTime()));
                stmt.setDouble(2, tp.getLat());
                stmt.setDouble(3, tp.getLon());
                stmt.setLong(4, a.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot insert TP", se);
        }
    }

    public List<Activity> activitiesBeforeDate(LocalDate lDate) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM activities WHERE start_time < ?")
        ) {
            LocalDateTime actualDate = lDate.atTime(0, 0, 0);//LocalDate-ból LocalDateTime.  Ez előttieket kérem!
            ps.setTimestamp(1, Timestamp.valueOf(actualDate));
            return selectByPreparedStatement(ps);
        } catch (SQLException se) {
            throw new IllegalArgumentException("Cannot connect", se);
        }
    }

    public List<Activity> selectActivityByType(Type type) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT * FROM activities WHERE activity_type = ?")
        ) {
            ps.setString(1, type.toString());
            return selectByPreparedStatement(ps);
        } catch (SQLException se) {
            throw new IllegalArgumentException("Cannot connect", se);
        }
    }

    public List<Activity> selectAllActivities() {
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM activities")
        ) {
            return selectByPreparedStatement(ps);
        } catch (SQLException se) {
            throw new IllegalArgumentException("Cannot connect", se);
        }
    }

    public Activity selectById(long id) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM activities WHERE id = ?")
        ) {
            ps.setLong(1, id);
            return selectByPreparedStatement(ps).get(0);
        } catch (SQLException | NullPointerException e) {
            throw new IllegalArgumentException("No such id", e);
        }
    }

    private List<Activity> selectByPreparedStatement(PreparedStatement ps) {
        List<Activity> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDateTime tStamp = rs.getTimestamp("start_time").toLocalDateTime();
                String description = rs.getString("activity_desc");
                Type type = Type.valueOf(rs.getString("activity_type"));
                long aId = rs.getLong("id");
                Activity a = new Activity(aId, tStamp, description, type);
                a.setTrackPoint(getTrackPointsById(aId));
                result.add(a);
            }
            return result;
        } catch (SQLException se) {
            throw new IllegalArgumentException("Wrong statement", se);
        }
    }

    private List<TrackPoint> getTrackPointsById(long aId) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM track_point WHERE a_id = ?"
             )) {
            ps.setLong(1, aId);

            return getTrackPointList(ps);
        } catch (SQLException se) {
            throw new IllegalArgumentException("Cannot connect to TP", se);
        }
    }

    private List<TrackPoint> getTrackPointList(PreparedStatement ps) {
        List<TrackPoint> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LocalDateTime time = rs.getTimestamp("tr_time").toLocalDateTime();
                double lat = rs.getDouble("tr_lat");
                double lon = rs.getDouble("tr_lon");
                TrackPoint tr = new TrackPoint(time, lat, lon);
                tr.setId(rs.getInt("tr_id"));
                result.add(tr);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Cannot connect", e);
        }
    }

}
