package fork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains constructors that retrieve
 * the recomments of a comment from the database.
 */
public class Recomment extends Comment {
    /**
     * CommentID that the recomment refers to.
     */
    protected int toComment;
    /**
     * Unique recomment ID.
     */
    protected int recommentID;
    /**
     * Constructor that retrieves recomment characteristics
     * from database by recomment ID and the ID of the comment
     * to witch recomments refer.
     * @param recId the ID of the recomment that we want to retrieve
     * @param toComment the ID of the comment that the recomment refers
     */

    public Recomment(int recId, int toComment) {
        super();
        this.toComment = toComment;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            con = DBcon.openConnection();
            preparedStatement = con.prepareStatement("SELECT Content, ToPost, Sender, Username "
            + "FROM Comment, User WHERE CommentID=? AND Sender = ID");
            preparedStatement.setInt(1, recId);
            rs = preparedStatement.executeQuery();
            this.toComment = toComment;
            recommentID = recId;
            while (rs.next()) {
                commentContent = rs.getString("Content");
                from = rs.getInt("Sender");
                to = rs.getInt("ToPost");
                username = rs.getString("Username");
                break;
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(preparedStatement);
            DBcon.closeConnection(con);
        }

    }
    /**
     * Basic recomment constructor.
     * @param content a string that will be our recomment
     * @param from The ID of the sender of the recomment
     * @param toPost the ID of the post that the comment refers to
     * @param toComment the ID of the comment that the recomment refers to
     *
     */

    public Recomment(String content, int from, int toPost, int toComment) {
        super(content, from, toPost);
        this.toComment = toComment;
        Connection connection = null;
        PreparedStatement pst = null;
        Statement statement = null;
        recommentID = commentId;
        try {
            connection = DBcon.openConnection();
            pst = connection.prepareStatement("INSERT INTO Recomment"
            + "(RecommentID, Receiver) VALUES (?,?)");
            pst.setInt(1, recommentID);
            pst.setInt(2, this.toComment);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }  finally {
            DBcon.closeStatement(pst);
            DBcon.closeConnection(connection);
        }
    }
}
