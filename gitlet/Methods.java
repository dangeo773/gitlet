package gitlet;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;

class Methods {
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    public static final File commits = Utils.join(GITLET_DIR, "commits");
    public static final File staging_area = Utils.join(GITLET_DIR, "staging_area");
    public static final File HEAD = Utils.join(GITLET_DIR, "HEAD");
    public static final File branches = Utils.join(GITLET_DIR, "branches");
    public static final File Blobs = Utils.join(GITLET_DIR, "blobs");
    public static final File additions = Utils.join(staging_area, "additions");
    public static final File removals = Utils.join(staging_area, "removals");
    public static final File master = Utils.join(branches, "master");
    public static final File ignorefiles = Utils.join(GITLET_DIR, "ignorefiles");
    public static File headaddress;
    public static File headcommit;
    public static final SimpleDateFormat change = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ");



    static void init() {
        //creates .gitlet folder which contains
        // commits folder
        // staging area folder which contains
        // additions
        //removals
        //HEAD file which is serialized but contains pointer to current head commit
        // branches folder which contains
        // a file for each branch that points to a commit file and represents where it is
        // Blobs folder which contains actual text files w information,
        File cwd = new File(System.getProperty("user.dir"));

        File gitlet = Utils.join(cwd, ".gitlet");
        if (!gitlet.exists()){
            Repository.createRepository();


        }else{
            System.out.println("A Gitlet version-control system already exists in the current directory");
        }
    }
    static void commit(String message) {
        //read and find the head commit object and the staging area - headcommit, additions, removals
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        Commit commitcontents = Utils.readObject(headcommit, Commit.class);
        HashMap<String, String> newfiles = new HashMap<>();
        HashMap<String, String> addstage = Utils.readObject(additions, HashMap.class);
        ArrayList<String> removestage = Utils.readObject(removals, ArrayList.class);
        if (message.equals("")){
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        if ((removestage.size() == 0) && (addstage.size() == 0)){
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if(commitcontents.files != null){
            newfiles.putAll(commitcontents.files);
        }
        if(Utils.readObject(additions, HashMap.class) != null){
            newfiles.putAll(Utils.readObject(additions, HashMap.class));
        }
        if(Utils.readObject(removals, ArrayList.class) != null){
            ArrayList<String> removelist = Utils.readObject(removals, ArrayList.class);
            for (String d : removelist){
             newfiles.remove(d);
            }
        }
        Commit newcommit = new Commit(message, Utils.readContentsAsString(headaddress), newfiles);
        newcommit.saveCommit();
        Repository.wipestaging();
        Utils.writeContents(headaddress, newcommit.UID);
        //Head commit object is a serialized file which contains a sha-1 id that points to the commit(with sha-1 id as name) that is the head
        //clone head commit and create new file in commits folder that
        //will contain a hash map with data including timestamp message and sha-1 id of parent and blobs tracked
        //modify timestamp and message according to user input
        //use staging area to modify files tracked by commit and save new blobs in folder w files
        // write back any new object made or any modified object
    }

    static void add(String filename) {
        File cwd = new File(System.getProperty("user.dir"));
        File f = Utils.join(CWD, filename);
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        if (!f.exists()) {
            System.out.println("File does not exist");
        } else {
            String contents = Utils.readContentsAsString(f);
            String name = Utils.sha1(contents);
            Commit currentcommitcontents = Utils.readObject(headcommit, Commit.class);
            if(currentcommitcontents.files != null){
                if (currentcommitcontents.files.containsKey(filename)){
                String address = currentcommitcontents.files.get(filename);
                File k = Utils.join(Blobs, address);

                if (contents.equals(Utils.readContentsAsString(k))){
                    HashMap<String, String> currentadditions = Utils.readObject(additions, HashMap.class);
                    ArrayList<String> currentremovals = Utils.readObject(removals, ArrayList.class);
                    if (currentadditions.containsKey(filename)){
                        currentadditions.remove(filename);
                        Utils.writeObject(additions, currentadditions);
                    }
                    if (currentremovals.contains(filename)){
                        System.out.println("removing");
                        currentremovals.remove(filename);
                        Utils.writeObject(removals, currentremovals);
                    }
                    System.exit(0);
                }}
                
                if(name == currentcommitcontents.files.get(filename)) {
                    HashMap<String, String> currentadditions = Utils.readObject(additions, HashMap.class);
                    currentadditions.remove(filename);
                    ArrayList<String> currentremovals = Utils.readObject(removals, ArrayList.class);
                    currentremovals.remove(filename);

                }else{
                    File copy = Utils.join(cwd, ".gitlet/blobs/" + name);
                    Utils.writeContents(copy, contents);
                    HashMap<String, String> currentadditions = Utils.readObject(additions, HashMap.class);
                    currentadditions.put(filename, name);
                    Utils.writeObject(additions, currentadditions);}
            }else{
                File copy = Utils.join(cwd, ".gitlet/blobs/" + name);
                Utils.writeContents(copy, contents);
                HashMap<String, String> currentadditions = Utils.readObject(additions, HashMap.class);
                currentadditions.put(filename, name);
                Utils.writeObject(additions, currentadditions);}
        }
    }
    static void log(){
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        Commit currentcommit = Utils.readObject(headcommit, Commit.class);
        while(!(currentcommit.parent == null)) {
            System.out.println("===");
            System.out.println("commit " + currentcommit.UID);
            System.out.println("Date: " + change.format(currentcommit.timestamp));
            System.out.println(currentcommit.message);
            System.out.println();
            File newcommitheader = Utils.join(commits, currentcommit.parent);
            currentcommit = Utils.readObject(newcommitheader, Commit.class);
        }

        System.out.println("===");
        System.out.println("commit " + currentcommit.UID);
        System.out.println("Date: " + change.format(currentcommit.timestamp));
        System.out.println(currentcommit.message);
        System.out.println();


    }
    static void checkout(String dash, String filename) {

        if(!("--".equals(dash))){
            System.out.println("Incorrect operands");
            System.exit(0);
        }
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        File addedfile = Utils.join(CWD, filename);
        Commit commitobjects = Utils.readObject(headcommit, Commit.class);
        if(commitobjects.files != null){
           if(commitobjects.files.get(filename) != null){
                File copy = Utils.join(Blobs, commitobjects.files.get(filename));
                Utils.writeContents(addedfile, Utils.readContentsAsString(copy));
           }else{
               System.out.println("File does not exist in that commit.");
           }
        }else{
            System.out.println("File does not exist in that commit.");
        }




    }
    static void checkoutcommit(String commitid, String filename, String dashdash) {
        if (!("--".equals(dashdash))){
            System.out.println("Incorrect operands");
            System.exit(0);
        }
        String id = getcommit(commitid);
        File commitobject = Utils.join(commits, id);
        if (commitobject.exists()) {
            File addedfile = Utils.join(CWD, filename);
            Commit commitinfo = Utils.readObject(commitobject, Commit.class);
            if (commitinfo.files != null) {
                if (commitinfo.files.get(filename) != null) {
                    File copy = Utils.join(Blobs, commitinfo.files.get(filename));
                    Utils.writeContents(addedfile, Utils.readContentsAsString(copy));
                } else {
                    System.out.println("File does not exist in that commit.");
                }
            } else {
                System.out.println("File does not exist in that commit.");
            }
        }else{
            System.out.println("No commit with that id exists.");
        }
    }
    static void checkoutbranch(String branch){

        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        File branchaddress = Utils.join(branches, branch);

        if (branchaddress.getAbsolutePath().equals(headaddress.getAbsolutePath())){
            System.out.println("No need to checkout the current branch.");
        }
        if (!branchaddress.exists()){
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        String[] untracked = returnuntracked();
        if (untracked.length > 0){
            System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
            System.exit(0);
        }

        File branchcommit = Utils.join(commits, Utils.readContentsAsString(branchaddress));
        List<String> deletions = new ArrayList<>();
        Commit branchcommitfiles = Utils.readObject(branchcommit, Commit.class);
        Commit currentcommitfiles = Utils.readObject(headcommit, Commit.class);

        if (branchcommitfiles.files != null) {
            if (currentcommitfiles.files != null) {
                for (String name : currentcommitfiles.files.keySet()) {
                    if (!branchcommitfiles.files.containsKey(name)) {
                        deletions.add(name);
                    }
                }
            }else{
                deletions.addAll(branchcommitfiles.files.keySet());
            }
        }else{
            if (currentcommitfiles.files != null){
                deletions.addAll(currentcommitfiles.files.keySet());
            }

        }
        if (branchcommitfiles.files != null){
            for(Map.Entry mapElement: branchcommitfiles.files.entrySet()){
            File cwdfile = Utils.join(CWD, (String) mapElement.getKey());
            File blobfile = Utils.join(Blobs, (String) mapElement.getValue());
            Utils.writeContents(cwdfile, Utils.readContentsAsString(blobfile));
        }
        }

        for(String names : deletions){
            File f = Utils.join(CWD, names);
            f.delete();
        }
        if (headaddress.getAbsolutePath().equals(branchaddress.getAbsolutePath())){
            Repository.wipestaging();
        }
        Utils.writeContents(HEAD, "branches/" + branch);
    }
    static void rm(String filename){
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));

        boolean inadditions = false;
        boolean tracked = false;
        HashMap<String, String> currentadditions = Utils.readObject(Repository.getAdditions(), HashMap.class);
        Commit currentcommit = Utils.readObject(headcommit, Commit.class);
        if((!currentadditions.containsKey(filename))){
            if((currentcommit.files != null)){
                if (!(currentcommit.files.containsKey(filename))){
                    System.out.println("No reason to remove the file.");
                }
            }else{
                System.out.println("No reason to remove the file.");
            }

        }
        File f = Utils.join(Repository.getcwd(), filename);
        if (currentadditions.containsKey(filename)){
            currentadditions.remove(filename);
            Utils.writeObject(additions, currentadditions);
            inadditions = true;
        }

        if (currentcommit.files != null){
            if (currentcommit.files.containsKey(filename)){
            ArrayList<String> currentremovals = Utils.readObject(removals, ArrayList.class);
            currentremovals.add(filename);
            Utils.writeObject(removals, currentremovals);
            if(f.exists()){

                Utils.restrictedDelete(f);
            }
            tracked = true;
            }
        }
    }
    static void globallog(){
            List<String> commitnames = Utils.plainFilenamesIn(commits);
            for(int i = 0; i < commitnames.size(); i++){

                Commit currentcommit = Utils.readObject(Utils.join(commits, commitnames.get(i)), Commit.class);
                System.out.println("===");
                System.out.println("commit " + currentcommit.UID);
                System.out.println("Date: " + change.format(currentcommit.timestamp));
                System.out.println(currentcommit.message);
                System.out.println();
            }
        }

    static void find(String message){
        List<String> commitnames = Utils.plainFilenamesIn(commits);
        List<String> commitids = new ArrayList<>();

        for(int i = 0; i < commitnames.size(); i++){

            Commit currentcommit = Utils.readObject(Utils.join(commits, commitnames.get(i)), Commit.class);
            if (currentcommit.message.equals(message)){

                commitids.add(currentcommit.UID);
            }
        }
        if (commitids.size() != 0) {
            for (int i = 0; i < commitids.size(); i++) {
                System.out.println(commitids.get(i));
            }
        }else{
            System.out.println("Found no commit with this message.");
        }
    }

    static void branch(String message){
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        File branchfile = Utils.join(branches, message);
        if (branchfile.exists()){
            System.out.println("A branch with that name already exists.");
        }else{

            Utils.writeContents(branchfile, Utils.readContentsAsString(headaddress));
        }
    }
    static void rmbranch(String message){
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        File branchfile = Utils.join(branches, message);
        if ((branchfile.exists()) && !(branchfile.getAbsolutePath().equals(headaddress.getAbsolutePath()))){

            branchfile.delete();
            return;
        }
        if((branchfile.exists()) && (branchfile.getAbsolutePath().equals(headaddress.getAbsolutePath()))){
            System.out.println("Cannot remove the current branch.");
        }else{
            System.out.println("A branch with that name does not exist.");
        }
    }
    static void status(){
        if (!GITLET_DIR.exists()){
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        String[] brancheslist = returnbranches();
        String[] additions = returnadditions();
        String[] removalnames = returnremovals();
        String[] modifiednames = returnmodified();
        String[] untracked = returnuntracked();

        System.out.println("=== Branches ===");
        printallinlist(brancheslist);
        System.out.println("");
        System.out.println("=== Staged Files ===");
        printallinlist(additions);
        System.out.println("");
        System.out.println("=== Removed Files ===");
        printallinlist(removalnames);
        System.out.println("");
        System.out.println("=== Modifications Not Staged For Commit ===");
        printallinlist(modifiednames);
        System.out.println("");
        System.out.println("=== Untracked Files ===");
        printallinlist(untracked);
        System.out.println("");



    }
    //returns a list of branches with the headcommit indicated by a *
    static String[] returnbranches(){
        List<String> branchnames = Utils.plainFilenamesIn(branches);
        String headbranch = Utils.readContentsAsString(HEAD).substring(9);
        int d = branchnames.lastIndexOf(headbranch);
        branchnames.set(d, "*" + headbranch);
        String[] newbranchnames = new String[branchnames.size()];
        branchnames.toArray(newbranchnames);
        Arrays.sort(newbranchnames);
        return newbranchnames;
    }

    static String[] returnadditions(){
        Set addtionnames = (Utils.readObject(Utils.join(staging_area, "additions"), HashMap.class)).keySet();
        List<String> additions = new ArrayList<>();
        additions.addAll(addtionnames);
        String[] newadditions = new String[additions.size()];
        additions.toArray(newadditions);
        Arrays.sort(newadditions);
        return newadditions;
    }

    static String[] returnremovals(){
        List<String> removalnames = (Utils.readObject(Utils.join(staging_area, "removals"), ArrayList.class));
        String[] newremovals = new String[removalnames.size()];
        removalnames.toArray(newremovals);
        Arrays.sort(newremovals);
        return newremovals;
    }

    static String[] returnmodified(){
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        List<String> modified = new ArrayList<>();
        HashMap<String, String> additionhash = (Utils.readObject(Utils.join(staging_area, "additions"), HashMap.class));
        List<String> removallist = (Utils.readObject(Utils.join(staging_area, "removals"), ArrayList.class));
        Set<String> additionnames = additionhash.keySet();
        Commit currentcommit = Utils.readObject(Utils.join(commits, Utils.readContentsAsString(headaddress)), Commit.class);
        if (currentcommit.files!=null){
            Set<String> trackedfiles = currentcommit.files.keySet();
        for(String d: trackedfiles){
            File f = Utils.join(CWD, d);
            File blobfile = Utils.join(Blobs, currentcommit.files.get(d));
            if((!removallist.contains(d)) && (!f.exists())){
                modified.add(d + " (removed");
                continue;
            }
            if (f.exists()){
            if((!Utils.readContentsAsString(f).equals(Utils.readContentsAsString(blobfile))) && (!additionnames.contains(d))){
                modified.add(d + " (modified)");
            }}

            }
        }
        for (String d: additionnames){
            File blobfile = Utils.join(Blobs,additionhash.get(d));
            File f = Utils.join(CWD, d);
            if (!(Utils.readContentsAsString(blobfile).equals(Utils.readContentsAsString(f)))){
                modified.add(d + " (modified)");
            }else if(!f.exists()){
                modified.add(d + " (removed)");
            }
        }
        String[] newmodified = new String[modified.size()];
        modified.toArray(newmodified);
        Arrays.sort(newmodified);
        return newmodified;
    }
    static String[] returnuntracked(){
        List<String> untracked = new ArrayList<>();
        List<String> allfiles = Utils.plainFilenamesIn(CWD);
        Set additionkeys = (Utils.readObject(Utils.join(staging_area, "additions"), HashMap.class)).keySet();
        Commit currentcommit = Utils.readObject(Utils.join(commits, Utils.readContentsAsString(headaddress)), Commit.class);

        List<String> removallist = (Utils.readObject(Utils.join(staging_area, "removals"), ArrayList.class));
        for(String g : allfiles){
            if (currentcommit.files!=null) {
                Set<String> trackedfiles = currentcommit.files.keySet();
                if (!additionkeys.contains(g) && (!trackedfiles.contains(g))) {
                    untracked.add(g);
                } else if (removallist.contains(g)) {
                    untracked.add(g);
                }
            }else{
                if (!additionkeys.contains(g)) {
                    untracked.add(g);
                } else if (removallist.contains(g)) {
                    untracked.add(g);
            }
        }}

        ArrayList<String> defaultfiles = Utils.readObject(ignorefiles, ArrayList.class);
        for(String i: defaultfiles){
            if (untracked.contains(i)){
                untracked.remove(untracked.indexOf(i));
            }
        }
        String[] newuntracked = new String[untracked.size()];
        untracked.toArray(newuntracked);
        Arrays.sort(newuntracked);
        return newuntracked;
    }
    static void printallinlist(String[] list){
        for (String i : list){
            System.out.println(i);
        }

    }
    static void reset(String commit){
        String id = getcommit(commit);
        File n = Utils.join(commits, id);
        if (!n.exists()){
            System.out.println("No commit with that id exists.");
            return;
        }
        headaddress = Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD));
        headcommit = Utils.join(commits, Utils.readContentsAsString(headaddress));
        Commit currentcommit = Utils.readObject(headcommit, Commit.class);
        Commit resetcommit = Utils.readObject(Utils.join(commits, id), Commit.class);
        if (currentcommit.files == null){
            Set<String> files = resetcommit.files.keySet();
            for (String i : files) {
                checkoutcommit(id, i, "--");
            }
        }else {
            Set<String> trackedfiles = currentcommit.files.keySet();
            if (resetcommit.files != null) {
                Set<String> files = resetcommit.files.keySet();
                for (String i : files) {
                    File g = Utils.join(CWD, i);
                    if ((!trackedfiles.contains(i)) && g.exists() && !g.isDirectory()) {
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                    }
                }
                for (String i : files) {
                    checkoutcommit(id, i, "--");
                }
                for (String i : trackedfiles) {
                    if (!files.contains(i)) {
                        File k = Utils.join(CWD, i);
                        k.delete();
                    }
                }
            } else {
                for (String i : trackedfiles) {

                    File f = Utils.join(CWD, i);
                    f.delete();
                }
            }
        }
        Utils.writeContents(Utils.join(GITLET_DIR, Utils.readContentsAsString(HEAD)), id);
        Repository.wipestaging();
    }

    static String getcommit(String commitid){
        List<String> commitnames = Utils.plainFilenamesIn(commits);
        for (String i : commitnames){
            if (i.startsWith(commitid)){
                return i;
            }
        }
        return "false";
    }
}



