package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StagingArea implements Serializable {
    private Commit Head;
    private Map<String , String> stagingForAddional;
    private Map<String , String> stagingForRemoval;
    private final String Name = "stagingArea";

    public StagingArea(Commit Head){
        stagingForAddional = new HashMap<>();
        stagingForRemoval = new HashMap<>();
        setHead(Head);
        write();
    }
    public void setHead(Commit Head){this.Head = Head;}
    public Commit getHead(){ return Head; }
    public void write(){
        FileSystem.deleteFile(FileSystem.getFromGit(Name , "staging"));
        FileSystem.SerializingObject(Name , this , "staging");
    }
    public StagingArea read(){
        return FileSystem.DeserializingObject(Name , StagingArea.class , "staging");
    }

    public void stage(String fileName , Blob B){
        if(stagingForAddional.containsKey(fileName)){
            FileSystem.deleteFile(FileSystem.getFromGit(fileName , "stagingForAddional"));
        }
        stagingForAddional.put(fileName , B.getBlobName());
        B.write("stagingForAddional");
    }
    public void unstage(String fileName){
        if(stagingForAddional.containsKey(fileName)){
            FileSystem.deleteFile(FileSystem.getFromGit(stagingForAddional.get(fileName) , "stagingForAddional"));
        }
        stagingForAddional.remove(fileName);
    }

    void rm(String fileName){
            /*
            1. Unstage the file if it is currently staged for addition.
            2. If the file is tracked in the current commit, stage it for removal
               and remove the file from the working directory if the user has not already done
               so (do not remove it unless it is tracked in the current commit).
             */
        unstage(fileName);
        if(Head.isTracked(fileName)){
            stagingForRemoval.put(fileName , Head.getBlobName(fileName));
        }
        FileSystem.deleteFile(FileSystem.getAbsolutePath(fileName));

    }

    public boolean inRemoval(String file) {
        return stagingForRemoval.containsKey(file);
    }
    public Map<String , String> getAddedfile() {
        return stagingForAddional;
    }

    public boolean hashAddedFile(String fileName) {
        return stagingForAddional.containsKey(fileName);

    }
}
