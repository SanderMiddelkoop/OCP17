package OCPEssentials;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.function.*;

public class C8Lambdas {

    static public final void main(String... args) {
        C8Lambdas c8Lambdas = new C8Lambdas();
        c8Lambdas.simpleLambdas();
        c8Lambdas.functionalInterfaces();
        c8Lambdas.methodReferences();
        c8Lambdas.builtInFunctionalInterfaces();
        c8Lambdas.variablesInLambdas();
    }

    //A lambda is a piece of functional programming, you can describe a functionality, without requiring an object or method
    //It's a method body, without the method surrounding it so to speak
    //It looks as follows, considering you have the interfaceForSimpleLamda as seen below:
    interface InterfaceForSimpleLambda {
        void doSomethingWithAnInt(int a);
    }

    interface InterfaceForSimpleLambda2 {
        int doSomethingWithAnInt(int a);
    }

    @FunctionalInterface
    interface InterfaceForSimpleLambda3 {
        int doSomethingWithTwoInts(int a, int b);
    }

    private void simpleLambdas() {
        //You can create an unnamed/anonymous class using this interface by using the following syntax:
        InterfaceForSimpleLambda anonymousImplementation = (int a) -> {
            System.out.println(a);
        };
        //You specify the input parameter(s) of the single abstract method, and what the method should do with this parameter

        //You can then use this anonymous later to execute this piece of code you just wrote, this is called deferred execution:
        anonymousImplementation.doSomethingWithAnInt(10);

        //You can use the following syntax, this is called an expression lambda
        InterfaceForSimpleLambda2 anonymousImplementation2 = a -> ++a;
        //If you use a method body (this is called a statement lambda),
        //and your interface returns something, you need to return something in the method body
        InterfaceForSimpleLambda2 anonymousImplementation3 = a -> {
            return ++a;
        };

        //You can use two parameters as follows:
        InterfaceForSimpleLambda3 anonymousImplementation4 = (a, b) -> a + b;
        //Parentheses are obligatory in that case, if you want to specify type you have to do it for both parameters:
        //NB if you specify type parentheses are ALWAYS obligatory even if you only have 1 parameter
        InterfaceForSimpleLambda3 anonymousImplementation5 = (int a, int b) -> a + b;
    }

    private void functionalInterfaces() {
        //A functional interface is an interface with EXACTLY ONE abstract method, which can be used to create a lambda
        //It can be annotated with @FunctionalInterface to ensure it has EXACTLY ONE abstract method, and otherwise
        //Throws a compiler error
        //A functional interface can contain default or static methods:
    }

    @FunctionalInterface
    interface FunctionalInterfaceWithMultipleMethods {
        void abstractMethod();

        static void staticMethod() {
        }

        default void defaultMethod() {
        }

        private void privateMethod() {
        }

        //Exception: You can define an abstract method that exists in Object:
        String toString();

        boolean equals(Object o);

        int hashCode();
    }


    private void methodReferences() {
        //Another way to create a lambda is through a method reference. You can refer to a static method:
        FunctionalInterfaceForMethodReference1 example1 = Math::random;
        FunctionalInterfaceForMethodReference2 example2 = Math::max;
        System.out.println(example1.createDouble()); //Output random number
        System.out.println(example2.createInt(5, 6)); //Output the max of 5 and 6

        //You can refer to a method of an instance of an object:
        ExampleClass1 exampleClass1 = new ExampleClass1(3);
        FunctionalInterfaceForMethodReference2 example3 = exampleClass1::addTwoInts;
        //This will use the function addTwoInts, of instance exampleClass1, with the input 4 and 6
        //So it adds 4 and 6 to 3 which outputs 13
        System.out.println(example3.createInt(4, 6));

        //You can similarly refer to a method of this instance
        FunctionalInterfaceForMethodReference2 example4 = this::multiplyIntByInt;
        //This multiplies 5 by 6 and outputs 30:
        System.out.println(example4.createInt(5, 6));

        //You can call an instance method on the first parameter that the functional interfaces takes
        FunctionalInterfaceForMethodReference3 example5 = ExampleClass1::getString;
        //You can call method of a parent of the instance
        FunctionalInterfaceForMethodReference3 example6 = Object::toString;
        //The first parameter of the FunctionalInterfaceForMethodReference3 method createString is of
        //type ExampleClass1, so you can use the getString of that class as such
        //This prints the getString method of the exampleClass1 instance
        System.out.println(example5.createString(exampleClass1)); //which is test3
        //This prints whatever Objects toString returns on the exampleClass1 instance
        System.out.println(example6.createString(exampleClass1));

        //You can even pass parameters to this method, the 2nd to nth parameters of the Functional Interface
        // will be seen as arguments to the method:
        FunctionalInterfaceForMethodReference4 example7 = ExampleClass1::getString;
        //This calls the getString(a,b) method on the instance exampleClass
        System.out.println(example7.createString(exampleClass1, "a", "b"));//which returns 3ab

        //Finally, you can even call a constructor in your lambda:
        FunctionalInterfaceForMethodReference5 example8 = ExampleClass1::new;
        //This lambda calls the constructor of ExampleClass, with int argument 5 as input for the constructor
        System.out.println(example8.createExampleClass(5).getString()); //prints test5
    }

