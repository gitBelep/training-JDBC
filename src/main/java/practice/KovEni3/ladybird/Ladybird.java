package practice.KovEni3.ladybird;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Ladybird {
    private final DataSource ds;

    public Ladybird(DataSource ds) {
        this.ds = ds;
    }

    public List<String> getLadybirdsWithExactNumberOfPoints(int points) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT hungarian_name FROM ladybirds WHERE number_of_points = ?"
             )) {
            ps.setInt(1, points);
            return getLadybirdList(ps);
        } catch (SQLException e) {
            throw new IllegalStateException("Wrong statement", e);
        }
    }

    private List<String> getLadybirdList(PreparedStatement ps) {
        List<String> result = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("hungarian_name");
                result.add(name);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

    public Map<Integer, Integer> getLadybirdsByNumberOfPoints() {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT number_of_points FROM ladybirds"
             )) {
            return getLadybirdsWithExistingPoints(ps);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

    private Map<Integer, Integer> getLadybirdsWithExistingPoints(PreparedStatement ps) {
        Map<Integer, Integer> result = new TreeMap<>();
        try(ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                int point = rs.getInt("number_of_points");
                if(!result.containsKey(point)){
                    result.put(point, 0);
                }
                result.put(point, result.get(point)+1);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

    public Set<Ladybug> getLadybirdByPartOfLatinNameAndNumberOfPoints(String partOfName, int numberOfPoints){
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT *  FROM ladybirds WHERE number_of_points = ? AND latin_name LIKE ?"
             )) {
            ps.setInt(1, numberOfPoints);
            String concatted = "%"+ partOfName +"%";
            ps.setString(2, concatted);
            return getLadybirdsMapByLatinName(ps);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

    private Set<Ladybug> getLadybirdsMapByLatinName(PreparedStatement ps){
        Set<Ladybug> result = new HashSet<>();
        try(ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                int point = rs.getInt("number_of_points");
//                long id = rs.getLong("id");
                String hungarianName = rs.getString("hungarian_name");
                String latinName = rs.getString("latin_name");
                String genus = rs.getString("genus");

                result.add(new Ladybug(hungarianName, latinName, genus, point));
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

    public Map<String,Integer> getLadybirdStatistics(){
        try(Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(genus) AS C, genus FROM ladybirds GROUP BY genus;")){
            return makeStatistic(ps);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

    private Map<String,Integer> makeStatistic(PreparedStatement ps) {
        Map<String, Integer> result = new TreeMap<>();
        try (ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                String genus = rs.getString("genus");
//                int count = rs.getInt("COUNT(genus)");
                int count = rs.getInt("C");
                result.put(genus,count);
            }
            return result;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot read file", e);
        }
    }

}
