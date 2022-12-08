package fOrk;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Messages {

	private int userid;
	private int receiversID;
	private String content;
	int maxid= 0;

    public Messages(int userid, int receiversID, String content) {
		this.content = content;
		this.userid = userid;
		this.receiversID = receiversID;
		sendMessagestoDB(this.userid, this.receiversID, this.content);
	}

	public void getChatbox(int userid) {
		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT Messages.Sender, Messages.Receiver FROM Messages WHERE Messages.Sender = userid OR Messages.Receiver = userid GROUP BY Sender, Receiver");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int sendersid = rs.getInt("Sender");
				int receiversid = rs.getInt("Receiver");
				if (sendersid == userid) {
					stmt2 = connection.prepareStatement("SELECT User.Username FROM User INNER JOIN Messages ON Messages.Receiver = User.ID GROUP BY User.Username ");
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()) {
						String receiversUN = rs2.getString("Username");
						System.out.println(receiversUN);
				    }
				} else {
					stmt2 = connection.prepareStatement("SELECT User.Username FROM User INNER JOIN Messages ON Messages.Sender = User.ID GROUP BY User.Username ");
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()) {
						String sendersUN = rs2.getString("Username");
						System.out.println(sendersUN);
					}
				}
			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public void getMessagesby_userid(int receiversID) {
		Connection connection = null;
        PreparedStatement stmt = null;
        try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT User.Username, Message.Content, Message.MDateTime FROM User, Messages INNER JOIN Messages ON Messages.Sender = User.ID WHERE (Sender = receiversID AND Receiver = userid) OR (Sender = userid AND Receiver = receicersID) ORDER BY MessageID DESC");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String MessageContent = rs.getString("Content");
				String sendersUN = rs.getString("Username");
				System.out.println(sendersUN + ":" + MessageContent);
			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}


	public void sendMessagestoDB(int user, int receiverid, String content) {
		Connection connection = null;
        PreparedStatement stmt = null;
        maxid++;
        try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("INSERT INTO Messages(MessageID,Content,MTime, MDate, Sender, Receiver) VALUES(maxid,content,java.time.LocalTime.now(), java.time.LocalDate.now(), user, receiverid)");
		    ResultSet rs = stmt.executeQuery();
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public void openConversation(int userid) {
		System.out.println("Do you want to open a conversation?");
		Scanner s = new Scanner(System.in);
		String answer;
		do {
			answer = s.next();
			if (answer == "yes") {
				System.out.println("Type the user you want to chat");
				String answer2;
				Scanner s2 = new Scanner(System.in);
				answer2 = s2.next();
				String receiver = answer2;
				int receiverid = getIDfromUsername(receiver);
				getMessagesby_userid(receiverid);
				typeMessage(userid, receiverid);
			} else if (answer != "no") {
				System.out.println("Wrong! Answer should be 'yes' or 'no'");
			}
		} while (answer != "yes" && answer != "no");

	}

	public static void typeMessage(int userid, int receiversid) {
		System.out.println("Do you want to type a message?");
		Scanner s3 = new Scanner(System.in);
		String answer3 = null;
		do {
			if (answer3 == "yes") {
				System.out.println("Type message");
				Scanner s4 = new Scanner(System.in);
				String MessageContent;
				MessageContent = s4.next();
				Messages message = new Messages(userid, receiversid, MessageContent);
			} else if (answer3 != "no") {
				System.out.println("Wrong! Answer should be 'yes' or 'no'.");
			}
		} while (answer3 != "yes" && answer3 != "no");
	}

	public int getIDfromUsername(String username) {
		Connection connection = null;
		PreparedStatement stmt = null;
		int rtrn = 0;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT ID FROM USERS WHERE Username = username");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				rtrn = rs.getInt("ID");

			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
		return rtrn;
	}
}

