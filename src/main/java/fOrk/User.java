package fOrk;

import java.util.*;

public class User {

	Scanner myscan = new Scanner(System.in);
	Scanner myscan1 = new Scanner(System.in);
	Scanner myscan2 = new Scanner(System.in);
	Scanner myscan3 = new Scanner(System.in);
	int ID;
	int password;
	String username;
	String name;
	String bio;
	String [] cookmates = new String[100];
	String [] post = new String[100];
	Post pst = new Post();

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getID() {
		return ID;
	}

	public int createPassword() {
		int tempPassword = 0;
				boolean flag = false;
				while (flag == false) {
					System.out.println("Please give a password");
					tempPassword = myscan.nextInt();
					if (tempPassword % 10000000 != tempPassword) {
						//password = tempPassword;
						flag = true;
					}
					else {
						System.out.println("Password must have over 8 charecters!");
					}
		}
		return tempPassword;
	}

	public void setPassword(int tempPassword) {
		password = tempPassword;
	}

	public int getPassword() {
		return password;
	}

	public String createUsername() {
		String tempUsername = "null";
		System.out.println("Please give a Username");
		tempUsername = myscan1.nextLine();
		return tempUsername;
	}

	public void setUsername(String tempUsername) {
		username = tempUsername;
	}

	public String getUsername() {
			return username;
	}

	public String createName() {
		String tempName = "null";
		System.out.println("Please give a Name");
		tempName = myscan2.nextLine();
		return tempName;
	}

	public void setName(String tempName) {
		name = tempName;
	}

	public String getName() {
		return name;
	}

	public String createBio() {
		String tempBio = "null";
		System.out.println("Please give a Bio");
		tempBio = myscan3.nextLine();
		return tempBio;
	}

	public void setBio(String tempBio){
		bio = tempBio;
	}

	public String getBio(){
		return bio;
	}



	public String notifications() {
		return "0";
	}

	public int search() {
		return 0;
	}

	int i = 0;
	public void makePost(){
		pst.setPost();
		post[i] = pst.getPost();
		System.out.println("\nPost\n" + post[i]);
		i++;
	}

	public void printPosts() {
		int j = 0;
		System.out.println("Print post works");
		while ( post[j] != null) {
			System.out.println("\nPost " + (j+1));
			System.out.println(post[j]);
			j = j + 1;
		}
	}
}