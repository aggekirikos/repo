package fOrk;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.SQLException;

public class Post {
	/**
	* Unique ID number for every post
	*/
	public int postId;
	/**
	* A number that shows if the post exists or not
	*/
	public int postStatus = 0;
	/**
	* The time that is required for the recipe of the post
	*/
	public int recipeTime;
	/**
	* The content of the post
	*/
	public String content;
	/**
	* The title of the post
	*/
	public String title;
	/**
	* The recipe category to which the post belongs
	*/
	public String recipeCategory;
	/**
	* The difficulty level to which the post belongs
	*/
	public String difficultyLevel;
	/**
	* The cost required for the recipe of the post
	*/
	public double recipeCost;
	/**
	* The comments that refer to this post
	*/
	ArrayList<Comment> comments = new ArrayList<Comment>();
	/**
	* The hashtags that refer to this post
	*/
	public String [] hashtags = new String [5];
	/**
	* The Reviews that other users have made for this post
	*/
	double reviews = 0;
	/**
	* Τhe ID of the user who is the creator of this post
	*/
	int creator ;
	/**
	* The table with the stars entered by other users for this post
	**/
	int [] stars = new int[5];
	/**
	* Τhe total number of stars entered by other users for this post
	*/
	int sum = 0;

	Scanner input = new Scanner(System.in,"utf-8");
	/**
	* Constructor that adds values to the instance variables
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
				String diLevel,String category, String[] has) {
		postId = maxidfromDB();
		creator = userId;
		postStatus = 1;
		this.title = title;
		this.content = content;
		recipeCost= cost;
		recipeTime = time;
		difficultyLevel = diLevel;
		recipeCategory = category;
		hashtags = has;
		sendPosttoDB();
	}

	/**
	* Method that retrieves the max ID from database
	* and increases it by 1
	*
	* @return the post's ID
	*/

