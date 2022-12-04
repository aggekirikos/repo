/*used to create the sql connection.*/
import java.sql.*;
import java.util.Scanner;


public class DBcon {
	
	/*Connection type object to make the connection*/
	public static Connection dbcon;
	/*Statement type object that contains the statement we will send to the server.*/
	public static Statement stmt;
	
	
	public static void Opencon() {
	/*Connection type object to make the connection*/
	Connection dbcon = null;
	
	/* Try block for trying to find the Driver to make the DB connection*/
	try {
		Class.forName("org.sqlite.JDBC");
	} catch (ClassNotFoundException e1) {
		
	}
	
	try {
		/*Makes the actual connection */
		dbcon = DriverManager.getConnection("jdbc:sqlite:csc205.db");
		System.out.println("connection opened");
		System.out.println("create or not: 1 for yes 2 for no: ");
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		if (a == 1) {
			createTable(dbcon);
		} else {
			System.out.println("Displaying DB");
		}
	} catch (Exception e) {
		System.out.println(e);
		}
	
	}
	
	
	public static void createTable(Connection dbcon) throws SQLException {
		/*Statement type object that contains the statement we will send to the server.*/
		Statement stmt = null;
		/* Creates the statement*/
		stmt = dbcon.createStatement();
		/*Executes the given statement*/
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS [User] "
						+ "(ID VARCHAR(10) NOT NULL PRIMARY KEY,"
						+"Password VARCHAR(20) NOT NULL,"
						+"Username VARCHAR(20) NOT NULL,"
						+"Name VARCHAR(20) NOT NULL,"
						+"Bio VARCHAR(50));");
		System.out.println("TABLE User CREATED");
		
		stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Post "
				+ "(PostID VARCHAR(20) NOT NULL PRIMARY KEY,"
				+"PostStatus INT,"
				+"RecipeTime TIME,"
				+"Content VARCHAR(800) NOT NULL,"
				+"Title VARCHAR(20) NOT NULL,"
				+"RecipeCategory VARCHAR(20),"
				+"DifficultyLevel VARCHAR(20),"
				+"RecipeCost VARCHAR(5),"
				+"Reviews INT,"
				+"Creator VARCHAR(10) NOT NULL,"
				+"CONSTRAINT FK_Post_User FOREIGN KEY(Creator) REFERENCES [User](ID));");
         System.out.println("TABLE User Post");
         
         stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Comment "
					+ "(CommentID VARCHAR(20) NOT NULL PRIMARY KEY,"
					+"Content VARCHAR(30) NOT NULL,"
					+"Sender VARCHAR(10) NOT NULL,"
					+"ToPost VARCHAR(20) NOT NULL,"
					+"Receiver VARCHAR(20),"
					+"CONSTRAINT FK_Comment_User_1 FOREIGN KEY(Sender) REFERENCES [User](ID),"
					+"CONSTRAINT FK_Comment_Comment FOREIGN KEY(Receiver) REFERENCES Comment(CommentID),"
					+"CONSTRAINT FK_Comment_Post FOREIGN KEY(ToPost) REFERENCES Post(PostID));");
	System.out.println("TABLE Comment CREATED");
	
	stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Messages "
			+ "(MessageID VARCHAR(20) NOT NULL PRIMARY KEY,"
			+"Content VARCHAR(500) NOT NULL,"
			+"MTime TIME NOT NULL,"
			+"MDate DATE NOT NULL,"
			+"Sender VARCHAR(10) NOT NULL,"
			+"Receiver VARCHAR(10) NOT NULL,"
			+"CONSTRAINT FK_Messages_User_1 FOREIGN KEY(Sender) REFERENCES [User](ID),"
			+"CONSTRAINT FK_Messages_User_2 FOREIGN KEY(Receiver) REFERENCES [User](ID));");
     System.out.println("TABLE Messages CREATED");
     
     stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Cookmates "
				+ "(UserID VARCHAR(10) NOT NULL,"
				+"CookmateID VARCHAR(10) NOT NULL,"
				+"CONSTRAINT FK_Cookmates_User_1 FOREIGN KEY(UserID) REFERENCES [User](ID),"
				+"CONSTRAINT FK_Cookmates_User_2 FOREIGN KEY(CookmateID) REFERENCES [User](ID),"
				+"CONSTRAINT PK_Cookmates PRIMARY KEY(UserID, CookmateID));");
     System.out.println("TABLE Cookmates CREATED");
	
	}
	
	public static void close() throws SQLException {
		
		try {
			
			/* if connection is still open*/
			if (dbcon != null)
				dbcon.close(); // close the connection to the database to end database session
			
		} catch (SQLException e) {}
}
	
	public static void deleteTables() throws SQLException {
		/* Try block for making the DB connection and excecuting the given statement.*/
		try {
			/* Creates the statement*/
			stmt = dbcon.createStatement();
			/*Executes the given statement that saves the objects*/
			stmt.executeUpdate("DROP TABLE Cookmates;");
			stmt.executeUpdate("DROP TABLE Messages;");
			stmt.executeUpdate("DROP TABLE Comment;");
			stmt.executeUpdate("DROP TABLE Post;");
			stmt.executeUpdate("DROP TABLE [User];");
			System.out.println("SUCCESFULLY DELETED TABLES");
			stmt.close();
			dbcon.close();
		} catch (SQLException e) {
		}
	}
}
