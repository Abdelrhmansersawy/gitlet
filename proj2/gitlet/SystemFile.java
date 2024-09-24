package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.readObject;
import static gitlet.Utils.writeObject;

public class SystemFile implements Serializable {
    private static final String  SLASH = System.getProperty("file.separator");
    private static final String gitDirectory = getCWD() + SLASH + ".git";
    public static String getCWD(){
        /*
        Return the current working directory
         */
        return System.getProperty("user.dir");
    }
    public static String getAbsolutePath(String File){
        /*
        Give the relative path and return the absolute path
         */
        return getCWD() + SLASH + File;
    }
    public static boolean checkGit(){
        /*
        Check if the ".git" directory exist or not
         */
        File GIT = new File(gitDirectory);
        return GIT.exists();
    }
    public static <T> void SerializedObject(String SHA, T object) {
        /*
            Create a new object through serializing it to a file.
        */
        File outFile = new File(SHA);
        writeObject(outFile, (Serializable) object);
    }
    public static <T extends Serializable> T DeserializedObject(String SHA, Class<T> objectType) {
        /*
            Restore a created object of type T through deserializing the object
         */
        File inFile = new File(SHA);
        if (!inFile.exists()) {
            System.out.println("File does not exist");
            return null;
        }
        return readObject(inFile, objectType);
    }
//    public static boolean createObject(String fileName,  String content){
//
//    }
}