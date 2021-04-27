package practice.KovEni3.ladybird;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.*;

public class LadybirdTest {

    private Ladybird ladybird;

    @BeforeEach
    public void setUp() {
        try {
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl("jdbc:mariadb://localhost:3306/employees?useUnicode=true");
            dataSource.setUser("employees");
            dataSource.setPassword("employees");

            Flyway fw = Flyway.configure()
                    .locations("/db/migration/katica")
                    .dataSource(dataSource).load();
            fw.clean();
            fw.migrate();

            ladybird = new Ladybird(dataSource);

        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not get data.", sqle);
        }
    }

    @Test
    public void testGetLadybirdsWithExactNumberOfPoints() {
        List<String> ladybirds = ladybird.getLadybirdsWithExactNumberOfPoints(11);

        Assertions.assertEquals(5, ladybirds.size());
        Assertions.assertEquals("sziki tizenegypettyes katica", ladybirds.get(2));
    }

    @Test
    public void testGetLadybirdsWithNotExistingNumberOfPoints() {
        List<String> ladybirds = ladybird.getLadybirdsWithExactNumberOfPoints(100);

        Assertions.assertEquals(0, ladybirds.size());
    }

    @Test
    public void testGetLadybirdsByNumberOfPoints() {
        Map<Integer, Integer> numberOfLadybirdsByPoints = ladybird.getLadybirdsByNumberOfPoints();

        Assertions.assertEquals(1, numberOfLadybirdsByPoints.get(24));
        Assertions.assertEquals(5, numberOfLadybirdsByPoints.get(11));
        Assertions.assertEquals(2, numberOfLadybirdsByPoints.get(7));
        System.out.println(numberOfLadybirdsByPoints.toString());
    }

    @Test
    public void testGetLadybirdByPartOfLatinNameAndNumberOfPoints() {
        Collection<Ladybug> ladybirds = ladybird.getLadybirdByPartOfLatinNameAndNumberOfPoints("Scymnus", 2);
        Assertions.assertEquals(4, ladybirds.size());
        Assertions.assertFalse(ladybirds.contains(new Ladybug("sárgafejű bödice", "(Scymnus auritus)", "bödiceformák (Scymninae)", 0)));
 //Csak úgy fut le,mert equals és hash alapján dolgozik,ha felülírom! Különben memóriacímet néz?
        Assertions.assertTrue(ladybirds.contains(new Ladybug("ostoros bödice", "(Scymnus flagellisiphonatus)", "bödiceformák (Scymninae)", 2)));
    }

    @Test
    public void testGetLadybirdStatistics() {
        Map<String, Integer> ladybirdStatistics = ladybird.getLadybirdStatistics();

        Assertions.assertEquals(36, ladybirdStatistics.get("katicaformák (Coccinellinae)"));
        Assertions.assertEquals(5, ladybirdStatistics.get("szerecsenkata-formák (Chilocorinae)"));
    }
}
