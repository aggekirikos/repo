package fOrk;

import java.util.Scanner;
public class Recomment extends Comment {

    private String receiver; //CommentID that the recomment refers to
    private String RecommentID;
    public Recomment(int id, String content, String from, String to, String receiver ) {
        super(id, content, from, to);
        this.receiver = receiver;
    }
}
