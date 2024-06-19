package OCPEssentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class C11Exceptions {
    public static void main(String... args) {
        new UnderstandingExceptionsLesson().understandingExceptions();
        new ThrowingExceptionsLesson().throwingExceptions();
        new HandlingExceptionsLesson().handlingExceptions();
        new TryWithResourcesLesson().tryWithResources();
        new FormattingLesson().formatting();
    }
}

class UnderstandingExceptionsLesson {
    public void understandingExceptions() {
        //A program can fail due to unforeseen circumstances such as programming errors, input errors or connection issues
        //An exception is a way the program/method can tell the caller of the program/method that an error occurred, and the
        //caller has to handle it.

        //All exceptions Extend the Throwable class, there are two classes of Throwables:
        //A) Exceptions - which can be handled
        //   A1) Checked Exceptions extend the Exception class and have to be handled
        //   A2) Unchecked Exceptions extend the RuneTimeException (which is a subclass of Exception) Class
        //       and can be handled, but do not have to be
        //B) Errors - which should not be handled because they tell the caller that something went wrong and cannot be fixed
        //       errors are also unchecked
    }
}

class ThrowingExceptionsLesson {
    public void throwingExceptions() {
        //Any java code can throw an exception. For example the following code:
        String[] empty = new String[0];
        //System.out.println(empty[0]);
        //would throw an ArrayIndexOutOfBoundsException which is a RuntimeException.

        //You can also write code to explicitly throw an exception
        if(false) throw new RuntimeException(); //You can use default message of the Exception
        if(false) throw new RuntimeException("This won't be thrown"); //Or you can pass a String with a message
        //Beware that Exceptions are Objects, you need to use throw NEW to create the Object

        if(false){
            throw  new RuntimeException("1");
            //Additionally, any code that is in a code block after an exception is thrown is deemed unreachable, therefore this:
            //throw  new RuntimeException("Unreachable");
            //Wouldn't compile
        }
        callingMethodsThatThrowExceptions();
        overridingMethodsThatThrowExceptions();
        printingExceptions();
        recognizingExceptions();
    }

    //you can declare a method that throws an exception by using the throws keyword
    private void methodThatThrowsException() throws IOException {
        //As you can see, the method that declares the exception doesn't actually have to throw it. Java doesn't care.
    }

    private void methodThatDoesNot() {}
    private void callingMethodsThatThrowExceptions() {
        //if you call a method that throws an exception:
        //you have to either catch the exception, or use another throws class in your method definition to throw the
        //exception even further. Not catching it causes a compiler error - unhandled exception:
        //methodThatThrowsException();

        //Catching can be done by using a try/catch block:
        try {
            methodThatThrowsException(); //Now this compiles
        } catch (IOException e){
            System.out.println("Error"); //The method doesn't actually throw the exception so we don't get here
        }


        //BEWARE: You cannot use a catch block for a checked exception if the try block doesn't call
        // a method that explicitly throw that checked exception or subclass of that checked exception
        try {
            methodThatDoesNot();
        } catch (RuntimeException e1){
            //Perfectly fine because an unchecked exception can always be (tried to be) caught
        } catch (Exception e2){
            //perfectly fine because a RuntimeException is a subclass of Exception
        }

        //NOT OK - IOException is a checked exception that is not thrown in the try block!
//        try {
//            methodThatDoesNot();
//        } catch (IOException e3) {
//
//        }

    }

    private void overridingMethodsThatThrowExceptions() {
        class ParentException extends Exception{}
        class ChildException extends ParentException{}


        class Parent {
            public void throwException() throws ParentException{}
            public void throwException(int a) throws ParentException{}
            public void throwException(long a) throws ParentException{}
        }

        //A class can inherit a method from a parent class that throws an exception
        class Child extends Parent {
            //The inherited method can be overridden in the child class
            //You have 3 options: you can disregard the Exception:
            public void throwException(){}

            //You can throw the same Exception:
            public void throwException(int a) throws ParentException{}

            //You can throw a subclass of the Exception:
            public void throwException(long a) throws ChildException{}

            //However: you cannot throw a completely new Exception or a superclass of the Exception
//            invalid: public void throwException() throws Exception{}
        }
    }