    interface FunctionalInterfaceForMethodReference1 {
        double createDouble();
    }

    interface FunctionalInterfaceForMethodReference2 {
        int createInt(int a, int b);
    }

    interface FunctionalInterfaceForMethodReference3 {
        String createString(ExampleClass1 class1);
    }

    interface FunctionalInterfaceForMethodReference4 {
        String createString(ExampleClass1 class1, String append1, String append2);
    }

    interface FunctionalInterfaceForMethodReference5 {
        ExampleClass1 createExampleClass(int a);
    }

    class ExampleClass1 {
        int a;

        ExampleClass1(int a) {
            this.a = a;
        }

        int addTwoInts(int b, int c) {
            return this.a + b + c;
        }

        String getString() {
            return "test" + a;
        }

        String getString(String b, String c) {
            return a + b + c;
        }
    }

    int multiplyIntByInt(int a, int b) {
        return a * b;
    }

    private void builtInFunctionalInterfaces() {
        //A couple of important built-in functional interfaces are described below this method
        //They all make use of generics (see C9), to let you specify which types they work with

        //A supplier is a functional interface that has a get method, which lets you write logic to return an object of type T
        Supplier<String> stringSupplier = String::new;
        stringSupplier.get(); //returns a new String
        Supplier<LocalDateTime> localDateTimeSupplier = LocalDateTime::now; //returns a localdatetime object with datetime now

        //A consumer is a FI that has an accept method which lets you write logic to do something with an object of type T, returning void
        Consumer<String> stringConsumer = System.out::println;
        stringConsumer.accept("Hello world");

        //A BiConsumer is a FI that has an accept method which lets you write logic to do something with an object of type T,
        //and another object of type U, returning void
        HashMap<String, LocalDateTime> testHashmap = new HashMap<>();
        BiConsumer<String, LocalDateTime> stringBiConsumer = testHashmap::put;
        stringBiConsumer.accept("FirstKey", LocalDateTime.now()); //Adds key "FirstKey" and a value of the current localdatetime to hashmap
        BiConsumer<StringBuilder, String> stringBiConsumer2 = StringBuilder::append; //Appends string to stringbuilder object

        //A predicate is a FI that has a test method that returns a boolean (not the wrapper type!!).
        //It performs any operation that takes a parameter Of type T as input and returns a boolean/Boolean
        String toCheck = "abcd";
        Predicate<String> stringPredicateEnds = toCheck::endsWith; //Perform endswith(String) on object "abcd"
        Predicate<String> stringPredicateStarts = toCheck::startsWith; //Perform startsWith(String) on object "abcd"
        System.out.println(stringPredicateEnds.test("cd")); //Checks if adcd ends with cd -- true
        System.out.println(stringPredicateStarts.test("cd")); //Checks if adcd start with cd -- false
        //You can return a Boolean in the lambda, it will be unboxed to a boolean
        Predicate<Object> objectPredicate = x -> Boolean.TRUE;

        //A bipredicate is the same thing but with 2 parameters of any type
        BiPredicate<String, String> stringBiPredicate = String::startsWith;
        System.out.println(stringBiPredicate.test("Hello", "H")); //Checks if Hello starts with H -- true

        //A function is an FI that performs an operation that takes parameter of type T. And returns something of type R.
        //It's method is called apply
        Function<String, Integer> stringToIntegerFunction = String::length;
        System.out.println(stringToIntegerFunction.apply("ThisHasALengthOf18")); //Prints the length of the string, 18

        //A Bifunction does the same but takes 2 input parameters:
        BiFunction<String, String, String> twoStringsToStringFunction = String::concat;
        System.out.println(twoStringsToStringFunction.apply("abcd", "efg")); //returns abcdefg

        //A binary operator is an FI that is used for the above, when the output is the same type as the input:
        //You only have to specify one type because all types are the same
        BinaryOperator<String> twoStringOperator = String::concat;
        System.out.println(twoStringOperator.apply("abcd", "efg")); //returns abcdefg

        //A unary operator looks exactly the same but only takes one parameter
        UnaryOperator<Integer> integerUnaryOperator = Math::abs; //Returns the absolute value of an int
        System.out.println(integerUnaryOperator.apply(-15)); //returns 15

        primitiveSpecificFunctionalInterfaces();
        convenienceFunctions();
    }

