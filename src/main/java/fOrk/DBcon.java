package fOrk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
* This class connects the java program with the database by opening
* the connection and it creates the tables that are needed using a
* statement. Afterwards, it closes both the connection and the
* statement in the createTables method. If the statement and the
* connection are still open, they are closed by the methods
* closeConnection and closeStatement.
*/
public class DBcon {
    /**
    * This method connects the java program with the database.
    * @return The Connection-type object called dbcon.
    */
    public static Connection openConnection() {
        /* Initialize Connection type object */
        Connection dbcon = null;
        /* Try block for trying to find the Driver to make the DB connection*/
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite error: " + e.getMessage());
        }
        /* Makes the actual connection */
        try {
            dbcon = DriverManager.getConnection("jdbc:sqlite:fOrk.db");
        } catch (Exception e) {
            System.out.println("Could not make connection with the database");
        }
        return dbcon;
    }

    /**
    * This method creates the tables for the database by creating a statement
    * and also using SQL text, then closes both the connection and the
    * statement.
    * @param dbcon The Connection-type object called dbcon.
    */
    public static void createTable(Connection dbcon) {
        /* Statement type object that contains the statement we will send to the server. */
        Statement stmt = null;
        /* Creates the statement */
        try {
            stmt = dbcon.createStatement();
            /* Executes the given statement */
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS [User] "
                    + "(ID INT NOT NULL PRIMARY KEY,"
                    + "Password VARCHAR(30) NOT NULL,"
                    + "Username VARCHAR(30) UNIQUE NOT NULL,"
                    + "Name VARCHAR(30) NOT NULL,"
                    + "Bio VARCHAR(100));");
            //System.out.println("TABLE User CREATED");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Post "
                    + "(PostID INT NOT NULL PRIMARY KEY,"
                    + "PostStatus INT,"
                    + "RecipeTime INT,"
                    + "Content VARCHAR(800) NOT NULL,"
                    + "Title VARCHAR(100) NOT NULL,"
                    + "RecipeCategory VARCHAR(30),"
                    + "DifficultyLevel VARCHAR(30),"
                    + "RecipeCost NUMERIC(5,2),"
                    + "Creator VARCHAR(30) NOT NULL,"
                    + "CONSTRAINT FK_Post_User FOREIGN KEY(Creator) REFERENCES [User](ID));");
            //System.out.println("TABLE Post CREATED");
            stmt.executeUpdate("Create TABLE IF NOT EXISTS Hashtags "
                    + "(Hashtag VARCHAR(50) NOT NULL,"
                    + "PostID INT NOT NULL,"
                    + "CONSTRAINT PK_Hashtags PRIMARY KEY (PostID,Hashtag)"
                    + "CONSTRAINT FK_Hashtags_Post FOREIGN KEY(PostID) REFERENCES Post);");
            //System.out.println("TABLE Hashtags CREATED");
            stmt.executeUpdate("Create TABLE IF NOT EXISTS stars "
                    + "(star1 INT,"
                    + "star2 INT,"
                    + "star3 INT,"
                    + "star4 INT,"
                    + "star5 INT,"
                    + "PostID INT NOT NULL PRIMARY KEY,"
                    + "CONSTRAINT FK_stars_Post FOREIGN KEY(PostID) REFERENCES Post);");
            //System.out.println("TABLE stars CREATED");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Comment "
                    + "(CommentID INT NOT NULL PRIMARY KEY,"
                    + "Content VARCHAR(100) NOT NULL,"
                    + "Sender INT NOT NULL,"
                    + "ToPost INT NOT NULL,"
                    //+ "Receiver VARCHAR(30),"
                    + "CONSTRAINT FK_Comment_User FOREIGN KEY(Sender) REFERENCES [User](ID),"
                    /*+ "CONSTRAINT FK_Comment_Comment FOREIGN KEY(Receiver)
                    REFERENCES Comment(CommentID),"*/
                    + "CONSTRAINT FK_Comment_Post FOREIGN KEY(ToPost) REFERENCES Post(PostID));");
            //System.out.println("TABLE Comment CREATED");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Recomment "
                    + "(RecommentID INT NOT NULL PRIMARY KEY, "
                    + "Receiver INT NOT NULL, "
                    + "CONSTRAINT FK_Recomment_Comment_1 FOREIGN KEY(RecommentID)"
                    + "REFERENCES Comment(CommentID), "
                    + "CONSTRAINT FK_Recomment_Comment_2 FOREIGN KEY(Receiver)"
                    + "REFERENCES Comment(CommentID));");
            //System.out.println("TABLE Recomment CREATED");
            /* if connection is still open*/
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Messages "
                    + "(MessageID INT NOT NULL PRIMARY KEY,"
                    + "Content VARCHAR(500) NOT NULL,"
                    + "MDateTime VARCHAR(25) NOT NULL,"
                    + "Sender INT NOT NULL,"
                    + "Receiver INT NOT NULL,"
                    + "CONSTRAINT FK_Messages_User_1 FOREIGN KEY(Sender) REFERENCES [User](ID),"
                    + "CONSTRAINT FK_Messages_User_2 FOREIGN KEY(Receiver)"
                    + "REFERENCES [User](ID));");
            //System.out.println("TABLE Messages CREATED");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Cookmates "
                    + "(UserID INT NOT NULL,"
                    + "CookmateID INT NOT NULL,"
                    + "CONSTRAINT FK_Cookmates_User_1 FOREIGN KEY(UserID) REFERENCES [User](ID),"
                    + "CONSTRAINT FK_Cookmates_User_2 FOREIGN KEY(CookmateID)"
                    + "REFERENCES [User](ID),"
                    + "CONSTRAINT PK_Cookmates PRIMARY KEY(UserID, CookmateID));");
        //System.out.println("TABLE Cookmates CREATED");
        } catch (Exception e) {
            System.out.println("Could not create tables: " + e.getMessage());
        } finally {
            closeStatement(stmt);
            closeConnection(dbcon);
        }
    }

    /**
    * This method checks if the connection is still open and
    * closes it.
    * @param dbcon The Connection-type object called dbcon.
    */
    public static void closeConnection(Connection dbcon) {
        try {
            /* if connection is still open */
            if (dbcon != null) {
                dbcon.close(); // close the connection to the database to end database session
            }
        } catch (SQLException e) {
            System.out.println("Could not close connection with the database: " + e.getMessage());
        }
    }

    /**
    * This method checks if the statement is still open and
    * closes it.
    * @param stmt The Statement-type object called stmt.
    */
    public static void closeStatement(Statement stmt) {
        try {
            /* if statement is still open */
            if (stmt != null) {
                stmt.close(); // close the statement
            }
        } catch (SQLException e) {
            System.out.println("Could not close the SQL statement: " + e.getMessage());
        }
    }

    /**
    * This method deletes the tables that are created
    * in case it is needed.
    * @param dbcon The Connection-type object called dbcon.
    */
    public static void deleteTables(Connection dbcon) {
        /* Try block for making the DB connection and excecuting the given statement. */
        Statement stmt = null;
        try {
            /* Creates the statement */
            stmt = dbcon.createStatement();
            /* Executes the given statement that drops the tables */
            stmt.executeUpdate("DROP TABLE Cookmates;");
            stmt.executeUpdate("DROP TABLE Recomment;");
            stmt.executeUpdate("DROP TABLE Messages;");
            stmt.executeUpdate("DROP TABLE Comment;");
            stmt.executeUpdate("DROP TABLE stars;");
            stmt.executeUpdate("DROP TABLE Hashtags;");
            stmt.executeUpdate("DROP TABLE Post;");
            stmt.executeUpdate("DROP TABLE [User];");
            //System.out.println("SUCCESFULLY DELETED TABLES");
            stmt.close();
            dbcon.close();
        } catch (SQLException e) {
            System.out.println("Could not delete the tables of the database: " + e.getMessage());
        }
    }
}
