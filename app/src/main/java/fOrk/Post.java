import java.util.Scanner;
public class Post {
	private int PostId = 0;
	private int PostStatus = 0;//When PostStatus is 0 the post is deleted. When PostStatus is 1 the post exists
	private String Content,Title,RecipeTime,Reviews,RecipeCategory,DifficultyLevel,RecipeCost;
	Comment [] comments = new Comment[100];//array of the comments with capacity of 100 comments
	User user = new User();
	//private String Creator = user.getUsername();
	String [] likes = new String[100];
	public Post(){
		PostId = PostId + 1;
		PostStatus = 1;
		Scanner input = new Scanner(System.in);
		System.out.println("Create a Title for your post");
		Title = input.next();
		System.out.println("Create a content for your post");
		Content = input.next();
		System.out.println("Report the cost of the recipe in your post");
		RecipeCost= input.next();
		System.out.println("Report the time required for the recipe in your post");
		RecipeTime = input.next();
		System.out.println("Report the difficulty level of the recipe in your post");
		DifficultyLevel = input.next();
		System.out.println("Report the Category in which the recipe of your post belongs");
		RecipeCategory = input.next();
		System.out.println("Do you like the post?Press 1 to give it a star or 0 to ignore it");
		int rate = input.nextInt();
		if (rate == 1){
			likes [PostId] = likes [PostId] + 1;
		}else{
			likes [PostId] = 0;
		}
	}
	public String getTitle() {
		return Title;
	}
	public String setTitle(String Title) {
		this.Title = Title;
	}
	public String getContent() {
		return Content;
	}
	public String setContent(String Content) {
		this.Content = Content;
	}
	public 	String getRecipeCost() {
		return RecipeCost;
	}
	public String setRecipeCost(String RecipeCost) {
		this.RecipeCost = RecipeCost;
	}
	public String getRecipeTime() {
		return RecipeTime;
	}
	public String setRecipeTime(String RecipeTime) {
		this.RecipeTime = RecipeTime;
	}
	public String getDifficultyLevel(){
		return DifficultyLevel;
	}
	public String setDifficultyLevel(String DifficultyLevel) {
		this.DifficultyLevel = DifficultyLevel;
	}
	public String getRecipeCategory() {
		return RecipeCategory;
	}
	public String setRecipeCategory(String RecipeCategory) {
		this.RecipeCategory = RecipeCategory;
	}
	public static void editPost() {
		if (creator == user.getUsername) {
			Scanner input =  new Scanner(System.in);
			System.out.println("Change the Title of your post");
			Title = input.next();
			System.out.println("Change the content of your post");
			Content = input.next();
			System.out.println("Change the cost of the recipe in your post");
			RecipeCost = input.next();
			System.out.println("Change the time of the recipe in your post");
			RecipeTime = input.next();
			System.out.println("Change the Category of the recipe in your post");
			RecipeCategory = input.next();
			System.out.println("Change the Difficulty Level of the recipe in your post");
			DifficultyLevel = input.next();
		}else{
			System.out.println("You cannot edit this post");
		}
	}
	public static void deletePost() {
		if (creator = user.getUsername) {
		Poststatus = null;
		Title = null;
		Content = null;
		RecipeCost = null;
		RecipeTime = null;
		RecipeCategory = null;
		DifficultyLevel = null;
		likes[PostId] = 0;
		comments[PostId] = null;
		}else{
					System.out.println("You cannot delete this post");
		}
	}
	public static void getPost() {
		/*String [][] posts = new String[100][6];
		int i;
		for (i = 0; i<PostId; i++){
			posts[i][0] = Title;
			posts[i][1] = Content;
			posts[i][2] = RecipeCost;
			posts[i][3] = RecipeTime;
			posts[i][4] = RecipeCategory;
			posts[i][5] = DifficultyLevel;
		}
		for (i = 0; i<PostId; i++){
			int j;
			for(j = 0; j<6; j++){
				System.out.println(posts[i][j]);
			}
			System.out.println(comments[i]);
			System.out.println("This post has"+likes[i]+"likes");
		}
	}*/
}








