package fOrk;


import org.sqlite.core.DB;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

	static Scanner myscan = new Scanner(System.in);
	static Scanner myscan1 = new Scanner(System.in);
	static Scanner myscan2 = new Scanner(System.in);
	static Scanner myscan3 = new Scanner(System.in);

	public static void main(String args[]){
		Connection connection1 = DBcon.openConnection();
		DBcon.deleteTables(connection1);
		Connection connection2 = DBcon.openConnection();
		DBcon.createTable(connection2);

		int i = 0;
		Scanner input1 = new Scanner(System.in);
		Scanner scanner1 = new Scanner(System.in);
		User user = null;
		do {
			System.out.print("Welcome to fOrk! Type 1 to SING UP, 2 to LOG IN, or -1 to END the program: ");
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
				user = new User(tempPassword, tempUsername, tempName, tempBio);
			} else if(preference == 2) {
               int id = logIn();
			   user = new User(id);
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
			System.out.println("Choose the act you want to do! \n 1: Search a recipe \n 2: Check your chatbox"
					+ "\n 3: Make a post \n 4: Profile \n 5: Disconnect");
			int preference = input2.nextInt();
			switch (preference) {
				case 1:
					searchPost(user);
					break;
				case 2:
					getChatbox(user.getUserId());
					openConversation(user);
					break;
				case 3:
					System.out.println("Please insert your recipes title.");
					String title = input2.next();
					String content = create();
					System.out.println("Please insert the cost of your total recipe.");
					double cost = input2.nextDouble();
					System.out.println("Please insert the time required to make your recipe.");
					int time = input2.nextInt();
					System.out.println("Please insert the category of your recipe.");
					String category = input2.next();
					System.out.println("Please describe the difficulty level of your recipe.");
					String diffLevel = input2.next();
					int flag=1;
					while (flag==1) {
						if ((diffLevel.equals("Simple") || diffLevel.equals("Mediun")) || diffLevel.equals("Difficult")) {
							flag = 2;
						} else {
							System.out.println("Please describe the difficulty level with easy or medium or difficult");
							diffLevel = input2.next();
							flag = 1;
						}
					}
					String[] hastag = new String[5];
					System.out.println("Please insert the number of hashtags needed to describe your post");
					int answ = input2.nextInt();
					while (answ < 0 || answ > 5) {
						System.out.println("The allowed number of hashtags for each post is 0-5. Please insert a valid answer");
						answ = input2.nextInt();
					}
					int i = 0;
					while (i < answ) {
						hastag[i] = input2.next();
						i++;
					}

					for (int k=0; k<5; k++) {
						System.out.println(hastag[k]);
					}

					Connection con = DBcon.openConnection();
					Statement stm = null;
					Post post = new Post(user.getUserId(), title, content, cost, time, diffLevel, category, hastag);
					post.getPost();
					break;
				case 4 :
					System.out.println("\nPROFILE ");
					System.out.println("\nUsername : " + user.getUsername());
					System.out.println("Name : " + user.getName());
					System.out.println("Bio : " + user.getBio());
					user.printPosts();
					System.out.println("If you want to go to Edit you Profile press 1\n");
					int answer2 = input2.nextInt();
					if(answer2 == 1) {
						user.editProfile();
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

	private static void searchPost(User user) {
		Scanner input2 = new Scanner(System.in);
		int ttl = 0;
		String rec = null;
		do {
			System.out.print("Give 1 hashtag correlated with the recipe you want or type back to go back: ");
			rec = input2.nextLine();
			if (!rec.equals("back")) {
				int postId = search(rec);
				if (postId != -1) {
					Post post = new Post(postId);
					post.getPost();
					System.out.println("Would you like to review it? Yes/No");
					//post.makeReview
					System.out.println("Would you like to make a comment? Yes/No");
					String answer = input2.next();
					if (answer.equals("Yes")) {
						System.out.println("Please type the comment: ");
						String content = input2.nextLine();
						Comment comment = new Comment(content, user.getUserId(), postId);
					}
					System.out.println("Would you like to make recomment? Yes/No");
					answer = input2.nextLine();
					if (answer.equals("Yes")) {
						System.out.println("please enter the id of the comment that you want to answer");
						int ans = input2.nextInt();
						Comment comment = new Comment(ans);
						int userId = user.getUserId();
						comment.makeReComment(userId, post.getPostId(), ans);
					}
					System.out.println("Do you want to make this user your cookmate");
					answer = input2.nextLine();
					if (answer.equals("Yes")) {
						user.makeCookmates(post.getCreator());
					}
				}
			}
		} while (!rec.equals("back"));
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
		    String select = "(SELECT ID FROM [User] WHERE Username =?, Password=?);";
		    try {
				connection = DBcon.openConnection();
				pst = connection.prepareStatement(select);
				pst.setString(1, un);
				pst.setString(2, pw);
				ResultSet resultSet = pst.executeQuery();
				while (resultSet.next()) {
					userId = resultSet.getInt("ID");
					flag = true;

			    }
		   }
		   catch (SQLException e) {
		   }
		   finally {
			   DBcon.closeStatement(pst);
			   DBcon.closeConnection(connection);
		   }
		    if (flag == false) {
		   		System.out.println("Sorry, the password or/and the username you inserted are not valid. Please try again.");
	        }
	  } while (flag == true);
	  return userId;
	}
	public static int search(String hashtag) {
		String select = ("SELECT PostID, Title, Username FROM User, Hashtags, Post WHERE Post.PostID = Hashtags.PostID AND User.ID = Hashtags.Creator AND Hashtags.Hashtag = ?");
		Connection connection = null;
		PreparedStatement srch = null;
		int usrChoice = 0;
		int counter = 0;
		ArrayList<Integer> postIdList = new ArrayList<Integer>();
		do {
			try {
			     connection = DBcon.openConnection();
			     srch = connection.prepareStatement(select);
			     srch.setString(1, hashtag);
			     ResultSet rs = srch.executeQuery();
			     while (rs.next()) {
					counter++;
					int aa = rs.getInt("PostId");
					postIdList.add(aa);
					String bb = rs.getString("Title");
					String cc = rs.getString("username");
					System.out.println(counter + " " + bb + " " + cc);
			     }
			     if (counter == 0) {
				 	System.out.println("Seems like there was no recipe with this hashtag, try another one:(");
				 } else {
				 	System.out.println("Choose a recipe you like from the following list or -1 to go back!");
				 	Scanner input = new Scanner(System.in);
				 	usrChoice = input.nextInt();
				 }
			} catch (SQLException e) {
			} finally {
				DBcon.closeStatement(srch);
				DBcon.closeConnection(connection);
			}
		  } while ((usrChoice <= counter && usrChoice > 0) || (usrChoice != -1));
		  int UsersChoice = -1;
		  if (usrChoice != -1) {
			   UsersChoice = postIdList.get(usrChoice - 1);
		  }
		  return UsersChoice;
	  }

	public static void getChatbox(int userid) {
		Connection connection = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT Messages.Sender, Messages.Receiver FROM Messages WHERE Messages.Sender = ? OR Messages.Receiver = ? GROUP BY Sender, Receiver");
			stmt.setInt(1, userid);
			stmt.setInt(2, userid);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int sendersid = rs.getInt("Sender");
				int receiversid = rs.getInt("Receiver");
				if (sendersid == userid) {
					stmt2 = connection.prepareStatement("SELECT User.Username FROM User INNER JOIN Messages ON Messages.Receiver = User.ID WHERE Messages.Sender = ? GROUP BY User.Username ");
					stmt2.setInt(1, userid);
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()) {
						String receiversUN = rs2.getString("Username");
						System.out.println(receiversUN);
					}
				} else {
					stmt2 = connection.prepareStatement("SELECT User.Username FROM User INNER JOIN Messages ON Messages.Sender = User.ID WHERE Messages.Receiver = ? GROUP BY User.Username ");
					stmt2.setInt(1, userid);
					ResultSet rs2 = stmt2.executeQuery();
					while(rs2.next()) {
						String sendersUN = rs2.getString("Username");
						System.out.println(sendersUN);
					}
				}
				DBcon.closeStatement(stmt2);
			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public static void openConversation(User user) {
		System.out.println("Do you want to open a conversation?");
		Scanner s = new Scanner(System.in);
		String answer;
		do {
			answer = s.next();
			if (answer.equals("yes")) {
				System.out.println("Type the user you want to chat");
				String answer2;
				Scanner s2 = new Scanner(System.in);
				answer2 = s2.next();
				String receiver = answer2;
				int receiverid = getIDfromUsername(receiver);
				getMessagesby_userid(receiverid, user);
				typeMessage(user.getUserId(), receiverid);
			} else if (!answer.equals("no")) {
				System.out.println("Wrong! Answer should be 'yes' or 'no'");
			}
		} while (!answer.equals("yes") && !answer.equals("no"));

	}

	public static void typeMessage(int userid, int receiversid) {
		String answer3 = null;
		do {
			System.out.println("Do you want to type a message?");
			Scanner s3 = new Scanner(System.in);
			answer3 = s3.next();
			do {
				if (answer3.equals("yes")) {
					System.out.println("Type message");
					Scanner s4 = new Scanner(System.in);
					String MessageContent;
					MessageContent = s4.next();
					Messages message = new Messages(userid, receiversid, MessageContent);
				} else if (!answer3.equals("no")) {
					System.out.println("Wrong! Answer should be 'yes' or 'no'.");
				}
			} while (!answer3.equals("yes") && !answer3.equals("no"));
		} while (!answer3.equals("no"));
	}

	public static void getMessagesby_userid(int receiversID, User user) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT User.Username, Messages.Content, Messages.MDateTime FROM User INNER JOIN Messages ON Messages.Sender = User.ID WHERE (Sender = ? AND Receiver = ?) OR (Sender = ? AND Receiver = ?) ORDER BY MessageID");
			stmt.setInt(1, receiversID);
			stmt.setInt(2, user.getUserId());
			stmt.setInt(3, user.getUserId());
			stmt.setInt(4, receiversID);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				String MessageContent = rs.getString("Content");
				String sendersUN = rs.getString("Username");
				String dt = rs.getString("MDateTime");
				System.out.println(sendersUN + ":" + MessageContent + "at" + dt);
			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public static int getIDfromUsername(String username) {
		Connection connection = null;
		PreparedStatement stmt = null;
		int rtrn = 0;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT ID FROM USERS WHERE Username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				rtrn = rs.getInt("ID");

			}
		} catch (SQLException e) {
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
		return rtrn;
	}

	public static 	String create() {
		Scanner input1 = new Scanner(System.in);
		Scanner input2 = new Scanner(System.in);

		System.out.println("Please enter the number of ingredients of your recipe.");
		int i1 = input1.nextInt();
		System.out.println("Please enter the ingredients of your recipe.");
		String content = "Ingredients: \n";
		for (int i2 = 0; i2 < i1+1; i2++) {
			content += input1.nextLine() + "\n";
		}
		System.out.println("Please enter the number of steps needed to create your recipe.");
		//String[] steps = new String [input2.nextInt()];
		int i3 = input2.nextInt();
		System.out.println("Please enter the recipes steps in order.");
		content += "Recipe steps: \n";
		for (int i4 = 0; i4 < i3+1; i4++) {
			content += input2.nextLine() + "\n";
		}
		System.out.println("\nYou have entered the following recipe: ");

		System.out.println(content);
		return content;
	}

}//end of class