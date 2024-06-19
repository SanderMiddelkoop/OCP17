package OCPEssentials;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class C4CoreAPIs {
    public static void main(String... args){
        everythingStrings();
        everythingArrays();
        mathApis();
        dateAndTimeApis();
    }

    private static void everythingStrings() {
        //**Creating and manipulating**
        //A string is basically a sequence of characters, it's a reference type. It is created by:
        String firstString = "example1";
        String firstStringAsBlock = """
                example1AsBlock""";
        System.out.println(firstString);
        System.out.println(firstStringAsBlock);

        stringConcatenating();
        stringMethods();
        stringBuilder();
        equalsWithStrings();

    }



    //**Concatenating**
    private static void stringConcatenating() {

        //String concatenations means attaching one string after another. It's done with the + operator
        //If a + b, either of them is a String, the operator will do String concatenation
        //a + b will ONLY do addition if BOTH a and b are numeric
        System.out.println(1 + 2);
        System.out.println("1" + 2);
        System.out.println(1+2+"3"); //Because operators are applied from left to right, 1+2 is done as numeric addition first
        System.out.println("3" + 1 + 2); //Because operators are applied from left to right, "3" + 1 is done as concatenation first

        //null will be printed as null
        System.out.println("b" + null + 1);

        //Beware: you can't do null + 1 first, because null + 1 is not a valid addition
        //System.out.println(null + 1 + "b");

        //The += operator also does concatenation:
        var a1 = "a";
        a1 += "b";
        System.out.println(a1);
    }

    private static void stringMethods() {
        //For all these methods, it's important to know that the index of a string starts with 0!!
        //length is a method that outputs the length of a String, simple enough:
        System.out.println("Length of abcdef = " + "abcdef".length());

        //charAt returns the character at the requested index (charAt(int index))
        System.out.println("Character at index 3 of string abcdef = " + "abcdef".charAt(3));

        //!! charAt returns an indexOutOfBoundsException if you specifiy an index thats higher than length() - 1

        //indexOf finds the first index that matches a specified string/char. Char input is technically an int:
        //indexOf(int charToFind):
        System.out.println("The index of d in string abcdef = " + "abcdef".indexOf('d'));
        //You can also search for a string, it will return the index where the searched string begins:
        System.out.println("The index of def in string abcdef = " + "abcdef".indexOf("def"));
        //You can also specify the index where to start searching from, this is inclusive:
        System.out.println("The index of d in string abcdefd, starting from index 3 = " + "abcdefd".indexOf('d', 3));
        System.out.println("The index of d in string abcdefd, starting from index 4 = " + "abcdefd".indexOf('d', 4));

        //Returns -1 if not found, fromIndex can be bigger than length() of the String, but will always return -1
        System.out.println("The index of d in string abcdef, starting from index 4 = " + "abcdef".indexOf('d', 4));

        //substring basically 'crops' the string it will return a sequence of characters starting from the specified
        //beginIndex, and the last character being the character at endIndex-1. If endIndex isn't specified it will return all characters
        //starting from the beginIndex
        System.out.println("Substring index 3 to 5 of abcdef = " + "abcdef".substring(3,6));
        System.out.println("Substring index 3 to end of abcdef = " + "abcdef".substring(3));

        //endIndex being the same as beginIndex will return an empty string
        //!! endIndex being lower than beginIndex will throw an IndexOutOfBounds exception
        //!! endIndex being higher than length() - 1 will throw an IndexOutOfBounds exception
        System.out.println("Substring index 3 to 5 of abcdef = " + "abcdef".substring(3,3));
        // exc: System.out.println("Substring index 3 to 5 of abcdef = " + "abcdef".substring(3,2));
        // exc: System.out.println("Substring index 3 to 5 of abcdef = " + "abcdef".substring(3,7));

        //toLowerCase converts a string to lower case:
        System.out.println("AbC123 to lowercase = " + "AbC123".toLowerCase());
        //toUpperCase converts to upper case:
        System.out.println("AbC123 to uppercase = " + "AbC123".toUpperCase());

        //equals checks whether the contents of the string are identical
        System.out.println("abc".equals("ABC")); //false
        System.out.println("ABC".equals("ABC")); //true
        //equalsIgnoreCase does the same but ignores lower/upper case differences:
        System.out.println("abc".equalsIgnoreCase("ABC")); //true

        //startsWith returns a boolean that specifies whether a string startswith a certain string:
        System.out.println("abc".startsWith("a")); //true, note: only takes a string not a char
        System.out.println("abc".startsWith("A")); //false, case sensitive
        //endsWith does a similar thing but with the end of a string
        System.out.println("abc".endsWith("c")); //true, note: only takes a string not a char
        //contains checks if a String contains a certain sequence of characters:
        System.out.println("abcdef".contains("cd"));

        //replace can replace a certain sequence of characters:
        System.out.println("heReplacelo".replace('R','r'));//works with chars
        System.out.println("heReplacelo".replace("Replace","l")); //works with string, as long as both arguments are same type

        //trim() and strip() replace whitespace (spaces, \n, \t, \r characters)
        System.out.println(" \n \t abc \n \t");
        System.out.println(" \n \t abc \n \t".trim());
        System.out.println(" \n \t abc \n \t".strip());
        System.out.println(" \n \t abc \n \t".stripTrailing()); //stripTrailing to only trim at the ending
        System.out.println(" \n \t abc \n \t".stripLeading() + "test"); //stripLeading to only trim at the beginning

        //Indent will ad x number of spaces to each line and adds a LINEBREAK to the end of the string, and convert all linebreaks to \n
        System.out.println("abc\nabc".indent(1)+"test");
        //Indent with input 0 doesn't change the string, but still adds a line break, and converts linebreaks to \n
        System.out.println("abc\rabc".indent(0)+"test");
        //Indent with a negative number x tries to remove x number of spaces, it WILL STILL ADD A LINEBREAK
        System.out.println(" abc\n abc\n   abc".indent(-2)+"test");
        System.out.println(" abc\n abc\n   abc".indent(-5)+"test"); //x can be more than the number of spaces there are to begin with

        //stripIndent will try to remove the same number of whitespace from each line, it WONT ADD A LINEBREAK
        System.out.println(" abc\n abc\n   abc".stripIndent()+"test"); //it will remove 1 space from each line
        System.out.println("abc\n abc\r   abc".stripIndent()+"test"); //it will remove 0 spaces from each line, does nothing except normalize linebreaks

        //translateEscapes will print escapes as their actual values:
        System.out.println("first\\nsecond"); //\n is escaped by \\
        System.out.println("first\\nsecond".translateEscapes()); //the escape is translated and it will print the linebreak again

        //isEmpty checks if string length = 0
        System.out.println("".isEmpty()); //true
        System.out.println("   ".isEmpty()); //false
        //isBlank checks if String isEmpty or contains only whitespace:
        System.out.println("  \n  \t \r".isBlank()); //true

        //String can be formatted to pass variables to strings:
        System.out.println(String.format("This is a %s format", "variable"));
        System.out.println("This is a %s format".formatted("variable2"));
        //%n inserts a linebreak using the format of the operating system
        System.out.println("This is a %s format %nwith %s inputs".formatted("variable2", "multiple"));

        //%d prints a digit, it takes any numeric value
        System.out.println("this is a digit: %d. How neat.".formatted(12000000000L));

        //%f takes a floating point numeric to print decimals:
        //it will print 6 decimals by default
        System.out.println("this is a decimal numeric %f. How neat as well".formatted(12.3456789123456789)); //prints 12.3456789
        System.out.println("this is a decimal numeric %.18f. How neat as well".formatted(12.123456789123456789)); //prints 18 decimals -- NOTE Java precision doesn't go beyond 16
        System.out.println("this is a decimal numeric %6.1f. How neat as well".formatted(12.3)); //prints 12.3 and pads the result with spaces to make it 6 long
        System.out.println("this is a decimal numeric %2.3f. How neat as well".formatted(123.343)); //prints 123.343 NOTE: it will only pad, not remove digits/decimals!
    }
    private static void stringBuilder() {
        //Everytime you mutate or change a String, a new object is created
        //This can be very inefficient
        //A StringBuilder can fix this by 'holding' a String and not changing the underlying object upon mutations
        //Create by
        StringBuilder builder1 = new StringBuilder(); //empty stringbuilder
        StringBuilder builder2 = new StringBuilder("start"); //initialized stringbuilder

        //Note how a reference to builder3 also applies to builder2 and viceversa, the underlying object is the same!
        StringBuilder builder3 = builder2.append("second").append("third"); //builder 3 and 2 are now "startsecondthird"
        System.out.println(builder2.append("test")); //builder 3 and 2 are now "startsecondthirdtest"
        System.out.println(builder3);

        //You can also create a StringBuilder with an allocated/reserved initial size
        StringBuilder builder4 = new StringBuilder(0);
        System.out.println(builder4.append("123456789123456789")); //But you can easily go over this size

        stringBuilderMethods();
    }

    private static void stringBuilderMethods() {
        //You can use substring, length, charAt and indexOf on a StringBuilder identically to how it works on a String:
        var builder1 = new StringBuilder("testString");
        System.out.println(builder1.substring(builder1.indexOf("e"), builder1.indexOf("i"))); //estStr
        System.out.println(builder1.substring(builder1.indexOf("e"), builder1.indexOf("i")).charAt(2)); //t
        System.out.println(builder1.substring(builder1.indexOf("e"), builder1.indexOf("i")).length()); //6

        //append() does string concatenation on the stringbuilder:
        StringBuilder builder2 = new StringBuilder("Builder");
        System.out.println(builder2.append(1)); //doesn't need to be a string, can be char or int, will always do String concatenation anyways

        //insert() inserts a String BEFORE the requested index
        System.out.println(builder2.insert(0,"insert")); //insertBuilder1
        System.out.println(builder2.insert(6,"Insert")); //insertInsertBuilder1 - index6 is "B", it inserts before the "B"
        //This inserts at the end:
        System.out.println(builder2.insert(builder2.length(),"Insert")); //technically the index doesn't exist, but because it's the same as the length it is understood to be at the end
        //If it exceeds length: it throws IndexOutOfBoundsException:
        //System.out.println(builder2.insert(builder2.length()+1,"Insert"));

        //delete(a, b) removes characters starting from index a, ending at index b-1, works just like substring, but b is REQUIRED
        System.out.println(builder2.delete(0,12));
        //does not compile: System.out.println(builder2.delete(8));
        System.out.println(builder2.delete(8,500)); //b is REQUIRED, but can be any number, unlike with substring it will not go out of bounds if it exceeds length
        System.out.println(builder2.delete(7,7)); //won't remove anything
        //This WILL trow IndexOutOfBoundsException:
        //System.out.println(builder2.delete(7,6));

        //deleteCharAt to delete a single char:
        System.out.println(builder2.deleteCharAt(7)); //NOTE: this WILL throw out of bounds exception when exceeding length()-1

        //replace(a, b, c) let's you replace the substring between index a and (EXCLUSIVE) b with String c
        System.out.println(builder2.replace(0,300,"123456789")); //replaces the entire string with 123456789
        System.out.println(builder2.replace(3,4,"test")); //substring(3,4) is "4", so the "4" is replaced with "test"
        System.out.println(builder2.replace(3,7,"4")); //put it back
        //Special case: start and end the same will function as if it's an insert, it will insert the string before the specified index
        System.out.println(builder2.replace(3,3,"test")); //"test" is inserted before index 3 ("4")
        //Again, this will throw exception: System.out.println(builder2.replace(3,2,"test")); //a > b cannot be true

        //reverse() simply reverses a String
        System.out.println(builder2.reverse());
    }
    private static void equalsWithStrings() {
        //using == on a StringBuilder checks object equality, so whether the references point to the same object
        StringBuilder a = new StringBuilder("testA"); //Create reference a pointing to Object 1
        StringBuilder b = new StringBuilder("testA"); //Create reference b pointing to Object 2
        StringBuilder c = a.append("gotcha"); //Create reference c, pointing to Object 1 and appending gotcha to that object
        System.out.println(a == b); //so a does not equal b
        System.out.println(a == c); //but a does equal c

        //using .equals() on a StringBuilder will ALSO check object equality, and not check the contents of the String like with String:
        System.out.println(a.equals(b)); //false; Contents are the same but does not equals

        //When creating a String not using the new keyword , JAVA will use the String Pool, this means that identical Strings use the same object:
        String d = "String a";
        String e = "String a";
        System.out.println(d == e); //True, they point to the same object

        //This only works if they are the same at compile time (it's value is set on the same line as the reference is initialized
        // and no method or constructor calls are used in the initialization
        String f = "String".concat(" a"); //method is used in initialization
        System.out.println(e == f); //False, the method concat is used and the compiler doesn't know whether they are the same
        String g = "String" + " a";
        System.out.println(e == g); //True, no method is used on the same line as it is initialized so the compiler knows it's the same
        String h = "String";
        h = h + " a";
        System.out.println(e == h); //False, + is used on a different line as it is initialized so the compiler doesn't know whether it's the same
        String j = "String" + new String( "a"); //Constructor is used in initialization
        System.out.println(e == j); //False, because constructor was used

        //When using the new keyword, it's by definition not the same object:
        String i = new String("String a");
        System.out.println(e == i); //false, different object

        //intern() checks if a String is in the String pool and returns the String from the pool.
        System.out.println(e == i.intern()); //So this is true again because the intern() returns the String Pool object
    }

    private static void everythingArrays() {
        //An array is an ordered list, that can contain duplicates, it can contain both Objects and primitives
        //Create an array as such:
        int [] arrayA = new int [3];
        int[] arrayB = new int[3];
        int arrayC[] = new int[3];
        int arrayD [] = new int[3];
        //Each of these will create an array of size 3, initialized with 0s : [0,0,0]
        //You can also initialize with numbers, in that case you may not specify size
        int[] arrayE = new int[]{1,2,3};
        int[] arrayF = {1,2,3}; //shorthand

        //won't compile: int[] arrayF = new int[2]{1,2} -> because both size and initialized with numbers
        //won't compile: int[] arrayG = new int[] -> because no initialization in either size or specification

        //BEWARE: this will create two arrays:
        int[] arrayG, arrayH;
        //while this will create an array and an int:
        int arrayI[], intA;

        //using equals on an array will check reference equality, so it won't check contents
        System.out.println(arrayB.equals(arrayA)); //false

        //You can store a smaller type in a bigger type array:
        Object[] objectArrayA = { "test" };

        //You can force a bigger type in a smaller type by casting, if it's the same type
        //This doesn't work because the objectArrayA is actually pointing towards an object that is an array of OBJECTS
        // String[] stringArrayA = (String[]) objectArrayA;

        //This works because all the references are pointing towards the same object that is an array of Stringds
        String[] stringArrayA = { "test" };
        Object[] objectArrayB = stringArrayA;
        String[] stringArrayB = (String[]) objectArrayB;
        //BEWARE: This will not lead to a compiler error, because the reference type allows any Object
        //but it will cause an Exception because the actual underlying object type of String[] is not compatible with LocalDate
        //objectArrayB[0] = LocalDate.now();

        usingArrays();
        multidimensionalArrays();
    }

    private static void usingArrays() {
        //Access an array index as follows:
        String[] stringArrayA = {"one","two","three"};
        System.out.println(stringArrayA[2]); //three; access String with index 2

        //length is a property of array, NOT A METHOD:
        System.out.println(stringArrayA.length);
        //will not compile: stringArrayA.length()

        //Arrays can be sorted using the java.util.Arrays class it's sort method:
        //primitive arrays will be sorted based on natural value:
        int[] intArrayA = {7,8,3}; //8,7,3
        for(int a : intArrayA) System.out.print(a);
        System.out.println();
        Arrays.sort(intArrayA); //3,7,8
        for(int a : intArrayA) System.out.print(a);
        System.out.println();
        //String arrays will be sorted based on alphabetical order
        String[] stringArrayB = {"10, ", "0, ", "100, ", "3, ", "4, "}; //10, 0, 100, 3, 4
        for(String b : stringArrayB) System.out.print(b);
        System.out.println();
        Arrays.sort(stringArrayB); //0, 10, 100, 3, 4
        for(String b : stringArrayB) System.out.print(b);
        System.out.println();

        //if an array is sorted, you can use binarySearch(array, key) to find the index of a key in the array:
        System.out.println(Arrays.binarySearch(stringArrayB, "10, ")); //returns the index of the searched key
        //if the key doesn't exist, it will return the negated index at which it should be placed, minus 1
        System.out.println(Arrays.binarySearch(stringArrayB, "10")); //belongs at index 1, so (1*-1)-1=-2 is returned

        //BEWARE: IF AN ARRAY ISN'T SORTED, THE OUTCOME WILL BE COMPLETELY RANDOM

        //Arrays can be compared using Arrays.compare(arrayA, arrayB)
        //types need to be the same, this doesn't work: Arrays.compare(stringArrayB, intArrayA)
        //compare returns 0 if arrays are identical:
        System.out.println(Arrays.compare(new int[]{1,2,3}, new int[]{1,2,3})); //0
        //compare returns >0 if arrayA > arrayB:
        System.out.println(Arrays.compare(new int[]{1, 4, 3}, new int[]{1, 2, 6})); //1, a second element is bigger
        System.out.println(Arrays.compare(new int[]{6,1,4}, new int[]{1,2,6,1})); //1, a first element is bigger
        System.out.println(Arrays.compare(new int[]{1,2,3,4}, new int[]{1,2,3})); //1, first 3 elements are the same, but a has extra
        //compare returns <0  if arrayA < arrayB, same rules as above
        //Remind: null is the smallest value
        //Remind: for numbers: natural order
        //Remind: for Strings: numbers are smaller than letters
        //Remind: for Strings: uppercase are smaller than lowercase
        //Remind: for Strings: "test" is smaller than "testXXX"

        //Arrays.mismatch(arrayA, arrayB) can be used similarly to compare
        //it returns the first index where the arrays differ
        System.out.println(Arrays.mismatch(new int[]{1,2,3}, new int[]{1,2,4})); //2
        System.out.println(Arrays.mismatch(new int[]{1,2,3}, new int[]{1,2,3,4})); //3
        System.out.println(Arrays.mismatch(new int[]{1,2,3}, new int[]{1,2,3})); //-1 if no mismatch
    }

    private static void multidimensionalArrays() {
        //A multidimensional array is an array in which each element of the array is another array
        //a 2D array can be created as follows, this creates an array containing 2 arrays of unspecified size
        int[][] a2DArray = new int[2][]; //the size of the first array is mandatory, the second isn't
        int[] a2DArray2 [] = new int[2][]; //this is the same
        int[] a2DArray3[], a3DArray[][]; //This is a confusing way to create 1 2D array and 1 3D array;
        
        //Can also be instantiated directly, size does not have to be the same:
        int[][] a2DArray4 = {{1},{1,2},{1,2,3},{1,2,3,4}}; //An array containing 4 arrays, each of a different size
    }

    private static void mathApis() {
        //Math api is in java.lang.Math (auto import)
        //min() returns the lowest of two longs, int, doubles or floats
        byte a = 3;
        int b = 2;
        long c = 12L;
        Long d = 12L;
        System.out.println(Math.min(a,b)); //2, note how byte is automatically upgraded to int
        //max() returns the highest of two longs, int, doubles or floats
        System.out.println(Math.max(b,c)); //12, note how int is automatically upgraded to long
        System.out.println(Math.max(a,d)); //12, note how byte is automatically upgraded to long, and Long fits in long

        //round() takes a double or float and rounds it to the nearest integer
        //a double returns a long and a float returns an int
        long e = Math.round(12.2);
        long f = Math.round(12.2f);
        int g = Math.round(12.2f);
        //won't compile: int e = Math.round(12.2);
        //won't compile: Long e = Math.round(12.2f);
        System.out.println(Math.round(12.5000)); //12.50 is rounded up to 13

        //ceil() takes a double and returns the next whole number as a double:
        System.out.println(Math.ceil(10.2)); //11.0
        //floor() takes a double and returns the previous whole numbers as a double:
        System.out.println(Math.floor(10.9)); //10.0

        //pow(double a,double b) calculates a^b, returns as double
        System.out.println(Math.pow(7,2)); //7^2 = 49.0

        //random() generates a random number
    }

    private static void dateAndTimeApis() {
        //date and time APIs of java are located in java.time.* , import is needed to use, 4 types of date/time are used:
        //LocalDate, contains just a date, format yyyy-mm-dd
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.of(2024, Month.JANUARY, 20));
        System.out.println(LocalDate.of(2024,12, 20));
        //LocalTime, contains just a time, format HH:MM:ss.sss
        System.out.println(LocalTime.now());
        System.out.println(LocalTime.of(12,0)); //12:00
        System.out.println(LocalTime.of(13,12,3)); //13:12:03
        System.out.println(LocalTime.of(8,5,3,999999999)); //08:05:03.999999999
        //LocalDateTime, contains date AND time, format yyyy-mm-ddTHH:MM:ss.sss
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        System.out.println(LocalDateTime.of(2022, Month.JANUARY, 20, 6, 15));
        System.out.println(LocalDateTime.of(2022, 1, 20, 6, 15));
        System.out.println(LocalDateTime.of(2022, 1, 20, 6, 15, 2)); //etc
        //ZonedDateTime, contains date AND time, plus timezone relative to GMT, format yyyy-mm-ddTHH:MM:ss.sss[TIMEZONE]
        System.out.println(ZonedDateTime.now());
        System.out.println(ZonedDateTime.of(2022,12,1,15,0,0,0,ZoneId.of("Europe/Paris")));
        System.out.println(ZonedDateTime.of(LocalDate.now(), LocalTime.now(), ZoneId.of("Europe/Paris")));
        System.out.println(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Paris")));
        //T15:00:00.000+02:00[Whatever] means that in timezone [whatever] it is 2 hours later than GMT, so GMT is 13:00 in that case

        manipulatingDateAndTime();
        periodsAndDurations();

        //An instant is a point in time in the GMT timezone
        System.out.println(Instant.now()); //format yyyy-mm-ddTHH:MM:ss.sssZ
        //You can only convert a ZonedDateTime object to an instant, because you need to know the timezone

        daylightSavings();
    }

    private static void manipulatingDateAndTime() {
        //You can add/subtract days, weeks, months, years to a localDate
        LocalDate localDateA = LocalDate.now();
        System.out.println(localDateA.plusYears(1).plusMonths(1).plusWeeks(1).plusDays(1));

        //You can add/subtract hours,minutes,seconds,nanos from a localtime
        LocalTime localTimeA = LocalTime.now();
        System.out.println(localTimeA.minusHours(1).minusMinutes(1).minusSeconds(1).minusNanos(1));

        //You can do all of this on a localdatetime
        //you can't do days, weeks, months, years on a localtime
        //you can't do hours, minutes, seconds, nanos on a localdate
    }

    private static void periodsAndDurations() {
        //LocalDate can also use a period to do plus and minus
        //Period can be created as such:
        Period periodA = Period.ofYears(1);
        Period periodB = Period.ofMonths(1);
        Period periodC = Period.ofWeeks(1);
        Period periodD = Period.ofDays(1);
        Period periodE = Period.of(1, 24, 12); //Years, months, days
        Period periodF = Period.of(0, 24, 0); //Years, months, days
        System.out.println(periodE); //period output in string = PxYxMxD
        System.out.println(periodF); //period output in string = PxM //0 values are omitted

        //Periods can be used on a localDate/localDateTime as such
        //BEWARE: Period may not be used on LocalTime
        System.out.println(LocalDate.now().plus(periodA));
        System.out.println(LocalDateTime.now().plus(periodB));

        //Durations are the same but for LocalTime
        //Durations can be created as such:
        Duration durationA = Duration.ofDays(1);
        Duration durationB = Duration.ofHours(1);
        Duration durationC = Duration.ofMinutes(1);
        Duration durationD = Duration.ofSeconds(1);
        Duration durationE = Duration.ofMillis(1);
        Duration durationF = Duration.ofNanos(1);
        Duration durationG = Duration.of(1, ChronoUnit.HALF_DAYS);

        //Duration can be used on LocalTime or LocalDateTime
        //BEWARE: Duration may not be used on LocalDate
        System.out.println(LocalTime.now().plus(durationA)); //Prints the current time since a day has no bearing on time
        System.out.println(LocalDateTime.now().plus(durationA)); //Prints the next day because a day has bearing on localdatetime
    }

    private static void daylightSavings() {
        //If daylight savings apply, clock will skip from 1:59 to 3:00 in march, and from 1:59 to 1:00 in november
        //If you do plus/minus a temporal amount, it will account for daylight savings:
        LocalDate localDate = LocalDate.of(2024,3,31);
        LocalTime localTime = LocalTime.of(1,15);
        ZonedDateTime zdtA = ZonedDateTime.of(localDate,localTime, ZoneId.of("Europe/Paris"));
        System.out.println(zdtA.plusHours(1)); //will print 3:15 as time

        //You can create a zoneddatetime that technically doesn't exist, it will automatically apply daylight savings
        ZonedDateTime zdtB = ZonedDateTime.of(localDate,localTime.plusHours(1), ZoneId.of("Europe/Paris"));
        System.out.println(zdtB); //2:15 doesn't exist, it will output 3:15

        //GMT has no daylight savings, so the offset will change over daylight savings:
        System.out.println("Offset before: " + zdtB.minusHours(3).getOffset() + " | Offset after: " + zdtB.getOffset());
    }

}
