package fOrk;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

	static Scanner scanner1 = new Scanner(System.in);
	static Scanner scanner2 = new Scanner(System.in);
	static Scanner scanner3 = new Scanner(System.in);

	int userId = 0;
	String password;
	String username;
	String name;
	String bio;
	ArrayList<String> cookmates = new ArrayList<String>();
	ArrayList<Post> posts = new ArrayList<Post>();
	Post pst = new Post();

	public User(String pass, String uname, String nm, String BIO) {
		userId = maxId();
		//System.out.println("Please create Password");
		password = pass;
		//System.out.println("Please create Username");
		username = uname;
		//System.out.println("Please create Name");
		name = nm;
		//System.out.println("Please create small Bio");
		bio = BIO;
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
			prst = connection.prepareStatement(select);
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
			stat = con.preparedStatement(slct);
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

	public void setPassword(int tempPassword) {
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
		password = tempPassword;
	}

	public String getPassword() {
		return password;
	}


	public void setUsername(String tempUsername) {
		username = tempUsername;
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

	public void setBio(tempBio) {
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
		while ( posts.next() != null) {
			System.out.println("\nPost " + (j+1));
			j = j + 1;
		}
	}

	public void editProfile(){
		System.out.println("If you want to Edit your Username press 1\n");
		System.out.println("If you want to Edit your Name press 2\n");
		System.out.println("If you want to Edit your Bio press 3\n");
		int temp2 = myscan2.nextInt();
		Connection connection= null;
		PreparedStatement stmt= null;
		if (temp2 == 1) {
			username =  = scanner1.nextLine();
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("UPDATE User SET Username=? WHERE ID=?");
				stmt.setString(1, username);
				stmt.setInt(2, userId);
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
			name =  = scanner2.nextLine();
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("UPDATE User SET Name=? WHERE ID=?");
				stmt.setString(1, name);
				stmt.setString(2, userId);
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
			bio =  = scanner3.nextLine();
			try {
				connection = DBcon.openConnection();
				stmt = connection.prepareStatement("UPDATE User SET Bio=? WHERE ID=?");
				stmt.setString(1, bio);
				stmt.setString(2, userId);
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

	public static int maxId() {
		String slct = "SELECT MAX(userId) FROM User";
		Connection con = null;
		PreparedStatement stat = null;
		try {
			connection = DBcon.openConnection();
			slct = con.preparedStatement.executeUpdate();
			userId = slct.getInt(1);
		}
		catch (SQLException e) {
		}
		finally {
			DBcon.closeStatement(slct);
			DBcon.closeConnection(con);
		}
		return userId;
	}
}