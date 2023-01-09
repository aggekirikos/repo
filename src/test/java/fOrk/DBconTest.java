package fOrk;

import static org.junit.Assert.assertTrue;
import java.sql.*;
import org.junit.Test; // for @Test
import org.junit.Before; // for @Before
import org.junit.After; // for @After
/**
* Unit Test on DBcon class. This class checks the connectivity
* of the DBcon class with the database, by making a connection
* and creating a statement. When the connection is executed
* successfully, both the connection and the statement are
* closed.
*/
 public class DBconTest {
    Connection connection;
    Statement statement;
/**
* This method makes the connection with the database before
* the Test method.
*/
    @Before
    public void before() {
        connection = DBcon.openConnection();
    }
/**
* This method closes the connection with the database after
* the Test method.
*/
    @After
    public void after() {
        DBcon.closeConnection(connection);
    }
/**
* This method creates a statement to check the actual
* connection, and afterwards closes it.
*/
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
