package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class StagingArea implements Serializable {
    private Commit Head;
    private Map<String , String> stagingForAddional;
    private Map<String , String> stagingForRemoval;
    private List<String> modifiedStagedFiles;
    private List<String> deletedStagedFiles;
    private List<String> untrackedFiles;
    private final String Name = "stagingArea";
    private final String CWD;

    public StagingArea(Commit Head){
        stagingForAddional = new HashMap<>();
        stagingForRemoval = new HashMap<>();
        CWD = FileSystem.getCWD();
        setHead(Head);
    }
    public void setHead(Commit Head){this.Head = Head;}
    public Commit getHead(){ return Head; }


    public void stage(String fileName , Blob B){
        if(stagingForAddional.containsKey(fileName)){
            FileSystem.deleteFile(FileSystem.getFromGit(stagingForAddional.get(fileName) , "stagingForAddional"));
        }
        stagingForAddional.put(fileName , B.getBlobName());
        B.write("stagingForAddional");
    }
    public void unstage(String fileName){
        if(stagingForAddional.containsKey(fileName)){
            FileSystem.deleteFile(FileSystem.getFromGit(stagingForAddional.get(fileName) , "stagingForAddional"));
        }
        stagingForAddional.remove(fileName);
    }

    void rm(String fileName){
            /*
            1. Unstage the file if it is currently staged for addition.
            2. If the file is tracked in the current commit, stage it for removal
               and remove the file from the working directory if the user has not already done
               so (do not remove it unless it is tracked in the current commit).
             */
        unstage(fileName);
        if(Head.isTracked(fileName)){
            stagingForRemoval.put(fileName , Head.getBlobName(fileName));
        }
        FileSystem.deleteFile(FileSystem.getAbsolutePath(fileName));

    }


    public Map<String , String> getstagingForAddional() {
        return stagingForAddional;
    }
    public Map<String , String> getstagingForRemoval() {
        return stagingForRemoval;
    }
    public boolean hashAddedFile(String fileName) {
        return stagingForAddional.containsKey(fileName);
    }
    public boolean hashRemovedFile(String fileName) {
        return stagingForRemoval.containsKey(fileName);
    }
    public boolean isClear(){ return  stagingForAddional.isEmpty() && stagingForRemoval.isEmpty(); }
    public void clear(){
        for(String blobName : stagingForAddional.values()){
            FileSystem.deleteFile(FileSystem.getFromGit(blobName , "stagingForAddional"));
        }
        stagingForAddional.clear();
        stagingForRemoval.clear();
        modifiedStagedFiles.clear();
        deletedStagedFiles.clear();
        untrackedFiles.clear();
    }
    public void buildUntrackedFiles(){
        modifiedStagedFiles = new ArrayList<>();
        deletedStagedFiles = new ArrayList<>();
        untrackedFiles = new ArrayList<>();
        File directory = new File(CWD);
        assert directory.exists() && directory.isDirectory();
        File[] fileList = directory.listFiles();
        if(fileList != null){
            for(File file : fileList){
                if(file.isFile()){
                    String fileName = file.getName();
                    String filePath = file.getAbsolutePath();
                    Blob currentBlob = new Blob(filePath);
                    if(hashRemovedFile(fileName)){
                        deletedStagedFiles.add(fileName);
                    }else if(Head.isTracked(fileName)){
                        Blob headBlob = Blob.read(Head.getBlobName(fileName), "object");
                        if(!currentBlob.equals(headBlob)){
                            if(stagingForAddional.containsKey(fileName)){
                                Blob stagingBlob = Blob.read(stagingForAddional.get(fileName), "stagingForAddional");
                                if(!stagingBlob.equals(currentBlob)){
                                    modifiedStagedFiles.add(fileName);
                                }
                            }else{
                                modifiedStagedFiles.add(fileName);
                            }
                        }
                    }else{
                        if(stagingForAddional.containsKey(fileName)){
                            Blob stagingBlob = Blob.read(stagingForAddional.get(fileName), "stagingForAddional");
                            if(!stagingBlob.equals(currentBlob)){
                                modifiedStagedFiles.add(fileName);
                            }
                        }else{
                            untrackedFiles.add(fileName);
                        }
                    }
                }
            }
        }
    }
    public boolean isThereUntrackedFiles(){
        buildUntrackedFiles();
        return untrac  !modifiedStagedFiles.isEmpty() || !deletedStagedFiles.isEmpty();
    }
    public void print(){
        buildUntrackedFiles();

        System.out.println("=== Staged Files ===");
        for(String fileName : stagingForAddional.keySet()){
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Removed Files ===");
        for(String fileName : stagingForRemoval.keySet()){
            System.out.println(fileName);
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===\n");
        for(String fileName : deletedStagedFiles){
            System.out.println(fileName + " (deleted)");
        }
        for(String fileName : modifiedStagedFiles){
            System.out.println(fileName + " (modified)");
        }
        System.out.println();

        System.out.println("=== Untracked Files ===\n");
        for(String fileName : untrackedFiles){
            System.out.println(fileName);
        }
        System.out.println();

    }
    public bool isThereUntrackedFile(){return untrackedFiles.size()>0;}
}
