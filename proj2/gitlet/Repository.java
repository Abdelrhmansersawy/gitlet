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
    private static Commit Head;
    private static final String Name = "main";
    public static void init(Commit Head){
        Repository.Head = Head;
    }
    public static void addFile(String fileName){
        /*
            Add File into Staging Area
        */
        String filePath = FileSystem.getAbsolutePath(fileName);
        if(!FileSystem.exist(filePath)){
            System.out.println("File does not exist.");
            return;
        }

        Blob B = new Blob(filePath);

        if(!Head.hasBlob(B.getBlobName()){
            stagingArea.rm(filePath);
            stagingArea.add(B);
        }
    }
    public static void rm(String fileName){
        /*
        Remove a file from staging Area
         */
        String filePath = FileSystem.getAbsolutePath(fileName);
        Blob B = new Blob(filePath);


        if(!stagingArea.rm(filePath) || !Head.isTracked(filePath)){

        }
    }
    public static void commit(String message){
        if(stagingArea.blobs.isEmpty()){
            // No changes aren't done for commit
            System.out.println("No changes added to the commit.");
            return;
        }
        /*
        Create a new commit with current changes
         */
        Head = Head.createCommit(fileAbsolutePaths);
    }
    public static void write(){
         /*
            Write a Repository through serializing the object through using its Name
         */
        FileSystem.SerializingObject(Name , this);
    }
    public static void read(){
        /*
            Read a created Blob through deserializing the object through using its SHA
         */
        FileSystem.DeserializingObject(Name,Repository.class);
    }
    private static class stagingArea{
        private static Map<String, String> blobs; // { File name, BlobName(AKA SHA1) }
        stagingArea(){
            blobs = new HashMap<>();
        }
        public static void add(Blob B){
            /*
                Adding a new File into staging area
             */
            B.write();
            blobs.put(B.getFilePath() , B.getBlobName());
        }
        public static boolean rm(String filePath){
            if(!blobs.containsKey(filePath)){
                return false; // File is not exist
            }
            blobs.remove(filePath);
            return true;
        }
        public static void print(){
            System.out.println("=== Staged Files ===");
            for(Blob B : blobs.values()){
                System.out.println(FileSystem.getFileName(B.getFilePath()));
            }
            System.out.println(); // newline
        }
    }
}
