package fork;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.sqlite.core.DB;

/**
 * The social media program (fOrk) main class.
 *
 * @author Elina Kapetanaki, Despoina Tsetsila, Loukas Tsaprounis,
 *     Vasiliki Papasideri, Angelos Kirikos, Angeliki Tsagkaraki,
 *     Vaggelis Siamantouras, Nicole Chlouveraki
 */
public class Main {

    /** The program's main method.
    *
    * @param args The command line arguments.
    */
    public static void main(String[] args) {
        //Connection connection1 = DBcon.openConnection();
        //DBcon.deleteTables(connection1);
        Connection connection = DBcon.openConnection();
        DBcon.createTable(connection);
        Scanner input = new Scanner(System.in, "utf-8");
        int i = 0;
        do {
            System.out.println("Welcome to fOrk! Type 1 to SING UP, 2 to LOG IN, "
                    + "or -1 to LEAVE fOrk : ");
            int preference = input.nextInt();
            input.nextLine();
            switch (preference) {
                case 1:
                    System.out.println("Please create Username");
                    String tempUsername = input.nextLine();
                    System.out.println("Please create Password");
                    String tempPassword = input.nextLine();
                    System.out.println("Please create Name(First Name)");
                    String tempName = input.nextLine();
                    System.out.println("Please create small Bio");
                    String tempBio = input.nextLine();
                    User signedUpUser = new User(tempPassword, tempUsername, tempName, tempBio);
                    forkNavigation(signedUpUser);
                    break;
                case 2:
                    int id = logIn();
                    User logedInUser = new User(id);
                    forkNavigation(logedInUser);
                    break;
                case -1:
                    i = -1;
                    break;
                default:
                    break;
            }
        } while (i != -1);
    } // end of main method

