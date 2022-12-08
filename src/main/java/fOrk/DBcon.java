package fOrk;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
public class DBcon {
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
	public static void createTable(Connection dbcon) {
		/* Statement type object that contains the statement we will send to the server. */
		Statement stmt = null;
		/* Creates the statement */
		try {
			stmt = dbcon.createStatement();
			/* Executes the given statement */
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS [User] "
					+ "(ID INT NOT NULL PRIMARY KEY,"
					+ "Password VARCHAR(20) NOT NULL,"
					+ "Username VARCHAR(20) NOT NULL,"
					+ "Name VARCHAR(20) NOT NULL,"
					+ "Bio VARCHAR(50));");
			System.out.println("TABLE User CREATED");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Post "
					+ "(PostID INT NOT NULL PRIMARY KEY,"
					+ "PostStatus INT,"
					+ "RecipeTime TIME,"
					+ "Content VARCHAR(800) NOT NULL,"
					+ "Title VARCHAR(20) NOT NULL,"
					+ "RecipeCategory VARCHAR(20),"
					+ "DifficultyLevel VARCHAR(20),"
					+ "RecipeCost NUMERIC(5,2),"
					+ "Reviews NUMERIC(5,2)"
					+ "Evaluators INT"
					+ "Creator VARCHAR(10) NOT NULL,"
					+ "CONSTRAINT FK_Post_User FOREIGN KEY(Creator) REFERENCES [User](ID));");
			System.out.println("TABLE Post CREATED");

			stmt.executeUpdate("Create TABLE IF NOT EXISTS Hashtags "
					+ "(Hashtag VARCHAR(30) NOT NULL,"
					+ "PostID INT NOT NULL,"
					+ "CONSTRAINT PK_Hashtags PRIMARY KEY (PostID,Hashtag)"
					+ "CONSTRAINT FK_Hashtags_Post FOREIGN KEY(PostID) REFERENCES Post);");
			System.out.println("TABLE Hashtags CREATED");

			stmt.executeUpdate("Create TABLE IF NOT EXISTS stars "
					+"(star1 INT,"
					+"star2 INT,"
					+"star3 INT,"
					+"star4 INT,"
					+"star5 INT,"
					+"PostID INT NOT NULL PRIMARY KEY,"
					+"CONSTRAINT FK_stars_Post FOREIGN KEY(PostID) REFERENCES Post);");
			System.out.println("TABLE stars CREATED");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Comment "
					+ "(CommentID INT NOT NULL PRIMARY KEY,"
					+ "Content VARCHAR(30) NOT NULL,"
					+ "Sender VARCHAR(10) NOT NULL,"
					+ "ToPost VARCHAR(20) NOT NULL,"
					//+ "Receiver VARCHAR(20),"
					+ "CONSTRAINT FK_Comment_User FOREIGN KEY(Sender) REFERENCES [User](ID),"
					//+ "CONSTRAINT FK_Comment_Comment FOREIGN KEY(Receiver) REFERENCES Comment(CommentID),"
					+ "CONSTRAINT FK_Comment_Post FOREIGN KEY(ToPost) REFERENCES Post(PostID));");
			System.out.println("TABLE Comment CREATED");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Recomment "
					+ "RecommentID INT NOT NULL PRIMARY KEY, "
					+ "Receiver VARCHAR(20) NOT NULL, "
					+ "CONSTRAINT FK_Recomment_Comment_1 FOREIGN KEY(RecommentID) REFERENCES Comment(CommentID), "
					+ "CONSTRAINT FK_Recomment_Comment_2 FOREIGN KEY(Receiver) REFERENCES Comment(CommentID)");
			System.out.println("TABLE Recomment CREATED");

			/* if connection is still open*/
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Messages "
					+ "(MessageID INT NOT NULL PRIMARY KEY,"
					+ "Content VARCHAR(500) NOT NULL,"
					+ "MTime TIME NOT NULL,"
					+ "MDate DATE NOT NULL,"
					+ "Sender VARCHAR(10) NOT NULL,"
					+ "Receiver VARCHAR(10) NOT NULL,"
					+ "CONSTRAINT FK_Messages_User_1 FOREIGN KEY(Sender) REFERENCES [User](ID),"
					+ "CONSTRAINT FK_Messages_User_2 FOREIGN KEY(Receiver) REFERENCES [User](ID));");
			System.out.println("TABLE Messages CREATED");

			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Cookmates "
					+ "(UserID INT NOT NULL,"
					+ "CookmateID INT NOT NULL,"
					+ "CONSTRAINT FK_Cookmates_User_1 FOREIGN KEY(UserID) REFERENCES [User](ID),"
					+ "CONSTRAINT FK_Cookmates_User_2 FOREIGN KEY(CookmateID) REFERENCES [User](ID),"
					+ "CONSTRAINT PK_Cookmates PRIMARY KEY(UserID, CookmateID));");
			System.out.println("TABLE Cookmates CREATED");
		} catch (Exception e) {
			System.out.println("Could not create tables: " + e.getMessage());
		}
	}
	public static void closeConnection(Connection dbcon) {
		try {
			/* if connection is still open */
			if (dbcon != null)
				dbcon.close(); // close the connection to the database to end database session

		} catch (SQLException e) {
			System.out.println("Could not close connection with the database: " + e.getMessage());
		}
	}
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
	public static void deleteTables(Connection dbcon) {
		/* Try block for making the DB connection and excecuting the given statement. */
		Statement stmt = null;
		try{
			/* Creates the statement */
			stmt = dbcon.createStatement();
			/* Executes the given statement that drops the tables */
			stmt.executeUpdate("DROP TABLE Cookmates;");
			stmt.executeUpdate("DROP TABLE Messages;");
			stmt.executeUpdate("DROP TABLE Comment;");
			stmt.executeUpdate("DROP TABLE stars;");
			stmt.executeUpdate("DROP TABLE Hashtags;");
			stmt.executeUpdate("DROP TABLE Post;");
			stmt.executeUpdate("DROP TABLE [User];");
			System.out.println("SUCCESFULLY DELETED TABLES");
			stmt.close();
			dbcon.close();
		} catch (SQLException e) {
			System.out.println("Could not delete the tables of the database: " + e.getMessage());
		}
	}
}