package fOrk;

import java.util.ArrayList;
import java.util.Scanner;
public class Post {
	public static int PostId = 0;
	public int PostStatus = 0;//When PostStatus is 0 the post is deleted. When PostStatus is 1 the post exists
	public int RecipeTime;
	public String Content,Title,RecipeCategory,DifficultyLevel;
	public double RecipeCost,Reviews;
	ArrayList<Comment> comments = new ArrayList<Comment>();
	public String [] hastags = new String [5];
	User user = new User();
	public String Creator = user.getUsername();
	int [] stars = new int[5];
	int evaluators = 0;
	int sum = 0;

	public Post(int userId){
		int Creator = userId;
		PostId = PostId + 1;
		PostStatus = 1;
		Scanner input = new Scanner(System.in);
		System.out.println("Create a Title for your post");
		Title = input.next();
		System.out.println("Create a content for your post");
		Content = input.next();
		System.out.println("Report the cost of the recipe in your post");
		RecipeCost= input.nextDouble();
		System.out.println("Report the time required for the recipe in your post");
		RecipeTime = input.nextInt();
		System.out.println("Report the difficulty level of the recipe in your post");
		DifficultyLevel = input.next();
		System.out.println("Report the Category in which the recipe of your post belongs");
		RecipeCategory = input.next();
		System.out.println("Add some hastags in your post");
		System.out.println("Type null if you do not have other hastags to add");
		int i = 0;
		hastags = input.next();
		while(i < 5 && hastags != null) {
			hastags[i+1] = hastags;
			i++;
			hastags = input.next();
		}
	}
	public void createComment(String from) {
		System.out.println("Would you like to make a comment to this post? Type Yes if you do, otherwise type No");
		String answer = input.next();
		if (answer == "Yes") {
			System.out.print("Please type the comment : ");
			String a = input.nextLine();
			Comment comment = new Comment(a, from);
			comments.add(comment);
		}
	}
	public void makeReview() {
		System.out.println("Do you like the post?Rate it with 0 to 5 stars");
		int rate = input.nextInt();
		if (rate != 0) {
			stars[rate -1] = stars[rate - 1] + 1;
			evaluators = evaluators + 1;
			for (int i = 0; i<5; i++) {
				sum = sum + (i+1) * stars[i];
			}
			Reviews = sum/evaluators;
		} else {
			System.out.println("This post has no ratings");
		}
	}
	public int getPostId() {
		return PostId;
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
	public void editPost() {
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
		//comments[PostId] = null;
		}else{
					System.out.println("You cannot delete this post");
		}
	}
	public /*static*/ String getPost(int PostId) {
		return "Title of the post:" + getTitle() + "/nContent of the post:" + getContent() + "/nThe time required for " +
				"this recipe is" + getRecipeTime() + "/nThe cost for this recipe is:" + getRecipeCost() + "euros" + "/The" +
				" difficulty Level of this recipe is:" + getDifficultyLevel() + "/nThe category of this recipe is:"
				+ getRecipeCategory() + "/nThis post has " + Reviews + "stars" + "/nThis post's comments are" + comments ;
	}
}








