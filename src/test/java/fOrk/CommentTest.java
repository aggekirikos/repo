package fOrk;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class CommentTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testGetCommentContent() {
        String content = "Excellent";
        Comment comment = new Comment(content, 1, 3);
        assertEquals(content, comment.getCommentContent());
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? ");
            prstm.setInt(1, comment.commentId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }
    // @Test
    public void testMakeReComment() {
        /*Scanner input = new Scanner(System.in);
        Comment comment = new Comment();
        /**
         * Making the recomment
         */
        /*comment.makeReComment(1, 2, comment.commentId);
        Connection con = null;
        Statement stm = null;
        int commentId = 0;
        try {
            con = DBcon.openConnection();
            stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(CommentID) FROM Comment");
            commentId = rs.getInt(1);
        } catch (Exception e) { System.out.println(e.getMessage());}
        finally {
            DBcon.closeStatement(stm);
            DBcon.closeConnection(con);
        }
        /**
         * Retrieve the recomment from the database
         * */
        /*Recomment recomment = new Recomment(commentId, comment.to);
        String content = input.nextLine();
        assertTrue(content.equals(recomment.commentContent));*/
    }

    public void testGetRecomments() {
        //Creating the comment and recomments
        Comment comment = new Comment("why there is no flour?", 1, 2);
        Recomment recomment1 = new Recomment("Cause he doesn't like it",
                2, 2, comment.commentId);
        Recomment recomment2 = new Recomment("Curiosity killed the cat",
                3, 2, comment.commentId);
        //Add the recomments to the comment
        comment.recomments.add(recomment1);
        comment.recomments.add(recomment2);
        //Making the expected string of recomments
        String expected = "   Responds:  " + System.lineSeparator() + "   "
                + recomment1.username +": "+ recomment1.commentContent +
                System.lineSeparator() +"   " +recomment2.username + ": "
                + recomment2.commentContent + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        PrintStream printStreamSender = new PrintStream(osSender);
        System.setOut(printStreamSender);
        comment.getRecomments();
        String actual = null;
        try {
            actual = osSender.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        assert (actual != null);
        assertEquals(expected, actual);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ? OR CommentID = ? OR CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.setInt(2, recomment1.commentId);
            prstm.setInt(3, recomment2.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ? OR RecommentID = ?");
            prstm.setInt(1, recomment1.commentId);
            prstm.setInt(2, recomment2.commentId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    @Test
    public void testRetrieveConstructor() {
        Comment comment = new Comment("Excellent", 2, 1);
        Comment comment1 = new Comment(comment.commentId);
        assertEquals(comment.commentContent,comment1.commentContent);
        assertEquals(comment.from, comment1.from);
        assertEquals(comment.to,comment1.to);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE CommentID = ? OR CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.setInt(2, comment1.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    public void testPrintCommentRec() {
        //Creating the comment and recomments
        Comment comment = new Comment("why there is no flour?", 1, 2);
        Recomment recomment1 = new Recomment("Cause he doesn't like it",
                2, 2, comment.commentId);
        Recomment recomment2 = new Recomment("Curiosity killed the cat",
                3, 2, comment.commentId);
        //Add the recomments to the comment
        comment.recomments.add(recomment1);
        comment.recomments.add(recomment2);
        //Making the expected string of recomments
        String expected = " 2: tsappy: why there is no flour?" +
                System.lineSeparator() + "   Responds:  " + System.lineSeparator()
                + "   " + recomment1.username +": "+ recomment1.commentContent +
                System.lineSeparator() +"   " +recomment2.username + ": "
                + recomment2.commentContent + System.lineSeparator();
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        PrintStream printStreamSender = new PrintStream(osSender);
        System.setOut(printStreamSender);
        comment.printCommentRec(1);
        String actual = null;
        try {
            actual = osSender.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        assert (actual != null);
        assertEquals(expected, actual);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE " +
                    "CommentID = ? OR CommentID = ? OR CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.setInt(2, recomment1.commentId);
            prstm.setInt(3, recomment2.commentId);
            prstm.executeUpdate();
            prstm = connection.prepareStatement("DELETE FROM Recomment WHERE" +
                    " RecommentID = ? OR RecommentID = ?");
            prstm.setInt(1, recomment1.commentId);
            prstm.setInt(2, recomment2.commentId);
            prstm.executeUpdate();
        } catch(SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }

    public void testPrintComment() {
        Comment comment = new Comment("perfect", 2, 1);
        String expected = comment.username + ": perfect";
        ByteArrayOutputStream osSender = new ByteArrayOutputStream();
        PrintStream printStreamSender = new PrintStream(osSender);
        System.setOut(printStreamSender);
        comment.printComment();
        String actual = null;
        try {
            actual = osSender.toString("UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        }
        assert (actual != null);
        assertEquals(expected, actual);
        Connection connection = DBcon.openConnection();
        PreparedStatement prstm = null;
        try {
            prstm = connection.prepareStatement("DELETE FROM Comment WHERE" +
                    " CommentID = ?");
            prstm.setInt(1, comment.commentId);
            prstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DBcon.closeStatement(prstm);
            DBcon.closeConnection(connection);
        }
    }
}
