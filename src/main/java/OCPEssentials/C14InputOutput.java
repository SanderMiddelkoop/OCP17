package OCPEssentials;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.IntStream;

public class C14InputOutput {
    public static void main(String... args) {
        new A_FilesAndDirectories().filesAndDirectories();
        new B_OperatingOnFilesAndPaths().operatingOnFilesAndPaths();
        new C_IOStreams().ioStreams();
        new D_SerializingData().serializingData();
        new E_InteractingWithUsers().InteractingWithUsers();
        new F_AdvancedApis().workingWithAdvancedApis();
    }
}

class A_FilesAndDirectories {
    public void filesAndDirectories() {
        //Files are organized in hierarchies by using directories
        //Files and directories are often operated on the same by Java
        //The root directory is the topmost directory in the filesystem, in windows this is a drive letter: C:\\
        //A path in the representation of the location of a file or directory within the file system: C:\\user\directory
        //You can get the file separator of your system by doing:
        System.out.println(System.getProperty("file.separator"));

        //The absolute path of a directory/file is the full path from the root to the location of the file
        //                                 e.g. C:\\user\\documents\\images\house.png
        //The relative path of a directory/file is the relative path is the path from the current working directory to the file:
        //                                 assuming the work directory is C:\\user\\documents -> images\\house.png

        //Paths starting with a forward slash or a drive are absolute, others are relative
        //Path symbols are:
        //      .   refers to the current directory
        //      ..  refers to the parent of the current directory

        //A symbolic link is a special file within a directory, that serves as a reference to another file/directory
        creatingFilesAndPaths();
    }

    private void creatingFilesAndPaths() {
        //Legacy IO uses the java.io.File class to represent files
        //NIO.2 uses the java.nio.file.Path interface
        /** File **/
        //Creating a File can be done using 3 different constructors:
        File file1 = new File("C:\\Users\\public"); //Constructor that takes a String that represents the path
        File file2 = new File("C:\\Users", "public"); //Constructor that takes a parent and child path as String
        File file3 = new File(file1, "public"); //Constructor that takes a parent as File and child as String

        //It is possible to pass null as the first argument here
        System.out.println(new File((String) null, "public").getAbsoluteFile()); //:: currentdir\public
        System.out.println(new File((File) null, "public").getAbsoluteFile()); //:: currentdir\public
        //You can check whether a file exists:
        System.out.println(file1.exists());

        /** Path **/
        //You can create paths using factory methods Paths.get or Path.of:
        Path path1 = Path.of("C:\\users", "public"); //It takes a varargs of String to construct the complete path
        Path path2 = Paths.get("C:\\users", "public"); //It takes a varargs of String to construct the complete path
        System.out.println(path1);
        System.out.println(path2);

        //You can create a Path using filesystems:
        Path path3 = FileSystems.getDefault().getPath("C:\\users", "public");
        //Check whether file exists:
        System.out.println(Files.exists(path1));

        /** Switching **/
        //Switch between file and path by doing:
        Path path4 = file3.toPath();
        File file4 = path3.toFile();

    }
}

class B_OperatingOnFilesAndPaths {
    File file1 = new File("C:\\users\\public\\test.png");
    Path path1 = Paths.get("C:\\", "users", "public", "test.png");

