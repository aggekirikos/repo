package fOrk;


import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class �ain2 {

	static Scanner myscan = new Scanner(System.in);
	static Scanner myscan1 = new Scanner(System.in);
	static Scanner myscan2 = new Scanner(System.in);
	static Scanner myscan3 = new Scanner(System.in);

	public static void main(String args[]){
		int i = 0;
		Scanner input1 = new Scanner(System.in);
		Scanner scanner1 = new Scanner(System.in);
		User user = null;
		do {
			System.out.print("Welcome to fOrk! Type 1 to SING IN, 2 to LOG IN, or -1 to END the program: ");
			int preference = input1.nextInt();
			if(preference == 1) {
				System.out.println("Please create Password");
				String tempPassword = myscan.nextLine();
				System.out.println("Please create Username");
				String tempUsername = myscan1.nextLine();
				System.out.println("Please create Name");
				String tempName = myscan2.nextLine();
				System.out.println("Please create small Bio");
				String tempBio = myscan3.nextLine();
				User user = new User();
				//���� User �� ������� safe � user ��� �� ���� ��� �� ��������
			} else if(preference == 2) {
               int id = lognIn();
			   User user = User(id);
			} else if(preference == -1) {
				i = -1;
				System.exit(-1);
			}
			fOrkNavigation(user);
		}while(i != -1);
	}

	private static void fOrkNavigation(User user) {
		int j = 0;
		Scanner input2 = new Scanner(System.in);
		do{
			System.out.println("Choose the act you want to do! /n 1: Search a recipe /n 2: Check your chatbox"
					+ "/n 3: Make a post /n 4: Profile /n 5: Disconnect");
			int preference = input2.nextInt();
			switch (preference) {
				case 1 :
					searchPost();
					break;
				case 2 :
					//�������� �������� ������� �� ���� ������� ���� ������� �������� (getMesseges)
					//OpenChatBox �������� ��������� �� ��� ���������� ������
					//���������� ������������ Message
					break;
				case 3 :
					System.out.println("Please enter the title, the content, the cost,"
												+" the recipe time, /n the recipe category, difficulty level" +
												" (simple, medium, difficult) and 1-5 hastags. Please follow the correct order : ");
					String title = input2.nextLine();
					String content = input2.nextLine();
					double cost = input2.nextDouble();
					int time = input2.nextInt();
					String category = input2.nextLine();
					String diffLevel = input2.nextLine();
					String[] hastag = new String[5];
					int c = 0;
					do {
						hastag[c] = input2.nextLine();
						c++;
						System.out.println("If you don;t want to enter more hastags, please type 'null'");
					} while (hastag != null && c<5);
					Connection con = DBcon.openConnection();
					Statement stm = null;
					int prevPostId = 0;
					try {
						stm = con.createStatement();
						ResultSet result = stm.executeQuery("SELECT max(PostID) FROM Post");
						prevPostId = result.getInt(1);
					} catch (SQLException e) {
					throw new RuntimeException(e);
					} finally {
							DBcon.closeStatement(stm);
							DBcon.closeConnection(con);
					}
					Post post = new Post(prevPostId, title, content, cost, time, diffLevel, category, hastag);
					System.out.println(post.getPost());
					break;
				case 4 :
					System.out.println("\nPROFILE ");
					System.out.println("\nUsername : " + user.getUsername());
					System.out.println("Name : " + user.getName());
					System.out.println("Bio : " + user.getBio());
					user.printPosts();
					System.out.println("If you want to go to Edit you Profile press 1\n");
					int answer2 = scanner1.nextInt();
					if(answer2 == 1) {
						user.editProfil();
					}
					//System.out.print("Do you want to edit any of your posts? Yes/No");
					break;
				case 5 :
					j = -1;
					//aposyndesi me th basi
					break;
			}
		} while (j != -1);
	}

	private static void searchPost(String a, /*User user*/) {
		Scanner input2 = new Scanner(System.in);
			int ttl = 0;
			do {
				System.out.print("Give 1 words correlated with the recipe you want");
				String rec = input2.nextLine();
				//searcing and display titles of posts. θα εμφανίζονται οι τίτλοι
				//αριθμοιμένοι 1 εως ν μεσω της hasNext
				//ΒΑΓΓΕΛΗΣ
				System.out.print("Choose the recipe you want by writing the number of the title, or get /n back to " +
						"the main menu by typing -1");
				ttl = input2.nextInt();
				if (ttl == -1) {
					break;
				}
				//Select post end displaiy it
				//Μέθοδος Βαγγέλη που βρίσκει το ποστ id
				int i = 0; //id που θα επιστρεφεται
				Post post = new Post(i);
				post.getFullPost();
				System.out.println("Would you like to review it? Yes/No");
				//καλείται η μέθοδος του review με ορισμα το αντικείμενο Post που εμφανιζεται παραπανω
				//post.makeReview
				System.out.println("Would you like to make a comment? Yes/No");
				String answer = input2.next();
				if (answer == "Yes") {
					String a = "a";
					int postId = post.getPostId();
					Connection connection = null;
					PreparedStatement stm = null;
					int commentId = 0;
					try {
						connection = DBcon.openConnection();
						stm = (PreparedStatement) connection.createStatement("SELECT MAX(CommentID) FROM Comment");
						ResultSet resultSet = stm.executeQuery();
						commentId = resultSet.getInt(1);
					} catch (Exception e) {}
						post.createComment(a, postId, commentId);
					}
					System.out.println("Would you like to make recomment? Yes/No");
					answer = input2.next();
					if (answer == "Yes") {
						System.out.println("please enter the id of the comment that you want to answer");
						int ans = input2.nextInt();
						Comment comment = new Comment(ans);
						String username = null; //δεν εχει χρησιμοτητα το κανω για να δουλεψει
						comment.makeReComment(username, post.getPostId(), ans);
					}
				}
			//ttl = -1;
			} while(ttl != -1);
	}//end of searchPost
	public static int logIn() {
		boolean flag = false;
		int userId = 0;
		 Connection connection = null;
		 PreparedStatement pst = null;
		do {
			System.out.println("Please enter your username");
			Scanner inU = new Scanner(System.in);
			System.out.println("Please enter your password");
			Scanner inP = new Scanner(System.in);
			String un = inU.nextLine();
			String pw = inP.nextLine();
		    String select = "SELECT username, password FROM User WHERE username =?, password=?";
		    try {
				connection = DBcon.openConnection();
				pst = connection.prepareStatement(select);
				pst.setString(1, un);
				pst.setString(2, pw);
				ResultSet resultSet = pst.executeQuery();
				while (resultSet.next()) {
					userId = resultSet.getInt("UserId");
					flag = true;

			    }
		   }
		   catch (SQLException e) {
		   }
		   finally {
			   DBcon.closeStatement(pst);
			   DBCON.closeConnection(connection);
		   }
		    if (flag == false) {
		   		System.out.println("Sorry, the password or/and the username you inserted are not valid. Please try again.");
	        }
	  } while (flag == true);
	  return userId;
	}
}//end of class