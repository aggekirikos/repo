package fOrk;

import java.sql.*;

public class Recomment extends Comment {

    private int toComment; //CommentID that the recomment refers to
    private int RecommentID;
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