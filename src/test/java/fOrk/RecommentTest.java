package fOrk;

import junit.framework.TestCase;

import java.sql.*;

public class RecommentTest extends TestCase {

    public void testRecommentConstructor() {
        Comment comment =new Comment("Perfect", 1, 1);
        int id = comment.commentId;
        Recomment recomment = new Recomment("yeah", 2, 1, id);
        comment.recomments.add(recomment);
        assertEquals("yeah", recomment.commentContent);
        assertEquals(2, recomment.from);
        assertEquals(1, recomment.to);
        assertEquals(comment.commentId, id);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        PreparedStatement prstm1 = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? ");
            prstm.setInt(1,  comment.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeStatement(prstm1);
            DBcon.closeConnection(connection);
        }
    }

    public void testRecommentRetrieveConstructor() {
        Comment comment =new Comment("Perfect", 1, 1);
        Recomment recomment = new Recomment("yeah", 2, 1, comment.commentId);
        comment.recomments.add(recomment);
        Recomment recomment1 = new Recomment(recomment.commentId, comment.commentId);
        assertEquals(recomment.commentContent, recomment1.commentContent);
        assertEquals(recomment.from, recomment1.from);
        assertEquals(recomment.to, recomment1.to);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        PreparedStatement prstm1 = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ? ");
            prstm.setInt(1,  recomment1.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? ");
            prstm.setInt(1,  recomment.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? ");
            prstm.setInt(1,  recomment1.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? ");
            prstm.setInt(1,  comment.commentId);
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
