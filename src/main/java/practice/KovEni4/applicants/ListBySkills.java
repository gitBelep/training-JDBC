package practice.KovEni4.applicants;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListBySkills implements ApplicantListGenerator{

    @Override
    public List<Applicant> getListFromDatabase(DataSource ds) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT first_name, last_name, skill FROM applicants WHERE LENGTH( skill) = 3;"
             )) {
            return getApplicantsList(ps);
        } catch (SQLException se) {
            throw new IllegalStateException("Cannot read file");
        }
    }

    private List<Applicant> getApplicantsList(PreparedStatement ps) throws SQLException {
        List<Applicant> result = new ArrayList<>();
        try(ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String skill = rs.getString("skill");
                result.add(new Applicant(firstName, lastName, skill));
            }
        }
        return result;
    }

}
