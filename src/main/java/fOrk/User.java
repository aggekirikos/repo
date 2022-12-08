package fOrk;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class User {

	Scanner myscan = new Scanner(System.in);
	Scanner myscan1 = new Scanner(System.in);
	Scanner myscan2 = new Scanner(System.in);
	Scanner myscan3 = new Scanner(System.in);

	int userId = 0;
	String password;
	String username;
	String name;
	String bio;
	ArrayList<String> cookmates = new ArrayList<String>();
	ArrayList<Post> posts = new ArrayList<Post>();
	Post pst = new Post();

	public User() {
		userId = userId + 1;
		System.out.println("Please create Password");
		password = setPassword();
		System.out.println("Please create Username");
		username = setUsername();
		System.out.println("Please create Name");
		name = setName();
		System.out.println("Please create small Bio");
		bio = setBio();
		Connection connection= null;
		PreparedStatement stmt= null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("INSERT INTO User(UserID, Password, Username, Name, Bio) VALUES(?,?,?,?,?)");
			stmt.setInt(1, userId);
			stmt.setString(2, password);
			stmt.setString(3, username);
			stmt.setString(4, name);
			stmt.setString(5, bio);
		    stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("Something went wrong while creating your Profile");
		}
		finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}

	public User(int id) {
		this.userId = id;
		String select = "SELECT * FROM User WHERE userId=?";
		Connection connection = null;
		PreparedStatement prst = null;
		try {
			connection = DBcon.openConnection();
			prst = connection.preparedStatement();
			prst.setString(1, String.valueOf(id));
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
		String slct = "SELECT PostId FROM Post WHERE Creator=?";
		Connection con = null;
		PreparedStatement stat = null;
		try {
			connection = DBcon.openConnection();
			stat = con.preparedStatement();
			stat.setString(1, String.valueOf(id));
			ResultSet resultSet = stat.executeQuery();
			while (resultSet.next()){
				String temp = resultSet.getString("PostID");
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

	public String setPassword() {
		String tempPassword = null;
		boolean flag = false;
		while (flag == false) {
			tempPassword = myscan.nextLine();
			if (tempPassword.length() > 8) {
				flag = true;
			}
			else {
				System.out.println("Password must have over 8 charecters!");
			}
		}
		return tempPassword;
	}

	public String getPassword() {
		return password;
	}


	public String setUsername() {
		String tempUsername = "null";
		System.out.println("Please give a Username");
		tempUsername = myscan1.nextLine();
		return tempUsername;
	}

	public String getUsername() {
			return username;
	}


	public String setName() {
		String tempName = "null";
		System.out.println("Please give a Name");
		tempName = myscan2.nextLine();
		return tempName;
	}

	public String getName() {
		return name;
	}

	public String setBio() {
		String tempBio = "null";
		System.out.println("Please give a Bio");
		tempBio = myscan3.nextLine();
		return tempBio;
	}

	public String getBio(){
		return bio;
	}

	/*public void addPost(){
		posts.add();
	}*/

	public void printPosts() {
		int j = 0;
		while ( post[j] != null) {  //make it arraylist
			System.out.println("\nPost " + (j+1));
			j = j + 1;
		}
	}

	public void editProfile(){
		System.out.println("If you want to Edit your Username press 1\n");
		System.out.println("If you want to Edit your Name press 2\n");
		System.out.println("If you want to Edit your Bio press 3\n");
		int temp2 = myscan2.nextLine();
		if (temp2 == 1) {
			username = setUsername();
			Connection connection= null;
			PreparedStatement stmt= null;
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("UPDATE User SET Username=?");
				stmt.setString(1, username);
			    int i = stmt.executeUpdate();
			    if (i != 0) {
					System.out.println("\nUpdated!\n");
				}
				else {
					System.out.println("\nUpdate Failed!\n");
				}
			}
			catch (SQLException e) {
				System.out.println("\nUpdate Failed!\n");
			}
			finally {
				DBcon.closeStatement(stmt);
				DBcon.closeConnection(connection);
			}
		}
		else if (temp2 == 2) {
			name = setName();
			Connection connection= null;
			PreparedStatement stmt= null;
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("UPDATE User SET Name=?");
				stmt.setString(1, name);
			    int i = stmt.executeUpdate();
				if (i != 0) {
					System.out.println("\nUpdated!\n");
				}
				else {
					System.out.println("\nUpdate Failed!\n");
				}
			}
			catch (SQLException e) {
				System.out.println("\nUpdate Failed!\n");
			}
			finally {
				DBcon.closeStatement(stmt);
				DBcon.closeConnection(connection);
			}
		}
		else if (temp2 == 3) {
			bio = setBio();
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("UPDATE User SET Bio=?");
				stmt.setString(1, bio);
				int i = stmt.executeUpdate();
				if (i != 0) {
					System.out.println("\nUpdated!\n");
				}
				else {
					System.out.println("\nUpdate Failed!\n");
				}
			}
			catch (SQLException e) {
				System.out.println("\nUpdate Failed!\n");
			}
			finally {
				DBcon.closeStatement(stmt);
				DBcon.closeConnection(connection);
			}
		}
	}

	public void makeCookmates(int creatorID) {
		//TODO: na ton prosthesoume kai sto arraylist stigmiotypoy me ta cookmates
		Connection connection= null;
		PreparedStatement stmt= null;
		try {
			connection = DBcon.openConnection();
			stmt = connection.prepareStatement("INSERT INTO Cookmates(UserID, CookmateID) VALUES(?,?)");
			stmt.setInt(1, getUserId());
			stmt.setInt(2, creatorID);
		    stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Something went wrong while making this user your cookmate.");
		} finally {
			DBcon.closeStatement(stmt);
			DBcon.closeConnection(connection);
		}
	}
}