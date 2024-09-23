package gitlet;

// TODO: any imports you need here

import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Vector;

import static gitlet.Utils.sha1;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String timeStamp;
    private String branchName;
    private String message;
    private Vector<String> parentCommit;
    private Vector<Blob> blobs;
    public Commit(){
        /* TODO: set timestamp to 00:00:00 UTC */
        timeStamp = "00:00:00";

        message = "initial commit.";
        branchName = "master";
        parentCommit = new Vector<>();
        parentCommit.add(null);
        blobs = new Vector<>();
    }
    public Commit(String message, String parentSHA , Vector<Blob> blobs , String branchName){
        /* TODO:

        */
    }
    public Commit(String parent1SHA , String parent2SHA){
        // Specify of merging two commit
    }
    /*TODO:
        - void: getCommitSHA() --- this
    */
    String getCommitSHA(){
        return sha1(timeStamp,branchName,message);
    }
}
