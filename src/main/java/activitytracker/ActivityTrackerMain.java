package activitytracker;

import org.mariadb.jdbc.MariaDbDataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class ActivityTrackerMain {

    public static void main(String[] args) {
        MariaDbDataSource ds;
        try {
            ds = new MariaDbDataSource();
            ds.setUrl("jdbc:mariadb://localhost:3306/activitytracker?useUnicode=true");
            ds.setUser("activitytracker");
            ds.setPassword("activitytracker");
        } catch (SQLException sq) {
            throw new IllegalStateException("Cannot connect", sq);
        }

//        ActivityTrackerMain am = new ActivityTrackerMain();   //DAO class létrejöttével nem ez kell
        ActivityDao dao = new ActivityDao(ds);

        Activity a1 = new Activity(LocalDateTime.of(2010, 1, 23, 10, 7, 0), "Bicó Zemplén", Type.BIKING);
        Activity a2 = new Activity(LocalDateTime.of(2011, 2, 23, 10, 7, 0), "gyalog Börzsöny", Type.HIKING);
        Activity a3 = new Activity(LocalDateTime.of(2012, 3, 23, 10, 7, 0), "kosaraztunk", Type.BASKETBALL);
        Activity a4 = new Activity(LocalDateTime.of(2013, 4, 23, 10, 7, 0), "Bicó Zemplén", Type.BIKING);
//        am.insertActivity(ds, a2);
//        dao.insertActivity(a1);
//        dao.insertActivity(a2);
//        dao.insertActivity(a3);
//        dao.insertActivity(a4);
//
//        Activity a5 = new Activity(LocalDateTime.of(2020, 5, 23, 10, 7, 0), "Bicó Bük", Type.BIKING);
//        dao.insertActivity(a5);
        System.out.println(dao.selectById(4));
        System.out.println(dao.selectActivityByType(Type.BIKING));
        System.out.println(dao.selectAllActivities());
    }

}
