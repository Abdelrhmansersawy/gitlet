package gitlet;
import java.io.Serializable;
import java.util.Objects;

import static gitlet.Utils.*;

public class Blob  implements Serializable{
    private final String filePath; // Store the Name of tracked file
    private final String content; // Store the content of file
    private final String BlobName;
    public Blob(){
        this.filePath = null;
        this.content = null;
        this.BlobName = null;
    }
    public Blob(String absolutePath){
        /*
        Initialize a blob with specific path file
         */
        this.content = FileSystem.readFile(absolutePath);
        this.filePath = absolutePath;
        this.BlobName = getBlobSHA();
    }


    public String getFilePath(){
        return this.filePath;
    }
    public String getContent(){
        return this.content;
    }
    public String getBlobName(){ return this.BlobName; }
    private String getBlobSHA() { return sha1(filePath, content); }
    public void write(String key){
         /*
            Write a Blob through serializing the object through using its SHA
         */
        FileSystem.SerializingObject(getBlobSHA() , this , key);
    }
    public static Blob read(String SHA , String key){
        /*
            Read a created Blob through deserializing the object through using its SHA
         */
        return FileSystem.DeserializingObject(SHA , Blob.class , key);
    }
    public int compareTo(Blob other){
        return this.getBlobName().compareTo(other.getBlobName());
    }
    public Boolean equals(Blob other){
        /*
            Compare two blob if they are equal or not
         */
        return Objects.equals(other.getBlobSHA(), this.getBlobSHA());
    }

}
