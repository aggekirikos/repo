package fork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
* This class contains methods that create, retrieve and display the
* characteristics of a post so that users can evaluate and review it.
* Also, via this class the characteristics of the post are saved
* in the database.
*/

public class Post {
    /**
   * Unique ID number for every post.
   */
    protected int postId;
    /**
   * A number that shows if the post exists or not.
   */
    protected int postStatus = 0;
    /**
   * The time that is required for the recipe of the post.
   */
    protected int recipeTime;
    /**
   * The content of the post.
   */
    protected String content;
    /**
   * The title of the post.
   */
    protected String title;
    /**
   * The recipe category to which the post belongs.
   */
    protected String recipeCategory;
    /**
   * The difficulty level to which the post belongs.
   */
    protected String difficultyLevel;
    /**
   * The cost required for the recipe of the post.
   */
    protected double recipeCost;
    /**
   * The comments that refer to this post.
   */
    protected ArrayList<Comment> comments = new ArrayList<Comment>();
    /**
   * The hashtags that refer to this post.
   */
    protected String[] hashtags = new String[5];
    /**
   * The Reviews that other users have made for this post.
   */
    protected double reviews = 0;
    /**
   * Τhe ID of the user who is the creator of this post.
   */
    protected int creator;
    /**
   * The table with the stars entered by other users for this post.
   **/
    protected int[] stars = new int[5];
    /**
   * Τhe total number of stars entered by other users for this post.
   */
    protected int sum = 0;

    /**
   * Constructor that adds values to the instance variables.
   *
   * @param userId   The creator of the post
   * @param title    The title of the post
   * @param content  The content of the post
   * @param cost     The cost required for the recipe of the post
   * @param time     The time required for the recipe of the post
   * @param diLevel  The difficulty level for the recipe of the post
   * @param category The category in which the recipe of the post belongs
   * @param has      Table that contains the hashtags of the post
   */

    public Post(int userId, String title, String content, double cost, int time,
        String diLevel, String category, String[] has) {
        postId = maxidfromDB() + 1;
        creator = userId;
        postStatus = 1;
        this.title = title;
        this.content = content;
        recipeCost = cost;
        recipeTime = time;
        difficultyLevel = diLevel;
        recipeCategory = category;
        for (int i = 0; i < has.length; i++) {
            hashtags[i] = has[i];
        }
        sendPosttoDB();
    }

    /**
   * Method that retrieves the max ID from database
   * and increases it by 1.
   *
   * @return the post's ID
   */

    public int maxidfromDB() {
        Connection connection = DBcon.openConnection();
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT max(PostID) FROM Post");
            while (rs.next()) {
                postId = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(statement);
            DBcon.closeConnection(connection);
        }
        return postId;
    }

    /**
    * Constructor that retrieves the post's characteristics from database
    * searching by post ID.
    *
    * @param id The post's ID
  */

