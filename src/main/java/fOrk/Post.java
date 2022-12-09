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
	public static int PostId = 0;
	private String DifficutlyLevel;
	public int PostStatus = 0;//When PostStatus is 0 the post is deleted. When PostStatus is 1 the post exists
	public int RecipeTime;
	public String Content,Title,RecipeCategory,DifficultyLevel;
	public double RecipeCost,Reviews;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	public String [] hashtags = new String [5];
	int Creator ;
	int [] stars = new int[5];
	int evaluators = 0;
	int sum = 0;
	Scanner input = new Scanner(System.in);

	public Post(int userId, String title, String content, double cost, int time,
				String diLevel,String category, String[] has) {
		Creator = userId;
		maxidfromDB();
		PostStatus = 1;
		Title = title;
		Content = content;
		RecipeCost= cost;
		RecipeTime = time;
		DifficultyLevel = diLevel;
		RecipeCategory = category;
		hashtags = has;
		sendPosttoDB(/*PostId,PostStatus,RecipeTime,Content,Title,RecipeCategory,DifficutlyLevel,RecipeCost,Creator,hashtags*/);
	}
	public static void maxidfromDB() {
		Connection connection = DBcon.openConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT max(PostID) FROM Post");
			PostId = rs.getInt(1);
			PostId++;
		} catch (Exception e) {}
		finally {
			DBcon.closeStatement(statement);
			DBcon.closeConnection(connection);
		}
	}

	public Post(int id) {
		Connection connection = DBcon.openConnection();
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement3 = null;
		try {
			preparedStatement1 = connection.prepareStatement("SELECT * FROM Post WHERE PostID = ?");
			preparedStatement3 = connection.prepareStatement("SELECT * FROM stars WHERE PostID = ?");
			preparedStatement1.setString(1, String.valueOf(id));
			preparedStatement3.setString(1, String.valueOf(id));
			ResultSet rs = preparedStatement1.executeQuery();
			ResultSet rs1 = preparedStatement3.executeQuery();
			while (rs.next()) {
				PostId = rs.getInt("PostID");
				Title = rs.getString("Title");
				Content = rs.getString("Content");
				RecipeCost = rs.getDouble("RecipeCost");
				RecipeCategory = rs.getString("RecipeCategory");
				DifficultyLevel = rs.getString("DifficultyLevel");
				evaluators = rs.getInt("evaluators");
				Reviews = rs.getDouble("Reviews");
				boolean flag = true;
				do{
					PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM Comment" +
							"WHERE PostID = ToPost");
					ResultSet resultSet = preparedStatement2.executeQuery();
					while (resultSet.next()) {
						int ID = resultSet.getInt("CommentID");
						Comment comment = new Comment(ID);
						comments.add(comment);
					}
					DBcon.closeStatement(preparedStatement2);
					flag = false;
				} while(flag);
			}
			while(rs1.next()) {
				stars[1] = rs1.getInt("star1");
				stars[2] = rs1.getInt("star2");
				stars[3] = rs1.getInt("star3");
				stars[4] = rs1.getInt("star4");
				stars[5] = rs1.getInt("star5");
			}
		} catch (Exception e) {}
		finally {
			DBcon.closeStatement(preparedStatement1);
			DBcon.closeStatement(preparedStatement3);
			DBcon.closeConnection(connection);
		}
	}
	public void getReview() {
		int sum2 = 0;
		for (int k = 0; k < 5; k++) {
			sum2 = sum2 + stars[k];
		}
		Reviews = sum2 / sum;
	}

