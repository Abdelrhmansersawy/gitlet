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
    private Vector <String>addedFiles;
    private Vector<String>modifiedFiles;
    public Commit(){
        timeStamp = getInitialTime();
        message = "initial commit.";
        branchName = "master";
        parentCommit = new Vector<>();
        addedFiles = new Vector<>();
        modifiedFiles = new Vector<>();
        parentCommit.add(null);
        blobs = new HashMap<>();
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
//    // paremetarized constructor to set a new commit
//    public Commit(String message, String parentSHA , Vector<String> blobs , String branchName){
//        this.timeStamp = getCurrentTime();
//        this.message = message;
//        this.branchName = branchName;
//        parentCommit = new Vector<>();
//        addedFiles = new Vector<>();
//        modifiedFiles = new Vector<>();
//        parentCommit.add(parentSHA);
//        blobs = new HashMap<>();
//    }
//    // paremetarized constructor to merge two commits
//    public Commit(String message , String parent1SHA , String parent2SHA, Vector<String>blobs , String branchName){
//        this.timeStamp = getCurrentTime();
//        this.message = message;
//        this.branchName = branchName;
//        parentCommit = new Vector<>();
//        parentCommit.add(parent1SHA);
//        parentCommit.add(parent2SHA);
//        blobs = new Vector<>();
//    }
    private String getCommitSHA(){
        // to get the SHA1 of the object
        return sha1(timeStamp,branchName,message);
    }
    // to create the blobs of the current commit;
//    public void createCommit(Vector<String>files , Commit parent){
//        // initialize the blobs array with the parent blobs
//        this.blobs = parent.blobs;
//        for(int i = 0 ;i < files.size();i++)
//        {
//            // transfer each bath in staging area to blob
//            Blob blob = Blob.read(files.get(i));
//            boolean isExist = false ;
//            for(int j = 0 ;j < parent.blobs.size();j++)
//            {
//                // if the bath exist it means that the file is modified or it is not modified
//                if(Objects.equals(blob.getBlobName(), parent.blobs.get(j).getBlobName()))
//                {
//                    // if the content is different so it is a modified file
//                    if(!Objects.equals(blob.getBlobName(), parent.blobs.get(j).getBlobName()))
//                    {
//                        modifiedFiles.add(blob.getBlobName());
//                        blobs.set(i , blob);
//                    }
//                    isExist = true ;
//                    break;
//                }
//            }
//            // if i don't find any file with that path that mean that it is a new file
//            if(!isExist)
//            {
//                addedFiles.add(blob.getBlobName());
//                blobs.add(blob);
//            }
//        }
//    }
    public Commit createCommit(StagingArea curStagingArea , String message){
        /*
        In Create new commit with a current staging Area
         */
        return null;
    }
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
}