package fOrk;


import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Comment {
    Scanner inp = new Scanner(System.in);
    protected int commentId;
    protected String commentContent;

    ArrayList<Recomment> recomments = new ArrayList<Recomment>();
    protected int from;
    protected int to;
    public Comment(){}
    public Comment(int id) {
        this.commentId = id;
        String select = "SELECT * FROM Comment WHERE CommentID=?";
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DBcon.openConnection();
            pst = connection.prepareStatement("SELECT * FROM Comment WHERE CommentID=?");
            pst.setString(1, String.valueOf(id));
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                this.commentContent = resultSet.getString("Content");
                this.to = resultSet.getInt("ToPost");
                this.from = resultSet.getInt("Sender");
            }
            pst = connection.prepareStatement("SELECT RecommentID FROM Recomment WHERE Receiver = ?");
            pst.setString(1, String.valueOf(id));
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Recomment recomment = new Recomment(resultSet.getInt(1),commentId);
                recomments.add(recomment);
            }
        } catch (SQLException e) {
        }  finally {
            DBcon.closeStatement(pst);
            DBcon.closeConnection(connection);
        }
    }
    public Comment(String content , int from, int to) {
        Connection con = null;
        Statement stm = null;
        try {
            con = DBcon.openConnection();
            stm = con.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(CommentID) FROM Comment");
            commentId = rs.getInt(1);
            commentId++;
        } catch (Exception e) {}
        finally {
            DBcon.closeStatement(stm);
            DBcon.closeConnection(con);
        }
        commentContent = content;
        this.from = from;
        this.to = to;//Refers to specific post
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DBcon.openConnection();
            pst = connection.prepareStatement("INSERT INTO Comment VALUES (?,?,?,?)");
            pst.setString(1, String.valueOf(commentId));
            pst.setString(2, commentContent);
            pst.setString(3, String.valueOf(from));
            pst.setString(4, String.valueOf(to));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBcon.closeStatement(pst);
            DBcon.closeConnection(connection);
        }
    }

    public String getCommentContent(){
        return commentContent;
    }
    public void makeReComment(int userId, int postId, int commentId) {
        System.out.print("Type the recomment: ");
        String a = inp.nextLine();
        int recId = recomments.size() + 1;
        Recomment r = new Recomment( a, userId, postId, commentId);
        recomments.add(r);
        System.out.print("Recomment is added!");
    }
    public void getRecomments() {
        int loops = recomments.size();
        int i = 0;
        while (i < loops) {
            System.out.println("     " + recomments.get(i));
            i++;
        }
    }
    public void printCommentRec() {
        System.out.print( getCommentContent() +" from " + from + " with responds: ");
        getRecomments();
    }
}