    private void printingExceptions() {
        //There are three ways to print an exception:
        RuntimeException e = new RuntimeException("TEST");

        //First is by using toString():
        System.out.println(e.toString()); //:: java.lang.RuntimeException: TEST

        //Second is by using getMessage():
        System.out.println(e.getMessage()); //:: TEST

        //Finally by using printStackTrace()
        //e.printStackTrace(); //:: java.lang.RuntimeException: TEST + info about where error occurred
    }


    private void recognizingExceptions() {
        //There are a few Exceptions that you need to be able to recognize

        //** RUNTIME EXCEPTIONS ** //
        //ArithmeticException:
        //  This is thrown when dividing by zero
        //  ex: int a = 5/0;

        //ArrayIndexOutOfBoundsException
        //  This is thrown when accessing an index of an array that does not exist
        String[] strings = new String[3];
        //  ex1: String a = strings[-1];
        //  ex2: String b = strings[3];

        //ClassCastException
        //  This is thrown when attempting to cast to a subtype, when the object you are casting
        //  not actually of that type
        Number number = 12L;
        // ex: Integer integer = (Integer)number; --The cast is allowed by the compiler because Integer and Number are related
        //                                        --However, during runtime, java sees that the number object
        //                                        --is not an Integer instance, or subtype of an Integer instance

        //NullpointerException
        // This is thrown when attempting to operate on a reference type, when the instance is actually null
        String b = null;
        //ex: b.length();

        //IllegalArgumentException
        // This is usually thrown by a programmer when a method receives an argument that is not allowed
        // A specific subtype of IllegalArgumentException is:
        //     NumberFormatException
        //       Thrown when attempting to parse numbers, and the input isn't a number:
        //       ex: Integer.parseInt("notanumber");


        //** CHECKED EXCEPTIONS ** //
        //IOException:
        //Thrown when there is a problem reading or writing a file, has two important subclasses:
        //    NotSerializableException:
        //        Thrown when attempting to perform serialization on a non-serializable class
        //    FileNotFoundException:
        //        Thrown when code references a non-existing file

        //ParseException:
        //Thrown when there is a problem parsing input

        //SQLException:
        //Thrown when accessing a database and there is an error


        //** ERROR CLASSES ** //
        //ExceptionInInitializerError
        //Thrown when an unhandled exception occurs in a static initializer

        //StackOverFlowError
        //Thrown upon infinite recursion: e.g. when a method calls itself

        //NoClassDefFoundError
        //Thrown when

    }
}


class HandlingExceptionsLesson {
    public void handlingExceptions() {
        tryCatchBlocks();
        finallyBlocks();
    }

    private void throwsParent() throws ParentException{throw new ParentException();}

    private void tryCatchBlocks() {
        //You can handle/catch Exceptions using the try/catch syntax
        //The most basic syntax is try {codeblock} catch (Exception e) {codeblock}
        try {
            throwsParent(); //code block, {} required, that tries to do something
        } catch (ParentException e) { //catch([Object] name) syntax declaring which exception to catch
            System.out.println(e.getMessage()); //code block, {} required, that says what to do when exception occurs ::PARENT
        }

        //A try statement without a catch/finally block will not compile:
        //try {}

        //You can chain catch blocks, meaning that you can check for multiple possible types of Exceptions
        //and handle them accordingly. This is done by simply including multiple catch blocks:
        try {
            throwsParent();
        } catch (ParentException e) { //Handle possible ParentException ::PARENT
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage()); //Handle possible RuntimeException
        }

        //The order of catch blocks is important! If you declare a catch block, it will also catch extensions
        //of the Exception you defined in the block, this means that the following code is functionally
        //the same as catching the ParentException
        try {
            throwsParent();
        } catch (Exception e) { //Supertype of ParentException, so it catches ALL Exception objects
            System.out.println(e.getMessage()); //:: PARENT
        }
        //This also means that if you declare another catch block with an Exception that is a subtype of an Exception you already
        //Caught before, this will be deemed unreachable code and it will NOT compile, eg:
        //catch (ParentException p) {} //--compiler error, exception has already been caught

        //If you switch the code around, it WILL compile:
        try {
            throwsParent();
        } catch (ParentException e) { //Catch the Exception ::PARENT
            System.out.println(e.getMessage());
        } catch (Exception e) { //Catch any other possible Exception
            System.out.println(e.getMessage()); //At runtime, at most 1 catch block will run because only one exception can be thrown
        }

