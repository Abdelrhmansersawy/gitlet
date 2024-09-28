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

import static gitlet.Utils.sha1;


public class Commit implements Serializable{
    private final String timeStamp;
    private String branchName;
    private final String message;
    private final Vector<String> parentCommit;
    private final Map<String,String> blobs;
    public Commit(){
        timeStamp = getInitialTime();
        message = "initial commit.";
        branchName = "master";
        parentCommit = new Vector<>();
        parentCommit.add(null);
        blobs = new HashMap<>();
    }
    public Commit(Commit other){
        // Clone commit
        this.timeStamp = other.timeStamp;
        this.branchName = other.branchName;
        this.message = other.message;
        this.parentCommit = other.parentCommit;
        this.blobs = other.blobs;
    }
    public Commit(Commit parent , String branchName , String message){
        this.timeStamp = getCurrentTime();
        this.branchName = branchName;
        this.message = message;
        this.parentCommit = new Vector<>();
        this.parentCommit.add(parent.getCommitSHA());
        this.blobs = parent.blobs;
    }
    public Commit(StagingArea currentStagingArea , String branchName , String message){
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
}
