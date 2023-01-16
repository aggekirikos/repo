package fork;

import static org.junit.Assert.assertEquals;

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


public class MainTest {

    // Make 2 test users, a test post and a test message
    private static final User user = new User("123456789", "TestUser", "Test", null);
    private static final User receiver = new User("111111111", "TestRec", "TestR", null);
    private static final String[] hashtags = {"TestHashtag", null, null, null, null};
    private static final Post post = new Post(user.getUserId(),
            "TestPost", "test", 10, 25,
            "Simple", "Test", hashtags);
    private static final Messages message = new Messages(user.getUserId(),
            receiver.getUserId(),"Hi");

    @Test
    public void testLogIn() {
        String userInput = String.format("TestUser%s123456789",
                System.lineSeparator());
        ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(
                StandardCharsets.UTF_8));
        System.setIn(is);
        int postIdFound = Main.logIn();
        assertEquals(user.getUserId(), postIdFound);
    }


    @Test
    public void testCreate() {
        String userInput = String.format("1%sRice%s1%sBoil",
            System.lineSeparator(),
            System.lineSeparator(),
            System.lineSeparator());
        ByteArrayInputStream bi = new ByteArrayInputStream(userInput.getBytes(
                StandardCharsets.UTF_8));
        System.setIn(bi);
        String recipe = Main.create();
        assertEquals("\nIngredients:\n"
                + " - " + "Rice" + "\n" + "\nRecipe steps:\n" + " - " + "Boil"
                + "\n", recipe);
    }


    @Test
    public void testSearchWithExistingHashtag() {
        // Set input as 1 since no other post with such hashtag will exist
        String userInput = "1";
        ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(
                StandardCharsets.UTF_8));
        System.setIn(is);
        int postIdFound = Main.search("TestHashtag");
        assertEquals("wrong - hashtag should be found",
                post.getPostId(), postIdFound);
    }

    @Test
    public void testSearchWithNonexistentHashtag() {
        int postIdFound = Main.search("Nonexistent");
        assertEquals("wrong - hashtag shouldn't be found",
                -1, postIdFound);
    }

    @Test
    public void testGetChatBoxAsSender() {
        // Run the chatbox method as the message's sender
        String expSender = "Your chatbox is :" + System.lineSeparator() + receiver.getUsername()
                + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(osSender, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Main.getChatbox(user.getUserId());
        String actualUsers = null;
        try {
            actualUsers = osSender.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Something went wrong" + e.getMessage());
        }

        assert (actualUsers != null);
        assertEquals(expSender, actualUsers);
    }

    @Test
    public void testGetChatBoxAsReceiver() {
        // Run the chatbox method as the message's receiver
        String expReceiver = "Your chatbox is :" + System.lineSeparator() + user.getUsername()
                + System.lineSeparator();
        ByteArrayOutputStream osReceiver = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(osReceiver, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Main.getChatbox(receiver.getUserId());
        String actualReceivers = null;
        try {
            actualReceivers = osReceiver.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Something went wrong" + e.getMessage());
        }

        assert (actualReceivers != null);
        assertEquals(expReceiver, actualReceivers);
    }

    @Test
    public void testGetMessagesByUserId() {
        String expSender = user.getUsername() + " : " + message.content
                + " sent at " + message.dateTime
                + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(osSender, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Main.getMessagesByUserId(receiver.getUserId(), user.getUserId());
        String actualUsers = null;
        try {
            actualUsers = osSender.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }

        assert (actualUsers != null);
        assertEquals(expSender, actualUsers);
    }

    @Test
    public void testGetIDFromUsername() {
        assertEquals(user.getUserId(), Main.getIDfromUsername(user.getUsername()));
    }

    @Test
    public void testGetUsernameFromID() {
        assertEquals(user.getUsername(), Main.getUsernamefromID(user.getUserId()));
    }

    @Test
    public void testAnswerIsYes() {
        String userInput = "Yes";
        ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(
                StandardCharsets.UTF_8));
        System.setIn(is);
        String answerIsYes = Main.checkAnswer("TestAnswer");
        assertEquals("Yes", answerIsYes);
    }

    @Test
    public void testAnswerIsNo() {
        String userInput = "No";
        ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(
                StandardCharsets.UTF_8));
        System.setIn(is);
        String answerIsYes = Main.checkAnswer("TestAnswer");
        assertEquals("No", answerIsYes);
    }

    @AfterClass
    public static void after() {
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
            System.out.println("Something went wrong" + e.getMessage());
        } finally {
            DBcon.closeStatement(statement);
            DBcon.closeConnection(connection);
        }
    }

}