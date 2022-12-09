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

    public Messages(int userid, int receiversID, String content) {
		this.content = content;
		this.userid = userid;
		this.receiversID = receiversID;
		sendMessagestoDB(this.userid, this.receiversID, this.content);
	}
/*
	public void getChatbox(int userid) {
		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT Messages.Sender, Messages.Receiver FROM Messages WHERE Messages.Sender = ? OR Messages.Receiver = ? GROUP BY Sender, Receiver");
			stmt.setInt(1, userid);
			stmt.setInt(2, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int sendersid = rs.getInt("Sender");
				int receiversid = rs.getInt("Receiver");
				if (sendersid == userid) {
					stmt2 = connection.prepareStatement("SELECT User.Username FROM User INNER JOIN Messages ON Messages.Receiver = User.ID WHERE Messages.Sender = ? GROUP BY User.Username ");
					stmt2.setInt(1, userid);
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()) {
						String receiversUN = rs2.getString("Username");
						System.out.println(receiversUN);
				    }
				} else {
					stmt2 = connection.prepareStatement("SELECT User.Username FROM User INNER JOIN Messages ON Messages.Sender = User.ID WHERE Messages.Receiver = ? GROUP BY User.Username ");
					stmt2.setInt(1, userid);
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()) {
						String sendersUN = rs2.getString("Username");
						System.out.println(sendersUN);
					}
				}
				DBcon.closeStatement(stmt2);
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
			stmt = connection.prepareStatement("SELECT User.Username, Messages.Content, Messages.MDateTime FROM User INNER JOIN Messages ON Messages.Sender = User.ID WHERE (Sender = ? AND Receiver = ?) OR (Sender = ? AND Receiver = ?) ORDER BY MessageID");
			stmt.setInt(1, receiversID);
			stmt.setInt(2, userid);
			stmt.setInt(3, userid);
			stmt.setInt(4, receiversID);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String MessageContent = rs.getString("Content");
				String sendersUN = rs.getString("Username");
				String dt = rs.getString("MDateTime");
				System.out.println(sendersUN + ":" + MessageContent + "at" + dt);
			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}
*/

	public void sendMessagestoDB(int user, int receiverid, String content) {
		Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
			int maxid = 0;
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT MAX(MessageID) FROM Messages");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				maxid = rs.getInt("MessageID");
				maxid++;
			}
			stmt2 = connection.prepareStatement("INSERT INTO Messages(MessageID,Content,MDateTime, Sender, Receiver) VALUES(?, ?, ?, ?, ?)");
		    LocalDateTime datetime1 = LocalDateTime.now();
   		    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		    String formatDateTime = datetime1.format(format);
		    stmt2.setInt(1, maxid);
		    stmt2.setString(2, content);
		    stmt2.setString(3, formatDateTime);
		    stmt2.setInt(4, user);
		    stmt2.setInt(5, receiverid);
		    stmt2.executeUpdate();
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeStatement(stmt2);
			DBcon.closeConnection(connection);
		}
	}
/*
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
		String answer3 = null;
		do {
			System.out.println("Do you want to type a message?");
			Scanner s3 = new Scanner(System.in);
			answer3 = s3.next();
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
		} while (answer3 != "no");
	}

	public int getIDfromUsername(String username) {
		Connection connection = null;
		PreparedStatement stmt = null;
		int rtrn = 0;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT ID FROM USERS WHERE Username = ?");
			stmt.setString(1, username);
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
	}*/
}


