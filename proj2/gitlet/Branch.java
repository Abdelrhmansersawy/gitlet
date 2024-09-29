package gitlet;

import java.io.Serializable;
public class Branch implements Serializable {
    private final String name;
    private String head;
    private final String startCommitSHA;
    public Branch(String name, String head) {
        this.name = name;
        this.head = head;
        this.startCommitSHA = head;
    }
    public void setHead(String newHead){ this.head = newHead; }
    public String getHead(){ return  this.head; }
    public String getStartCommitSHA(){return startCommitSHA;}
    public String getName(){return name;}
    public void write(){
        FileSystem.deleteFile(FileSystem.getFromGit(name , "branch")); // remove the branch if it exist
        FileSystem.SerializingObject(name , this , "branch");
    }
    public static Branch read(String branchName){
        //Check if the branch exist
        return FileSystem.DeserializingObject(branchName , Branch.class , "branch");
    }
}
