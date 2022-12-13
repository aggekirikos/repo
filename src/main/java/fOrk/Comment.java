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
    protected String username;
    protected int to;
    public Comment(){}
    public Comment(int id) {
        this.commentId = id;
        String select = "SELECT Content, ToPost, Sender, Username FROM Comment, User WHERE CommentID=? AND Sender = ID";
        Connection connection = null;
        PreparedStatement pst = null;
        PreparedStatement pst2 = null;
        try {
            connection = DBcon.openConnection();
            pst = connection.prepareStatement(select);
            pst.setInt(1, id);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                this.commentContent = resultSet.getString("Content");
                this.to = resultSet.getInt("ToPost");
                this.from = resultSet.getInt("Sender");
                this.username= resultSet.getString("Username");
            }
            pst2 = connection.prepareStatement("SELECT RecommentID FROM Recomment WHERE Receiver = ?");
            pst2.setInt(1, id);
            ResultSet resultSet2 = pst2.executeQuery();
            while (resultSet2.next()) {
                Recomment recomment = new Recomment(resultSet2.getInt(1),commentId);
                recomments.add(recomment);
            }
        } catch (SQLException e) { System.out.println(e.getMessage());
        }  finally {
            DBcon.closeStatement(pst);
            DBcon.closeStatement(pst2);
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
        } catch (Exception e) { System.out.println(e.getMessage());}
        finally {
            DBcon.closeStatement(stm);
            DBcon.closeConnection(con);
        }
        commentContent = content;
        this.from = from;
        this.to = to;//Refers to specific post
        recomments = null;
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DBcon.openConnection();
            pst = connection.prepareStatement("INSERT INTO Comment VALUES (?,?,?,?)");
            pst.setInt(1, commentId);
            pst.setString(2, commentContent);
            pst.setInt(3, from);
            pst.setInt(4, to);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
        System.out.println(recomments.size());
        if (!recomments.isEmpty()) {
            System.out.println("   with responds:  ");
            while (i < loops) {
                System.out.print("   ");
                recomments.get(i).printComment();
                i++;
            }
        }
    }
    public void printComment() {
        System.out.println( getCommentContent() +" from " + username );
    }
    public void printCommentRec(int counter) {
            System.out.print(" " + (counter + 1) + ": ");
            printComment();
            getRecomments();
    }
    public boolean checkFirstLineComment() {
        Connection connection = DBcon.openConnection();
        Statement statement = null;
        String select = "SELECT RecommentID FROM Recomment";
        boolean first_line = true;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while(resultSet.next()) {
                if(commentId == resultSet.getInt(1)) {
                    first_line = false;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(statement);
            DBcon.closeConnection(connection);
        }
        return first_line;
    }
}