    public void operatingOnFilesAndPaths() {
        /** Shared functionality **/
        //Get the name of a File or Directory:
        System.out.println(file1.getName()); // :: public
        System.out.println(path1.getFileName()); // :: public

        //Get the parent directory or null
        System.out.println(file1.getParent()); // :: C:\\users
        System.out.println(path1.getParent()); // :: C:\\users

        //Check if the path is absolute
        System.out.println(file1.isAbsolute()); // :: true
        System.out.println(path1.isAbsolute()); // :: true

        //Check if file exists:
        System.out.println(file1.exists());  // :: false
        System.out.println(Files.exists(path1)); // :: false

        //Delete file
        System.out.println(file1.delete()); // :: false -- returns true if delete was succesful, file doesn't exist here
        try {
            Files.delete(path1); // exception must be handled, throws if can't delete
        } catch (IOException e) {
            System.out.println("Couldn't find or delete file"); // is printed
        }
        try {
            System.out.println(Files.deleteIfExists(path1)); // :: false -- returns false if file doesn't exist, true if delete succesful
        } catch (
                IOException e) {                            //exception must be handled, throws if file found but can't delete
            System.out.println("Found file but couldn't delete"); // not printed in this case
        }

        //Get the absolute path of the file
        System.out.println(file1.getAbsolutePath()); // :: C:\\users\\public\\test.png
        System.out.println(path1.toAbsolutePath());  // :: C:\\users\\public\\test.png

        //Check if it is a directory:
        System.out.println(file1.isDirectory());  // :: false
        System.out.println(Files.isDirectory(path1));  // :: false

        //Check if it is a file:
        System.out.println(file1.isFile());  // :: false
        System.out.println(Files.isRegularFile(path1));  // :: false

        //Check last modified date
        System.out.println(file1.lastModified());  // :: 0
        try {
            System.out.println(Files.getLastModifiedTime(path1));  // throws exception, must be handled
        } catch (IOException e) {
            System.out.println("Couldn't find or access file"); //Prints this
        }

        //Check length of file
        System.out.println(file1.length());  // :: 0
        try {
            System.out.println(Files.size(path1));  // throws exception, must be handled
        } catch (IOException e) {
            System.out.println("Couldn't find or access file"); //Prints this
        }

        //get a list of File[] or stream of Path objects, of all possible children in the directory
        System.out.println(file1.listFiles());  // :: null
        try {
            System.out.println(Files.list(path1));  // throws exception, must be handled
        } catch (IOException e) {
            System.out.println("Couldn't find or access file"); //Prints this
        }

        //Paths are immutable objects, you should always store the result of an operation on a Path in an object
        nio2Operations();

        /** Moving, creaing and deleting **/
        //Make a directory using io File:
        //    NOTE: A directory will always be created, not a file, even if you call it test.png or something
        System.out.println(file1.mkdir()); //:: true, outputs whether directory could be created
        System.out.println(file1.mkdirs()); //:: false, directory already exists, mkdirs is equivalent to createDirectories
        //                                                                        but without checked exception

        //Make a directory using nio Files createDirectory()
        //    NOTE: Throws checked exception, is thrown when directory already exists
        //                                    is thrown when path leading up to directory doesn't exist
        try {
            Files.createDirectory(path1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Directory couldn't be created!");
        }
        //Make a directory using nio Files createDirectories
        // creates all parent directories if they do not exist
        // NOTE: Has checked exception, but never throws Exception when directories already exist
        //       Only throws when directory cannot be created for other reasons
        try {
            Files.createDirectories(path1); //Line is executed but does nothing.
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Directory couldn't be created!");
        }

        //You can copy a file by using Files.copy(source, target)
        //  This also has a checked exception if mentioned parent paths do not exist, or if the target already exists
        //  The copy is shallow, files and subdirectory within the source are NOT COPIED to the target
        try {
            Files.copy(path1, Paths.get(path1.toAbsolutePath().toString(), "..", "test2.png")); //Copies folder test.png to test2.png
        } catch (IOException e) {
            System.out.println("Copy couldn't be made!");
        }

        //Using the REPLACE_EXISTING option will make it so an exception isn't thrown when the target already exists:
        try {
            Files.copy(path1,
                    Paths.get(path1.toAbsolutePath().toString(), "..", "test2.png"),
                    StandardCopyOption.REPLACE_EXISTING); //Copies folder test.png to test2.png
            System.out.println("REPLACED EXISTING test2.png!");
        } catch (IOException e) {
            System.out.println("Copy couldn't be made!");
        }

        //You can copy from an inputstream to a path
        //    Files.copy(InputStream in, Path target)
        //Or the other way around:
        //    Files.copy(Path source, OutputStream out)
        //Both methods throw a checked exception

        //move() is similar to copy() except, it WILL move ALL of the contents of a directory,
        //and it will delete the source. It will throw an exception if the target already exists and the REPLACE_EXISTING
        //flag is not given.
        //will also throw an exception of one of the parent directories in the target do not exist
        //You can give a flag StandardCopyOption.ATOMIC_MOVE to make the move concurrent
        try {
            Files.move(path1, Paths.get(path1.toAbsolutePath().toString(), "..", "test3.png"));
            //Removes C:\\users\\public\\test.png and moves it to C:\\users\\public\\test3.png
        } catch (IOException e) {
            System.out.println("Couldn't move directory");
        }

        //isSameFile(Path1, Path2) checks if 2 paths refer to the same file or directory
        //If the toString representation of the paths are equal, it always returns true
        //Otherwise it returns an exception if either path doesn't exist
        //It returns true if the paths, normalized and following symbolic links, point to the same location
        //Otherwise, it returns false
        try {
            System.out.println(Files.isSameFile(Paths.get("C:\\"),
                    Paths.get("C:\\"))); // :: true
            System.out.println(Files.isSameFile(Paths.get("C:\\", "Users", "public"),
                    Paths.get("C:\\", "Users", "..", "Users", "public"))); // :: true
        } catch (IOException e) {
            System.out.println("Couldn't compare files!");
        }

        //misMatch(Path1, Path2) does the same as as isSameFile, it returns -1 if files are the same or if the
        //                                                       arguments point to the same location
        //if files aren't the same, it returns the first element where the files differ IN TERMS OF CONTENT OF THE FILE!
        //It won't work if you use it with 2 different directories
        try {
            System.out.println(Files.mismatch(Paths.get("C:\\", "Users", "public", "test3.png"),
                    Paths.get("C:\\", "Users", "public", "test3.png"))); //-1
            System.out.println(Files.mismatch(Paths.get("C:\\", "Users", "public", "abc.txt"),
                    Paths.get("C:\\", "Users", "public", "abcdef.txt"))); //-1
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Couldn't mismatch files");
        }

    }

    private void nio2Operations() {
        //toString returns a String representation of the Path object:
        System.out.println(path1); // :: C:\\users\\public\\test.png

        //getNameCount can be used to get the number of elements the path is made up of:
        //                                NOTE how the ROOT doesn't count.
        System.out.println(path1.getNameCount()); // :: 3 (0 = users, 1 = public, 2 = test.png)
        //GetName(index i) gets the name of the element at index i:
        System.out.println(path1.getName(2)); // :: test.png
        //getName(i) throws an IllegalArgumentException when the index doesn't exist

        //subPath(x can be used to create the full Path starting from element x to y (exclusive), as related to getName()
        System.out.println(path1.subpath(0, 3)); // :: users\\public\\test.png -- DOES NOT INCLUDE ROOT
        System.out.println(path1.subpath(1, 3)); // :: public\\test.png
        //As with getName, referring to indexes that do not exist, or inputting an invalid range will throw an IllegalArgumentException
        //path1.subpath(0,4);path1.subpath(2,2); path1.subpath(2,1); //All wrong

        //getRoot() returns the root element of the file, NULL if it is a relative Path
        System.out.println(path1.getRoot()); // :: C:\
        System.out.println(Paths.get("public").getRoot()); // :: null
        //getParent() returns the parent Path of the file, NULL if operated or root or top of relative path:
        System.out.println(path1.getParent());
        System.out.println(Paths.get("public").getParent()); // :: null
        System.out.println(Paths.get("C:\\public").getParent()); // :: C:\
        System.out.println(Paths.get("C:\\").getParent()); // :: null
        //getParent() will consider .. and . as valid parents:
        System.out.println(Paths.get("..", "public").getParent()); // :: ..

        //resolve(Path path2)
        //resolve(String path2) will try to create a full path where path2 is the child of path1:
        System.out.println(Paths.get("C:\\", "users").resolve("public\\test.png")); // :: C:\\users\\public\\test.png

        //If the argument passed to resolve is an absolute path, the result will simply be the argument that was passed:
        System.out.println(Paths.get("C:\\", "users").resolve(Paths.get("C:\\", "test"))); // :: C:\\test

        //relativize(Path path2) will try to get the relative path to get from path 1 to path 2
        System.out.println(path1.relativize(Paths.get("C:\\house.png"))); // :: ..\\..\\..\\house.png
        //It only works when used on 2 relative, or 2 absolute paths that have the same root drive
        //System.out.println(Paths.get("D:\\").relativize(Paths.get("C:\\house.png"))); //Different root error
        //System.out.println(Paths.get("D:\\").relativize(Paths.get("house.png"))); //Different type of path error

        //normalize() removes redundant . and .. operators
        System.out.println(Paths.get("C:\\", ".", "test1", "test2", "..", "image.png")); // :: C:\\.\\test1\\test2\\..\\image.png
        System.out.println(Paths.get("C:\\", ".", "test1", "test2", "..", "image.png").normalize()); // :: C:\\test1\\image.png
        //NOTE it does not necessarily remove all of those operators, just the redundant ones:
        System.out.println(Paths.get("..", "image.png").normalize()); // :: ..\\image.png -- .. not redundant
        System.out.println(Paths.get(".", "image.png").normalize()); // :: image.png -- . redundant

        //toRealPath will do multiple things:
        //  1) It will remove redundancies like normalize()
        //  2) It will join the path with the current working directory if relative, to create the absolute path
        //  3) It will throw an IOException if the path does not exist, exception must be handled
        //  4) It will follow symbolic links
        try {
            System.out.println(Paths.get("C:\\", "users", "public", "..", ".").toRealPath()); // :: C:\\Users
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}

class C_IOStreams {
    public void ioStreams() {
        //The contents of a file may be accessed using an I/O stream which is a list of data elements representing
        //the file presented sequentially.
        //It is usually read in chunks of x bytes, where a byte is a chunk of 8 bits (00 00 00 01)

        //There a two types of I/O streams
        //Byte streams: (base abstract classes: InputStream, OutputStream)
        //  read/write binary data, class names end in Input- or OutputStream, typically used for images or executables
        //Character streams (base abstract classes: Reader, Writer)
        //  read/write text data, class names end in Reader/Writer, typically used for text files

        //InputStreams/Readers often have a corresponding OutputStream/Writer class
        //                                       except PrintWriter and PrintStream.
        //A FileOutputStream writes data that can be read by a FileInputStream

        //Low-level streams connect directly with the source of the data
        //    example: A FileInputStream reads file data byte per byte
        //High-level streams are built on-top of (wrap) low-level streams. They take a low-level stream as part of their constructor,
        //    and provide a layer of utility to them.

        readingAndWritingFiles();
    }

    private void readingAndWritingFiles() {
        Path path1 = Paths.get("C:\\users\\public\\abcdef.txt");

        try {
            //You can read from an InputStream and output it to an OutputStream using the following method
            copyStream(null, null);
            //Reader/writer is similar
            copyReader(null, null);

            //High level streams give you convenience ways of achieving the same:
            copyTextfile(null, null);
            copyImagefile(null, null);

        } catch (IOException | RuntimeException e) {
            System.out.println("Cannot read stream");
        }

        //Nio 2 provides handy utilities for reading from files
        try {
            String fileAsString = Files.readString(path1);
            System.out.println(fileAsString); // :: abcdef -- Read the file as String
            //Write string to file location:
            Files.writeString(path1, fileAsString);

            Files.writeString(path1, fileAsString, StandardOpenOption.APPEND); //Append to the existing file if it already exists
            Files.writeString(path1, fileAsString, StandardOpenOption.CREATE); //Create new file if it does not exist, default setting
            Files.writeString(path1, fileAsString, StandardOpenOption.TRUNCATE_EXISTING); //Overwrite if file already exist,
            //                                                                            beware that this is the default setting
            Files.writeString(path1, fileAsString, StandardOpenOption.CREATE_NEW); //Force creating of new file, throws exception if file
            //                                                                     already exists

        } catch (FileAlreadyExistsException e) {
            System.out.println("File already exists, can't CREATE_NEW");
        } catch (IOException e) {
            System.out.println("Error");
        }

        //Similarly you can read the Bytes
        try {
            byte[] allBytes = Files.readAllBytes(path1);
            IntStream.range(0, allBytes.length).mapToObj(i -> allBytes[i]).forEach(b -> System.out.print(b + "-")); //Output all bytes
            List<String> allLines = Files.readAllLines(path1);
            System.out.println();
            allLines.forEach(System.out::println); //Output all lines

            //Get a stream of lines directly using Files.lines(Path path)
            Files.lines(path1).forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Files provides a way to directly create a bufferedReader/writer from a file:
        try (var reader = Files.newBufferedReader(path1);
             var writer = Files.newBufferedWriter(path1)) {

        } catch (IOException e) {
            System.out.println("Couldn't create buffered reader");
        }


    }

    private void copyStream(InputStream in, OutputStream out) throws IOException {
        int b; //The type is int because -1 represents the end of the stream, otherwise it holds 8 bits/a byte (0-255)
        while ((b = in.read()) != -1) { //Read the next byte and stop if it is the end of the stream (represented by -1)
            out.write(b); //write the byte to the outputstream
        }

        //Reading/writing one byte at a time isn't particularly efficient, you can read multiple bytes at a time by using
        //   public int read(byte[] buffer, int offset, int size)
        //   this reads size amount of bytes starting from position offset, and stores in the buffer array
        //      it returns the amount of bytes that were read, or -1 if the end was reached.
        int size = 1024;
        byte[] buffer = new byte[size];
        int lengthRead;
        while ((lengthRead = in.read(buffer, 0, size)) > 0) { //read 1024 bytes into the buffer
            out.write(buffer, 0, lengthRead); //write the buffer to the outputstream,
            //                                      the size is important because the buffer array still contains the
            //                                      elements from the previous read. If the lengthRead < 1024, which happens
            //                                      at the end of the inputStream, the elements of the previous read would
            //                                      be written to the outputstream without specifying the size
        }
    }

    private void copyReader(Reader in, Writer out) throws IOException {
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }

        int size = 1024;
        char[] buffer = new char[size]; //Only difference with copyStream, the buffered array is of chars
        int lengthRead;
        while ((lengthRead = in.read(buffer, 0, size)) > 0) {
            out.write(buffer, 0, lengthRead);
        }
    }

    private void copyTextfile(File source, File destination) throws IOException {
        //This example uses high-level BufferedReader and Writers that read/write to/from a textfile.
        try (BufferedReader reader = new BufferedReader(new FileReader(source)); //Autoclose the resources
             BufferedWriter writer = new BufferedWriter(new FileWriter(destination))) {
            String lineRead;
            while ((lineRead = reader.readLine()) != null) { //Use the bufferedReader utility to read an entire line
                //                                            from the textfile, until no next line can be found
                writer.write(lineRead); //Write the line to the output
                writer.newLine(); //Append linebreak
                //A PrintWriter instead of BufferedWriter allows you to do println, which automatically appends the linebreak!
            }
        }
    }

    private void copyImagefile(File source, File destination) throws IOException {
        //This example uses high-level BufferedReader and Writers that read/write to/from a textfile.
        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(source)); //Autoclose the resources
             BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(destination))) {

            byte[] allBytesAtOnce = reader.readAllBytes();
            writer.write(allBytesAtOnce);
        }
    }
}

