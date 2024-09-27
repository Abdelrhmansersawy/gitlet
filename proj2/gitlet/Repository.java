package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */
    private Commit Head;
    private final String Name = "main";
    private StagingArea stagingArea;
    private Vector<logs>globalLogs;
    public Repository(){}
    public Repository(Commit Head){
        this.Head = Head;
        this.globalLogs =new Vector<>();
        this.globalLogs.add(new logs(Head));
        this.Head.write();
        stagingArea = new StagingArea(Head);
        write();

    }
    public Commit getHead(){ return this.Head; }
    public void write(){
        FileSystem.deleteFile(FileSystem.getFromGit(Name , "repo"));
        FileSystem.SerializingObject(Name , this , "repo");
    }
    public Repository read(){
        return FileSystem.DeserializingObject(Name , Repository.class , "repo");
    }
    public void add(String fileName){
        /*
        Adds a copy of the file as it currently exists to the staging area
        Staging an already-staged file overwrites
        the previous entry in the staging area with the new contents.
         */
        if(!FileSystem.exist(FileSystem.getAbsolutePath(fileName))){
            System.out.println("File does not exist.");
            return;
        }
        Blob currentBlob = new Blob(FileSystem.getAbsolutePath(fileName));
        if(Head.isIdentical(fileName)){
            // check if the current file is identical from the Head commit
            stagingArea.unstage(fileName);
        }else{
            stagingArea.stage(fileName , currentBlob);
        }
    }
    public void rm(String fileName){
        if(!stagingArea.hashAddedFile(fileName) && !Head.isTracked(fileName)){
            System.out.println("No reason to remove the file.");
            return;
        }
        stagingArea.rm(fileName);
    }
    public void commit(String message){
        Head = new Commit(stagingArea , message);
        globalLogs.add(new logs(Head));
        //stagingArea.clear(); // TODO: Clear all blobs into (add , rm)
    }
    public void getGlobalLogs()
    {
        for (int i = 0 ;i < this.globalLogs.size();i++)
        {
            globalLogs.get(i).printLog();
        }
    }
    public void getLogs()
    {
        Commit currenCommit = new Commit();
        currentCommit = Head ;
        whlie(true)
        {
            System.out.println("=====");
            currenCommit.print();
            String ParentSHA = currenCommit.getParentSHA();
            //currenCommit.write();
            if(ParentSHA == null)
                break;
            currenCommit = Commit.read(ParentSHA);
        }
    }
    public void find(String message)
    {
        for (int i = 0 ;i < globalLogs.size();i++)
        {
            if(globalLogs.get(i).getMessage() == message)
                globalLogs.get(i).printLog();
        }
    }
}