    /**
     * Ask user to insert his username and password and find him in the database.
     * If he is not found, the process is repeated.
     *
     * @return the user's ID
     */
    public static int logIn() {
        boolean flag = false;
        int userId = -1;
        Connection connection = null;
        PreparedStatement pst = null;
        do {
            Scanner input = new Scanner(System.in, "utf-8");
            System.out.println("Please enter your username");
            String un = input.nextLine();
            System.out.println("Please enter your password");
            String pw = input.nextLine();
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
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                DBcon.closeStatement(pst);
                DBcon.closeConnection(connection);
            }
            if (!flag) {
                System.out.println("Sorry, the password or/and the username"
                        + " you inserted are not valid. Please try again.");
            }
        } while (!flag);
        return userId;
    } // end of logIn

    /**
     * Navigate the user currently using the application.
     * The user is asked which action he wants from the applications' main menu
     * and the method executes the chosen option.
     *
     * @param user The user currently using the application.
     */
    public static void forkNavigation(User user) {
        int j = 0;
        Scanner input = new Scanner(System.in, "utf-8");
        do {
            System.out.println("Choose the act you want to do! \n 1: Search a recipe "
                    + "\n 2: Check your chatbox \n 3: Make a post \n 4: See or Edit Your Profile "
                    + "\n 5: Log Out \n 6: Leave Fork");
            int preference = input.nextInt();
            while (preference < 1 || preference > 6) {
                System.out.println("Please insert a number from 1 to 6!");
                preference = input.nextInt();
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
                    String title = input.next();
                    String content = create();
                    System.out.println("Please insert the cost of your total recipe.");
                    double cost = input.nextDouble();
                    System.out.println("Please insert the time required to make your recipe.");
                    int time = input.nextInt();
                    System.out.println("Please insert the category of your recipe.");
                    String category = input.next();
                    System.out.println("Please describe the difficulty level of your recipe.");
                    String diffLevel = input.next();
                    while (!(diffLevel.equals("Simple") || diffLevel.equals("Medium")
                            || diffLevel.equals("Difficult"))) {
                        System.out.println("Please describe the difficulty level with Easy, "
                                + "Medium or Difficult");
                        diffLevel = input.next();
                    }
                    String[] hashtag = new String[5];
                    System.out.println("Please insert the number of hashtags needed "
                            + "to describe your post");
                    int answer = input.nextInt();
                    while (answer < 0 || answer > 5) {
                        System.out.println("The allowed number of hashtags for each post is 0-5."
                                + " Please insert a valid answer");
                        answer = input.nextInt();
                    }
                    int i = 0;
                    while (i < answer) {
                        System.out.println("Please insert your hashtag");
                        hashtag[i] = input.next();
                        i++;
                    }
                    Post post = new Post(user.getUserId(), title, content, cost, time,
                            diffLevel, category, hashtag);
                    // Print the newly made post
                    post.getPost();
                    // Add post to the current user's posts
                    user.updatePosts(post);
                    break;
                case 4 :
                    // Print the current user's profile
                    user.getProfile();
                    System.out.println("To edit your Profile press 1. "
                            + "To see your Posts press 2. Else press 3.");
                    int answer2 = input.nextInt();
                    while (answer2 != 1 && answer2 != 2 && answer2 != 3) {
                        System.out.println("Please insert 1, 2 or 3");
                        answer2 = input.nextInt();
                    }
                    if (answer2 == 1) {
                        user.editProfile();
                    } else if (answer2 == 2) {
                        // Print the current user's posts
                        user.printPosts();
                        //System.out.print("Do you want to edit any of your posts? Yes/No");
                    }
                    break;
                case 5 :
                    j = -1;
                    break;
                case 6 :
                    System.exit(1);
                    break;
            }
        } while (j != -1);
    } // end of forkNavigation

    /**
     * Search the post the user currently using the application
     * wants to see, and let him review, comment, respond to a comment
     * and make the post's creator his cookmate.
     * The searching of the post is done through a hashtag.
     *
     * @param user The user currently using the application.
     */
    public static void searchPost(User user) {
        Scanner input = new Scanner(System.in, "utf-8");
        String rec;
        do {
            System.out.println("Give 1 hashtag correlated with the recipe you want"
                    + " or type Back to go back.");
            rec = input.nextLine();
            if (!rec.equals("Back")) {
                // Chose and get the chosen post's id
                int postId = search(rec);
                if (postId != -1) {
                    // Retrieve the chosen post
                    Post post = new Post(postId);
                    // Print the chosen post
                    post.getPost();
                    System.out.println("Would you like to review it? Yes/No");
                    String answer = input.nextLine();
                    answer = checkAnswer(answer);
                    if (answer.equals("Yes")) {
                        post.makeReview();
                    }
                    System.out.println("Would you like to make a comment? Yes/No");
                    answer = input.nextLine();
                    if (answer.equals("Yes")) {
                        System.out.println("Please type the comment: ");
                        String com = input.nextLine();
                        Comment comment = new Comment(com, user.getUserId(), postId);
                        post.createComment(comment);
                    }
                    if (post.commentListSize()) {
                        System.out.println("Would you like to reply to a comment? Yes/No");
                        answer = input.nextLine();
                        answer = checkAnswer(answer);
                        if (answer.equals("Yes")) {
                            System.out.println("Please enter the number of the comment"
                                    + " you want to reply to");
                            int ans = input.nextInt();
                            input.nextLine();
                            Comment comment = post.comments.get(ans - 1);//new Comment(ans);
                            int userId = user.getUserId();
                            comment.makeReComment(userId, post.getPostId(), comment.commentId);
                        }
                    }
                    // If the post's creator was the one reviewing, commenting or recommenting
                    // the post, his arraylist of posts gets updated.
                    if (post.getCreator() == user.getUserId()) {
                        user.updatePosts(post);
                    }
                    System.out.println("Do you want to make this user your cookmate? Yes/No");
                    answer = input.next();
                    answer = checkAnswer(answer);
                    if (answer.equals("Yes")) {
                        user.makeCookmates(post.getCreator());
                    }
                    input.nextLine();
                }
            }
        } while (!rec.equals("Back"));
    } //end of searchPost

    /**
     * Find all posts containing a given hashtag, print their titles and creators
     * and find the post the user chose from the list.
     *
     * @param hashtag The chosen hashtag
     * @return The ID of the chosen post
     */
    public static int search(String hashtag) {
        String select = ("SELECT Post.PostID, Title, Username FROM User, Hashtags, Post "
                + "WHERE Post.PostID = Hashtags.PostID AND User.ID = Post.Creator "
                + "AND Hashtags.Hashtag = ?");
        Connection connection = null;
        PreparedStatement srch = null;
        ResultSet rs = null;
        int usrChoice = 0;
        int counter = 0;
        // The ids from posts containing given hashtag will be saved in postIdList
        ArrayList<Integer> postIdList = new ArrayList<>();
        try {
            connection = DBcon.openConnection();
            srch = connection.prepareStatement(select);
            srch.setString(1, hashtag);
            rs = srch.executeQuery();
            while (rs.next()) {
                counter++;
                postIdList.add(rs.getInt("PostID"));
                String title = rs.getString("Title");
                String username = rs.getString("Username");
                System.out.println(counter + " " + title + " " + username);
            }
            rs.close();
            if (counter == 0) {
                System.out.println("Seems like there was no recipe with this hashtag,"
                        + " try another one");
            } else {
                System.out.println("Please choose a recipe you like from the following list"
                        + " or type -1 to go Back!");
                Scanner input = new Scanner(System.in, "utf-8");
                usrChoice = input.nextInt();
                while (usrChoice != -1 && (usrChoice < 1 || usrChoice > counter)) {
                    System.out.println("Wrong input. Please choose a recipe you like "
                            + "from the following list or type -1 to go back!");
                    input = new Scanner(System.in, "utf-8");
                    usrChoice = input.nextInt();
                }
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong while searching your hashtag. "
                    + e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(srch);
            DBcon.closeConnection(connection);
        }
        int usersChosenId = -1;
        if (usrChoice != -1 && usrChoice != 0) {
            usersChosenId = postIdList.get(usrChoice - 1);
        }
        return usersChosenId;
    } // end of search

    /**
     * Find and print the user's ChatBox
     * (usernames of the users he has messages with).
     *
     * @param userid The ID of the user currently using the application.
     */
    public static void getChatbox(int userid) {
        System.out.println("Your chatbox is :");
        Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        try {
            connection = DBcon.openConnection();
            stmt = connection.prepareStatement("SELECT DISTINCT Messages.Sender, Messages.Receiver "
                    + "FROM Messages WHERE Messages.Sender = ? OR Messages.Receiver = ? "
                    + "GROUP BY Sender, Receiver");
            stmt.setInt(1, userid);
            stmt.setInt(2, userid);
            ResultSet rs = stmt.executeQuery();
            Set<String> usernames = new HashSet<>();
            while (rs.next()) {
                int sendersid = rs.getInt("Sender");
                int receiversid = rs.getInt("Receiver");
                if (sendersid == userid) {
                    stmt2 = connection.prepareStatement("SELECT DISTINCT User.Username "
                            + "FROM User INNER JOIN Messages ON Messages.Receiver = User.ID "
                            + "WHERE Messages.Sender = ? GROUP BY User.Username ");
                    stmt2.setInt(1, userid);
                    ResultSet rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        String receiversUN = rs2.getString("Username");
                        usernames.add(receiversUN);
                    }
                } else {
                    stmt2 = connection.prepareStatement("SELECT DISTINCT User.Username "
                            + "FROM User INNER JOIN Messages ON Messages.Sender = User.ID "
                            + "WHERE Messages.Receiver = ? GROUP BY User.Username");
                    stmt2.setInt(1, userid);
                    ResultSet rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        String sendersUN = rs2.getString("Username");
                        usernames.add(sendersUN);
                    }
                }
            }
            for (String username : usernames) {
                System.out.println(username);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            DBcon.closeStatement(stmt);
            DBcon.closeStatement(stmt2);
            DBcon.closeStatement(stmt3);
            DBcon.closeConnection(connection);
        }
    } // end of getChatbox

    /**
     * The user is asked whether he wants to
     * start a new conversation and if the answer
     * is "Yes" he is asked to type the username of the
     * user he wants to have a conversation with. This
     * procedure is contimued untill the user gives "No"
     * as an aswer to the first question.
     * @param userid the ID of the user opening the conversation.
     */
    public static void openConversation(int userid) {
        System.out.println("Would you like to open a conversation? Yes/No");
        Scanner s = new Scanner(System.in, "utf-8");
        String answer;
        do {
            int receiverid;
            answer = s.next();
            answer = checkAnswer(answer);
            if (answer.equals("Yes")) {
                do {
                    System.out.println("Type the user you want to chat with: ");
                    String answer2;
                    Scanner s2 = new Scanner(System.in, "utf-8");
                    answer2 = s2.next();
                    String receiver = answer2;
                    receiverid = -1;
                    receiverid = getIDfromUsername(receiver);
                } while (receiverid == -1);
                getMessagesByUserId(receiverid, userid);
                typeMessage(userid, receiverid);
            }
        } while (!answer.equals("Yes") && !answer.equals("No"));
    } // end of openConversation

    /**
     * The user is asked whether he wants to text a new
     * message and this procedure is continued untill
     * the user gives "No" as an aswer to the first question.
     * @param userid The ID of the user sending the message.
     * @param receiversid The ID of the user receiving the message.
     */
    public static void typeMessage(int userid, int receiversid) {
        String answer3;
        do {
            System.out.println("Would you like to type a message?");
            Scanner s3 = new Scanner(System.in, "utf-8");
            do {
                answer3 = s3.next();
                answer3 = checkAnswer(answer3);
                if (answer3.equals("Yes")) {
                    System.out.println("Please type your message : ");
                    Scanner s4 = new Scanner(System.in, "utf-8");
                    String messageContent;
                    messageContent = s4.nextLine();
                    Messages message = new Messages(userid, receiversid, messageContent);
                    System.out.println("Your message has been successfully delived to "
                            + getUsernamefromID(message.receiversId));
                }
            } while (!answer3.equals("Yes") && !answer3.equals("No"));
        } while (!answer3.equals("No"));
    } // end of typeMessage

    /**
     * This method receives as parameters the senders and
     * the receivers ID and displays the message that has been
     * sent from the sender to the user.
     * @param receiversID the ID of the user receiving the message.
     * @param userid the ID of the user sending the message.
     */
    public static void getMessagesByUserId(int receiversID, int userid) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DBcon.openConnection();
            stmt = connection.prepareStatement("SELECT Content, MDateTime, Sender "
                    + "FROM Messages WHERE (Sender = ? AND Receiver = ?) "
                    + "OR (Sender = ? AND Receiver = ?) ORDER BY MessageID");
            stmt.setInt(1, receiversID);
            stmt.setInt(2, userid);
            stmt.setInt(3, userid);
            stmt.setInt(4, receiversID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int sendersid = rs.getInt("Sender");
                String sendersun = getUsernamefromID(sendersid);
                String messageContent = rs.getString("Content");
                String dt = rs.getString("MDateTime");
                System.out.println(sendersun + " : " + messageContent + " sent at " + dt);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(stmt);
            DBcon.closeConnection(connection);
        }
    } // end of getMessagesby_userid

    /**
     * Based on the username that this method
     * receives as a parameter, the method looks
     * up the database and returns this specific
     * users ID.
     * @param username The username of the user whose ID we want.
     * @return rtrn The users ID.
     */
    public static int getIDfromUsername(String username) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int rtrn = -1;
        try {
            connection = DBcon.openConnection();
            stmt = connection.prepareStatement("SELECT ID FROM User WHERE Username = ?");
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                rtrn = rs.getInt("ID");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(stmt);
            DBcon.closeConnection(connection);
        }
        if (rtrn == -1) {
            System.out.println("The user you entered does not exist. Please type a new one");
        }
        return rtrn;
    } // end of getIDfromUsername

    /**
     * Based on a Users ID that this method
     * receives as a parameter the method looks
     * up the database and returns the users username.
     * @param id The ID of the user whose ID we want.
     * @return rtrn The users username.
     */
    public static String getUsernamefromID(int id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String rtrn = null;
        try {
            connection = DBcon.openConnection();
            stmt = connection.prepareStatement("SELECT Username FROM User WHERE ID = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                rtrn = rs.getString("Username");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            DBcon.closeResultSet(rs);
        } finally {
            DBcon.closeStatement(stmt);
            DBcon.closeConnection(connection);
        }
        return rtrn;
    } // end of getUsernamefromID

    /**
     * This method asks the user to give the
     * number of ingredients and steps as well as
     * the ingredients and steps of the recipe he wants
     * to upload and returns a formatted string that
     * contains the recipe.
     * @return content A formatted String that containes the whole recipe.
     */
    public static String create() {
        Scanner input1 = new Scanner(System.in, "utf-8");
        System.out.println("Please enter the number of ingredients of your recipe.");
        int ingredientsNumber = input1.nextInt();
        input1.nextLine();
        System.out.println("Please enter the ingredients of your recipe.");
        StringBuilder content = new StringBuilder("\nIngredients:\n");
        for (int i = 1; i <= ingredientsNumber; i++) {
            content.append(" - ").append(input1.nextLine()).append("\n");
        }
        System.out.println("Please enter the number of steps needed to create your recipe.");
        int stepsNumber = input1.nextInt();
        input1.nextLine();
        System.out.println("Please enter the recipes steps in order.");
        content.append("\nRecipe steps:\n");
        for (int j = 1; j <= stepsNumber; j++) {
            content.append(" - ").append(input1.nextLine()).append("\n");
        }
        System.out.println("\nYou have entered the following recipe:");
        System.out.println(content);
        return content.toString();
    } // end of create

    /**
     * This method checks the users answer.
     * The only valid answers are "Yes" and
     * "No".
     * @param an The users input.
     * @return an The users input which is definitely "Yes" or "No".
     */
    public static String checkAnswer(String an) {
        Scanner n = new Scanner(System.in, "utf-8");
        while (!an.equals("Yes") && !an.equals("No")) {
            System.out.println("Invalid answer. Please insert Yes or No");
            an = n.nextLine();
        }
        return an;
    } // end of checkAnswer

} //end of class
