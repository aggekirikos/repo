package fOrk;

import java.util.Scanner;
public class Recomment /*extends Comment*/ {
    private static int recommentId;
    private String recContent;
    //private User recFrom;

    public Recomment(String content /*, User from*/) {
        super();
        recContent = content;
        recommentId++;
        //recFrom = from;
    }
    public void setRecContent(String newContent){
        recContent = newContent;
    }
    public void editRecomment(){
        Scanner input = new Scanner(System.in);

        System.out.println("PLease enter the new comment that will replace the current one");
        String a = input.nextLine();
        recContent = a;
    }
    public String getRecContent(){
        return recContent;
    }

    /*public void deleteRec(User currentUser) {
        //if ( currentUser = super.from){
        recContent= null;
        // }
    }*/
}
