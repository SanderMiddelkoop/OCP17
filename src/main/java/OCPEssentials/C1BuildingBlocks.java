//Package declaration goes first
package OCPEssentials;


/*
   In order to access a class you need an import statement, these are at the top of your file
   Classes are located in packages. In this case, the class Random is located in the package java.util
   The rules for package names are identical to the rules for variable names.

   You can use a wildcard to import everything from a package -- e.g. import java.util.*
     This wildcard ONLY includes all classes in THAT package, not classes in child packages.
     So: java.util.* includes all classes in package java.util, but not classes in package java.util.concurrent
     You can only use ONE wildcard per import statement

   Using wildcards has no effect on program execution time, only imported classes that are actually used are loaded.
 */

import java.util.Random;
//Special case: everything in java.lang is automatically imported

/*
   Importing 2 classes that have the same name causes a compiler error due to a naming conflict/ambiguous import
   import java.sql.Date
   import java.util.Date

   An explicit import takes priority over a wildcard import: the following does not give a conflict, as the java.util.Date will be used:
   import java.sql.*
   import java.util.Date
 */



public class C1BuildingBlocks {
   /*
       If you really need to use two classes that have the same name, you can use the class without importing it by using the
       fully qualified name when declaring the variable:
    */
    java.util.Date utilDate = new java.util.Date();
    java.sql.Date sqlDate = new java.sql.Date(12);


    /*
    JDK Commands:
      o javac: compiles .java files to .class bytecode -- e.g. javac C1BuildingBlocks.java
            - You can use wildcards to compile all files in a package: javac packageName/*.java.
                 In a way similar to how import statement wildcards work, this widlcard does not include subpackages or directories.
            - You can use the -d flag to specify which directory the class files will be created in. The package structure will be kept intact.

      o java: execute a java class or jar -- e.g. java C1BuildingBlocks
            - You can launch a single file source code program by explicitly using this command on a java file
              e.g. java C1BuildingBlocks.java. This lets you execute java files without using the javac command first.
            - You can use the -cp, -classpath or --class-path flags to specify in which directory to look for an executable file

      o jar: packages files into an executable jar
            - You can use the -c or --create flag to create a jar file
            - You can use the -v or --verbode flag to print details
            - You can use the -f or --file <filename> flag to specify the name of the jar you want to create
            - You can use the -C <directory> flag to specify the directory from where all files will be packaged into a jar
                 By default: all files under the current directory will be packaged into a jar

      o javadoc: generate documentation
     */

    /*
       A top level type is a data structure that can be  defined independently within a source file
         eg: the class C1BuildingBlocks
       Top level types do not have to be public, but you may only have ONE public top level type in a single file,
         and the name of a public top level type must match the file name
     */

    /*
       The JVM calls our code through the main method: the simplest possible program is just a public static void main(String... args){}
         Entry points must have a (public) static main method in order to do something!
     */
    public static void main(String... args){
        System.out.println("Simplest progam");
        useImportedClass();
        createTextblocks();
        primitiveAndWrapperTypes();
        namingAndInitalizingVariables();
        variableScope();
        garbageCollection();
        byte b= 80;
    }

    /*
        Use an imported class like this:
     */
    public static void useImportedClass(){
        //An object is an instance of a class, making an instance is done by using the new keyword:
        Random random = new Random();
        //The () is called the constructor. The variable random is now a reference to an instance of the Random class.

        System.out.println(random.nextInt());
    }

    /*
       Create a constructor by making a method WITHOUT a return type, with the same name as the class
       Constructor is only called when making an instance of a class. Not when accessing static methods/fields.
     */
    public C1BuildingBlocks(){
        //This constructor won't be called since only the static method of the class is accessed.
        System.out.println("In C1BuildingBlocks constructor");

        //Implicitly, each class has a default constructor that does nothing.
    }

    /*
       An instance initializer is a code block that runs before the constructor runs, that can be declared anywhere within a class
       The are executed in order of appearance. Again, these are only executed if you actually create an instance,
       not when accessing static members.
     */

    { System.out.println("This is a C1BuildingBlocks instance initializer"); }


