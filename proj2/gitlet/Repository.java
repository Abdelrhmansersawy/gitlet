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
    private final String Name = "main";
    private StagingArea stagingArea;
    private Vector<logs>globalLogs;
    private GlobalBranches globalBranches;

    public Repository(){
        new FileSystem();

    }

    public Repository(Commit Head){
        this.globalLogs =new Vector<>();
        this.globalLogs.add(new logs(Head));
        globalBranches = new GlobalBranches();
        globalBranches.addNewBranch("master" , Head.getCommitSHA());
        Head.write();
        stagingArea = new StagingArea(Head);
        write();

    }
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
        if(stagingArea.getHead().isIdentical(fileName)){
            // check if the current file is identical from the Head commit
            stagingArea.unstage(fileName);
        }else{
            stagingArea.stage(fileName , currentBlob);
        }
    }
    public void rm(String fileName){
        if(!stagingArea.hashAddedFile(fileName) && !stagingArea.getHead().isTracked(fileName)){
            System.out.println("No reason to remove the file.");
            return;
        }
        stagingArea.rm(fileName);
    }
    public void commit(String message){
        stagingArea.setHead(new Commit(stagingArea , stagingArea.getHead().getBranchName() ,message));
        globalLogs.add(new logs(stagingArea.getHead()));
        stagingArea.clear();
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
        Commit currenCommit = new Commit(stagingArea.getHead());
        while(true)
        {
            System.out.println("=====");
            currenCommit.print();
            String ParentSHA = currenCommit.getParentSHA();
            if(ParentSHA == null)
                break;
            currenCommit = Commit.read(ParentSHA);
        }
    }
    public void find(String message)
    {
        for (int i = 0 ;i < globalLogs.size();i++)
        {
            if(Objects.equals(globalLogs.get(i).getMessage(), message))
                globalLogs.get(i).printLog();
        }
    }
    public void status(){
        stagingArea.print();
    }
    public void checkoutFile(String fileName){
        if(!FileSystem.exist(FileSystem.getAbsolutePath(fileName))){
            System.out.println("File does not exist in that commit.\n");
            return;
        }
        checkoutFile(fileName , stagingArea.getHead().getCommitSHA());
    }
    public void checkoutFile(String fileName , String commitId) {
        // TODO: A [commit id] is, as described earlier, a hexadecimal numeral. A convenient feature of real Git is that one can abbreviate commits with a unique prefix
        if(!FileSystem.getFromGit(commitId, "object").exists()){
            System.out.println("No commit with that id exists.");
            return;
        }
        if(!FileSystem.exist(FileSystem.getAbsolutePath(fileName))){
            System.out.println("File does not exist in that commit.\n");
            return;
        }
        Commit currentCommit = Commit.read(commitId);
        Blob currentBlob = Blob.read(currentCommit.getBlobName(fileName) , "object");
        currentBlob.ovewrite();
        stagingArea.unstage(fileName);
    }
    public void checkoutBranch(String branchName){
        if(!FileSystem.getFromGit(branchName , "branch").exists()){
            System.out.println("No such branch exists.");
            return;
        }
        if(Objects.equals(branchName, stagingArea.getHead().getBranchName())){
            System.out.println("No need to checkout the current branch.");
            return;
        }
        Commit currentCommit = Commit.read(globalBranches.getBranchHead(branchName));
        // check if all tracked blobs into currentCommit are tracked by the current head commit
        for(String fileName : currentCommit.getBlobs().values()){
            if(!stagingArea.getHead().isTracked(fileName)){
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        for(Map.Entry<String, String> entry : currentCommit.getBlobs().entrySet()){
            String fileName = entry.getKey();
            Blob currentBlob = Blob.read(currentCommit.getBlobName(fileName) , "object");
            currentBlob.ovewrite();
            stagingArea.unstage(fileName);
        }
        stagingArea.setHead(Commit.read(globalBranches.getBranchHead(branchName)));
        stagingArea.clear();
    }
    public void createNewBranch(String branchName){
        if(globalBranches.hasBranchWithName(branchName)){
            System.out.println("A branch with that name already exists.");
            return;
        }
        globalBranches.addNewBranch(branchName , globalBranches.getBranchHead("master"));
    }
    public void removeBranch(String branchName){
        if(!globalBranches.hasBranchWithName(branchName)){
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if(Objects.equals(branchName, stagingArea.getHead().getBranchName())){
            System.out.println("Cannot remove the current branch.");
            return;
        }
        globalBranches.removeBranch(branchName);
    }
    public void reset(String commitId){
        if(!FileSystem.getFromGit(commitId,"object").exists()){
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit currentCommit = Commit.read(commitId);
        for(Map.Entry<String,String> entry : currentCommit.getBlobs().entrySet()){
            String fileName = entry.getKey();
            if(!stagingArea.getHead().isTracked(fileName)){
                /*
                If a working file is untracked in the current branch and would be overwritten by the reset
                 */
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }
        for(Map.Entry<String,String> entry : currentCommit.getBlobs().entrySet()){
            String blobName = entry.getValue();
            Blob currentBlob = Blob.read(blobName , "object");
            currentBlob.ovewrite();
        }
        stagingArea.setHead(Commit.read(commitId));
        stagingArea.clear();
    }
    public void merge(String SecondBrach)
    {
        String secondBranchHead = globalBranches.getBranchHead(SecondBranch);
        String currentBranchHead = stagingArea.getHead().getBranchName();
        // missing untracked file
        if(secondBranchHead == currentBranchHead)
        {
            System.out.println("Cannot merge a branch with itself");
            return ;
        }
        Commit currentCommit = Commit.read(currentBranchHead);
        Commit secondCommit = Commit.read(secondBranchHead);
        Commit ref ;// miss
        Map<String,String> blob1 = new HashMap<>(currenCommit.getBlobs()), blob2 =new HashMap<>(secondCommit.getBlobs())  ,blob3 = new HashMap<>(ref.getBlobs()) , currentblobs =new HashMap<>();
        Set<String> allBlobs = new HashSet<>();
        for(Map.Entry<String,String> entry: blob1.entrySet()){
            allBlobs.add(entry.getKey());
        }
        for(Map.Entry<String,String> entry: blob2.entrySet()){
            allBlobs.add(entry.getKey());
        }
        for(Map.Entry<String,String> entry: ref.entrySet()){
            allBlobs.add(entry.getKey());
        }
        boolean err  = false ;
        for(auto blob : allBlobs)
        {
            if(blob1.get(blob).equals(blob3.get(blob)))
            {
                if(!blob2.get(blob).equals(blob3.get(blob)))
                    currentblobs.put(blob, blob2.get(blob));
                else
                    currentblobs.put(blob,blob2.get(blob));
            }
            else
            {
              if(!blob2.get(blob).equals(blob3.get(blob)))
              {
                if(!blob2.get(blob).equals(blob1.get(blob)))
                {
                    err = true ;
                    break;
                }
                else
                {
                    currentblobs.put(blob,blob2.get(blob));
                }
              }
              else
              {
                currentblobs.put(blob, blob1.get(blob));
              }
            }
        }
        if(err)
        {
            System.out.println("There is a confilect");
            return ;
        }
    }
}
