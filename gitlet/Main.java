package gitlet;

import java.io.File;
import java.util.ArrayList;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if(args == null || args.length < 1){
            System.out.println("Please enter an command.");
        }else{
            String firstArg = args[0];
            switch (firstArg) {
                case "reset":
                    if (args.length < 2){
                        System.out.println("Must have commit id");
                    }else {
                        Methods.reset(args[1]);
                    }
                    break;
                case "status":
                    Methods.status();
                    break;
                case "rm-branch":
                    if (args.length < 2){
                        System.out.println("Must have branch name");
                    }else {
                        Methods.rmbranch(args[1]);
                    }
                    break;
                case "branch":
                    if (args.length < 2){
                        System.out.println("Must have branch name");
                    }else {
                        Methods.branch(args[1]);
                    }
                    break;
                case "find":
                    if (args.length < 2){
                        System.out.println("must have commit message");
                    }else {
                        Methods.find(args[1]);
                    }
                    break;
                case "global-log":
                    Methods.globallog();
                    break;
                case "checkout":
                    if(args.length == 2){
                        Methods.checkoutbranch(args[1]);
                    }else if(args.length == 3) {
                        Methods.checkout(args[1], args[2]);
                        break;
                    } else if(args.length == 4){
                            Methods.checkoutcommit(args[1], args[3], args[2]);
                        }else{
                        System.out.println("Incorrect operands");
                    }
                    break;
                case "commit":
                    if(args.length > 1) {
                        Methods.commit(args[1]);
                    }else{System.out.println("Must have commit message");
                    }
                    break;
                case "init":
                    Methods.init();
                    break;
                case "add":
                    Methods.add(args[1]);
                    break;
                case "rm":
                    Methods.rm(args[1]);
                    break;
                case "log":
                    Methods.log();
                    break;
                default:
                    System.out.println("No command with that name exists.");
        }
        }
    }
        }