        //A multi-catch block is used to catch multiple Exceptions in one block, if the logic you want to execute is the same for
        //all the declared exceptions in the block:
        try {
            throwsParent();
        } catch (ParentException | RuntimeException ex) { //Beware of the syntax: catch(Exception | Exception name)
            //                                              you can only use a single identifier!
            System.out.println(ex.getMessage()); //Prints exception message for either a ParentException or a RunTimeException!
        }

        //You cannot define related Exceptions in a multicatch block, so one cannot be a super/subtype of the other
        //This would be redundant!
        try {
            throwsParent();
        } catch (ParentException
                 //| Exception -- This line wouldn't compile since ParentException is a subtype of Exception, it's redundant!
        ex) { //Beware of the syntax: catch(Exception | Exception name)
            //
            System.out.println(ex.getMessage());
        }
    }

    private void finallyBlocks() {
        //A finally clause allows you to run a block of code regardless of whether an exception was thrown
        //If an Exception is thrown, finally runs after the catch block
        //So the following code example prints 1 2 3
        try {
            System.out.print("1 ");
            throw new ChildException();
        } catch (ParentException p) {
            System.out.print("2 ");
        } finally {
            System.out.print("3");
            System.out.println();
        }

        //If no Exception is thrown, finally runs after the try block:
        //So the following code example prints 1 3
        try {
            System.out.print("1 ");
        } catch (RuntimeException p) {
            System.out.print("2 ");
        } finally {
            System.out.print("3");
            System.out.println();
        }

        //If a finally block is included, a catch block is NOT mandatory, so these are valid:
        try{}catch(RuntimeException e){}finally{}
        try{}finally{}
        //However, if a finally block is included and there is a catch block, the finally block has to be after the catch block:
        //wrong: try{}finally{}catch(RuntimeException e){};

        //A finally block is always executed! Even if you include a return statement in a try/catch block:
        System.out.println(finallyIsAlwaysExecuted()); //:: I will return now - But not before finally block executes! - 2

        //This also means that an Exception thrown in a finally block will always be thrown, and if it's
        //a checked Exception, it needs to be handled or declared
        System.out.println(exceptionInFinally()); //:: I will return now! -But not before finally block executes! - Pretty convoluted - 3


    }

    //A finally block is always executed! Even if you include a return statement in a try/catch block:
    //Because the finally is always executed, a return statement inside finally will precede over a return statement in a try block
    private int finallyIsAlwaysExecuted() {
        try {
            System.out.print("I will return now! -");
            return 1; //Before return, finally will be executed
        } finally {
            System.out.print("But not before finally block executes! - ");
            return 2; //2 will be returned, effectively ignoring the return statement in the try block
        }
    }

    private int exceptionInFinally() {
        try {
            System.out.print("I will return now! -");
            return 1; //Before return, finally will be executed
        } finally {
            System.out.print("But not before finally block executes! - ");
            try {
                throw new ChildException(); //Finally blocks throws an exception and it needs to be declared in the method signature
                //                            or handled within the block
            } catch (ParentException e) {
                System.out.print("Pretty convoluted - ");
                return 2;
            } finally {
                return 3; //This is the actual returned result
            }
            //unreachable code: return 3;
        }
    }
}

