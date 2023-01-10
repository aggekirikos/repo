package fOrk;

import java.sql.*;

public class Recomment extends Comment {
    /**
     * CommentID that the recomment refers to
     */
    private int toComment;
    /**
     * Unique recomment ID
     */
    private int RecommentID;
    /**
     * Constructor that retrieves recomment characteristics
     * from database by recomment ID and the ID of the comment
     * to witch recomments refer
     *
     * @param recId the ID of the recomment that we want to retrieve
     * @param toComment the ID of the comment that the recomment refers
     * @return Recomment object with the same characteristics of the one
     * we enterd the parametrs
     */
    public Recomment(int recId, int toComment) {
        super();
        this.toComment = toComment;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = DBcon.openConnection();
            preparedStatement = con.prepareStatement("SELECT Content, ToPost, Sender, Username FROM Comment, User WHERE CommentID=? AND Sender = ID");
            preparedStatement.setInt(1, recId);
            ResultSet rs = preparedStatement.executeQuery();
            this.toComment = toComment;
            RecommentID = recId;
            while (rs.next()) {
                commentContent = rs.getString("Content");
                from = rs.getInt("Sender");
                to = rs.getInt("ToPost");
                username= rs.getString("Username");
                break;
            }
        } catch (SQLException e) {System.out.println(e.getMessage());
        }finally{
            DBcon.closeStatement(preparedStatement);
            DBcon.closeConnection(con);
        }

    }
    /**
     * Basic recomment constructor
     *
     * @param content a strting that will be our recomment
     * @param from The ID of the sender of the recomment
     * @param toPost the ID of the post that the comment refers
     * @param toComment the ID of the comment that the recomment refers
     *
     * @return Recomment Object with the entered characteristics
     */
    public Recomment(String content, int from, int toPost, int toComment ) {
        super(content, from, toPost);
        this.toComment = toComment;
        Connection connection = null;
        PreparedStatement pst = null;
        Statement statement = null;
        RecommentID = commentId;
        try {
            connection = DBcon.openConnection();
            pst = connection.prepareStatement("INSERT INTO Recomment(RecommentID, Receiver) VALUES (?,?)");
            pst.setInt(1, RecommentID);
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