package fork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

public class RecommentTest {

    private static final User user = new User("333333333", "tsappy", "Loukas", "runner");
    private static final User user2 = new User("444444444", "kontsek", "kostas", "swimmer");
    private static final String[] hashtags = {"TestHashtag3", null, null, null, null};
    private static final Post post = new Post(user.getUserId(), "TestPost3", "test", 10, 25,
            "Simple", "Test", hashtags);

    @Test
    public void testRecommentConstructor() {
        Comment comment = new Comment("Perfect", user.getUserId(), post.getPostId());
        int id = comment.commentId;
        Recomment recomment = new Recomment("yeah", user2.getUserId(), post.getPostId(), id);
        comment.recomments.add(recomment);
        Assert.assertEquals("yeah", recomment.commentContent);
        Assert.assertEquals(user2.getUserId(), recomment.from);
        Assert.assertEquals(post.getPostId(), recomment.to);
        Assert.assertEquals(comment.commentId, id);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                    + " RecommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  comment.commentId);
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
    public void testRecommentRetrieveConstructor() {
        Comment comment = new Comment("Perfect", user.getUserId(), post.getPostId());
        Recomment recomment = new Recomment("yeah",
                user2.getUserId(), post.getPostId(), comment.commentId);
        comment.recomments.add(recomment);
        Recomment recomment1 = new Recomment(recomment.commentId, comment.commentId);
        Assert.assertEquals(recomment.commentContent, recomment1.commentContent);
        Assert.assertEquals(recomment.from, recomment1.from);
        Assert.assertEquals(recomment.to, recomment1.to);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                    + " RecommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE"
                    + " RecommentID = ? ");
            prstm.setInt(1,  recomment1.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  recomment1.commentId);
            prstm.executeUpdate();
            DBcon.closeStatement(prstm);
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE"
                    + " CommentID = ? ");
            prstm.setInt(1,  comment.commentId);
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
                    + " OR ID = " + user2.getUserId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(statement);
            DBcon.closeConnection(connection);
        }
    }
}