	public int maxidfromDB() {
		Connection connection = DBcon.openConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT max(PostID) FROM Post");
			rs.close();
			postId = rs.getInt(1);
			postId++;
		} catch (SQLException e) {}
		finally {
			DBcon.closeStatement(statement);
			DBcon.closeConnection(connection);
		}
		return postId;
	}

	/**
	* Constructor that retrieves the post's characteristics from database
	* searching by post ID
	*
	* @param id The post's ID
	*/

	public Post(int id) {
		this.postId = id;
		ArrayList<Integer> commId= new ArrayList<Integer>();
		Connection connection = DBcon.openConnection();
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		try {
			preparedStatement1 = connection.prepareStatement("SELECT * FROM Post WHERE PostID = ?");
			preparedStatement3 = connection.prepareStatement("SELECT * FROM stars WHERE PostID = ?");
			preparedStatement1.setInt(1, id);
			preparedStatement3.setInt(1, id);
			ResultSet rs = preparedStatement1.executeQuery();
			rs.close();
			ResultSet rs1 = preparedStatement3.executeQuery();
			rs1.close();
			while (rs.next()) {
				postId = rs.getInt("PostID");
				title = rs.getString("Title");
				content = rs.getString("Content");
				recipeCost = rs.getDouble("RecipeCost");
				recipeCategory = rs.getString("RecipeCategory");
				difficultyLevel = rs.getString("DifficultyLevel");
				boolean flag = true;
			}
			while(rs1.next()) {
				stars[0] = rs1.getInt("star1");
				stars[1] = rs1.getInt("star2");
				stars[2] = rs1.getInt("star3");
				stars[3] = rs1.getInt("star4");
				stars[4] = rs1.getInt("star5");
			}
			for (int i = 0; i<5; i++) {
				sum = sum + (i+1) * stars[i];
			}
			if (evaluators() != 0) {
				reviews = (double ) sum/evaluators();
			}

			preparedStatement2 = connection.prepareStatement("SELECT * FROM Comment " +
					" WHERE ToPost = ? ");
			preparedStatement2.setInt(1, postId);
			ResultSet resultSet = preparedStatement2.executeQuery();
			resultSet.close();
			while (resultSet.next()) {
				commId.add(resultSet.getInt("CommentID"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "post");
		}
		finally {
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
	* Method that calls the class comment and adds a comment to the post
	*/

	public void createComment(Comment comment) {
			comments.add(comment);
	}

	/**
	* Method that calculates the average number of stars that a publication has
	*/

	public void makeReview() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("How much do you like this post? Rate from 1 to 5 stars.");
		int rate;
		do {
			rate = scanner.nextInt();
		} while (rate < 1 || rate > 5);

		stars[rate - 1] = stars[rate - 1] + 1;
		for (int i = 0; i<5; i++) {
			sum = sum + (i+1) * stars[i];
		}
		sendReviewstoDB(rate);
		if (evaluators() != 0) {
			reviews = (double) sum / evaluators();
		}

	}

	/**
	* Method that retrieves the number of people that have evaluate this post
	* from the database searching by PostID
	*
	* @return evaluatorsNumber The number of people that have evaluate the post
	*/

	public int evaluators(){
		Connection connection = DBcon.openConnection();
		PreparedStatement preparedStatement = null;
		String select = "SELECT (star1 + star2 + star3 + star4 + star5) AS Evaluators FROM stars WHERE PostID = ?";
		int evaluatorsNumber = 0;
		try {
			preparedStatement = connection.prepareStatement(select);
			preparedStatement.setInt(1, postId);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				evaluatorsNumber = rs.getInt(1);
			}
		} catch(SQLException e){

		} finally {
			DBcon.closeStatement(preparedStatement);
			DBcon.closeConnection(connection);
		}
		return evaluatorsNumber;
	}

	/**
	* Method that returns the PostId
	*
	* @return the post's ID
	*/

	public int getPostId() {
		return postId;
	}

	/**
	* Method that returns the Creator of the post
	*
	* @return the post's creator
	*/

	public int getCreator() {
		return creator;
	}

	/**
	* Method that returns the title of the Recipe in the post
	*/

	public String getTitle() {
		return title;
	}

	/**
	* Method that returns the Content of the Recipe in the post
	*
	* @return the post's content
	*/
	public String getContent() {
		return content;
	}

	/**
	* Method that returns the Cost of the Recipe in the post
	*
	* @return the post's recipecost
	*/

	public double getRecipeCost() {
		return recipeCost;
	}

	/**
	* Method that returns the Time of the Recipe in the post
	*
	* @return the post's recipetime
	*/

	public int getRecipeTime() {
		return recipeTime;
	}

	/**
	* Method that returns the DifficultyLevel of the Recipe in the post
	*
	* @return the post's difficultylevel
	*/

	public String getDifficultyLevel(){
		return difficultyLevel;
	}

	/**
	* Method that returns the Category of the Recipe in the post
	*
	* @return the post's recipecategory
	*/

	public String getRecipeCategory() {
		return recipeCategory;
	}

	/**
	* Method that sends the characteristics of the post to the database
	*/

	public void sendPosttoDB() {
		Connection connection = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		PreparedStatement stmt3 = null;
		int maxid = getPostId();
		try {
			connection = DBcon.openConnection();
			stmt1 = connection.prepareStatement("INSERT INTO Post(PostID,PostStatus,RecipeTime,Content,Title,RecipeCategory,DifficultyLevel,RecipeCost,Creator) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int i = 0;
			stmt1.setInt(1, maxid);
			stmt1.setInt(2, postStatus);
			stmt1.setInt(3, recipeTime);
			stmt1.setString(4, content);
			stmt1.setString(5, title);
			stmt1.setString(6, recipeCategory);
			stmt1.setString(7, difficultyLevel);
			stmt1.setDouble(8, recipeCost);
			stmt1.setInt(9, creator);
			stmt1.executeUpdate();
			while(hashtags[i] != null) {
				stmt2 = connection.prepareStatement("INSERT INTO Hashtags(Hashtag,PostID) VALUES(?, ?)");
				stmt2.setString(1, hashtags[i]);
				stmt2.setInt(2, maxid);
				stmt2.executeUpdate();
				i++;
			}
			stmt3 = connection.prepareStatement("INSERT INTO stars(star1,star2,star3,star4,star5,PostID) VALUES(0, 0, 0, 0, 0, ?)");
			stmt3.setInt(1, maxid);
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
	* Method that sends the Reviews of a post to the database
	*
	*@param rate The number of the star entered by the user who rated the post
	*/

	public void sendReviewstoDB( int rate) {
		// rate (1-5)
		Connection connection = null;
		PreparedStatement stmt2 = null;
		String select = java.text.MessageFormat.format("UPDATE stars SET {0} = ? WHERE PostID=?", "star" + rate);
		try {
			connection = DBcon.openConnection();
			stmt2 = connection.prepareStatement(select);
			stmt2.setInt(1, stars[rate-1]);
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
	* Method that checks if the comment has content or not
	*
	* @return true if the comment has content and false if the comment does not have content
	*/

	public boolean commentListSize() {
		if (comments.size() != 0)
			return true;
		else
			return false;
	}

	/**
	* Method that displays the characteristics of the post on the user's screen
	*/

	public void getPost() {
		System.out.println( "Title of the post: " + getTitle() + "\n" +
				"Content of the post: " + getContent() + "\n" +
				"The time required for this recipe is: " + getRecipeTime() + "\n" +
				"The cost for this recipe is:" + getRecipeCost() + " €" + "\n" +
				"The difficulty Level of this recipe is: " + getDifficultyLevel() + "\nT" +
				"he category of this recipe is: " + getRecipeCategory() + "\n" +
				"This post has " + reviews + " stars" + "\n" +
				"This post's comments are: ");
		if (comments.size() == 0) {
			System.out.println("  This post has no comments yet");
		} else {
		  int loops = comments.size();
		  int counter = 0;
		  for  (int i = 0; i < loops; i++) {
				Comment comment = comments.get(i);
			    if (comment.checkFirstLineComment()) {
					comment.printCommentRec(counter);
					counter++;
				}
			}
		}
	}
}