    private static void convenienceFunctions() {
        //Predicates have 3 convenience functions
        //and, which lets you combine 2 predicates
        java.util.function.BiPredicate<String, String> stringPredicatePart1 = String::startsWith;
        java.util.function.BiPredicate<String, String> stringPredicatePart2 = String::endsWith;
        java.util.function.BiPredicate<String, String> stringPredicate1and2Combined = stringPredicatePart1.and(stringPredicatePart2);
        System.out.println(stringPredicate1and2Combined.test("abcdab", "ab")); //Checks if abcdab both starts, and ends with ab
        //Similar you can choose to return the opposite of the predicate
        java.util.function.BiPredicate<String, String> stringPredicate1with2Negated = stringPredicate1and2Combined.negate();
        System.out.println(stringPredicate1with2Negated.test("abcdab", "ab")); //Returns the opposite of the predicate tested before
        //Finally, you have or:
        java.util.function.BiPredicate<String, String> stringPredicate1or2 = stringPredicatePart1.or(stringPredicatePart2); //Checks if either 1 or 2 is true
        System.out.println(stringPredicate1or2.test("abcdef", "ef")); //Checks if abcdab either starts, or ends with ef

        //A consumer has 1 convencience function
        //andThen first runs the first predicate, and then independently runs the second predicate on the same argument
        java.util.function.Consumer<StringBuilder> stringBuilderConsumer1 = x -> x.append("Appending");
        java.util.function.Consumer<StringBuilder> stringBuilderConsumer2 = System.out::println;
        java.util.function.Consumer<StringBuilder> stringBuilderConsumer3 = stringBuilderConsumer1.andThen(stringBuilderConsumer2);
        stringBuilderConsumer3.accept(new StringBuilder("Starting with.."));

        //For functions, andThen runs the first predicate and then passes the outcome to the second predicate:
        java.util.function.Function<String, String> function1 = String::new;
        java.util.function.Function<String, String> function2 = x -> x + "Appended";
        java.util.function.Function<String, String> function1andThen2 = function1.andThen(function2);
        System.out.println(function1andThen2.apply("Starting with.."));

        //For functions, compose does the same but in different order:
        java.util.function.Function<String, String> function1andThen2OtherWay = function2.compose(function1);
        System.out.println(function1andThen2OtherWay.apply("Starting with.."));
    }

    private static void primitiveSpecificFunctionalInterfaces() {
        //Each Functional Interface has an Int/Double/Long variant which changes the input parameter to int/double/long,
        //So you don't have to specify it, for example:
        DoublePredicate doublePredicate = Double::isFinite; //Checks if argument is finite
        LongFunction<Integer> longFunction = x -> (int) x; //Casts argument to int and wraps to Integer

        //In addition, there is, for each Double, Int and Long
        ToDoubleFunction<String> stringToDoubleFunction; //Takes <String> and returns double
        ToDoubleBiFunction<String, String> stringStringToDoubleBiFunction; //Takes two <String>s and returns double

        //And the following:
        DoubleToIntFunction doubleToIntFunction; //Takes double and returns int
        DoubleToLongFunction doubleToLongFunction; //Takes double and returns int
        IntToLongFunction intToLongFunction; //Takes int and returns long
        IntToDoubleFunction intToDoubleFunction; //Takes int and returns double
        LongToDoubleFunction longToDoubleFunction; //Takes long and returns double
        LongToIntFunction longToIntFunction; //Takes long and returns int
    }


    //Have a look at how each interface looks below
    @FunctionalInterface
    interface Supplier<T> {
        T get();
    }

    @FunctionalInterface
    interface Consumer<T> {
        void accept(T t);
    }


    @FunctionalInterface
    interface BiConsumer<T, U> {
        void accept(T t, U u);
    }

    @FunctionalInterface
    interface Predicate<T> {
        boolean test(T t);
    }

    @FunctionalInterface
    interface BiPredicate<T, U> {
        boolean test(T t, U u);
    }

    @FunctionalInterface
    interface Function<T, R> {
        R apply(T t);
    }

    @FunctionalInterface
    interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }

    @FunctionalInterface
    interface UnaryOperator<T> {
        T apply(T t);
    }

    @FunctionalInterface
    interface BinaryOperator<T> {
        T apply(T t, T t2);
    }


    int a = 101;

    private void variablesInLambdas() {
        //You can use var in predicate:
        java.util.function.Predicate<String> stringPredicate = (var x) -> x.startsWith("abc");
        //The type is always found in the <>!

        //You can add final modifier to predicate variable:
        java.util.function.Predicate<String> stringPredicate2 = (final var x) -> {
            //This is impossible because final:
            // x = "Illegally_Changed";
            return x.startsWith("abc");
        };

        //If you use var, you must use var consistently:
        //Good
        java.util.function.BiPredicate<String, String> biPredicate1 = (var x, var y) -> x.startsWith(y);
        //Wrong:
        //java.util.function.BiPredicate<String, String> biPredicate1Wrong = (var x, String y) -> x.startsWith(y);

        //You can define local variables in lambda blocks:
        IntUnaryOperator intUnaryOperator = a -> {
            int b = 10;
            return a + b;
        };

        //You cannot redefine variables in lambda blocks:
        //IntUnaryOperator intUnaryOperator2 = a -> {int a = 10; return a + a;}; //Which a is which?

        int a = 99;
        //Lambda bodies can always use instance/class variables.
        //They can only use local variables or method parameters if they are effectively final, or explicitly final
        //This works:
        IntUnaryOperator intUnaryOperator3 = b -> {
            return b + a + this.a;
        };
        System.out.println(intUnaryOperator3.applyAsInt(50)); //50+99+101 = 250
        //If you make int a not effectively final by changing it here, the intUnaryOperator3 will no longer compile:
        //a = 98;

    }
}
