package fOrk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains methods that create a new user
 * (their username, password, bio) and saves their characteristics
 * to the database. There are also constructors that retrieve the
 * characteristics from the database and methods that display the
 * users posts.
 */

public class User {

    /**
    * Unique ID number for every user.
    */
    int userId;
    /**
    * The password that each user has.
    */
    String password;
    /**
    * The username that each user has.
    */
    String username;
    /**
    * The full name of each user.
    */
    String name;
    /**
    * Some Information that the user can add for himself.
    */
    String bio;
    /**
    * The people with whom the user has become friends.
    */
    ArrayList<String> cookmates = new ArrayList<>();
    /**
    * The posts that the user has made.
    */
    ArrayList<Post> posts = new ArrayList<>();
    /**
    * Constructor that adds values to the instance variables.
    *
    * @param pass  The password of the user
    * @param uname The username of the user
    * @param nm    The full name of the user
    * @param bio   Some Information that the user can add for himself
    */

    public User(String pass, String uname, String nm, String bio) {
        maxId();
        setPassword(pass);
        setUsername(uname);
        name = nm;
        this.bio = bio;

        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DBcon.openConnection();
            stmt = connection.prepareStatement("INSERT INTO User(ID, Password, Username, Name, Bio)"
            + "VALUES(?,?,?,?,?)");
            stmt.setInt(1, userId);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.setString(4, name);
            stmt.setString(5, this.bio);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Something went wrong while creating your Profile" + e.getMessage());
        } finally {
            DBcon.closeStatement(stmt);
            DBcon.closeConnection(connection);
        }
    }

    /**
     * Constructor that retrieves the user's characteristics from database.
    * Also the list of the posts that the user has made and the list of the user's
    * cookmates searching by user ID
    *
    * @param id  The user's ID
    */
    public User(int id) {
        this.userId = id;
        String select = "SELECT * FROM User WHERE ID=?";
        Connection connection = DBcon.openConnection();
        PreparedStatement psmtUser = null;
        ResultSet rsUser = null;
        try {
            psmtUser = connection.prepareStatement(select);
            psmtUser.setInt(1, id);
            rsUser = psmtUser.executeQuery();
            while (rsUser.next()) {
                this.password = rsUser.getString("Password");
                this.username = rsUser.getString("Username");
                this.name = rsUser.getString("Name");
                this.bio = rsUser.getString("Bio");
            }
            rsUser.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rsUser);
        } finally {
            DBcon.closeStatement(psmtUser);
        }
        String selectPost = "SELECT PostID FROM Post WHERE Creator=?";
        PreparedStatement psmtPost = null;
        ResultSet rsPost = null;
        try {
            psmtPost = connection.prepareStatement(selectPost);
            psmtPost.setInt(1, id);
            rsPost = psmtPost.executeQuery();
            while (rsPost.next()) {
                int temp = rsPost.getInt("PostID");
                Post post = new Post(temp);
                updatePosts(post);
            }
            rsPost.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rsPost);
        } finally {
            DBcon.closeStatement(psmtPost);
        }
        String selectCookmates = "SELECT CookmateID FROM Cookmates WHERE UserID=?";
        PreparedStatement psmtCookmates = null;
        ResultSet rsCookmates = null;
        try {
            psmtCookmates = connection.prepareStatement(selectCookmates);
            psmtCookmates.setInt(1, id);
            rsCookmates = psmtCookmates.executeQuery();
            while (rsCookmates.next()) {
                int cookmateID = rsCookmates.getInt("CookmateID");
                cookmates.add(Main.getUsernamefromID(cookmateID));
            }
            rsCookmates.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rsCookmates);
        } finally {
            DBcon.closeStatement(psmtCookmates);
        }
        DBcon.closeConnection(connection);
    }

    /**
    * Method that returns the User's ID.
    *
    * @return the user's ID
    */
    public int getUserId() {
        return userId;
    }

    /**
    * Method that checks if the password entered by the user is correct.
    *
    * @param tempPassword The password that the user has entered
    */
    public void setPassword(String tempPassword) {
        Scanner scanner = new Scanner(System.in, "utf-8");
        boolean flag = false;
        while (!flag) {
            if (tempPassword.length() > 8) {
                flag = true;
            } else {
                System.out.println("Password must have over 8 characters!"
                     + " Please choose a new one.");
                tempPassword = scanner.next();
            }
        }
        password = tempPassword;
    }

    /**
    *Method that checks if the username entered by the user is correct.
    *
    * @param tempUsername The username that the user has entered
    */
    public void setUsername(String tempUsername) {
        Scanner scanner = new Scanner(System.in, "utf-8");
        Connection connection = null;
        PreparedStatement stmt = null;
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
                }
            } catch (SQLException e) {
                System.out.println("Could not set your username.");
            } finally {
                DBcon.closeStatement(stmt);
                DBcon.closeConnection(connection);
            }
            if (flag) {
                System.out.println("This username already exists! Please choose a new one.");
                tempUsername = scanner.next();
            }
        } while (flag);
        username = tempUsername;
    }

    /**
    * Method that returns the user's username.
    *
    * @return the user's username
    */
    public String getUsername() {
        return username;
    }

    /**
    * Method that changes the full name of the user.
    *
    * @param tempName The user's new full Name
    */
    public void setName(String tempName) {
        name = tempName;
    }

    /**
    * Method that returns the user's full name.
    *
    * @return The user's full name
    */
    public String getName() {
        return name;
    }

    /**
    * Method that changes the Bio of the user.
    *
    * @param tempBio The new Bio of the user
    *
    */
    public void setBio(String tempBio) {
        bio = tempBio;
    }

    /**
    * Method that returns the user's Bio.
    *
    * @return The user's Bio
    *
    */
    public String getBio() {
        return bio;
    }

    /**
    * Method that updates the arraylist which contains the user's posts
    * if a post is added or updated.
    *
    * @param newPost  The user's new or updated post
    */
    public void updatePosts(Post newPost) {
        posts.removeIf(post -> newPost.getPostId() == post.getPostId());
        posts.add(newPost);
    }

    /**
    * Method that the user has the opportunity to change the characteristics of his profile.
    */
    public void editProfile() {
        Scanner scanner = new Scanner(System.in, "utf-8");
        System.out.println("If you want to Edit your Username press 1.");
        System.out.println("If you want to Edit your Name press 2.");
        System.out.println("If you want to Edit your Bio press 3.");
        System.out.println("If you want to Edit your Password press 4.");
        int temp2 = scanner.nextInt();
        Connection connection = null;
        PreparedStatement stmt = null;
        switch (temp2) {
            case 1:
                System.out.println("Please insert your new Username: ");
                setUsername(scanner.next());
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
                System.out.println("Please insert your new Name: ");
                setName(scanner.next());
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
                System.out.println("Please insert your new bio: ");
                scanner.nextLine();
                setBio(scanner.nextLine());
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
                System.out.println("Please insert your new password: ");
                String answer2 = scanner.next();
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

    /**
    * Method that adds a new cookmate to your cookmates.
    * If he/she is not already in the cookmates' list, the cookmate
    * is added to both the list of cookmates and the database.
    *
    * @param cookmateID The Id of the user's new cookmate
    */
    public void makeCookmates(int cookmateID) {
        String cookmateName = Main.getUsernamefromID(cookmateID);
        if (cookmates.contains(cookmateName)) {
            System.out.println(cookmateName + " is already your cookmate");
        } else if (cookmateID == getUserId()) {
            System.out.println("You can't add yourself as your cookmate");
        } else {
            cookmates.add(cookmateName);
            Connection connection = DBcon.openConnection();
            PreparedStatement stmt = null;
            try {
                stmt = connection.prepareStatement("INSERT INTO Cookmates(UserID, CookmateID)"
                        + " VALUES(?,?)");
                stmt.setInt(1, getUserId());
                stmt.setInt(2, cookmateID);
                stmt.executeUpdate();
                System.out.println("The post's creator " + cookmateName
                        + " was added as your cookmate");
            } catch (SQLException e) {
                System.out.println("Something went wrong "
                        + "while trying to make this user your cookmate."
                        + e.getMessage());
            } finally {
                DBcon.closeStatement(stmt);
                DBcon.closeConnection(connection);
            }
        }
    }

    /**
    * Method that retrieves the max Id from the table of users from the database.
    */
    public void maxId() {
        String slct = "SELECT MAX(ID) FROM User";
        Connection con = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            con = DBcon.openConnection();
            stat = con.createStatement();
            rs = stat.executeQuery(slct);
            userId = rs.getInt(1);
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(stat);
            DBcon.closeConnection(con);
        }
        userId++;
    }

    /**
    * Method which displays the posts of a user.
    */
    public void printPosts() {
        System.out.println("\nPOSTS\n");
        if (posts.size() == 0) {
            System.out.println("No posts yet\n");
        } else {
            for (Post post: posts) {
                post.getPost();
                System.out.println();
            }
        }
    }

    /**
    * Method that returns the list of cookmates that a user has made.
    * @return The user cookmates's usernames
    */
    public String getCookmates() {
        StringBuilder stringBuilder = new StringBuilder();
        if (cookmates.size() == 0) {
            stringBuilder.append("\nNo cookmates yet");
        } else {
            for (String cookmate : cookmates) {
                stringBuilder.append("\n - ").append(cookmate);
            }
        }
        return stringBuilder.toString();
    }

    /**
    * Method that prints the user's profile.
    */
    public void getProfile() {
        System.out.println("\nPROFILE\n" + "\nName: " + getName() + "\nUsername: " + getUsername()
                           + "\nBio: " + getBio() + "\nCookmates:" + getCookmates() + "\n");
    }
}
