import java.util.Scanner;
public class Comment {
    protected static int commentId;
    protected String commentContent;
    protected Recomment[] recomments;
    //protected String from;
    //protected String to;
    public Comment(){}
    public Comment( String content /*, String from, Post to*/){
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
    public void delete(/*User currentUser*/){
        //if (currentUser = from){
        commentContent = null;
        // }
        //Η κλάσση Post θα εχει εναν πίνακα Comment comments
        //και στην Post πρέπει να γίνεται η ολίσθιση
        //σπρώχνω δηλαδη στην θέση του null το commentId,
        //του επόμενου στοιχείου
        //if(commentContent[id] = null){
        //   for(int j = id; j < comments.length - 1 ή αριθμός που θα δηλώνει τα null περιεχόμενα; j++){
        //      if (commentContent[j] = null){
        //          break;
        //      }
        //      commentContent[j] = commentContent[j+1];
        //   }
        //   commentContent[comments.length - 1] = null;
        // }
    }
    /*protected void fixRecomments(){
        if( recomments.length != 0 ) {
            int i = 0;
            while (recomments[i].getRecContent() != null) {
                i++;
            }
            int nullPosition = i;
            for(int j = i; j < recomments.length - 1; j++){
                String s = recomments[j+1].getRecContent();
                recomments[j].setRecContent(s);
            }
            int a = recomments.length;
            recomments[a] = null;
        }
    }*/
}
