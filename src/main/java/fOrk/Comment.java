package fOrk;

import java.util.Scanner;
public class Comment {

    //Comment comment
    protected static int commentId;
    protected String commentContent;
    protected Recomment[] recomments;
    protected String from;
    protected String to;
    public Comment( String content , String from, String to){
        commentId++;
        commentContent = content;
        /*this.from = from;
         * this.to = to*/
    }
    public void editComment(){
        System.out.println("PLease enter the new comment that will replace the current one");
        Scanner input = new Scanner(System.in);

        String a = input.nextLine();
        commentContent = a;
    }
    public String getCommentContent(){
        return commentContent;
    }
    public String toString(){
        return /*from.getUser + */  " comment the post number " + /*to.getPost*/
                ". The comment is : " + getCommentContent();
    }
  /* public class Comment {
        int commentId;
        String content;
        String [100][2] recomments;
        public void setRecomment(String from){
            int a=0
            for (a ; a< recomments.length; a++){
                if(recomments[a][1] == null){
                    break;
                }
            }
            Scanner input = new Scanner(System.in);
            System.oyt.print("Enter the recomment : ");
            recomment[a][1] = input.nextLine();
            recomment[a][2] = from;
        }
  }
  * */

}
