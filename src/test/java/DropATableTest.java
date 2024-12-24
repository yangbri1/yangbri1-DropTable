import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DropATableTest {

    /**
     * The before annotation runs before every test so that way we drop the tables to avoid conflicts in future tests
     */
    @Before
    public void beforeTest(){
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "CREATE TABLE song (Title varchar(100), Artist varchar(100));";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     * The after annotation runs after every test so that way we drop the tables to avoid conflicts in future tests
     */
    @After
    public void afterEach(){

        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DROP TABLE IF EXISTS song;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();

        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    /**
     *  In programming we utilize try / catch constructs to catch when there are potential for errors / exceptions.
     *  For this test, if I am able to insert a song into the songs table, then the songs table was never dropped and the test should fail.
     */
    @Test
    public void dropTableTest(){
        try {
            String sql = FileUtil.parseSQLFile("lab1.sql");
            try {
                Connection connection = ConnectionUtil.getConnection();
                Statement s = connection.createStatement();
                s.executeUpdate(sql);
            } catch (SQLException e) {
                System.out.println("problem1: " + e.getMessage() + '\n');
            }
            Connection connection = ConnectionUtil.getConnection();
            String sql2 = "INSERT INTO song (Title, Artist) VALUES ('Let it be', 'Beatles');";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("problem1: Table 'song' was not dropped.");
            Assert.fail();
        } catch (SQLException e) {}
    }
}
