# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Blob

1. `Blob(absolutePath)` --- Initialize a blob with a content of file with path `absolutePath`
2. `String getFileName()` --- get the absolute path of the blob
3. `String getContent()` --- get the file content of the blob
4. `String getBlobName()` --- Return the blob name
5. `String getSHA()` ---  Return the SHA1 of the  Blob
6. `void write()` --- Write a Blob
7. `Boolean equals(Blob other)` --- compare two blob if they are equal or not

### Commit

1. Parent commit string --- stores SHA-1 of the parent commit (in case of root, the partent will null)

2. Timestamp --- store the created commit time

3. Blobs --- contain list of current Blobs

4. Changes log --- store the change that happen into working directory.

5. Branch Name --- Store the branch where this is commit is created

### FileSystem

#### About: Contain all function specified dealing with files

1. `String getCWD()` --- return the current working directory

2. `String getAbsolutePath(relativePath)` --- convert from relative path to absolute path

3. `void SerializingObject(fileName , T object)` --- serializing object with a file name `fileName`

4. `T DeserializingObject(fileName , objectType)` --- deserializing object with a file name `fileName` and return it as object

5. `String readFile(string absolutePath)` --- read the content of file

6. `Boolean initGit()` --- Initialize Git version-control system

7. `Boolean checkGit()` --- check if the Git version-control sytsem is intialized or not

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
