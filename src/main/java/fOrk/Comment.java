package fOrk;


import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains a retriever constructor
 * for the comments on a specific post and displays them.
 *
 */

public class Comment {
    Scanner inp = new Scanner(System.in);
    /**
     * Unique ID number for every comment and recomment
     */
    protected int commentId;
    /**
     * The content of the comment
     */
    protected String commentContent;
    /**
     * Recomments that refer to this comment
     */
    ArrayList<Recomment> recomments = new ArrayList<Recomment>();
    /**
     * The ID og the sender of to comment
     */
    protected int from;
    /**
     * The username of the sender of the comment
     */
    protected String username;
    /**
     * The ID of the post in witch the comment refers
     */
    protected int to;
    /**
     * Default constructor
     */
    public Comment(){}
    /**
     * Constructor that retrieves the comment characteristics
     * from database, searching by comment ID
     *
     * @param id the id of the comment that we want to retrieve
     * @retun Comment object with the same characteristics
     * that the comment with the entered id has
     */
    public Comment(int id) {
        this.commentId = id;
        String select = "SELECT Content, ToPost, Sender, Username FROM " +
                "Comment, User WHERE CommentID=? AND Sender = ID";
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
    /**
     * Basic constructor that creates a comment using the arguments
     *
     * @param content A string that will be our comment
     * @param from The ID of the sender of the comment
     * @param to the ID of the post that the comment refers
     * @return Comment object with the inserted characteristics
     */
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
        this.username = Main.getUsernamefromID(from);
        this.to = to;//Refers to specific post
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
    /**
     * Returns the content of every comment
     *
     *
     */
    public String getCommentContent(){
        return commentContent;
    }
    /**
     * Method we use to create a recomment on a comment
     *
     * @param userId the id of the sender
     * @param postId the id of the post that the comment refers
     * @param commentId the id of the comment that recomment refers
     */
    public void makeReComment(int userId, int postId, int commentId) {
        System.out.print("Type the recomment: ");
        Scanner input = new Scanner(System.in);
        String a = input.nextLine();
        int recId = recomments.size() + 1;
        Recomment r = new Recomment( a, userId, postId, commentId);
        recomments.add(r);
        System.out.println("Recomment is added!");
        input.close();
    }
    /**
     * Method that prints all the recomments of a comment
     */
    public void getRecomments() {
        if (!recomments.isEmpty()) {
            int loops = recomments.size();
            int i = 0;
            System.out.println("   Responds:  ");
            while (i < loops) {
                System.out.print("   ");
                recomments.get(i).printComment();
                i++;
            }
        }
    }
    /**
     * Method that prints the comments and the username of the sender
     */
    public void printComment() {
        System.out.println(  username + ": " + getCommentContent() );
    }
    /**
     * Method that prints the comment with the username of the sender by using printComment,
     * and all the recomments of this comment
     */
    public void printCommentRec(int counter) {
        System.out.print(" " + (counter + 1) + ": ");
        printComment();
        getRecomments();
    }

    /**
     * This method returns a boolean result
     * based on if a comment is a first line
     * comment and not a recomment.
     * @return first_line the method returns true if the comment is first
     * line and false if it is not.
     */
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
