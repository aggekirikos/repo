package fOrk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    // Make 2 test users, a test post and a test message
    private static final User user = new User("123456789", "TestUser", "Test", null);
    private static final User receiver = new User("111111111", "TestRec", "TestR", null);
    private static final String[] hashtags = {"TestHashtag", null, null, null, null};
    private static final Post post = new Post(user.getUserId(), "TestPost", "test", 10, 25,
            "Simple", "Test", hashtags);
    private static final Messages message = new Messages(user.getUserId(), receiver.getUserId(),"Hi");

    @Test
    public void testSearchWithExistingHashtag() {
        // Set input as 1 since no other post with such hashtag will exist
        String userInput = "1";
        ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
        System.setIn(is);
        int postIdFound = Main.search("TestHashtag");
        assertEquals( "wrong - hashtag should be found", post.getPostId(), postIdFound);
    }

    @Test
    public void testSearchWithNonexistentHashtag() {
        int postIdFound = Main.search("Nonexistent");
        assertEquals( "wrong - hashtag shouldn't be found",-1, postIdFound);
    }

    @Test
    public void testGetChatBoxAsSender() {
        // Run the chatbox method as the message's sender
        String expSender = "Your chatbox is :" + System.lineSeparator() + receiver.getUsername()
                + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        PrintStream printStreamSender = new PrintStream(osSender);
        System.setOut(printStreamSender);
        Main.getChatbox(user.getUserId());
        String actualUsers = null;
        try {
            actualUsers = osSender.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {}

        assert (actualUsers != null);
        assertEquals(expSender, actualUsers);
    }

    @Test
    public void testGetChatBoxAsReceiver() {
        // Run the chatbox method as the message's receiver
        String expReceiver = "Your chatbox is :" + System.lineSeparator() + user.getUsername()
                + System.lineSeparator();
        ByteArrayOutputStream osReceiver = new ByteArrayOutputStream();
        PrintStream printStreamReceiver = new PrintStream(osReceiver);
        System.setOut(printStreamReceiver);
        Main.getChatbox(receiver.getUserId());
        String actualReceivers = null;
        try {
            actualReceivers = osReceiver.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {}

        assert (actualReceivers != null);
        assertEquals(expReceiver, actualReceivers);
    }

    @Test
    public void testGetIDFromUsername() {
        assertEquals(user.getUserId(), Main.getIDfromUsername(user.getUsername()));
    }

    @Test
    public void testGetUsernameFromID() {
        assertEquals(user.getUsername(), Main.getUsernamefromID(user.getUserId()));
    }

    @AfterClass
    public static void after(){
        Statement statement = null;
        Connection connection = DBcon.openConnection();
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Messages WHERE Sender = " + user.getUserId());
            statement.executeUpdate("DELETE FROM stars WHERE PostID = " + post.getPostId());
            statement.executeUpdate("DELETE FROM Hashtags WHERE PostID = " + post.getPostId());
            statement.executeUpdate("DELETE FROM Post WHERE PostID = " + post.getPostId());
            statement.executeUpdate("DELETE FROM User WHERE ID = " + user.getUserId()
                    + " OR ID= " + receiver.getUserId());
        } catch (SQLException e) {
        } finally {
            DBcon.closeStatement(statement);
            DBcon.closeConnection(connection);
        }
    }

}