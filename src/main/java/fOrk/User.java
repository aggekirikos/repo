package fOrk;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

	static Scanner scanner1 = new Scanner(System.in);
	static Scanner scanner2 = new Scanner(System.in);
	static Scanner scanner3 = new Scanner(System.in);
	static Scanner myscan = new Scanner(System.in);

	int userId;
	String password;
	String username;
	String name;
	String bio;
	ArrayList<String> cookmates = new ArrayList<String>();
	ArrayList<Post> posts = new ArrayList<Post>();

	public User(String pass, String uname, String nm, String BIO) {
		maxId();
		setPassword(pass);
		setUsername(uname);
		name = nm;
		bio = BIO;
		System.out.println(userId + password + username + name + bio);
		Connection connection= null;
		PreparedStatement stmt= null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("INSERT INTO User(ID, Password, Username, Name, Bio) VALUES(?,?,?,?,?)");
			stmt.setInt(1, userId);
			stmt.setString(2, password);
			stmt.setString(3, username);
			stmt.setString(4, name);
			stmt.setString(5, bio);
		    stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Something went wrong while creating your Profile"+ e.getMessage());
		}
		finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public User(int id) {
		this.userId = id;
		String select = "SELECT * FROM User WHERE ID=?";
		Connection connection = null;
		PreparedStatement prst = null;
		try {
			connection = DBcon.openConnection();
			prst = connection.prepareStatement(select);
			prst.setInt(1, id);
			ResultSet resultSet = prst.executeQuery();
			while (resultSet.next()){
				this.password = resultSet.getString("Password");
				this.username = resultSet.getString("Username");
				this.name = resultSet.getString("Name");
				this.bio = resultSet.getString("Bio");
			}
		}
		catch (SQLException e) {
		}
		finally {
			DBcon.closeStatement(prst);
			DBcon.closeConnection(connection);
		}
		String slct = "SELECT PostID FROM Post WHERE Creator=?";
		Connection con = null;
		PreparedStatement stat = null;
		try {
			con = DBcon.openConnection();
			stat = con.prepareStatement(slct);
			stat.setInt(1, id);
			ResultSet resultSet = stat.executeQuery();
			while (resultSet.next()){
				int temp = resultSet.getInt("PostID");
				Post post = new Post(temp);
				posts.add(post);
			}
		}
		catch (SQLException e) {
		}
		finally {
			DBcon.closeStatement(stat);
			DBcon.closeConnection(con);
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setPassword(String tempPassword) {
		boolean flag = false;
		while (!flag) {
			if (tempPassword.length() > 8) {
				flag = true;
			}
			else {
				System.out.println("Password must have over 8 characters! Please choose a new one.");
				tempPassword = myscan.next();
			}
		}
		password = tempPassword;
	}

	public void setUsername(String tempUsername) {
		Connection connection= null;
		PreparedStatement stmt= null;
		boolean flag;
		do  {
			flag = false;
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("SELECT ID FROM User WHERE Username = ?");
				stmt.setString(1, tempUsername);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					flag = true;
					System.out.println("This username already exists! Please choose a new one.");
				}
			} catch (SQLException e) {
				System.out.println("Could not set your username.");
			} finally {
				DBcon.closeStatement(stmt);
				DBcon.closeConnection(connection);
			}
			if (flag) {
				Scanner scanner = new Scanner(System.in);
				tempUsername = scanner.next();
			} else {
				username = tempUsername;
			}
		} while (flag);
	}

	public String getPassword() {
		return password;
	}


	public String getUsername() {
			return username;
	}


	public void setName(String tempName) {
		name = tempName;
	}

	public String getName() {
		return name;
	}

	public void setBio(String tempBio) {
		bio = tempBio;
	}

	public String getBio(){
		return bio;
	}

	/*public void addPost(){
		posts.add();
	}*/

	public void printPosts() {
		int j = 0;
		while (j < posts.size()) {
			System.out.println("\nPost " + (j+1));
			j = j + 1;
		}
		if(posts.size() == 0) {
			System.out.println("No posts yet");
		}
	}

	public void editProfile(){
		System.out.println("If you want to Edit your Username press 1.");
		System.out.println("If you want to Edit your Name press 2.");
		System.out.println("If you want to Edit your Bio press 3.");
		System.out.println("If you want to Edit your Password press 4.");
		int temp2 = myscan.nextInt();
		Connection connection= null;
		PreparedStatement stmt= null;
		switch (temp2) {
			case 1:
				System.out.println("Please insert your new Username.");
				String answer1 = scanner1.nextLine();
				setUsername(answer1);
				try {
					connection = DBcon.openConnection();
					stmt = connection.prepareStatement("UPDATE User SET Username=? WHERE ID=?");
					stmt.setString(1, username);
					stmt.setInt(2, userId);
					int i = stmt.executeUpdate();
					if (i != 0) {
						System.out.println("\nUpdated!\n");
					} else {
						System.out.println("\nUpdate Failed!\n");
					}
				} catch (SQLException e) {
					System.out.println("\nUpdate Failed!\n");
				} finally {
					DBcon.closeStatement(stmt);
					DBcon.closeConnection(connection);
				}
				break;
			case 2:
				System.out.println("Please insert your new Name.");
				name = scanner2.nextLine();

				try {
					connection = DBcon.openConnection();
					stmt = connection.prepareStatement("UPDATE User SET Name=? WHERE ID=?");
					stmt.setString(1, name);
					stmt.setString(2, String.valueOf(userId));
					int i = stmt.executeUpdate();
					if (i != 0) {
						System.out.println("\nUpdated!\n");
					} else {
						System.out.println("\nUpdate Failed!\n");
					}
				} catch (SQLException e) {
					System.out.println("\nUpdate Failed!\n");
				} finally {
					DBcon.closeStatement(stmt);
					DBcon.closeConnection(connection);
				}
				break;
			case 3:
				System.out.println("Please insert your new bio");
				bio = scanner3.nextLine();
				try {
					connection = DBcon.openConnection();
					stmt = connection.prepareStatement("UPDATE User SET Bio = ? WHERE ID=?");
					stmt.setString(1, bio);
					stmt.setInt(2, userId);
					int i = stmt.executeUpdate();
					if (i != 0) {
						System.out.println("\nUpdated!\n");
					} else {
						System.out.println("\nUpdate Failed!\n");
					}
				} catch (SQLException e) {
					System.out.println("\nUpdate Failed!\n");
				} finally {
					DBcon.closeStatement(stmt);
					DBcon.closeConnection(connection);
				}
				break;
			case 4:
				System.out.println("Please insert your new password.");
				String answer2 = scanner3.nextLine();
				setPassword(answer2);
				try {
					connection = DBcon.openConnection();
					stmt = connection.prepareStatement("UPDATE User SET Password = ? WHERE ID = ?");
					stmt.setString(1, password);
					stmt.setInt(2, userId);
					int j = stmt.executeUpdate();
					if (j != 0) {
						System.out.println("\nUpdated!\n");
					} else {
						System.out.println("\nUpdate Failed!\n");
					}
				} catch (SQLException e) {
					System.out.println("\nUpdate Failed!\n");
				} finally {
					DBcon.closeStatement(stmt);
					DBcon.closeConnection(connection);
				}
				break;
		}
	}

	public void makeCookmates(int creatorID) {
		Connection connection= null;
		PreparedStatement stmt= null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("INSERT INTO Cookmates(UserID, CookmateID) VALUES(?,?)");
			stmt.setInt(1, getUserId());
			stmt.setInt(2, creatorID);
		    stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while trying to make this user your cookmate.");
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public void maxId() {
		String slct = "SELECT MAX(ID) FROM User";
		Connection con = null;
		Statement stat = null;
		try {
			con = DBcon.openConnection();
			stat = con.createStatement();
			ResultSet rs = stat.executeQuery(slct);
			userId = rs.getInt(1);
		}
		catch (SQLException e) {
		}
		finally {
			DBcon.closeStatement(stat);
			DBcon.closeConnection(con);
		}
		userId++;
	}
}