class TryWithResourcesLesson {
    public void tryWithResources()  {
        //Java has a lot of resources that you can call, that "stay open" if you don't explicitly close them
        //Think of datastreams or database connections.
        //Java has a handy syntax that is called try-with-resources, that automatically closes these kind of connections by
        //using an implicit finally block.
        //It can be used by defining an Object that implements the autocloseable/closeable interface in a try block
        //the syntax is : try(Autocloseable resource = Autocloseable) :
        try(var in = new FileInputStream("doesnotexist.txt")){
            //Try to do stuff
        }catch (IOException notfound){
            System.out.println("File does not exist!");
        } //Implicit finally block that does the close() method defined in the autocloseable interface implementation
          //The implicit finally block runs BEFORE an explicit one, so if you include a finally block here as well,
          //the AutoCloseable close method will run first

        //A try with resources statement does not need a finally/catch block:
        //try(var in = new FileInputStream("doesnotexist.txt")){} //valid syntax

        //You can create your own closeable resource, see the:
        CloseableString referToThis = new CloseableString(); //Object

        //You can declare multiple resources, it needs to be separate statements separated by ;
        try(CloseableString string1 = new CloseableString(); var string2 = new CloseableString()){
            //The variables are only in scope in the try block! It's not in scope for any catch/finally block you write yourself!
            string1.append("Hi");
            string2.append("Bye");
            throw new RuntimeException("Programmed Catch Block executes");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Programmed Finally Block executes");
        }
        //Prints "Closing closable String: Bye"
        //       "Closing closable String: Hi"
        //       "Programmed Catch Block executes"
        //       "Programmed Finally Block executes"

        //Notice 2 very important things!
        //  1) The closeable implementations run BEFORE the explicitly programmed catch/finally block
        //  2) The closeable implementations are executed in reverse order of which they were declared
        //         See how string2.close() is ran before string1.close

        //You can use a predefined resource in a try with resources statement, but it needs to be final or effectively final:
        final CloseableString finalFloseableString = new CloseableString();
        CloseableString effectivelyFinalCloseableString = new CloseableString();
        try (finalFloseableString;effectivelyFinalCloseableString;CloseableString string = new CloseableString();){ //You can mix it up
            finalFloseableString.append("Predefined resource 1");
            effectivelyFinalCloseableString.append("Predefined resource 2");
            string.append("Predefined resource 3");
            //dont do this: effectivelyFinalCloseableString = null;
        }

        suppressedExceptions();
    }

    private void suppressedExceptions() {
        //A closeable implementation can of course run into an exception of it's one when executing the close method
        //For example, the following implementation of AutoCloseable throws a runtimeexception upon closing:
        final CloseableString excCloseable = new CloseableString(true);
        try(excCloseable){
            excCloseable.append("excCloseable");
        } catch (RuntimeException ex){ //You can catch the exception here, so far so good
        //                               The order of operations is:
        //                                 1) the try block runs
        //                                 2) the close() method of the Closeable object runs
        //                                 3) the exception is thrown and the close() method exits
        //                                 4) the catch block is activated
            System.out.println(ex.getMessage()); //:: CloseableString: excCloseable encountered exception upon closing
        }

        //Things get tricky when you also throw an error in the try block:
        try(excCloseable){
            excCloseable.clear();
            excCloseable.append("excCloseable2");
            throw new IllegalArgumentException("Runtime exception thrown in try block!");
        } catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            //The exception that is caught is the exception thrown in the try block! -- this is called the primary exception
            //The exception thrown by the autocloseable interface is called a suppressed exception
            //You can check for it by doing ex.getSuppressed():
            Arrays.stream(ex.getSuppressed()).forEach(System.out::println); //::Prints the runtime exception thrown by the close interface
        }

        //This behavior also applies when there is no catch block! If an exception is thrown before the close() method is executed,
        //this exception will always show up and any possible exception thrown in the close() block is added as a suppressed exception

        //Beware: you lose the suppressed exception if you throw a new exception in a finally block
        try {
            howToLoseASuppressedException(excCloseable);
        } catch (RuntimeException rte){
            System.out.println(rte.getMessage()); //:: Completely unrelated exception
            System.out.println(rte.getSuppressed().length); //:: 0
        }
    }

    private static void howToLoseASuppressedException(CloseableString excCloseable) {
        try(excCloseable){
            excCloseable.clear();
            excCloseable.append("excCloseable3");
            throw new IllegalArgumentException("Runtime exception thrown in try block!");
        } finally {
            throw new RuntimeException("Completely unrelated exception");
        }
    }
}

class FormattingLesson {
    public void formatting() {
        //** FORMATTING NUMBERS **//
        //The NumberFormat interface has a method that allows you to format floating point and integer values
        //   using the method format(long) or format(double)
        //A concrete implementation of this interface is the DecimalFormat class
        //  It can be instantiated using a pattern
        //      0 for "number goes here, put 0 if there is no number"
        //      # for "number goes here, omit if there is no number"
        NumberFormat format1 = new DecimalFormat("###.###");
        NumberFormat format2 = new DecimalFormat("#.#");
        NumberFormat format3 = new DecimalFormat("000.000");
        NumberFormat format4 = new DecimalFormat("bla0.0bla");
        double a = 25.64;
        System.out.println(format1.format(a)); //:: 25.64
        System.out.println(format2.format(a)); //:: 25.6 - Note how it will not cut off numbers before the decimal, this wouldn't make sense
        System.out.println(format3.format(a)); //:: 025,640
        System.out.println(format4.format(a)); //:: bla25,6bla

        //** FORMATTING DATES AND TIMES**//
        //Java provides a DateTimeFormatter class to display standard date formats:
        LocalDate date = LocalDate.of(2024, 1, 24);
        LocalTime time = LocalTime.of(15, 59, 28);
        LocalDateTime datetime = LocalDateTime.of(date, time);

        System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); //:: 2024-08-24
        System.out.println(time.format(DateTimeFormatter.ISO_LOCAL_TIME)); //:: 15:59:28
        System.out.println(datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); //:: 2024-08-24T15:59:28
        //Note that you can only use time with a time/datetime, Date with a date/datetime, and datetime with a datetime

