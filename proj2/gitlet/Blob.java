package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Objects;

import static gitlet.SystemFile.*;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    private String fileName; // Store the Name of tracked file
    private String content; // Store the content of file
    private String blobPath;
    private final String  SLASH = System.getProperty("file.separator");
    public Blob(){
        fileName = null;
        content = null;
        blobPath = null;
    }
    public Blob(String path){
        /*
        Initialize a blob with specific path file
         */
        try {
            this.content = Files.readString(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fileName = path;
        this.blobPath = ".git" + SLASH + getSHA();
    }

    public String getFileName(){
        return this.fileName;
    }
    public String getContent(){
        return this.content;
    }
    public String getBlobPath(){
        return this.blobPath;
    }
    String getSHA(){
        /*
            Return the SHA1 of the current Blob
         */
        return sha1(fileName, content);
    }
    private void createBlob(){
         /*
            Create a new Blob through serializing the object
         */
        File outFile = new File(blobPath);
        writeObject(outFile, this);

        SerializedObject(blobPath , this);
    }
    public static Blob restoreObject(String SHA){
        /*
            Restore a created Blob through deserializing the object
         */
        File inFile = new File(SHA);
        if(!inFile.exists()){
            System.out.println("File is not exist");
            return null;
        }
        return DeserializedObject(SHA , Blob.class);
    }
    public Boolean equals(Blob other){
        /*
            Compare two blob if they are equal or not
         */
        return Objects.equals(other.getSHA(), this.getSHA());
    }

}
