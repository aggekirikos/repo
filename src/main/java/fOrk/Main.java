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
		//Connection connection1 = DBcon.openConnection();
		//DBcon.deleteTables(connection1);
		Connection connection2 = DBcon.openConnection();
		DBcon.createTable(connection2);

		int i = 0;
		Scanner input1 = new Scanner(System.in);
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
				User user = new User(tempPassword, tempUsername, tempName, tempBio);
				fOrkNavigation(user);
			} else if(preference == 2) {
               int id = logIn();
			   User user = new User(id);
			   fOrkNavigation(user);
			} else if(preference == -1) {
				i = -1;
			}
		}while(i != -1);
	}

	private static void fOrkNavigation(User user) {
		int j = 0;
		Scanner input2 = new Scanner(System.in);
		do{
			System.out.println("Choose the act you want to do! \n 1: Search a recipe \n 2: Check your chatbox"
					+ "\n 3: Make a post \n 4: See or Edit Your Profile \n 5: Log Out \n 6: Leave Fork");
			int preference = input2.nextInt();
			while (preference < 1 || preference > 6) {
				System.out.println("Please insert a number from 1 to 6!");
				preference = input2.nextInt();
			}
			switch (preference) {
				case 1:
					searchPost(user);
					break;
				case 2:
					getChatbox(user.getUserId());
					openConversation(user.getUserId());
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

					Post post = new Post(user.getUserId(), title, content, cost, time, diffLevel, category, hastag);
					post.getPost();
					break;
				case 4 :
					System.out.println("\nPROFILE ");
					System.out.println("\nUsername : " + user.getUsername());
					System.out.println("Name : " + user.getName());
					System.out.println("Bio : " + user.getBio());
					user.printPosts();
					System.out.println("If you want to edit you Profile press 1. Else press 2");
					int answer2 = input2.nextInt();
					while (answer2 != 1 && answer2 != 2) {
						System.out.println("Please insert 1 or 2");
						answer2 = input2.nextInt();
					}
					if(answer2 == 1) {
						user.editProfile();
						//System.out.print("Do you want to edit any of your posts? Yes/No");
					}
					break;
				case 5 :
					j = -1;
					break;
				case 6 :
					System.exit(1);
			}
		} while (j != -1);
	}

	private static void searchPost(User user) {
		Scanner input2 = new Scanner(System.in);
		String rec;
		do {
			System.out.println("Give 1 hashtag correlated with the recipe you want or type back to go back.");
			rec = input2.nextLine();
			System.out.println("1");
			if (!rec.equals("back")) {
				int postId = search(rec);
				System.out.println("2");
				if (postId != -1) {
					Post post = new Post(postId);
					post.getPost();
					System.out.println("Would you like to review it? Yes/No");
					String answer = input2.next();
					while (!answer.equals("Yes") && !answer.equals("No")) {
						System.out.println("Please insert Yes or No");
						answer = input2.next();
					}
					if (answer.equals("Yes")) {
						post.makeReview();
					}
					System.out.println("Would you like to make a comment? Yes/No");
                    answer = input2.next();
					while (!answer.equals("Yes") && !answer.equals("No")) {
						System.out.println("Please enter Yes or No");
						answer = input2.next();
					}
					if (answer.equals("Yes")) {
						System.out.print("Please type the comment: ");
						input2.nextLine();
						String com = input2.nextLine();
						Comment comment = new Comment(com, user.getUserId(), postId);
					}
					if (post.commentListSize()) {
						System.out.println("Would you like to respond to a comment? Yes/No");
						input2.nextLine();
						answer = input2.nextLine();
						if (answer.equals("Yes")) {
							System.out.println("please enter the number of the comment that you want to answer");
							int ans = input2.nextInt();
							Comment comment = post.comments.get(ans-1);//new Comment(ans);
							int userId = user.getUserId();
							comment.makeReComment(userId, post.getPostId(), comment.commentId);
						}
					}
					System.out.println("Do you want to make this user your cookmate");
					//input2.nextLine();
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
		int userId = -1;
		 Connection connection = null;
		 PreparedStatement pst = null;
		do {
			System.out.println("Please enter your username");
			Scanner inU = new Scanner(System.in);
			String un = inU.nextLine();
			System.out.println("Please enter your password");
			Scanner inP = new Scanner(System.in);
			String pw = inP.nextLine();
		    String select = "SELECT ID FROM User WHERE Username=? AND Password=?";
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
				System.out.println(e.getMessage() );
		   }
		   finally {
			   DBcon.closeStatement(pst);
			   DBcon.closeConnection(connection);
		   }
		    if (!flag) {
		   		System.out.println("Sorry, the password or/and the username you inserted are not valid. Please try again.");
	        }
	  } while (!flag);
	  return userId;
	}
	public static int search(String hashtag) {
		String select = ("SELECT Post.PostID, Title, Username FROM User, Hashtags, Post WHERE Post.PostID = Hashtags.PostID AND User.ID = Post.Creator AND Hashtags.Hashtag = ?");
		Connection connection = null;
		PreparedStatement srch = null;
		int usrChoice = 0;
		int counter = 0;
		ArrayList<Integer> postIdList = new ArrayList<Integer>();
		System.out.println("6");
			try {
			     connection = DBcon.openConnection();
				 System.out.println("5");
			     srch = connection.prepareStatement(select);
				 System.out.println("3");
			     srch.setString(1, hashtag);
			     ResultSet rs = srch.executeQuery();
				 System.out.println("4");
			     while (rs.next()) {
					counter++;
					int aa = rs.getInt("PostID");
					postIdList.add(aa);
					String bb = rs.getString("Title");
					String cc = rs.getString("Username");
					System.out.println(counter + " " + bb + " " + cc);
			     }
			     if (counter == 0) {
				 	System.out.println("Seems like there was no recipe with this hashtag, try another one:(");
				 } else {
				 	System.out.println("Please choose a recipe you like from the following list or -1 to go back!");
				 	Scanner input = new Scanner(System.in);
				 	usrChoice = input.nextInt();
					while (usrChoice != -1 && (usrChoice < 1 || usrChoice > counter)) {
						System.out.println("Wrong input. Please choose a recipe you like from the following list or -1 to go back!");
						input = new Scanner(System.in);
						usrChoice = input.nextInt();
					 }
				 }
			} catch (SQLException e) {
				System.out.println("Something went wrong while searching your hashtag." + e.getMessage());
			} finally {
				DBcon.closeStatement(srch);
				DBcon.closeConnection(connection);
			}
		  int UsersChoice = -1;
		  if (usrChoice != -1 && usrChoice!=0) {
			   UsersChoice = postIdList.get(usrChoice - 1);
		  }
		  return UsersChoice;
	  }

	public static void getChatbox(int userid) {
		System.out.println("Your chatbox is :");
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
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeStatement(stmt2);
			DBcon.closeConnection(connection);
		}
	}

	public static void openConversation(int userid) {
		System.out.println("Would you like to open a conversation?");
		Scanner s = new Scanner(System.in);
		String answer;
		do {
			int receiverid;
			answer = s.next();
			if (answer.equals("yes")) {
				do {
					System.out.println("Type the user you want to chat with: ");
					String answer2;
					Scanner s2 = new Scanner(System.in);
					answer2 = s2.next();
					String receiver = answer2;
					receiverid = -1;
					receiverid = getIDfromUsername(receiver);
				} while (receiverid == -1);
				getMessagesby_userid(receiverid, userid);
				typeMessage(userid, receiverid);
			} else if (!answer.equals("no")) {
				System.out.println("Wrong! Answer should be 'yes' or 'no'");
			}
		} while (!answer.equals("yes") && !answer.equals("no"));

	}

	public static void typeMessage(int userid, int receiversid) {
		String answer3 = null;
		do {
			System.out.println("Would you like to type a message?");
			Scanner s3 = new Scanner(System.in);
			do {
				answer3 = s3.next();
				if (answer3.equals("yes")) {
					System.out.println("Please type your message : ");
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

	public static void getMessagesby_userid(int receiversID, int userid) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT Messages.Content, Messages.MDateTime, Messages.Sender FROM Messages WHERE (Sender = ? AND Receiver = ?) OR (Sender = ? AND Receiver = ?) ORDER BY MessageID");
			stmt.setInt(1, receiversID);
			stmt.setInt(2, userid);
			stmt.setInt(3, userid);
			stmt.setInt(4, receiversID);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int sendersid = rs.getInt("Sender");
				//int receiverid = rs.getInt("Receiver");
				String sendersun = getUsernamefromID(sendersid);
				String MessageContent = rs.getString("Content");
				String dt = rs.getString("MDateTime");
				System.out.println(sendersun + " : " + MessageContent + " sent at " + dt);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public static int getIDfromUsername(String username) {
		Connection connection = null;
		PreparedStatement stmt = null;
		int rtrn = -1;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("SELECT ID FROM User WHERE Username = ?");
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				rtrn = rs.getInt("ID");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
		if (rtrn == -1) {
			System.out.println("The user you entered does not exist. Please type a new one");
		}
		return rtrn;
	}

	public static String getUsernamefromID(int id) {
			Connection connection = null;
			PreparedStatement stmt = null;
			String rtrn = null;
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("SELECT Username FROM User WHERE ID = ?");
				stmt.setInt(1, id);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					rtrn = rs.getString("Username");
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
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