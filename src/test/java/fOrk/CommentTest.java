package fork;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

public class CommentTest {

    private static final User user = new User("333333333", "tsappy", "Loukas", "runner");
    private static final User user2 = new User("444444444", "kontsek", "kostas", "swimmer");
    private static final User user3 = new User("555555555", "vaspap", "Vasiliki", "tzouras");
    private static final String[] hashtags = {"TestHashtag2", null, null, null, null};
    private static final Post post = new Post(user3.getUserId(), "TestPost2", "test", 10, 25,
            "Simple", "Test", hashtags);

    @Test
    public void testGetCommentContent() {
        String content = "Excellent";
        Comment comment = new Comment(content, user.getUserId(), post.getPostId());
        Assert.assertEquals(content, comment.getCommentContent());
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1, comment.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testMakeReComment() {
        Comment comment = new Comment("why is there no flour?", user.getUserId(), post.getPostId());
        String recomment = "Because he doesn't like it";
        ByteArrayInputStream is = new ByteArrayInputStream(recomment.getBytes(
                StandardCharsets.UTF_8));
        System.setIn(is);
        comment.makeReComment(user2.getUserId(), post.getPostId(), comment.commentId);
        Assert.assertEquals(1, comment.recomments.size());
        Assert.assertEquals(user2.getUserId(), comment.recomments.get(0).from);
        Assert.assertEquals(post.getPostId(), comment.recomments.get(0).to);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                   + " CommentID = ? ");
            prstm.setInt(1, comment.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                   + " RecommentID = ? ");
            prstm.setInt(1, comment.recomments.get(0).commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  comment.recomments.get(0).commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            is.close();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            DBcon.closeStatement(prstm);
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testGetRecomments() {
        //Creating the comment and recomments
        Comment comment = new Comment("why there is no flour?", user.getUserId(), post.getPostId());
        Recomment recomment1 = new Recomment("Cause he doesn't like it",
                user2.getUserId(), post.getPostId(), comment.commentId);
        Recomment recomment2 = new Recomment("Curiosity killed the cat",
                user3.getUserId(), post.getPostId(), comment.commentId);
        //Add the recomments to the comment
        comment.recomments.add(recomment1);
        comment.recomments.add(recomment2);
        //Making the expected string of recomments
        String expected = "   Responds:  " + System.lineSeparator()
                + "   "
                + recomment1.username + ": " + recomment1.commentContent
                + System.lineSeparator() + "   " + recomment2.username + ": "
                + recomment2.commentContent + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(osSender, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        comment.getRecomments();
        String actual = null;
        try {
            actual = osSender.toString("UTF-8");
            try {
                osSender.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        assert (actual != null);
        Assert.assertEquals(expected, actual);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  comment.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                    + " RecommentID = ? ");
            prstm.setInt(1, comment.recomments.get(1).commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  comment.recomments.get(1).commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                    + " RecommentID = ? ");
            prstm.setInt(1, comment.recomments.get(0).commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  comment.recomments.get(0).commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeStatement(prstm);
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testRetrieveConstructor() {
        Comment comment = new Comment("Excellent", user2.getUserId(), post.getPostId());
        Comment comment1 = new Comment(comment.commentId);
        Assert.assertEquals(comment.commentContent, comment1.commentContent);
        Assert.assertEquals(comment.from, comment1.from);
        Assert.assertEquals(comment.to, comment1.to);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement(
                    "DELETE FROM Comment WHERE CommentID = ? OR CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.setInt(2, comment1.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testPrintCommentRec() {
        //Creating the comment and recomments
        Comment comment = new Comment("why is there no flour?", user.getUserId(), post.getPostId());
        Recomment recomment1 = new Recomment("Cause he doesn't like it",
                user2.getUserId(), post.getPostId(), comment.commentId);
        Recomment recomment2 = new Recomment("Curiosity killed the cat",
                user3.getUserId(), post.getPostId(), comment.commentId);
        //Add the recomments to the comment
        comment.recomments.add(recomment1);
        comment.recomments.add(recomment2);
        //Making the expected string of recomments
        String expected = " 2: tsappy: why is there no flour?"
                + System.lineSeparator() + "   Responds:  " + System.lineSeparator()
                + "   " + recomment1.username + ": " + recomment1.commentContent
                + System.lineSeparator() + "   " + recomment2.username
                + ": " + recomment2.commentContent + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(osSender, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        comment.printCommentRec(1);
        String actual = null;
        try {
            actual = osSender.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        assert (actual != null);
        Assert.assertEquals(expected, actual);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE "
                    + "CommentID = ? OR CommentID = ? OR CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.setInt(2, recomment1.commentId);
            prstm.setInt(3, recomment2.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                    + " RecommentID = ? OR RecommentID = ?");
            prstm.setInt(1, recomment1.commentId);
            prstm.setInt(2, recomment2.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeStatement(prstm);
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testPrintComment() {
        Comment comment = new Comment("perfect", user2.getUserId(), post.getPostId());
        String expected = comment.username + ": perfect";
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(osSender, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        comment.printComment();
        String actual = null;
        try {
            actual = osSender.toString("UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        assert (actual != null);
        Assert.assertEquals(expected, actual);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @AfterClass
    public static void close() {
        Connection connection = DBcon.openConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Messages WHERE Sender = " + user.getUserId());
            statement.executeUpdate("DELETE FROM stars WHERE PostID = " + post.getPostId());
            statement.executeUpdate("DELETE FROM Hashtags WHERE PostID = " + post.getPostId());
            statement.executeUpdate("DELETE FROM Post WHERE PostID = " + post.getPostId());
            statement.executeUpdate("DELETE FROM User WHERE ID = " + user.getUserId()
                    + " OR ID = " + user2.getUserId()
                    + " OR ID = " + user3.getUserId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(statement);
            DBcon.closeConnection(connection);
        }
    }
}
