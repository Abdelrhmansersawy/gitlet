package gitlet;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    public static void main(String[] args) {
        // TODO: check if the git is already initialized deserializing the head object.
        Repository repository = new Repository();
        if(FileSystem.checkGit()){
            // The Git version control system is already initialized
            repository = new Repository().read();
        }
        if(args.length == 0){
            // TODO: what if args is empty?

            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if(FileSystem.checkGit()){
                    System.out.println("A Gitlet version-control system already exists in the current directory.");
                    return;
                }
                FileSystem.initGit();
                repository = new Repository(new Commit());
                break;
            case "add":
                repository.add(args[1]);
                // TODO: handle the `add [filename]` command
                break;
            case "rm":
                repository.rm(args[1]);
                break;
            case "commit":
                repository.commit(args[1]);
            // TODO: FILL THE REST IN
                break;
            case "global-log":
                repository.getGlobalLogs();
                break;
            case "log":
                repository.getLogs();
                break;
            case "find":
                repository.find(args[1]);
                break;
            case "status":
                repository.status();
                break;
            case "checkout":
                if(args.length == 3){
                    repository.checkoutFile(args[2]); // java gitlet.Main checkout -- [file name]
                }else if(args.length == 4){
                    repository.checkoutFile(args[3] , args[1]); // java gitlet.Main checkout [commit id] -- [file name]
                }else if(args.length == 2){
                    repository.checkoutBranch(args[1]); //  java gitlet.Main checkout [branch name]
                }
                break;
            case "branch":
                repository.createNewBranch(args[1]);
                break;
            case "rm-branch":
                repository.removeBranch(args[1]);
                break;
            case "reset":
                repository.reset(args[1]);
                break;
            case "merge":
                repository.merge(args[1]);
                break;
        }
        repository.write();
    }
}
