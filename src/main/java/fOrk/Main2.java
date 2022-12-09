package fOrk;



import java.util.Scanner;

public class �ain2 {

	static Scanner myscan = new Scanner(System.in);
	static Scanner myscan1 = new Scanner(System.in);
	static Scanner myscan2 = new Scanner(System.in);
	static Scanner myscan3 = new Scanner(System.in);

	public static void main(String args[]){
		int i = 0;
		Scanner input1 = new Scanner(System.in);
		Scanner scanner1 = new Scanner(System.in);
		do {
			System.out.print("Welcome to fOrk! Type 1 to SING IN, 2 to LOG IN, or -1 to END the program: ");
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
				User user = new User();
				//���� User �� ������� safe � user ��� �� ���� ��� �� ��������
			} else if(preference == 2) {
				//log in ������� ��� ���������� �� id ��� ������ ���� ��� username ��� password
				//����� ������������ �� ������ �� int ���� �� ������ ����������� �� �� ��������������
				//��� ������������ ��� id �� �� ����� �� ������� ��� �������� ��� ������� ��� ���
				//������������ ������
			} else if(preference == -1) {
				i = -1;
				System.exit(-1);
			}
			fOrkNavigation();
		}while(i != -1);
	}

	private static void fOrkNavigation(/*User user*/) {
		int j = 0;
		Scanner input2 = new Scanner(System.in);
		do{
			System.out.println("Choose the act you want to do! /n 1: Search a recipe /n 2: Check your chatbox"
					+ "/n 3: Make a post /n 4: Profile /n 5: Disconnect");
			int preference = input2.nextInt();
			switch (preference) {
				case 1 :
					searchPost();
					break;
				case 2 :
					//�������� �������� ������� �� ���� ������� ���� ������� �������� (getMesseges)
					//OpenChatBox �������� ��������� �� ��� ���������� ������
					//���������� ������������ Message
					break;
				case 3 :
					Post post = new Post();
					//����� ������� showPost
					break;
				case 4 :
					System.out.println("\nPROFILE ");
					System.out.println("\nUsername : " + user.getUsername());
					System.out.println("Name : " + user.getName());
					System.out.println("Bio : " + user.getBio());
					user.printPosts();
					System.out.println("If you want to go to Edit you Profile press 1\n");
					int answer2 = scanner1.nextInt();
					if(answer2 == 1) {
						user.editProfil();
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
	private static void searchPost(String a, /*User user*/) {
		Scanner input2 = new Scanner(System.in);
		int ttl = 0;
			do {
				System.out.print("Give 1 words correlated with the recipe you want, seperated wiht comma: ");
				String rec = input2.nextLine();
				//separate rec and taking the substrings as the hastags for searcing
				//searcing and display titles of posts. �� ������������ �� ������
				//������������ 1 ��� � ���� ��� hasNext
				System.out.print("Choose the recipe you want by writing the number of /the title, or get back to " +
						"the main menu by typing -1");
				ttl = input2.nextInt();
				if (ttl == -1) {
					break;
				}
				//Select post end displaiy it
				//������� ������� ��� ������� �� ���� id
				//��������� ���� ���� ��� ��������� ��� ��� ��� ������������ ����� ���������
				System.out.println("Would you like to review it? Yes/No");
				//�������� � ������� ��� review
				System.out.println("Would you like to make a commen/t? Yes/No");
				//������� ������������ Comment
				System.out.println("Would you like to make this user your cookmate? Yes/No");
				//kaloume method pou kanw cookmate ton creator (user.makeCookmates(creatorID)) (thelw to creatorId tou)
				//methodos
				//ttl = -1;
			} while(ttl != -1);
	}//end of searchPost


}//end of class