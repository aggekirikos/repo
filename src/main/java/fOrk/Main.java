package fOrk;



import java.util.Scanner;
public class Μain {
	public static void main(String args[]){
		int i = 0;
		Scanner input1 = new Scanner(System.in);
		do {
			System.out.print("Welcome to fOrk! Type 1 to SING IN, 2 to LOG IN, or -1 to END the program: ");
			int preference = input1.nextInt();
			if(preference == 1) {
				//sing in
				//στον User να γίνεται safe ο user που θα εχει ολα τα ορίσματα που θα
				//πληκτρολογήσει ο χρήστης
			} else if(preference ==2) {
				//log in μέθοδος που επιστρέφει το id του χρήστη μέσω του username και password
				//κλήση κατασκευαστή με ορισμα το int ώστε να έχουμε αντικείμενο με τα χαρακτηριστικά
				//που αντιστοιχουν στο id με το οποιο θα καλουμε τις μεθόδους που θελουμε για τον
				//συγκεκριμένο χρήστη
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
					//Εμφανιση ονομάτων χρηστών με τους οποίους έχει στείλει μηνύματα (getMesseges)
					//OpenChatBox εμφανιση μηνυματων με τον επιλεγμένο χρήστη
					//Δημιουργία αντικειμένου Message
					break;
				case 3 :
					//Κλήση κατασκευαστή Post
					//κληση μεθοδου showpost
					break;
				case 4 :
					//view profil του αντικειμενου χρηστη στον οποίο είμαστε
					System.out.print("Do you want to edit your profile? Yes/No");
					//καλει την editProfil η οποία θα πρεπει να κανει ενα update στην βάση
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
				//searcing and display titles of posts. θα εμφανίζονται οι τίτλοι
				//αριθμοιμένοι 1 εως ν μεσω της hasNext
				System.out.print("Choose the recipe you want by writing the number of /the title, or get back to " +
						"the main menu by typing -1");
				ttl = input2.nextInt();
				if (ttl == -1) {
					break;
				}
				//Select post end displaiy it
				//Μέθοδος Βαγγέλη που βρίσκει το ποστ id
				//Επιστροφή ποστ μέσω της ανάκτησης του από τον κατασκευαστή μονού ορίσματος
				System.out.println("Would you like to review it? Yes/No");
				//καλείται η μέθοδος του review
				System.out.println("Would you like to make a commen/t? Yes/No");
				//καλούμε κατασκευαστη Comment
				System.out.println("Would you like to make this user your cookmate? Yes/No");
				//kaloume method pou kanw cookmate ton creator (user.makeCookmates(creatorID)) (thelw to creatorId tou)
				//methodos 
				//ttl = -1;
			} while(ttl != -1);
	}//end of searchPost
	
	
}//end of class