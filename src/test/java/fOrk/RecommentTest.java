package fOrk;

import junit.framework.TestCase;

import java.sql.*;

public class RecommentTest extends TestCase {

    public void testRecommentConstructor() {
        Comment comment =new Comment("Perfect", 1, 1);
        int id = comment.commentId;
        Recomment recomment = new Recomment("yeah", 2, 1, id);
        assertEquals("yeah", recomment.commentContent);
        assertEquals(2, recomment.from);
        assertEquals(1, recomment.to);
        assertEquals(comment.commentId, id);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        PreparedStatement prstm1 = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE " +
                    "RecommentID = ?");
            prstm.setInt(1, recomment.commentId);
            prstm.executeUpdate();
            prstm1 = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ?");
            prstm1.setInt(1, comment.commentId);
            prstm1.executeUpdate();
            prstm1 = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ?");
            prstm1.setInt(1, recomment.commentId);
            prstm1.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeStatement(prstm1);
            DBcon.closeConnection(connection);
        }
    }
    public void testRecommentRetrieveConstructor() {
        Comment comment = new Comment("WOW", 2, 2);
        Recomment recomment = new Recomment("Yeah", 1, 2, comment.commentId);
        Connection con = null;
        Statement stm = null;
        Recomment recomment1 = new Recomment(recomment.commentId, 2);
        assertEquals(recomment.commentContent, recomment1.commentContent);
        assertEquals(recomment.from, recomment1.from);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        PreparedStatement prstm1 = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ?");
            prstm.setInt(1, recomment.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ?");
            prstm.setInt(1, recomment1.commentId);
            prstm.executeUpdate();
            prstm1 = connection.prepareStatement("DELETE FROM Comment WHERE " +
                    "CommentID = ?");
            prstm1.setInt(1, recomment.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE " +
                    "RecommentID = ?");
            prstm.setInt(1, recomment1.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeStatement(prstm1);
            DBcon.closeConnection(connection);
        }
    }
}