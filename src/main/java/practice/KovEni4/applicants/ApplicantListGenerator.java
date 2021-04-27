package practice.KovEni4.applicants;

import javax.sql.DataSource;
import java.util.List;

public interface ApplicantListGenerator {

    List<Applicant> getListFromDatabase(DataSource ds);

}
