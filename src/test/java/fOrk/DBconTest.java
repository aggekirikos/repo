package fOrk;

import static org.junit.Assert.assertTrue;
import java.sql.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

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
