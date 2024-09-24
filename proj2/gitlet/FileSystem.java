package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import static gitlet.Utils.*;

public class FileSystem implements Serializable {
    private static final String  SLASH = System.getProperty("file.separator");
    private static final String CWD = getCWD();
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final String GITLET_PATH = String.valueOf(join(CWD, ".gitlet"));
    public static String getCWD(){
        /*
        Return the current working directory
         */
        return System.getProperty("user.dir");
    }
    public static String getAbsolutePath(String relativePath){
        /*
        Give the relative path and return the absolute path
         */
        return getCWD() + SLASH + relativePath;
    }
    public static String getGitletPath(String fileName){
        /*
        Return the absolute path of a file inside ".gitlet"
         */
        return GITLET_PATH + SLASH + fileName;
    }
    public static <T> void SerializingObject(String SHA, T object) {
        /*
            Create a new serialized object through serializing it to a file.
        */
        File outFile = new File(getGitletPath(SHA));
        System.out.println(SHA);

        writeObject(outFile, (Serializable) object);
    }
    public static <T extends Serializable> T DeserializingObject(String SHA, Class<T> objectType) {
        /*
            Restore a created object of type T through deserializing the object
         */
        File inFile = new File(getGitletPath(SHA));
        if (!inFile.exists()) {
            System.out.println("File does not exist");
            return null;
        }
        return readObject(inFile, objectType);
    }
    public static String readFile(String absolutePath){
        try {
           return Files.readString(Path.of(absolutePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean initGit(){
        /*
        Initialize Git version-control system
         */
        if(GITLET_DIR.exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return  false;
        }
        boolean isCreated = GITLET_DIR.mkdir();
        if(!isCreated){
            // TODO: Throw expection can't create a directory with name .gitlet
        }
        return isCreated;
    }
    public static boolean checkGit(){
        /*
        Check if the ".gitlet" directory exist or not
         */
        return GITLET_DIR.exists();
    }
    public static boolean exist(String absolutePath){
        /*
        Check if the file with absolute path exist or not
         */
        File F = new File(absolutePath);
        return F.exists();
    }
}