class D_SerializingData {
    Path path1 = Paths.get("C:\\users\\public\\object");
    public void serializingData() {
        //Converting objects to datastreams is called Serialization
        //Deserializing is the opposite, converting a datastream to a Java Object
        //Java has a built-in mechanism to facilitate serializing, any Object that implements the serializable interface
        //can be serialized

        //A serializable object can be written to a file as follows:
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path1.toFile())))){
            D1_SerializableObject serializableObject = new D1_SerializableObject();
            //Simply use the writeObject method of the ObjectOutputStream!
            out.writeObject(serializableObject);
        } catch (IOException e) { //Must handle IOException
            System.out.println("Error");
            e.printStackTrace();
        }

        //A serializable object can be deserialized from a file as follows:
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path1.toFile())))){
            while (true) {
                //Need the loop because there is no end of file char, so it throws an exception to end.
                //Read as object
                var object = in.readObject(); //ClassNotFoundException and EOFException must be handled
                if (object instanceof D1_SerializableObject serializableObject1) {
                    System.out.println(serializableObject1.getName()); // :: ThisIsMyName
                }
            }
        } catch (EOFException e) {
            System.out.println("End of file reached"); //Can be ignored, what happens upon finished reading
        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            System.out.println("Class not found to deserialize"); //What happens if class isn't available at runtime
        }
    }
}

