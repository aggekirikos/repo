package fOrk;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.*;

import static org.junit.Assert.*;

public class PostTest {

    @Test
    public void testBasicPost() {
        int userId = 1;
        String title = "Delicious Lasagna";
        String content = "This recipe for lasagna is sure to be a hit at your next dinner party. It's easy to make and tastes great!";
        double cost = 20.00;
        int time = 90;
        String diLevel = "Easy";
        String category = "Italian";
        String[] has = {"#lasagna", "#dinner", "#italian", null, null};
        Post post = new Post(userId, title, content, cost, time, diLevel, category, has);
        assertEquals(userId, post.creator);
        assertEquals(1, post.postStatus);
        assertEquals(title, post.title);
        assertEquals(content, post.content);
        assertEquals(cost, post.recipeCost, 2);
        assertEquals(time, post.recipeTime);
        assertEquals(diLevel, post.difficultyLevel);
        assertEquals(category, post.recipeCategory);
        assertArrayEquals(has, post.hashtags);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    //@Test
    @Test
    public void testMaxIdFromDB() {
        // call the maxidfromDB method and assert that it returns the expected value
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(2, "Ntolmadakia", "Tylixe to fyllo me ti gemisi"
                , 15, 120, "difficult", "Traditional", ingr);
        assertEquals(post.postId, post.maxidfromDB());
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }


    @Test
    public void testRetrievePost() {
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(1, "Ntolmadakia", "Tylixe to fyllo me ti gemisi", 15, 120,
                "difficult", "Traditional", ingr);
        Connection con = null;
        Post post1 = new Post(post.postId);
        assertEquals(post.creator, post1.creator);
        assertEquals(post.content, post1.content);
        assertEquals(post.title, post1.title);
        assertEquals(post.difficultyLevel, post1.difficultyLevel);
        assertEquals(post.recipeCost, post1.recipeCost, 2);
        assertEquals(post.recipeTime, post1.recipeTime);
        assertArrayEquals(post.stars, post1.stars);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE " +
                    "PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM stars WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post1.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post1.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post1.postId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testCreateComment() {
        Connection connection = DBcon.openConnection();
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(2, "Ntolmadakia", "Tylixe to fyllo me ti gemisi"
                , 15, 120, "difficult", "Traditional", ingr);
        // call createComment method with mock input and comment list
        Comment comment = new Comment("Well done", 1, post.postId);
        post.createComment(comment);
        // verify that the new Comment object was added to the comments list
        assertEquals(2, post.comments.size(), 1);
        assertEquals(post.comments.get(0).commentContent, "Well done");
        assertEquals(1,post.comments.get(0).from);
        assertEquals(post.postId, post.comments.get(0).to);
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE" +
                    " PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " ToPost = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testMakeReview() {
        String review = "3";
        Connection connection = DBcon.openConnection();
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(2, "Ntolmadakia", "Tylixe to fyllo me ti gemisi", 15, 120,
                "difficult", "Traditional", ingr);
        PreparedStatement prstm = null;
        try {
            System.out.println("one");
            ByteArrayInputStream is = new ByteArrayInputStream(review.getBytes(StandardCharsets.UTF_8));
            System.out.println("2");
            System.setIn(is);
            System.out.println("3");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            System.out.println("4");
            PrintStream printStreamSender = new PrintStream(os);
            System.out.println("5");
            System.setOut(printStreamSender);
            System.out.println("6");
            post.makeReview();
            System.out.println("7");
            String actual = null;
            try {
                actual = os.toString("UTF-8");
                os.close();
                is.close();
                printStreamSender.close();
            } catch (UnsupportedEncodingException e) {
                e.getMessage();
            }
            assert (actual != null);
            assertEquals("Your review has been inserted", actual);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                prstm = connection.prepareStatement("DELETE FROM stars WHERE PostID = ?;");
                prstm.setInt(1, post.postId);
                prstm.executeUpdate();
                prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE PostID = ?;");
                prstm.setInt(1, post.postId);
                prstm.executeUpdate();
                prstm = connection.prepareStatement("DELETE FROM Post WHERE PostID = ?;");
                prstm.setInt(1, post.postId);
                prstm.executeUpdate();
            } catch (SQLException e) {
                e.getMessage();
            } finally {
                DBcon.closeStatement(prstm);
                DBcon.closeConnection(connection);
            }
        }
    }

    @Test
    public void testEvaluators() {
        //Insert test data into database
        Connection connection = DBcon.openConnection();
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(2, "Ntolmadakia", "Tylixe to fyllo me ti gemisi"
                , 15, 120,
                "difficult", "Traditional", ingr);
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement( "UPDATE stars " +
                    "SET star1 = 3, star2 = 3, star3 = 2, star4 = 2," +
                    " star5 = 0 WHERE PostId = ?");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
        //Run test
        int evaluators = post.evaluators();
        assertEquals(10, evaluators);

        //Clean up test data
        connection = DBcon.openConnection();
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testGetPostId() {
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(2, "Ntolmadakia", "Tylixe to fyllo me ti gemisi", 15, 120,
                "difficult", "Traditional", ingr);
        assertEquals(post.postId, post.getPostId());
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }


    @Test
    public void testGetPost() {
        String[] ingr = {"fylla","rizi", "kimas", null, null};
        Post post = new Post(2, "Ntolmadakia", "Tylixe to fyllo me ti gemisi", 15, 120,
                "difficult", "Traditional", ingr);
        Comment comment = new Comment("Well done", 1, post.postId);
        post.comments.add(comment);
        Connection con = null;
        Statement stm = null;
        //Create a ByteArrayOutputStream to capture the output of the method
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //Call the getPost method
        post.getPost();
        String expected = "Title of the post: Ntolmadakia\n" +
                "Content of the post: Tylixe to fyllo me ti gemisi\n" +
                "The time required for this recipe is: 120 minutes \n" +
                "The cost for this recipe is:15.0 euros\n" +
                "The difficulty Level of this recipe is: difficult\n" +
                "The category of this recipe is: Traditional\n" +
                "This post has 0.0 stars\n" +
                "This post's comments are: " + System.lineSeparator() +
                " 1: tsappy: Well done";
        //Verify that the correct output was printed
        assertEquals("The output is not as expected ",(expected + System.lineSeparator()), outContent.toString());
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM stars WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Hashtags WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Post WHERE PostID = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE ToPost = ?;");
            prstm.setInt(1, post.postId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }
}