package gitlet;


/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {

        // TODO: check if the git is already initialized deserializing the head object.
        new FileSystem();
        Repository Repo = null;
        if(FileSystem.checkGit()){
            // The Git version control system is already initialized
            Repo = new Repository().read();
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
                Repo = new Repository(new Commit());
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }

        // update Current Working Repository
        assert Repo != null;
        Repo.write();
    }
}