class D1_SerializableObject implements Serializable {
    //A serialVersionUID is common practice so you know which version of the object you have in memory
    private static final long serialVersionUID = 1L;
    //
    private String name = "ThisIsMyName";
    private int number = 12;
    //Object marked transient will be mapped as 0 or null
    private transient String transientString;
    //Any class that isn't a String, primitive or Wrapper must be serializable or marked transient
    //NOTE: except for the UID, static fields aren't serialized, they are ignored. When deserialized, static fields
    //    are either 0, null or the value that happens to have been set already on the object in the static context
    //    also, constructors are completely ignored
    //NOTE: Subclasses of classes that implement Serializable CAN be serialized within the same rules

    public String getName(){
        return name;
    }

}

class E_InteractingWithUsers {
    public void InteractingWithUsers() {
        //Java has 2 printstream instances:
        System.out.println("Example"); //System.out
        System.err.println("Example2"); //System.err
        //Difference between the 2 depends on the machine it's running on
        //It's normalized to not use them in practice but use logging apis

        //You can read input by using System.in as an InputStreamReader
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        //You can read input by doing:
//        try{
//            System.out.println(userInputReader.readLine());
//        } catch (IOException e) {
//            System.out.println("Error reading INPUT");
//        }

        //Don't close the System.in and System.err streams! Normally writing to closed streams give an exception,
        //but for System.in and System.err closing them and then writing to them just means you will never see any output again

        usingConsole();
    }

