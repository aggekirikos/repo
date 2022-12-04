package fOrk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
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

	public void typeMessage(int userid, int receiver) {
		System.out.println("Do you want to send message?");
		Scanner s = new Scanner(System.in);
		String answer;
		answer = s.next();
		if (answer = "yes") {
			System.out.println("Type message");
			Scanner s = new Scanner(System.in);
			String message;
			message = s.next();
			sendMessagestoDB(userid, receiver, message);
		} else if (answer =!"no") {
			System.out.println("Wrong! You should answer 'yes' or 'no'");
		}
	}

	public void openChatbox(userid) {
		System.out.println("Do you want to open a chat box");
		Scanner s = new Scanner(System.in);
		String answer;
		answer = s.next();
		if (answer = "yes") {
			System.out.println("Type the user you want to chat");
			String answer2;
			Scanner s2 = new Scanner(System.in);
			answer2 = s2.next();
			receiverid = getuserid(answer2);
			getMessagesby_userid(int receiverid);
			typeMessage(userid, receiverid);
		} else if (answer =! no) {
			System.out.println("Wrong! Answer should be 'yes' or 'no'");
		}
	}
}





