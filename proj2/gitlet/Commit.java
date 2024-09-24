import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.time.LocalTime;
import java.util.Objects;
import java.nio.file.Files;
import java.nio.file.Path;


class Commit {
     private LocalTime timeStamp;
     private String branchName;
     private String message;
     private Vector<String> parentCommit;
     private Vector<Blob> blobs;
     private Vector <String>addedFiles;
     private Vector<String>modifiedFiles;
     public Commit(){
        timeStamp = LocalTime.MIDNIGHT;
        message = "initial commit.";
        branchName = "master";
        parentCommit = new Vector<>();
        addedFiles = new Vector<>();
        modifiedFiles = new Vector<>();
        parentCommit.add(null);
        blobs = new Vector<>();
     }
     public Commit(String message, String parentSHA , Vector<String> blobs , String branchName){
         this.timeStamp = LocalTime.now();
         this.message = message;
         this.branchName = branchName;
         parentCommit = new Vector<>();
         addedFiles = new Vector<>();
         modifiedFiles = new Vector<>();
         parentCommit.add(parentSHA);
         blobs = new Vector<>();
     }
     // paremetarized
     public Commit(String message , String parent1SHA , String parent2SHA, Vector<String>blobs , String branchName){
         this.timeStamp = LocalTime.now();
         this.message = message;
         this.branchName = branchName;
         parentCommit = new Vector<>();
         parentCommit.add(parent1SHA);
         parentCommit.add(parent2SHA);
         blobs = new Vector<>();
     }
    // to get the SHA1 of the object
    String getCommitSHA(){
        return sha1(timeStamp,branchName,message);
    }
    // to create the blobs of the current commit;
    public void createCommit(Vector<String>files , Commit parent){
        // initialize the blobs array with the parent blobs
        this.blobs = parent.blobs;
        for(int i = 0 ;i < files.size();i++)
        {
            // transfer each bath in staging area to blob
            Blob blob = Blob.restoreObject(files.get(i));
            boolean isExist = false ;
            for(int j = 0 ;j < parent.blobs.size();j++)
            {
                // if the bath exist it means that the file is modified or it is not modified
                if(blob.blobPath == parent.blobs.get(j).blobPath)
                {
                    // if the content is different so it is a modified file
                    if(blob.getSHA() != parent.blobs.get(j).getSHA())
                    {
                        modifiedFiles.add(blob.fileName);
                        blobs.set(i , blob);
                    }
                    isExist = true ;
                    break;
                }
            }
            // if i don't find any file with that path that mean that it is a new file
            if(!isExist)
            {
                addedFiles.add(blob.fileName);
                blobs.add(blob);
            }
        }
    }

}
