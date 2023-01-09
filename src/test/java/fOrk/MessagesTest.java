package fOrk;

import junit.framework.TestCase;
import org.junit.Test;

public class MessagesTest extends TestCase {

    @Test
    public void testSendMessagestoDB() {
        int userid = 1;
        int receiversID = 2;
        String content = "Kalimera";
        Messages message = new Messages(userid, receiversID, content);
        message.sendMessagestoDB(userid, receiversID, content);
    }

}