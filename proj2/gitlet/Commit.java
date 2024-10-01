package gitlet;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static gitlet.Utils.sha1;


public class Commit implements Serializable{
    private final String timeStamp;
    private final String branchName;
    private final String message;
    private final Vector<String> parentCommit;
    private final Map<String,String> blobs;
    private final int depthInTree;
    private boolean isThereError ;
    private String mergedCommit ;
    public Commit(){
        timeStamp = getInitialTime();
        message = "initial commit.";
        branchName = "master";
        parentCommit = new Vector<>();
        parentCommit.add(null);
        blobs = new HashMap<>();
        mergedCommit = "Not a merged commit";
        depthInTree = 0; // root of the tree
    }
    public Commit(Commit other){
        // Clone Object
        this.timeStamp = other.timeStamp;
        this.branchName = other.branchName;
        this.message = other.message;
        this.parentCommit = other.parentCommit;
        this.blobs = other.blobs;
        this.depthInTree = other.getDepthInTree();
    }
    public Commit(Commit parent , String branchName , String message){
        this.timeStamp = getCurrentTime();
        this.branchName = branchName;
        this.message = message;
        this.parentCommit = new Vector<>();
        this.parentCommit.add(parent.getCommitSHA());
        this.blobs = parent.blobs;
        this.depthInTree = 1 + parent.getDepthInTree();
    }
    public Commit(StagingArea currentStagingArea , String branchName , String message){
        // Constructor for commit command
        this(currentStagingArea.getHead() , branchName , message);
        for(Map.Entry<String,String> entry: currentStagingArea.getstagingForAddional().entrySet()){
            String fileName = entry.getKey();
            String blobName = entry.getValue();
            Blob createdBlob = Blob.read(blobName , "stagingForAddional");
            createdBlob.write("object");
            this.blobs.put(fileName , createdBlob.getBlobName());
        }
        for(Map.Entry<String,String> entry: currentStagingArea.getstagingForRemoval().entrySet()){
            String fileName = entry.getKey();
            assert this.blobs.containsKey(fileName);
            this.blobs.remove(fileName);
        }
        write();
    }


    public boolean isIdentical(String fileName){
        if(!blobs.containsKey(fileName)) return false;
        Blob B1 = new Blob(blobs.get(fileName));
        Blob B2 = new Blob(fileName);
        return B1.equals(B2);
    }
    public boolean isTracked(String fileName){
        return blobs.containsKey(fileName);
    }
    public String getCommitSHA(){
        // to get the SHA1 of the object
        return sha1(timeStamp,branchName,message);
    }
    public int getDepthInTree(){ return this.depthInTree; }
    public Map<String , String > getBlobs(){ return this.blobs; };
    public String getBlobName(String fileName){
        assert blobs.containsKey(fileName);
        return blobs.get(fileName);
    }
    public String getBranchName(){return this.branchName;}
    public String getTimeStamp(){ return this.timeStamp; }
    public String getMessage(){ return  this.message; }
    public String getParentSHA(){return parentCommit.get(parentCommit.size() - 1);}
    private String getInitialTime()
    {
        ZonedDateTime epochTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(0), ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return epochTime.format(formatter);
    }
    private String getCurrentTime()
    {
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss z, EEEE, d MMMM yyyy");
        return currentTime.format(formatter);
    }
    public void write(){
         /*
            Write a Blob through serializing the object through using its SHA
         */
        FileSystem.SerializingObject(getCommitSHA() , this , "object");
    }
    public static Commit read(String SHA){
        /*
            Read a created Commit through deserializing the object through using its SHA
         */
        return FileSystem.DeserializingObject(SHA , Commit.class , "object");
    }
    public void print(){
        System.out.println("commit " + getCommitSHA());
        System.out.println("Date: " + getTimeStamp());
        System.out.println(getMessage());
    }

    // Merging two branches
    private String move(int steps){
        String currentCommit = getCommitSHA();
        while(steps > 0){
            currentCommit  = Commit.read(currentCommit).getParentSHA();
            steps--;
        }
        return currentCommit;
    }
    public String getLowestCommonCommit(String commitID1 , String commitID2){
        Commit c1 = Commit.read(commitID1);
        Commit c2 = Commit.read(commitID2);
        // Move the two commit to the same depth into the tree
        if(c1.getDepthInTree() > c2.getDepthInTree()){
            commitID1 = c1.move(c1.getDepthInTree() - c2.getDepthInTree());
        }else{
            commitID2 = c2.move(c2.getDepthInTree() - c1.getDepthInTree());
        }
        // Move every commit to its parentCommit until they go into the same commit;
        while (!Objects.equals(commitID1, commitID2)){
            commitID1 = Commit.read(commitID1).getParentSHA();
            commitID2 = Commit.read(commitID2).getParentSHA();
        }
        return commitID1;
    }
    private void setMergedBlobs(Commit currentCommit , Commit secondCommit , Commit ref)
    {
        Map<String,String> blob1 = new HashMap<>(currentCommit.getBlobs()), blob2 =new HashMap<>(secondCommit.getBlobs())  ,blob3 = new HashMap<>(ref.getBlobs());
        Set<String> allBlobs = new HashSet<>();
        for(Map.Entry<String,String> entry: blob1.entrySet()){
            allBlobs.add(entry.getKey());
        }
        for(Map.Entry<String,String> entry: blob2.entrySet()){
            allBlobs.add(entry.getKey());
        }
        for(Map.Entry<String,String> entry: blob3.entrySet()){
            allBlobs.add(entry.getKey());
        }
        for(String blob : allBlobs)
        {
            if(blob1.get(blob).equals(blob3.get(blob)))
            {
                if(!blob2.get(blob).equals(blob3.get(blob)))
                    blobs.put(blob, blob2.get(blob));
                else
                    blobs.put(blob,blob2.get(blob));
            }
            else
            {
                if(!blob2.get(blob).equals(blob3.get(blob)))
                {
                    if(!blob2.get(blob).equals(blob1.get(blob)))
                    {
                        Blob mergedBlob = blob1;

                    }
                    else
                    {
                        blobs.put(blob,blob2.get(blob));
                    }
                }
                else
                {
                    blobs.put(blob, blob1.get(blob));
                }
            }
        }
    }
    public Commit(String par1 , String par2) {
        this.timeStamp = getCurrentTime();
        this.message = "message";
        parentCommit = new Vector<>();
        parentCommit.add(null);
        this.mergedCommit= "merged Commit";
        this.parentCommit.add(par2);
        this.parentCommit.add(par1);
        blobs = new HashMap<>();
        Commit currentCommit = Commit.read(par1);
        Commit secondCommit = Commit.read(par2);
        Commit ref = Commit.read(getLowestCommonCommit(par1 , par2));
        this.branchName = currentCommit.getBranchName();
        setMergedBlobs(currentCommit, secondCommit, ref);
        depthInTree = Math.max(currentCommit.getDepthInTree(), secondCommit.getDepthInTree());
    }
}
