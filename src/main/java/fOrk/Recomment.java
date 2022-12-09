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
            preparedStatement = con.prepareStatement("SELECT * FROM Comment WHERE CommentID = ?");
            preparedStatement.setInt(1, recId);
            ResultSet rs = preparedStatement.executeQuery();
            this.toComment = toComment;
            RecommentID = recId;
            while (rs.next()) {
                String content = rs.getString("Content");
                String sender = rs.getString("Sender");
                int toPost = rs.getInt("ToPost");
                break;
            }

        } catch (Exception w) {}
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
            pst = connection.prepareStatement("INSERT INTO Recomment VALUES (?,?)");
            pst.setString(1, String.valueOf(commentId));
            pst.setString(2, String.valueOf(RecommentID));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }  finally {
            DBcon.closeStatement(pst);
            DBcon.closeConnection(connection);
        }
    }
}