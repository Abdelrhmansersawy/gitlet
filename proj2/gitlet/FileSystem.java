package gitlet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static gitlet.Utils.*;

public class FileSystem implements Serializable {
    private static final String  SLASH = System.getProperty("file.separator");
    private static final String CWD = getCWD();
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    private static final String GITLET_PATH = String.valueOf(join(CWD, ".gitlet"));
    private static Map<String,File> DIRECTORY;
    public FileSystem(){
        DIRECTORY = new LinkedHashMap<>();
        DIRECTORY.put("git" , GITLET_DIR);
        DIRECTORY.put("object" , join(DIRECTORY.get("git") , "object"));
        DIRECTORY.put("refr" , join(DIRECTORY.get("git") , "refr"));
        DIRECTORY.put("repo" , join(DIRECTORY.get("refr"), "repo") );
        DIRECTORY.put("staging" , join(DIRECTORY.get("repo"), "staging") );
        DIRECTORY.put("stagingForAddional" , join(DIRECTORY.get("staging"), "add") );
        DIRECTORY.put("stagingForRemoval" , join(DIRECTORY.get("staging"), "rm") );
        DIRECTORY.put("branch" , join(DIRECTORY.get("git") , "branches"));
    }

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
    public static String getFileName(String absolutePath){
        File file = new File(absolutePath);
        if(!file.exists()){
            System.out.println("Can't found a file with absolute path " + absolutePath);
            return null;
        }
        return file.getName();
    }
    public static File getFromGit(String fileName , String key){
        /*
        Return the absolute path of a file inside ".gitlet"
         */
        return join(DIRECTORY.get(key) , fileName);
    }
    public static <T> void SerializingObject(String fileName, T object , String key) {
        /*
            Create a new serialized object through serializing it to a file.
        */
        File outFile = getFromGit(fileName , key);

        writeObject(outFile, (Serializable) object);
    }
    public static <T extends Serializable> T DeserializingObject(String fileName, Class<T> objectType , String key) {
        /*
            Restore a created object of type T through deserializing the object
         */
        File inFile = getFromGit(fileName , key);
        if (!inFile.exists()) {
            System.out.println("File does not exist");
            return null;
        }
        return readObject(inFile, objectType);
    }
    public static String readContentFromFile(String absolutePath){
        return readContentsAsString(new File(absolutePath));
    }
    public static void deleteFile(String aboslutePath){
        deleteFile(new File(aboslutePath));
    }
    public static void deleteFile(File file){
        if(file.exists()){
            file.delete();
        }
    }
    public static void initGit(){
        /*
        Initialize Git version-control system for the first time
         */
        try {
            // Create ".gitlet" directory
            if (GITLET_DIR.exists()) {
                throw new IOException("A Gitlet version-control system already exists in the current directory.");
            }

            for( Map.Entry<String, File> entry : DIRECTORY.entrySet()){
                File file = entry.getValue();
                if(!file.mkdir()){
                    throw  new IOException("Can't create " + entry.getKey() + " with directory " + entry.getValue());
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void writeContentIntoFile(String absolutePath , String content){

        try {
            FileWriter writer = new FileWriter(absolutePath);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.out.println("Can't create a file with absolute path equal " + absolutePath);
            e.printStackTrace();
        }
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
