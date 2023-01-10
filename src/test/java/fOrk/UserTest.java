package fOrk;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Test;

public class UserTest {

	private static final User user1 = new User("123456789", "Depy", "Despoina Tsetsila", "I love cooking");
	private static final User user3 = new User("123456789", "Nicole", "Nicole Chlouveraki", "I love pasta");

    @Test
	public void testRetrieveConstructor() {
		User user2 = new User(user1.userId);
		assertEquals(user1.userId, user2.userId);
	    assertEquals(user1.password, user2.password);
	    assertEquals(user1.username, user2.username);
	    assertEquals(user1.name, user2.name);
	    assertEquals(user1.bio, user2.bio);
	    assertEquals(user1.cookmates, user2.cookmates);
    }

	@Test
	public void testSetPasswordWithCorrectParameter() {
		user1.setPassword("1111111111");
		assertEquals("Password should be set to '1111111111'", "1111111111", user1.password);
		// Change password back to initial values so that other tests run correctly
		user1.setPassword("123456789");
	}

	@Test
	public void testSetPasswordWithWrongParameter() {
		String userInput = "2222222222";
		ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
		System.setIn(is);
		user1.setPassword("111");
		assertEquals("Password should be set to '2222222222'", "2222222222", user1.password);
		// Change password back to initial values so that other tests run correctly
		user1.setPassword("123456789");
	}

	@Test
	public void testSetInvalidUsername() {
		String userInput = "Elina";
		ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
		System.setIn(is);
		user1.setUsername("Nicole");
		assertEquals("Username should not be set to an already existing username, "
				+ "but to the user's input", user1.getUsername(), userInput);
		// Change username back to initial values so that other tests run correctly
		user1.setUsername("Depi");
	}

	@Test
	public void testGetUsername() {
		assertEquals("Method should return the correct username", user1.username, user1.getUsername());
	}

	@Test
	public void testSetName() {
		user1.setName("Despina Tsetsila");
		assertEquals("Name should be set to 'Despina Tsetsila'", "Despina Tsetsila", user1.getName());
		// Change name back to initial values so that other tests run correctly
		user1.setName("Despoina Tsetsila");
	}

	@Test
	public void testGetName() {
		assertEquals("Method should return the correct username", user1.name, user1.getName());
	}

	@Test
	public void testSetBio() {
		user1.setBio("I love cooking!");
		assertEquals("Name should be set to 'I love cooking!'", "I love cooking!", user1.getBio());
		// Change bio back to initial values so that other tests run correctly
	}

	@Test
	public void testGetBio() {
		assertEquals("Method should return the correct bio", user1.bio, user1.getBio());
	}

	@Test
	public void testEditUsername() {
		 String userInput = String.format("1%sAngeliki",
				 System.lineSeparator());
		 ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
		 System.setIn(is);
		 String username = null;
		 user1.editProfile();
		 try {
			 Connection con = DBcon.openConnection();
			 PreparedStatement preparedStatement = con.prepareStatement(" SELECT Username"
					 + " FROM [User]"
					 + " WHERE ID = ?");
			 preparedStatement.setInt(1, user1.getUserId());
			 ResultSet resultSet = preparedStatement.executeQuery();
			 while (resultSet.next()) {
				 username = resultSet.getString("Username");
			 }
			 con.close();
			 preparedStatement.close();
			 resultSet.close();
		 } catch(SQLException e) {
			 System.out.println(e.getMessage());
		 }
		 assertEquals(user1.getUsername(), "Angeliki");
		 assertEquals(username, "Angeliki");
	}

	@Test
	public void testEditName() {
		String userInput = String.format("2%sAngelikiTsagkaraki",
				System.lineSeparator());
		ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
		System.setIn(is);
		String name = null;
		user1.editProfile();
		try {
			Connection con = DBcon.openConnection();
			PreparedStatement preparedStatement = con.prepareStatement(" SELECT Name"
					+ " FROM [User]"
					+ " WHERE ID = ?");
			preparedStatement.setInt(1, user1.getUserId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				name = resultSet.getString("Name");
			}
			con.close();
			preparedStatement.close();
			resultSet.close();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		assertEquals(user1.getName(), "AngelikiTsagkaraki");
		assertEquals(name, "AngelikiTsagkaraki");
	}

	@Test
	public void testEditBio() {
		String userInput = String.format("3%sI love cooking!!",
				System.lineSeparator());
		ByteArrayInputStream is = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
		System.setIn(is);
		String bio = null;
		user1.editProfile();
		try {
			Connection con = DBcon.openConnection();
			PreparedStatement preparedStatement = con.prepareStatement(" SELECT Bio"
					+ " FROM [User]"
					+ " WHERE ID = ?");
			preparedStatement.setInt(1, user1.getUserId());
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				bio = resultSet.getString("Bio");
			}
			con.close();
			preparedStatement.close();
			resultSet.close();
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		assertEquals("I love cooking!!", user1.getBio());
		assertEquals("I love cooking!!", bio);
	}

	@AfterClass
	public static void delete(){
		Connection connection = DBcon.openConnection();
		PreparedStatement prstm = null;
		try {
		    prstm = connection.prepareStatement("DELETE FROM User WHERE ID = ? OR ID = ?");
		    prstm.setInt(1, user1.userId);
		    prstm.setInt(2, user3.userId);
		    prstm.executeUpdate();
		} catch (SQLException e) {
		    //e.getMessage();
		} finally {
		    DBcon.closeStatement(prstm);
		    DBcon.closeConnection(connection);
		}
	}
}