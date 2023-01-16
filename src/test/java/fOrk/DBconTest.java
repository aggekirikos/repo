package fork;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBconTest {
    Connection connection;
    Statement statement;

    @Before
    public void before() {
        connection = DBcon.openConnection();
    }

    @After
    public void after() {
        DBcon.closeConnection(connection);
    }

    @Test
    public void closeStatementShouldCloseStatement() {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Could not create Statement" + e.getMessage());
        }
        DBcon.closeStatement(statement);
        try {
            assertTrue(statement.isClosed());
        } catch (SQLException e) {
            System.out.println("Could not close Statement" + e.getMessage());
        }
    }

    @Test
    public void closeStatementWithNullShouldNotThrow() {
        DBcon.closeStatement(null);
    }
}
