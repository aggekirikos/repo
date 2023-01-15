package fOrk;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/** This class contains methods responsible for the creation
 * of messages and their insert to their database.
 */
public class Messages {

    /** The user ID of the sender of the message */
	protected int userId;
	/** The user ID of the receiver of the message */
	protected int receiversId;
	/** The text contained in the message */
	protected String content;
	/** The creation date and time of the message */
	protected String dateTime;

	/**
	 * This method contains the constructor of the class and calls the method
	 * responsible for sending the messages to DB.
	 *
	 * @param userid Sender's ID
	 * @param receiversID Receiver's ID
	 * @param content The content of the message
	 */
    public Messages(int userid, int receiversID, String content) {
		this.content = content;
		this.userId = userid;
		this.receiversId = receiversID;
		LocalDateTime datetime1 = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		this.dateTime = datetime1.format(format);
		sendMessagestoDB();
	}

	/**
	 * This method sends the messages which
	 * have been created to the database.
	 */
	public void sendMessagestoDB() {
		Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
			int maxid = 0;
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT MAX(MessageID) FROM Messages");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				maxid = rs.getInt(1);
				maxid++;
			}
			stmt2 = connection.prepareStatement("INSERT INTO Messages(MessageID,Content,MDateTime,Sender,Receiver) VALUES(?, ?, ?, ?, ?)");

		    stmt2.setInt(1, maxid);
		    stmt2.setString(2, content);
		    stmt2.setString(3, dateTime);
		    stmt2.setInt(4, userId);
		    stmt2.setInt(5, receiversId);
		    stmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeStatement(stmt2);
			DBcon.closeConnection(connection);
		}
	}
}
