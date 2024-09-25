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
    public Repository(){}
    public Repository(Commit Head){
        this.Head = Head;
        this.Head.write();
        write();
    }
    public void write(){
        FileSystem.SerializingObject(Name , this , "repo");
    }
    public Repository read(){
        return FileSystem.DeserializingObject(Name , Repository.class , "repo");
    }

    private static class stagingArea{

    }
}