    public Post(int id) {
        this.postId = id;
        ArrayList<Integer> commId = new ArrayList<Integer>();
        Connection connection = DBcon.openConnection();
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet resultSet = null;
        try {
            preparedStatement1 = connection.prepareStatement("SELECT Creator, "
            + "Title, Content, RecipeCost, RecipeCategory, DifficultyLevel,"
            + "RecipeTime FROM Post WHERE PostID = ?");
            preparedStatement3 =
            connection.prepareStatement("SELECT * FROM stars WHERE PostID = ?");
            preparedStatement1.setInt(1, id);
            preparedStatement3.setInt(1, id);
            rs = preparedStatement1.executeQuery();
            while (rs.next()) {
                creator = rs.getInt(1);
                title = rs.getString(2);
                content = rs.getString(3);
                recipeCost = rs.getDouble(4);
                recipeCategory = rs.getString(5);
                difficultyLevel = rs.getString(6);
                recipeTime = rs.getInt(7);
                boolean flag = true;
            }
            rs1 = preparedStatement3.executeQuery();
            while (rs1.next()) {
                stars[0] = rs1.getInt("star1");
                stars[1] = rs1.getInt("star2");
                stars[2] = rs1.getInt("star3");
                stars[3] = rs1.getInt("star4");
                stars[4] = rs1.getInt("star5");
            }
            for (int i = 0; i < 5; i++) {
                sum = sum + (i + 1) * stars[i];
            }
            if (evaluators() != 0) {
                reviews = (double) sum / evaluators();
            }

            preparedStatement2 = connection.prepareStatement("SELECT * FROM Comment "
            + " WHERE ToPost = ? ");
            preparedStatement2.setInt(1, id);
            resultSet = preparedStatement2.executeQuery();
            while (resultSet.next()) {
                commId.add(resultSet.getInt("CommentID"));
            }
            rs.close();
            rs1.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "post");
            DBcon.closeResultSet(rs);
            DBcon.closeResultSet(rs1);
            DBcon.closeResultSet(resultSet);
        } finally {
            DBcon.closeStatement(preparedStatement1);
            DBcon.closeStatement(preparedStatement2);
            DBcon.closeStatement(preparedStatement3);
            DBcon.closeConnection(connection);
        }
        for (int commentId: commId) {
            Comment comment = new Comment(commentId);
            if (comment.checkFirstLineComment()) {
                comments.add(comment);
            }
        }
    }

    /**
    * Method that calls the class comment and adds a comment to the post.
    * @param comment A comment of the post
   */

    public void createComment(Comment comment) {
        comments.add(comment);
    }

    /**
    * Method that calculates the average number of stars that a publication has.
   */

    public void makeReview() {
        Scanner input = new Scanner(System.in, "utf-8");
        System.out.println("How much do you like this post? Rate from 1 to 5 stars.");
        int rate;
        do {
            rate = input.nextInt();
        } while (rate < 1 || rate > 5);

        stars[rate - 1] = stars[rate - 1] + 1;
        for (int i = 0; i < 5; i++) {
            sum = sum + (i + 1) * stars[i];
        }
        sendReviewstoDB(rate);
        if (evaluators() != 0) {
            reviews = (double) sum / evaluators();
        }
        System.out.println("Your review has been inserted");
    }

    /**
    * Method that retrieves the number of people that have evaluate this post
    * from the database searching by PostID.
    *
    * @return evaluatorsNumber The number of people that have evaluate the post
   */

    public int evaluators() {
        Connection connection = DBcon.openConnection();
        PreparedStatement preparedStatement = null;
        String select = "SELECT (star1 + star2 + star3 + star4 + star5) AS"
            + " Evaluators FROM stars WHERE PostID = ?";
        int evaluatorsNumber = 0;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(select);
            preparedStatement.setInt(1, postId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                evaluatorsNumber = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(preparedStatement);
            DBcon.closeConnection(connection);
        }
        return evaluatorsNumber;
    }

    /**
    * Method that returns the PostId.
    *
    * @return the post's ID
   */

    public int getPostId() {
        return postId;
    }

    /**
    * Method that returns the Creator of the post.
    *
    * @return the post's creator
   */

    public int getCreator() {
        return creator;
    }

    /**
    * Method that returns the title of the Recipe in the post.
    *
    * @return the post's title
   */

    public String getTitle() {
        return title;
    }

    /**
    * Method that returns the Content of the Recipe in the post.
    *
    * @return the post's content
   */
    public String getContent() {
        return content;
    }

    /**
    * Method that returns the Cost of the Recipe in the post.
    *
    * @return the post's recipecost
   */

    public double getRecipeCost() {
        return recipeCost;
    }

    /**
    * Method that returns the Time of the Recipe in the post.
    *
    * @return the post's recipetime
   */

    public int getRecipeTime() {
        return recipeTime;
    }

    /**
    * Method that returns the DifficultyLevel of the Recipe in the post.
    *
    * @return the post's difficultylevel
   */

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
    * Method that returns the Category of the Recipe in the post.
    *
    * @return the post's recipecategory
   */

    public String getRecipeCategory() {
        return recipeCategory;
    }

    /**
    * Method that sends the characteristics of the post to the database.
   */

    public void sendPosttoDB() {
        Connection connection = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        try {
            connection = DBcon.openConnection();
            stmt1 = connection.prepareStatement("INSERT INTO Post(PostID,"
            + "PostStatus,RecipeTime,Content,Title,RecipeCategory,"
            + "DifficultyLevel,RecipeCost,Creator) VALUES(?, ?, ?, ?,"
            + " ?, ?, ?, ?, ?)");
            int i = 0;
            stmt1.setInt(1, postId);
            stmt1.setInt(2, postStatus);
            stmt1.setInt(3, recipeTime);
            stmt1.setString(4, content);
            stmt1.setString(5, title);
            stmt1.setString(6, recipeCategory);
            stmt1.setString(7, difficultyLevel);
            stmt1.setDouble(8, recipeCost);
            stmt1.setInt(9, creator);
            stmt1.executeUpdate();
            while (hashtags[i] != null && i <= 4) {
                stmt2 = connection.prepareStatement("INSERT INTO Hashtags(Hashtag,PostID) "
                    + "VALUES(?, ?)");
                stmt2.setString(1, hashtags[i]);
                stmt2.setInt(2, postId);
                stmt2.executeUpdate();
                i++;
            }
            stmt3 = connection.prepareStatement("INSERT INTO "
                + "stars(star1,star2,star3,star4,star5,PostID) "
                + "VALUES(0, 0, 0, 0, 0, ?)");
            stmt3.setInt(1, postId);
            stmt3.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Something went wrong while creating this post" + e.getMessage());
        } finally {
            DBcon.closeStatement(stmt1);
            DBcon.closeStatement(stmt2);
            DBcon.closeStatement(stmt3);
            DBcon.closeConnection(connection);
        }
    }

    /**
    * Method that sends the Reviews of a post to the database.
    *
    *@param rate The number of the star entered by the user who rated the post
   */

    public void sendReviewstoDB(int rate) {
        // rate (1-5)
        Connection connection = null;
        PreparedStatement stmt2 = null;
        String select = java.text.MessageFormat.format("UPDATE stars SET {0} ="
            + " ? WHERE PostID=?", "star" + rate);
        try {
            connection = DBcon.openConnection();
            stmt2 = connection.prepareStatement(select);
            stmt2.setInt(1, stars[rate - 1]);
            stmt2.setInt(2, getPostId());
            stmt2.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(stmt2);
            DBcon.closeConnection(connection);
        }
    }

    /**
    * Method that checks if the comment has content or not.
    *
    * @return true if the comment has content and false if the comment does not have content
   */

    public boolean commentListSize() {
        if (comments.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
   * Method that displays the characteristics of the post on the user's screen.
   */

    public void getPost() {
        System.out.println("Title of the post: " + getTitle() + "\n"
            + "Content of the post: " + getContent() + "\n"
            + "The time required for this recipe is: " + getRecipeTime() + " minutes \n"
            + "The cost for this recipe is: " + getRecipeCost() + " euros" + "\n"
            + "The difficulty Level of this recipe is: " + getDifficultyLevel() + "\n"
            + "The category of this recipe is: " + getRecipeCategory() + "\n"
            + "This post has " + reviews + " stars" + "\n"
            + "This post's comments are: ");
        if (comments.size() == 0) {
            System.out.println("  This post has no comments yet");
        } else {
            int loops = comments.size();
            int counter = 0;
            for (int i = 0; i < loops; i++) {
                Comment comment = comments.get(i);
                if (comment.checkFirstLineComment()) {
                    comment.printCommentRec(counter);
                    counter++;
                }
            }
        }
    }
}