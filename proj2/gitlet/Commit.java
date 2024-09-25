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
    private final String branchName;
    private final String message;
    private final Vector<String> parentCommit;
    private Map<String,String> blobs;
    public Commit(){
        timeStamp = getInitialTime();
        message = "initial commit.";
        branchName = "master";
        parentCommit = new Vector<>();
        parentCommit.add(null);
        blobs = new HashMap<>();
    }
    public Commit(StagingArea curStagingArea , String message){
        this.message = message;
        this.timeStamp = getCurrentTime();
        Commit par = curStagingArea.getHead();
        this.parentCommit = new Vector<>();
        parentCommit.add(par.getCommitSHA());
        this.branchName = par.getBranchName();
        this.blobs = par.getBlobs() ;
        for (Map.Entry<String, String> blob : par.blobs.entrySet()) {
            if(!this.blobs.containsKey(blob.getKey())){
                if(curStagingArea.inRemoval(blob.getKey()))
                    continue;
                else
                    this.blobs.put(blob.getKey(), blob.getValue());
            }
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
    private String getCommitSHA(){
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