    public static void primitiveAndWrapperTypes() {
        //java has 8 built in data types, 4 of those are based on non-decimal numbers
        //byte 8bit can store a number from -128 to 127 and defaults to 0
        byte exByte = 121;
        Byte exWrapperByte = Byte.valueOf(exByte);
        //The valueOf is called boxing, this is not necessary in most cases the same is:
        exWrapperByte = exByte;
        //The reverse is:
        exWrapperByte = Byte.parseByte("12");

        //short 16bit can store a number from -32768 to 32767 and defaults to 0
        short exShort = 22222;
        Short exWrapperShort = Short.valueOf(exShort);

        //int 32bit can store a number from -2147483648 to 2147483647 and defaults to 0
        int exInt = 123456;
        Integer exWrapperInteger = Integer.valueOf(exInt);

        //long 64bit can store a number from -2^63 to 2^63-1 and defaults to 0L, you can use underscores for readability
        //You can place underscores everywhere except at the beginning, end or just before or after decimal points
        long exLong = 12_346_789_012L;
        Long exWrapperLong = Long.valueOf(exLong);

        //2 are decimal numbers
        //float 32bit floating point defaults to 0.0f;
        float exFloat = 123.8f;
        Float exWrapperFloat = Float.valueOf(exFloat);

        //double 64bit floating point defaults to 0.0;
        double exDouble = 0.0;
        Double exWrapperDouble = Double.valueOf(exDouble);

        //boolean represents true or false, default false:
        boolean exBoolean = true;
        Boolean exWrapperBoolean = Boolean.valueOf(exBoolean);

        //char represent 16bit unicode value, ranges from 0 to 65535, defaults to \u0000
        char exChar = 'x';
        Character exWrapperChar = Character.valueOf(exChar);

        //signed integer means it can be negative: a short is signed, char is unsigned

        //Octal can be defined as:
        short exOctalShort = 032;

        //Hexadecimal can be defined as:
        short exHexaShort = 0x32;

        //binary can be defined as:
        short exBinaryShort = 0b101010;
    }

    public static void createTextblocks(){
        //A textblock is a multiline String that does not require you to explicitly mention whitespace characters
        //It is initialized by using triple " and a linebreak. The linebreak is REQUIRED.
        String exTextblock = """
                this
                is
                a \n
                different \""" 
                line \
                this isnt
                """; //this is an empty line, it does output a line!
        //The \ may be used at the end to tell the compiler to ignore the linebreak,
        //Otherwise it signals an 'escape', which is used to force the use of a character that would otherwise end the string
        //The \n may be used to force a linebreak
        System.out.println(exTextblock);
    }

    public static void namingAndInitalizingVariables(){
        /*
       There are 4 rules for naming variables:
       - Must start with a letter, currency ($, € etc) or _ symbol
       - Other than above characters, may also contain numbers but not at the start
       - May not be JUST a _
       - May not be a reserved keyword such as public, class etc
     */
        String _ThisIsOkay;
        String $This_Is123_€OK12_;
        //String 124This_IsNOTOK;

        //You can instantiate multiple variables of THE SAME type within the same statement by doing
        String exString1 = "", exString2 = "Test";
        System.out.println(exString2);
        //This initializes exString1 as "", and exString2 as "Test"
        //You cannot do this with different types, this will not compile:
        //String exString3, int 2

        //A final variable may not be changed
        final String exFinalString = "final";
        // exFinalString = ""; //Will not compile

        //In contrast to instance (field)/class variables, local variables MUST be initialized before use, this compiles:
        String exUnitializedString1, exInitializedString = "";
        System.out.println(exInitializedString);
        //But when the unitialized is used it will lead to a compiler error:
        //System.out.println(exUnitializedString); doSomething(exUnitializedString);

        //Local variables, and ONLY local variables, can use '(local variable) type inference:
        var exVar = "inferredString";
        //You cannot change the type afterwards, this won't compile:
        //exVar = 1;

        //This only works if you initialize ON THE SAME LINE. THis will not compile no matter what comes afterwards:
        //var exVarB;

        //var is not a reserved keyword, you can only not name top level types 'var', you can name them 'Var' however
        var var = ""; //this compiles

    }

    public static void variableScope(){
        //Variables defined in a code block are limited to that scope
        if(true){
            //localVar can only be accessed in this scope/block
            var localVar = "test";
            if(localVar.equals("test")){
                //it can be used in a subscope, this compiles:
                System.out.printf(localVar);
            }
        }
        //But this will not compile:
        //localVar = "";

        //Defining a method with a variable as a parameter, defines a new scope for that parameter.
        //Changes to the parameter only apply within that scope so:
        short exReferencedShort = 2;
        System.out.println("Value of the short is:" + exReferencedShort);
        passByReferencePrimitive(exReferencedShort);
        System.out.println("Value of the short after passing to method is still:" + exReferencedShort);

    }

    public static void passByReferencePrimitive(short exReferencedShort){
        exReferencedShort++;
        System.out.println("Value of the short within the method scope is:" + exReferencedShort);
    }

    public static void garbageCollection(){
        /*
          Java objects are stored in the memory heap (or free store). This is memory allocated to running your application.
          Objects that are no longer usable/reachable get automatically deleted by the JVM in a process called garbage collection.
          Objects that are eligible for garbage collection will eventually be deleted by the JVM, but it is not deterministic when this happens
          You can suggest the garbage collection to run by saying:
         */
        System.gc(); //However this is not guaranteed to immediately do anything.

        /*
           An object is eligible for garbage collection when there either are
             No references to the object anymore
             Only references that are no longer in scope
           See the following:
         */
        //An instance of Date is referenced
        java.util.Date obsoleteDate = new java.util.Date();
        java.util.Date inScopeDate = new java.util.Date();
        //The reference to obsoleteDate is changed to null:
        obsoleteDate = null;
        //The Date object that was referenced by obsoleteDate is now eligible for garbage collection.

        //At the end of this method scope, the Date object that is referenced by inScopeDate is ALSO eligible for garbage collection
        //As the scope of the method ends here, and thus the reference is no longer in scope
    }
}
