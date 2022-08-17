# Gitlet Design Document

**Name**: Daniel George

## Classes and Data Structures

### Commit
Sets up most persistence in terms of Commits
#### Instance Variables

* Message - stores the message of the commit
* Timestamp - stores the timestamp of the commit
* Parent - stores the parent of the commit
* Files - has the files that are tracked as a hashmap with the first value as the name of the file and the second as the UID


#### Methods
* saveCommit() - Creates a new file as a placeholder and writes a commit object to the file, gets the sha value of this file, and creates a new file named that UID and deletes the placeholder

### Repository

#### Instance Variables

1. CWD - cwd
   2. GITLET_DIR - .gitlet folder
   3. commits - folder containing commits
   4. staging_area - folder containing addition and removal files
   5. HEAD - pointer to branch which contains pointer to head commt
   6. branches - folder containing branches
   7. Blobs - folder containing actual files
   8. additions - file with all addition files stored as hashmap objects containing filenames as strings associated with UIDS as strings
   9. removals - file containing
   10. master - first branch
   11. Headaddress - file address of the head commit
   12. Headcommit - file object of the head commit


#### Methods
* createRepository()
   * creates all of the directories and files needed, clears the staging area using wipestaging(), creates a commit class with message initial commit and no parent and saves it and writes the UID of this commit to the "master" file
   
* wipestaging()
   * clears additions and removals
### Methods (a class containing methods)
#### This is where most of the code exists, every method that is required for the project is here

#### Fields

* File objects
   1. CWD - cwd
   2. GITLET_DIR - .gitlet folder
   3. commits - folder containing commits
   4. staging_area - folder containing addition and removal files
   5. HEAD - pointer to branch which contains pointer to head commt
   6. branches - folder containing branches
   7. Blobs - folder containing actual files 
   8. additions - file with all addition files stored as hashmap objects containing filenames as strings associated with UIDS as strings 
   9. removals - file containing 
   10. master - first branch
   11. Headaddress - file address of the head commit
   12. Headcommit - file object of the head commit
  


#### Methods


#### init
Everything below is ran through Repository.createRepository()
1. creates .gitlet folder which contains
    2. commits folder
3. staging area folder which contains
    4. additions
    5. removals
6. HEAD file which is serialized but contains pointer to current head commit
7. branches folder which contains
    8. a file for each branch that points to a commit file and represents where it is
9. Blobs folder which contains actual text files w information,
#### add
- creates cwd, headaddress, & headcommit in the environment and a file object pointing to the tracked file
- checks if the file exists, otherwise prints error
- saves the contents, name, and current commit
- write the file down in additions
#### log
- iterates down the branch and prints needed details
#### checkout
* checks the arguments
* if it starts with "--"
   * creates headaddress, headcommit, checks if the head commit has tracked files and if the file is in the head commit, writes the contents from the corressponding file in blobs onto the file in the cwd, creating it if it doesn't exist
* if it starts with a string and has "--" afterwards
   * undergoes same process, except using the commitid to locate the specific commit instead of the head commit
#### commit
- creates headaddress & headcommit in the environment 
- makes a Commit object from the headcommit 
- adds all filenames and corresponding UIDs from the commit and additions to a hashmap and sets that as the files variable for a new Commit with the head commit as the parent 
- wipes stage area
- saves commit
- writes new head on "master"
## Algorithms

## Persistence

1. CWD - cwd
   2. GITLET_DIR - .gitlet folder
   3. commits - folder containing commits
   4. staging_area - folder containing addition and removal files
      5. additions - file with all addition files stored as hashmap objects containing filenames as strings associated with UIDS as strings
      6.  removals - file containing
   7. HEAD - pointer to branch which contains pointer to head commit
   8. branches - folder containing branches
      10. master - first branch
   9. Blobs - folder containing actual files
   
Most persistence is in the Commits class and the Repository class and consists of writing objects to files and reading the objects off later.