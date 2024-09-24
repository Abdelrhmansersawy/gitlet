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
        if(FileSystem.checkGit()){
            // The Git version control system is already initialized
        }

        if(args.length == 0){
            // TODO: what if args is empty?

            return;
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                if(FileSystem.initGit()){
                    System.out.println("Successfully initialized GIT version control-system");
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }

        Blob B = Blob.read("9a968190b5aac094c62ff5c62532bf799f69fbd5");
        System.out.println(B.getContent());
    }
}
