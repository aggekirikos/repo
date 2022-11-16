import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
public class Messages2 {
	int userid;
	int receiverid;
	String message;

	public void getMessages(int userid) {
		Statement s = dbcon.createStatement();
		ResultSet rs = s.executeQuery("SELECT ID,firstname,lastname FROM Users,Messages WHERE receiver_userid=userid OR user_id=userid");
		while(rs.next()) {
			System.out.println(rs.getString("firstname, lastname"));
		}
	}

	int userid2;
	public void getMessagesby_userid(int userid2) {
		Statement s = dbcon.createStatement();
		ResultSet rs = s.executeQuery("SELECT Message FROM Messages,Users WHERE (Messages.receiver_userid=userid2 AND Messages.user_id=userid) OR (Messages.receiver_userid=userid AND Messages.user_id=userid2)");
		while (rs.next()) {
			System.out.println(rs.getString("Messages"));
		}
	}

	public void sendMessagestoDB(user, receiver, message) {
		Statement s = dbcon.createStatement();
		ResultSet rs = s.executeQuery("INSERT INTO Messages(user,receiver,message) VALUES(userid,receiverid,message)");
	}

	public void dbc() {
		String url = "jdbc:sqlserver://sqlserver.dmst.aueb.gr;databaseName=DB74";
		String user = "G574";
		String password= "5f3045";
		try {
			Connection connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}