public void createComment(int
								  from, int toPost, int id ) {
		String answer = input.next();
		if (answer == "Yes") {
			System.out.print("Please type the comment : ");
			String a = input.nextLine();
			Comment comment = new Comment(a, from, toPost);
			comments.add(comment);
		}
	}
	public void makeReview() {
		System.out.println("Do you like the post?Rate it with 0 to 5 stars");
		int rate;
		do {
			rate = input.nextInt();
		} while (rate < 0 || rate > 5);

		stars[rate] = stars[rate] + 1;
		evaluators = evaluators + 1;
		for (int i = 0; i<5; i++) {
			sum = sum + (i+1) * stars[i];
		}
		Reviews = sum/evaluators;
		sendReviewstoDB(Reviews,evaluators,stars);
	}
	public int getPostId() {
		return PostId;
	}
	public int getCreator() {
		return Creator;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String Title) {
		this.Title = Title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String Content) {
		this.Content = Content;
	}
	public double getRecipeCost() {
		return RecipeCost;
	}
	public void setRecipeCost(double RecipeCost) {
		this.RecipeCost = RecipeCost;
	}
	public int getRecipeTime() {
		return RecipeTime;
	}
	public void setRecipeTime(int RecipeTime) {
		this.RecipeTime = RecipeTime;
	}
	public String getDifficultyLevel(){
		return DifficultyLevel;
	}
	public void setDifficultyLevel(String DifficultyLevel) {
		this.DifficultyLevel = DifficultyLevel;
	}
	public String getRecipeCategory() {
		return RecipeCategory;
	}
	public void setRecipeCategory(String RecipeCategory) {
		this.RecipeCategory = RecipeCategory;
	}
	public double getReviews() {
		return Reviews;
	}

	public void sendPosttoDB(/*int postid, int poststatus, int recipetime, String content, String title,
	String recipecategory, String difficultylevel, double recipecost, int creator, String[] hashtags*/) {
		Connection connection = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		int maxid = getPostId();
		try {
			connection = DBcon.openConnection();
			stmt1 = connection.prepareStatement("INSERT INTO Post(PostID,PostStatus,RecipeTime,Content,Title,RecipeCategory,DifficultyLevel,RecipeCost,Creator) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int i = 0;
			while(hashtags[i] != null) {
				stmt2 = connection.prepareStatement("INSERT INTO Hashtags(Hashtag,PostID) VALUES(?, ?)");
				stmt2.setString(1, hashtags[i]);
				stmt2.setInt(2, maxid);
			}
			stmt1.setInt(1, maxid);
			stmt1.setInt(2, PostStatus);
			stmt1.setInt(3, RecipeTime);
			stmt1.setString(4, Content);
			stmt1.setString(5, Title);
			stmt1.setString(6, RecipeCategory);
			stmt1.setString(7, DifficultyLevel);
			stmt1.setDouble(8, RecipeCost);
			stmt1.setInt(9, Creator);
			stmt1.executeUpdate();
			stmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while creating this post" + e.getMessage());
		} finally {
			DBcon.closeStatement(stmt1);
			DBcon.closeStatement(stmt2);
			DBcon.closeConnection(connection);
		}
	}

	public void sendReviewstoDB(double reviews, int evaluators, int[] stars) {
		Connection connection = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		int maxid = getPostId();
		try {
			connection = DBcon.openConnection();
			stmt2 = connection.prepareStatement("INSERT INTO stars(star1,star2,star3,star4,star5,PostID) VALUES(?, ?, ?, ?, ?, ?)");
			stmt2.setInt(1, stars[1]);
			stmt2.setInt(2, stars[2]);
			stmt2.setInt(3, stars[3]);
			stmt2.setInt(4, stars[4]);
			stmt2.setInt(5, stars[5]);
			stmt2.setInt(6, maxid);
			stmt1.executeUpdate();
			stmt2.executeUpdate();
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt2);
			DBcon.closeConnection(connection);
		}
	}

	/*public void editPost() {
		Scanner input =  new Scanner(System.in);
		if (Creator == User.getID()) {
			System.out.println("Change the Title of your post");
			Title = input.next();
			System.out.println("Change the content of your post");
			Content = input.next();
			System.out.println("Change the cost of the recipe in your post");
			RecipeCost = input.nextDouble();
			System.out.println("Change the time of the recipe in your post");
			RecipeTime = input.nextInt();
			System.out.println("Change the Category of the recipe in your post");
			RecipeCategory = input.next();
			System.out.println("Change the Difficulty Level of the recipe in your post");
			DifficultyLevel = input.next();
		}else{
			System.out.println("You cannot edit this post");
		}
	}
	public void deletePost() {
		if (Creator == User.getID) {
		PostStatus = 0;
		Title = null;
		Content = null;
		RecipeCost = 0.0;
		RecipeTime = 0;
		RecipeCategory = null;
		DifficultyLevel = null;
		Reviews = 0;
		comments[PostId] = null;
		}else{
					System.out.println("You cannot delete this post");
		}
	}*/
	public void getPost() {
		System.out.println( "Title of the post:" + getTitle() + "/nContent of the post:" + getContent() + "/nThe time required for " +
				"this recipe is" + getRecipeTime() + "/nThe cost for this recipe is:" + getRecipeCost() + "euros" + "/The" +
				" difficulty Level of this recipe is:" + getDifficultyLevel() + "/nThe category of this recipe is:"
				+ getRecipeCategory() + "/nThis post has " + Reviews + "stars" + "/nThis post's comments are");
		int loops = comments.size();
		int i = 0;
		while (i < loops) {
			Comment comment = comments.get(i);
			comment.printCommentRec();
		}
	}
}