package gitlet;

import java.io.Serializable;
public class Branch implements Serializable {
    private final String name;
    private String head;
    public Branch(String name, String head) {
        this.name = name;
        this.head = head;
    }
    public void setHead(String newHead){ this.head = newHead; }
    public String getHead(){ return  this.head; }
    public void write(){
        FileSystem.deleteFile(FileSystem.getFromGit(name , "branch")); // remove the branch if it exist
        FileSystem.SerializingObject(name , this , "branch");
    }
    public static Branch read(String branchName){
        return FileSystem.DeserializingObject(branchName , Branch.class , "branch");
    }
}
