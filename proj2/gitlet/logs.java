package gitlet;

import java.io.Serializable;

public class logs implements Serializable{
    private String message ; // Store the message of the Commit
    private String timeStamp; // Store the TimeStamp of the Commit
    private String SHA;// Store the SHA value of the commit

    public logs(Commit head)
    {
        this.message = head.getMessage() ;
        this.timeStamp = head.getTimeStamp();
        this.SHA = head.getCommitSHA();
    }
    public String getMessage()
    {
        return this.message;
    }
    public void printLog()
    {
        System.out.println("===");
        System.out.println("commit " + this.SHA);
        System.out.println("Date: " + this.timeStamp);
        System.out.println(this.message);
    }
}
