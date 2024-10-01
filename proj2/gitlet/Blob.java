package gitlet;
import java.io.Serializable;
import java.util.Objects;

import static gitlet.Utils.*;

public class Blob  implements Serializable{
    private final String trackedFileAbsolutePath; // Store the Name of tracked file
    private final String content; // Store the content of file
    private final String BlobName;
    public Blob(){
        this.trackedFileAbsolutePath = null;
        this.content = null;
        this.BlobName = null;
    }
    public Blob(String filePath , String content){
            this.trackedFileAbsolutePath = filePath;
            this.content = content;
            this.BlobName = generateBlobSHA();
    }
    public Blob(String absolutePath){
        /*
        Initialize a blob with specific path file
         */
        this.content = FileSystem.readContentFromFile(absolutePath);
        this.trackedFileAbsolutePath = absolutePath;
        this.BlobName = generateBlobSHA();
    }


    public String getTrackedFileAbsolutePath(){return this.trackedFileAbsolutePath; }
    public String getContent(){
        return this.content;
    }
    public String getBlobName(){ return this.BlobName; }
    private String generateBlobSHA() { return sha1(trackedFileAbsolutePath, content); }
    public void write(String key){
         /*
            Write a Blob through serializing the object through using its SHA
         */
        FileSystem.SerializingObject(generateBlobSHA() , this , key);
    }
    public static Blob read(String SHA , String key){
        /*
            Read a created Blob through deserializing the object through using its SHA
         */
        return FileSystem.DeserializingObject(SHA , Blob.class , key);
    }
    public void overWriteWorkingDirectory(){
        /*
        Overwrite the content of this blob of the one into working directory
         */
        FileSystem.writeContentIntoFile(this.getTrackedFileAbsolutePath() , this.getContent());
    }
    public int compareTo(Blob other){
        return this.getBlobName().compareTo(other.getBlobName());
    }
    public Boolean equals(Blob other){
        /*
            Compare two blob if they are equal or not
         */
        return Objects.equals(other.getBlobName(), this.getBlobName());
    }

}