    private void usingConsole() {
        //The Console class is designed to handle user interaction
        //The Console class is a Singleton, get it by doin:
        Console console = System.console();
        //Depending on the system, there might not be a console, so check for null when using:
        if(console != null){
            String input = console.readLine("Please enter"); //readLine takes a String as prompt, which can also be formatted
            char[] password = console.readPassword("Please enter %s", "password"); //readPassword returns a char[]
            //                                                                                  and the data is not shown on screen
            console.writer().println("You entered:" + input);
        } else {
            System.err.println("NO CONSOLE");
        }

        //PrintStream classes use format/printf, which means you can directly format data:
        System.out.format("Hello %s \n","ARG1");
        System.out.printf("Hello %s \n","ARG2");
    }
}

class F_AdvancedApis {
    Path path1 = Paths.get("C:\\users\\public\\alphabet.txt");

    public void workingWithAdvancedApis(){
        //Input streams have the possibility to mark data, which buffers the data, so that you can reset to that position later
        //Check if you can do this by using markSupported() which returns true if it is supported
        try (var in = new BufferedInputStream(new FileInputStream(path1.toFile()))){

            if(in.markSupported()) {
                //Read the first character
                System.out.println((char) in.read()); // :: a
                //Mark the next 100 bytes to potentially reset to this point
                in.mark(100);
                System.out.println((char) in.read()); // :: b
                System.out.println((char) in.read()); // :: c
                //return to the position when mark was called
                in.reset();
                System.out.println((char) in.read()); // :: b
                System.out.println((char) in.read()); // :: c
                //You can also skip any number of bytes
                in.skip(2);
                System.out.println((char) in.read()); // :: f -- d and e are skipped
                //You can reset multiple times to the mark -- It may throw an exception if you call it after reading more than
                //                                            The allocated bytes
                in.reset();
                System.out.println((char) in.read()); // :: b -- again

            }

        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        fileAttributes();
        traversingDirectoryTrees();
    }

    private void fileAttributes() {
        Path path1 = Paths.get("C:\\users\\public\\alphabet.txt");
        //You can check whether a file is a symbolic link by doing
        System.out.println(Files.isSymbolicLink(Path.of("C:\\"))); // :: false

        //This only prints true if the destination is specifically a symbolic link.
        //NORMALLY, isDirectory() or isRegularFile() will print true for symbolic links if they point to a directory or file, respectively

        //Check file accessibility:
        try {
            System.out.println(Files.isHidden(path1)); //Check if file is hidden, throws checked IOException
        } catch (IOException e) {
            System.out.println("File cannot be found");
        }

        System.out.println(Files.isReadable(path1)); // :: true -- returns false if file cannot be found
        System.out.println(Files.isWritable(path1)); // :: true -- returns false if file cannot be found
        System.out.println(Files.isExecutable(path1)); // :: true -- returns false if file cannot be found

        //You can access multiple attributes of files at once by using Attribute Views. For Java the standard is
        //A basic file attribute view is include for all systems
        //Get file attributes by doing
        try {
            //throws IOException of file cannot be found
            BasicFileAttributes attributes = Files.readAttributes(path1, BasicFileAttributes.class);
            //attributes allow you to do:
            System.out.println(attributes.isRegularFile()); // :: true
            System.out.println(attributes.isDirectory()); // :: false
            System.out.println(attributes.isSymbolicLink()); // :: false
            System.out.println(attributes.creationTime()); //
            System.out.println(attributes.lastAccessTime()); //
            System.out.println(attributes.lastModifiedTime()); //
            System.out.println(attributes.size()); // :: size in bytes
            //Without having to access the file again each time you want to read one of these attributes
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //A modifiable variant is the FileAttributeView, which you can get by doing
        BasicFileAttributeView attributeView = Files.getFileAttributeView(path1, BasicFileAttributeView.class);
        //This does not throw an exception but simply returns null if it's not available

        //Read the attributes from the view by doing:
        try {
            BasicFileAttributes attributes = attributeView.readAttributes(); //This DOES throw an IO exception
            //Update the view by doing
            attributeView.setTimes(null,null,null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void traversingDirectoryTrees() {
        //The walk(Path path) allows you to get a stream of all files/directories under the starting Path
        //It uses depth-first searching, which means it first fully processes folder A under the starting Path,
        //And then goes to folder B
        //It is possible to specify the depth in which to search by doing walk(Path, int maxDepth)

            //Throws an IO exception if Starting path cannot be found
            //Prints all subfolders/files that are in C:\\Users, but only the first element of those folders
            try (var a = Files.walk(Paths.get("C:\\", "Users"), 1);
                 var b = Files.walk(Paths.get("C:\\", "Users"), 2);
                 var c = Files.walk(Paths.get("C:\\", "Users"), 1, FileVisitOption.FOLLOW_LINKS);
                 var d = Files.find(
                         Paths.get("C:\\", "Users", "Public"),
                         1,
                         (path, attributes) -> attributes.isRegularFile() && path.getFileName().toString().startsWith("a"))) {
                //Depth of 1:
                a.forEach(System.out::println);
                //Depth of 2:
                //b.forEach(System.out::println);
                //Symbolic links are NOT FOLLOWED by default when using walk. If you want to follow them you can use
                //FileVisitOption.FOLLOW_LINKS:
                //Beware of not creating a circular path when doing this because you WILL get an exception
                //c.forEach(System.out::println);

                //You can use the find method to quickly find a file using a bipredicate:
                //Find files that start with a
                d.forEach(System.out::println);

            } catch (AccessDeniedException e) {
                System.out.println("Access denied");

            } catch (IOException e) {
                System.out.println("Couldn't find file");
            } catch (UncheckedIOException e) {
                System.out.println("Access to file denied");
            }

        }



}



