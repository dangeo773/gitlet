package gitlet;

import java.io.File;
import java.util.*;
import java.lang.Object;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    public static final File commits = Utils.join(GITLET_DIR, "commits");
    public static final File staging_area = Utils.join(GITLET_DIR, "staging_area");
    public static final File branches = Utils.join(GITLET_DIR, "branches");
    public static final File Blobs = Utils.join(GITLET_DIR, "blobs");
    public static final File additions = Utils.join(staging_area, "additions");
    public static final File removals = Utils.join(staging_area, "removals");
    public static final File master = Utils.join(branches, "master");
    public static File headaddress;
    public static File headcommit;
    public static File HEAD;
    public static final File ignorefiles = Utils.join(GITLET_DIR, "ignorefiles");

    public static File getgitdir(){return GITLET_DIR;    }
    public static File getcwd(){
        return CWD;
    }
    public static File getcommits(){
        return commits;
    }
    public static File getStaging_area(){
        return staging_area;
    }
    public static File getHead(){ return HEAD; }
    public static File getbranches(){
        return branches;
    }
    public static File getBlobs(){ return Blobs; }
    public static File getAdditions(){
        return additions;
    }
    public static File getRemovals(){
        return removals;
    }
    public static File getmaster(){return master; }
    public static File getHeadaddress(){
        return headaddress;
    }
    public static File getHeadcommit(){return headcommit;}
    public static File getHEAD(){return HEAD; }

    static void createRepository() {
        GITLET_DIR.mkdir();
        commits.mkdir();
        staging_area.mkdir();
        branches.mkdir();
        Blobs.mkdir();
        HEAD = Utils.join(GITLET_DIR, "HEAD");
        Utils.writeContents(HEAD, "branches/master");

        wipestaging();
        Commit first = new Commit("initial commit", null, null);
        first.saveCommit();
        Utils.writeContents(master, first.UID);
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        ArrayList<String> removalslist = new ArrayList<String>();
        Utils.writeObject(removals, removalslist);
        Utils.writeObject(additions, new HashMap<>());
        List<String> ignorefiles1 = Utils.plainFilenamesIn(CWD);
        ArrayList<String> ignoredfiles = new ArrayList<>();
        for (String i: ignorefiles1){
            File s = Utils.join(CWD, i);
            if (!s.isDirectory()){
                ignoredfiles.add(i);
            }
        }
        Utils.writeObject(ignorefiles, ignoredfiles);
    }

    static void wipestaging(){
        HashMap<String, String> additionfiles = new HashMap<>();
        ArrayList<String> removalfiles = new ArrayList<String>();
        Utils.writeObject(additions, additionfiles);
        Utils.writeObject(removals, removalfiles);
    }

}
