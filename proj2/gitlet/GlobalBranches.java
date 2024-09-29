package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

public class GlobalBranches implements Serializable {
    private final TreeSet<String> branches;
    GlobalBranches(){
        branches = new TreeSet<>();
    }
    public void addNewBranch(String branchName , String branchHead){
        Branch newBranch = new Branch(branchName , branchHead);
        newBranch.write();
        branches.add(branchName);
    }
    public void setBranchHead(String branchName , String head){
        Branch targetBranch = Branch.read(branchName);
        targetBranch.setHead(head);
        targetBranch.write();
    }
    public String getBranchHead(String branchName){
        Branch currentBranch = Branch.read(branchName);
        return  currentBranch.getHead();
    }
    public String getBranchStart(String branchName)
    {
        //Check if the branch exist
        Branch currentBranch = Branch.read(branchName);
        return  currentBranch.getStartCommitSHA();
    }
    public void removeBranch(String branchName){
        branches.remove(branchName);
    }
    public void print(String currentBranch){
        System.out.println("=== Branches ===");
        for(String branchName : branches){
            if(Objects.equals(branchName, currentBranch)) System.out.print('*');
            System.out.println(branchName);
        }
        System.out.println(); // newline
    }
    public boolean hasBranchWithName(String branchName){
        return branches.contains(branchName);
    }
}
