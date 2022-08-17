package gitlet;
import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
// TODO: any imports you need here



/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {



    //when the commit was made
    public Date timestamp;
    //the parent of the commit
    public String parent;
    public String UID;
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    public String message;
    public HashMap<String, String> files;
    public Commit(String message, String parent, HashMap<String, String> values){
        this.parent = parent;
        this.message = message;

        if(this.parent == null){
            this.timestamp = new Date(0);
        }else{
            this.timestamp = new Date();
        }
        this.files = values;
    }

    public String getMessage(){
        return this.message;
    }
    public Date getTimestamp(){
        return this.timestamp;
    }
    public String getParent(){
        return this.parent;
    }
    /* TODO: fill in the rest of this class. */
    // hashmap iteration from https://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
    public void saveCommit(){


        File n = new File("./.gitlet/commits/placeholder");
        Utils.writeObject(n, this);
        UID = Utils.sha1(Utils.readContentsAsString(n));
        File d = new File("./.gitlet/commits/" + UID);
        Utils.writeObject(d, this);
        n.delete();
    }
}