        //You can use your own custom format with a datetimeformatter by using the ofPattern method:
        //Month patterns:
        String pattern1 = "M/dd/yyyy"; //Month as single digit, if possible -- 2
        String pattern2 = "MM/dd/yyyy"; //Month as 2 digits, always prefaced by 0 if it's 1-9 -- 02
        String pattern3 = "MMM/dd/yyyy"; //Month as 3 character string -- FEB
        String pattern4 = "MMMM/dd/yyyy"; //Month as full string -- FEBRUARY
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern1)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern2)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern3)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern4)));

        //Day patterns
        String pattern5 = "MMMM/dd/yyyy"; //Day as 2 digits, prefaced by 0 if it's 1-31 -- 08
        String pattern6 = "MMMM/d/yyyy"; //Day as 1 digit, if possible -- 8
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern5)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern6)));

        //Year patterns:
        String pattern7 = "MMMM/dd/yyyy"; //Full year -- 2024
        String pattern8 = "MMMM/dd/yy"; //Last two digits of year --24

        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern7)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern8)));

        //Hour/minute/second patterns:
        String pattern9 = "hh"; //Hour prefaced by 0 if one digit -- 03
        String pattern10 = "h"; //Hour as one digit if possible -- 3
        String pattern11 = "mm"; //Minute prefaced by 0 if one digit -- 03
        String pattern12 = "m"; //Minute as one digit if possible -- 3
        String pattern13 = "ss"; //Second prefaced by 0 if one digit -- 03
        String pattern14 = "s"; //second as one digit if possible -- 3
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern9)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern10)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern11)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern12)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern13)));
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(pattern14)));

        //Furthermore, a is used for am:pm, z for timezone, and Z for offset of timezone
        //To use z-Z you need a ZonedDateTime
        //You cannot use characters that are alphabetical and not part of one of the above mentioned
        //You can use the separators displayed below:
        String fullPattern = "MMMM-dd/yyyy |_-[]~`.,?|): hh:mm:ss a";
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(fullPattern)));
        //You can use it the other way around
        System.out.println(DateTimeFormatter.ofPattern(fullPattern).format(datetime));

        //If you want to use custom alphabetical text, you need to use single quotes:
        String fullPatternCustomText = "MMMM-dd/yyyy 'Custom:' hh:mm:ss a";
        System.out.println(datetime.format(DateTimeFormatter.ofPattern(fullPatternCustomText)));

        //Failure to use ' or not terminating them correctly results in runtime errors:
        String fullPatternCustomTextWrong = "MMMM-dd/yyyy 'Custom: hh:mm:ss a"; //Failure to terminate
        String fullPatternCustomTextWrong2 = "MMMM-dd/yyyy Custom: hh:mm:ss a"; //Failure to use '' for custom characters
    }
}





//** RESOURCES USED **//
class ParentException extends Exception{
    public ParentException(){
        super("PARENT");
    }
    public ParentException(String a){
        super(a);}
}
class ChildException extends ParentException{public ChildException(){super("CHILD");}}

//You can create your own closeable resource:
class CloseableString implements AutoCloseable { //implement AutoCloseable
    //Override close method
    @Override
    public void close() {
        System.out.println("Closing closable String:" + get());
        if(doThrow) throw new RuntimeException("CloseableString: " + get() + " encountered exception upon closing");
    }
    private StringBuilder sb = new StringBuilder();
    private boolean doThrow;
    public String get(){
        return sb.toString();
    }
    public void append(String append){
        sb.append(append);
    }
    public void clear(){
        sb.delete(0,100);
    }
    public CloseableString(boolean doThrow){
        this.doThrow = doThrow;
    }
    public CloseableString(){}
}