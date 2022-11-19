import java.util.*;
import java.util.Scanner;

public class Main {
	private static Scanner scanner1 = new Scanner(System.in);

	public static void main(String[] args) {
		int id = 0;
		//for (int i = 0; i <=2; i ++){
			System.out.println("\nNew User\n");
			User user = new User();
			id = id + 1;
			user.setID(id);
			System.out.println("ID : " + user.getID());
			user.setPassword(user.createPassword());
			user.setUsername(user.createUsername());
			user.setName(user.createName());
			user.setBio(user.createBio());
			/*System.out.println("ID : " + user.getID());
			System.out.println("Password : " + user.getPassword());
			System.out.println("Username : " + user.getUsername());
			System.out.println("Name : " + user.getName());
			System.out.println("Bio : " + user.getBio());*/
			int answer = -1;
			while(answer != 0) {
				System.out.println("\nMENU : ");
				System.out.println("\nIf you want to see your Profile press 1\n");
				System.out.println("If you want to search a recipe press 2\n");
				System.out.println("If you want to make a Post press 3\n");
				System.out.println("If you want to go to Chat press 4\n");
				System.out.println("If you want to open your Notifications press 5\n");
				System.out.println("If you want to EXIT press 0\n");

				answer = scanner1.nextInt();
				Post postNumber = new Post();
				if(answer == 1) {
					System.out.println("\nPROFILE ");
					System.out.println("\nUsername : " + user.getUsername());
					System.out.println("Name : " + user.getName());
					System.out.println("Bio : " + user.getBio());
					user.printPosts();
				}
				else if (answer == 3){
					user.makePost();
				}
			}
		//}
	}
}