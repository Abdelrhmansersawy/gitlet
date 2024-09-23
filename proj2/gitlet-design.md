# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Blob

1. String path --- contains the directory path of the tracked file

### Commit

1. Parent commit string --- stores SHA-1 of the parent commit (in case of root, the partent will null)

2. Timestamp --- store the created commit time

3. Blobs --- contain list of current Blobs

4. Changes log --- store the change that happen into working directory.

5. Branch Name --- Store the branch where this is commit is created

### Branches

1. Name --- the name of branche (eg. master, feature-branch)

2. head --- point the SHA-1 of the head commit

### Repository

1. Current working branch ---  Store the name of current working branch

2. staging-area --- store add files 

3. List of branches --- vector contain all names of existing branches 

### Main

1. List of commit --- stores list of SHA-1 of all created commit

2. `void init()`: initial gitlet for the current working directory

3. `void add(string *arg[])`: Add the agrument files to staging area.

4. `void commit (string *arg[])`: Create a commit with the chages of staging area with a messege given into `arg[0]`.

5. `void rm (string *arg[])`: Unstage the file if it is currently staged for addition

6. ## Algorithms

## Persistence
