package OCPEssentials;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;

//You don't need a public top level class
class C6ClassDesign {
    public int intFromC6Class = 6;

    public int methodFromC6Class() {
        return intFromC6Class;
    }

    public static void main(String... args) {
        C6ClassDesign lesson = new C6ClassDesign();
        lesson.understandingInheritance();
        lesson.creatingClasses();
        lesson.declaringConstructors();
        lesson.initializingObjects();
        lesson.inheritingMembers();
        lesson.redeclaringAndHiding();
        lesson.abstractClasses();
        lesson.immutableObjects();
    }

    private void understandingInheritance() {
        //A class can inherit from an existing class
        //A class that inherits from another class is called a child or subclass
        //The class that is inherited from is called the parent or superclass
        C6ClassDesignExtendedAgain c6ClassDesignExtendedAgain = new C6ClassDesignExtendedAgain();

        //If you mark a class as final, you cannot extend it.
        //Syntax: [ACCESS] final class, or final [ACCESS] class, for example public final class | final public class

        //A class can only extend ONE other class
        //It can however be a subclass of multiple classes through transitive inheritance
        //      -- Class A is extended by Class B, Class B is extended by Class C --> Class C is now a subclass of A AND B

        //All classes that do explicitly extend a class, implicitly extend Object. So Object is the superclass of all classes.
        //      -- This means that all classes have access to the following 3 methods:
        //            -- String toString()
        //            -- boolean equals(Object object)
        //            -- int hashCode()
    }

    //Class that extends another class uses the extends syntax:
    class C6ClassDesignExtended extends C6ClassDesign {
        public int intFromC6ExtendedClass = 7;
        private int privateMember = 8;

        public int methodFromC6ExtendedClass() {
            return intFromC6ExtendedClass;
        }
    }

    //Inheritance is transitive.
    //In short this means that the following class is a child of C6ClassDesign as well as a child of C6ClassDesignExtended
    //It therefore also inherits members of both of these classes
    class C6ClassDesignExtendedAgain extends C6ClassDesignExtended {
        //All public and protected (default if in same package) members of parents can be used in the child class:
        {
            System.out.println(intFromC6Class); //6
            System.out.println(methodFromC6Class()); //6;
            System.out.println(intFromC6ExtendedClass); //7
            System.out.println(methodFromC6ExtendedClass()); //7
        }
    }

    private void creatingClasses() {
        //Look at the below classes, Furniture and Chair
        Chair chair = new Chair();
        chair.init();
        chair.callOutProperties();
    }

    //You can only have one top level class with a public access modifier, but nested classes can be as many as you want!
    public class Furniture {
        private int legs; //This member is only accessible to this class
        protected String name; //This member is accessible to subclasses, and anywhere in the package
        protected String material = "Wood";

        public int getLegs() { //This method is accessible from everywhere
            return this.legs;
        }

        public void setLegs(int legs) { //This method is accessible from everywhere
            this.legs = legs;
        }
    }

    class Chair extends Furniture {
        public String material = "Plastic";

        protected void setProperties(int legs, String name) {
            setLegs(legs); //Uses the inherited method of parent "Furniture"
            //This is used to access the instance member:
            this.name = name; //Sets the inherited instance variable of parent "Furniture"
            //Without this, you will use a local variable if a local variable/method parameter with the same name exists
            name = "ThisIsIgnored"; //This used the method parameter 'name', not the instance variable 'name';
            System.out.println(this.name);

        }

        public void callOutProperties() {
            String pattern = new String("Name: {0}, nr of legs: {1}");
            System.out.println(MessageFormat.format(pattern, this.name, getLegs()));

            //If you have a member that is declared in both the superclass and the subclass,
            //and the member is accessible in the subtype, a call to the name of
            //this member will be a call to the subtype instance (this.[member])
            System.out.println("Material of instance of chair:" + material); //Prints plastic
            //If you want to explicitly use the member of the superclass, you can use the super keyword:
            System.out.println("Material of furniture supertype:" + super.material); //Prints wood
        }

        public void init() {
            this.setProperties(4, "Chair");
        }
    }


    private void declaringConstructors() {
        //A constructor is a special that matches the class name and has no return type specified
        //The constructor is called when a new instance of the class is created via the NEW keyword
        class InnerClass1 {
            //A standard, no argument constructor looks like this.
            public InnerClass1() {
                var a = Math.random() > 0.5 ? "String" : 2;
                var b = 2;
                System.out.println(a.getClass().getName());
                System.out.println("In InnerClass1 constructor");

            }

            //If you do not explicitly create a constructor, java will automatically add in a
            // default constructor which does:
            // NB THIS IS ONLY DONE WHEN THERE IS NO EXPLICIT CONSTRUCTOR
//            public InnerClass1(){
//                super();
//            }

            //A constructor can take arguments and can thus be overloaded, it cannot take var type arguments
            //It's overloaded in the same way as methods: if you input an int, this is the order in which a constructor is found:
            InnerClass1(int a) {
            } //If there is no such constructor try, larger primitive type (long)

            InnerClass1(long a) {
            } //If there is no such constructor, try wrapper type (Integer)

            InnerClass1(Integer a) {
            } //Lastly, try varargs

            InnerClass1(int... a) {
            }

            //Won't compile:
            //InnerClass1(var a){}

            //A constructor is case-sensitive, these won't compile:
            //public innerClass1(){}
            //public Innerclass1(){}

            //You can create a method with the same signature as the constructor, if it has a return type
            //It's a METHOD and not a constructor, regardless of signature/name:
            public void InnerClass1() {
                System.out.println("This is a method and not the constructor");
            }

            //You can call a constructor from a constructor by using this
            //This only works as the first statement in the constructor, and this can only be used once
            public InnerClass1(boolean random) {
                //System.out.println("Uncommenting this makes the next statement not compile because it's no longer the first");
                this("Test"); //Call to the InnerClass1(String a) constructor
                //Only one call to constructor allowed:
                //this(1);
            }

            //You cannot create an infinite loop of code by calling a method/constructor
            // that calls the method it came from again:
            //It will tell you it's recursive
            public InnerClass1(String a) {
                //Won't compile due to recursiveness: this("test");
                //Won't compile due to recursiveness: this(true);
                System.out.println(a);

            }
        }

        //The logic in the InnerClass1 is executed upon "new" call:
        InnerClass1 innerClass1 = new InnerClass1();

        class InnerClass2 extends InnerClass1 {
            InnerClass2(boolean a) {
                this("Prints again");
            }

            InnerClass2(String a) {
                //You can use Super to refer to the constructor of a superclass
                super("Prints"); //Calls to the InnerClass1(String a) constructor
                //Again like this, super must be the first statement, and there cannot be multiple super statements,
                //Also there cannot be a combination of super() and this()
                System.out.println("InnerClass2 constructor");
                System.out.println(a);
            }
        }

        InnerClass2 innerClass2 = new InnerClass2(true);

        class InnerClass3 extends InnerClass2 {
            //If you extend a class that does not have a no-argument constructor, but does have other constructors
            //You NEED to implement a constructor that calls a super() to an existing constructor
            InnerClass3() {
                super(true);
                System.out.println("InnerClass3 constructor");
            }
        }

        InnerClass3 innerClass3 = new InnerClass3();
    }


    private void initializingObjects() {
        //The order in which classes and it's members are initialized is imported to understand:
        //This order is:
        //A) process static variable declarations of superclass, if exists
        //B) process static initializers of superclass, if exists
        //C) process static variable declarations
        //D) process static initializers
        //E) process variables of superclass, if exists
        //F) process initializers of superclass, if exists
        //G) process constructor of superclass, if exists
        //H) process variables
        //I) process initializers
        //J) process constructor

        //However: the static (A-D) members and initializers are only done ONCE. Upon second instance,
        //only steps E-J are done;

        class Animal {
            static String first = "first";

            static {
                System.out.println("Animal: This prints " + first);
            }

            String third = "third";

            {
                System.out.println("Animal: This prints " + third);
            }

            final String fourth;

            public Animal() {
                fourth = "fourth";
                System.out.println("Animal: This prints " + fourth);
                ;
            }

        }

        class Ladybug extends Animal {
            //Final static instance variables must be initialized on the same line, or in static initializers:
            final static String second;

            static {
                second = "second";
                System.out.println("Ladybug: This prints " + second);
            }

            //Final non-static instance variables must be initialized on the same line, in initializers , or constructor
            final String fifth;

            {
                fifth = "fifth";
                //If the variable is used in the initializer, it must have been initialized before constructor runs
                System.out.println("Ladybug: This prints " + fifth);
            }

            public Ladybug() {
                System.out.println("Ladybug: This prints sixth");
            }
        }

        Ladybug ladybug = new Ladybug(); //First to sixth are printed
        Ladybug ladybug1 = new Ladybug(); //Third to sixth are printed
    }

    private void inheritingMembers() {
        //A subclass CAN define a method with the same signature as its superclass
        //This is called overriding, the rules for overriding are:
        //1) The methods must have the same signature
        //2) The method must be at least as accessible
        //3) Declared CHECKED exceptions cannot be new or broader than the parent CHECKED exception
        //4) Return value must be the same or covariant
        class SuperClass {
            public int increaseANumber(int a) {
                a = a + 5;
                System.out.println(a);
                return a;
            }

            public int increaseANumber2(int a) {
                a = a + 5;
                System.out.println(a);
                return a;
            }

            public Temporal returnAnObject() {
                return LocalDateTime.now();
            }

            public long returnLong() {
                return 10L;
            }
        }

        class SubClass extends SuperClass {
            //Overricdes the method from SuperClass by adding another 5 to its result
            public int increaseANumber(int b) { //Rule 2) Access modifier MUST BE public because all others are more restrictive
                return super.increaseANumber(b) + 5; //16
            }

            public int increaseANumber(long c) {
                //Rule 1): This isn't overriding, this is overloading. The signature is different.
                return 5;
            }

            public int increaseANumber2(int b) throws RuntimeException { //Rule 3) Unchecked exception MAY be added
                return super.increaseANumber(b) + 5; //16
            }

            //Rule 3) Checked Exception MAY NOT be added:
//            public int increaseANumber2(int b) throws Exception {
//                return super.increaseANumber(b)+5; //16
//            }

            //Rule 4) - You can return a subtype of the returntype of the method you are overriding
            @Override
            public LocalDateTime returnAnObject() { //LocalDateTime is a subtype of Temporal
                return LocalDateTime.now();
            }

            //Rule 4) - Incompatible return type
//            public Object returnAnObject(){
//                return new Object();
//            }

            //NB! primitive wrappers/smaller primitives are NOT compatible
//            public int returnLong(){ //int is not compatible with long
//                return 10;
//            }

//            public Long returnLong(){ //Long is not compatible with long
//                return Long.getLong("12");
//            }
        }

        Long a = 10L;
        SubClass subClass = new SubClass();
        System.out.println(subClass.increaseANumber(6));
    }

    private void redeclaringAndHiding() {
        //A class that extends a superclass can redeclare a method that is named as private in the superclass
        class SuperClass {
            public int myInt = 10;
            public int myInt2 = 12;

            private void shout() {
                System.out.println("SUPERCLASS shouting");
            }

            static void shoutStatic() {
                System.out.println("SUPERCLASS shouting statically!");
            }

            //Final modifier means a method cannot be overriden!
            public final void finalMethod(){
                System.out.println("This method cannot be overriden");
            }
        }

        class SubClass extends SuperClass {
            //Variables cannot be overriden. The can be hidden however.
            private int myInt = 11;
            public int myInt2 = 13;

            //The method is redeclared, it is not overriden because it was private!!
            public void shout() {
                System.out.println("SUBCLASS shouting");
            }

            //A static method cannot be overriden. If a static method with the same signature exists, the parent method is
            //hidden
            //The same rules apply as for overriding! A static method must remain static!
            static void shoutStatic() {
                System.out.println("SUBCLASS shouting statically!");
            }

            //Wont compile: you cannot change from static to nonstatic: void shoutStatic(){}
            //NB: you cannot change from nonstatic to static either
        }

        SubClass subClass = new SubClass();
        SuperClass superClass = new SuperClass();
        SuperClass superClass1 = new SubClass();
        superClass.shout();
        //When hiding, the method used is determined by the reference type!
        superClass1.shout(); //Superclass method is used despite instance being SubClass
        subClass.shout();

        superClass.shoutStatic();
        //When hiding the method used is determined by the reference type
        superClass1.shoutStatic(); //Superclass method is used despite instance being SubClass
        subClass.shoutStatic();

        //When hiding variables, the variable used is determined by the reference type!
        System.out.println(subClass.myInt); //Subclass Type is used  - 11
        System.out.println(subClass.myInt2); //Subclass Type is used - 13
        System.out.println(superClass.myInt); //Superclass Type is used - 10
        System.out.println(superClass.myInt2); //Superclass Type is used - 12
        System.out.println(superClass1.myInt); //Superclass Type is used - 10
        System.out.println(superClass1.myInt2); //Superclass Type is used - 12



    }

    private void abstractClasses() {
        //An abstract class is a class that cannot be instantiated directly
        //The class may contain abstract methods, a normal class may not
        abstract class AbstractBaseClass{
            //Abstract class cannot be instantiated directly, but can have a constructor that can be called by subclass
            AbstractBaseClass(){
                System.out.println("Constructing AbstractBaseClass");
            }

            //An abstract class can contain an abstract method - it is not required to contain abstract methods
            //This is a method marked abstract and which has not got a body.
            //You can only mark an instance method as abstract, not a field, constructor or a static method
            public abstract Object abstractMethod();

            public abstract Object secondAbstractMethod();

            public void concreteMethod(){
                System.out.println("ConcreteMethod");
                //This will use the implementation that is provided in runtime of the abstractmethod!
                System.out.println(abstractMethod());
            }
        }

        //A class that extends an abstract class must implement/override it's abstract method!
        class ImplementationClass extends AbstractBaseClass {
            //It it doesn't have a valid override like below, it won't compile!
            public LocalDateTime abstractMethod(){
                return LocalDateTime.now();
            }

            public LocalDateTime secondAbstractMethod(){
                return LocalDateTime.now().plusMonths(1);
            }
        }

        //An abstract class that extends an abstract class need not implement the abstract methods!
        //You cannot combine final/private and abstract modifiers anywhere, will lead to a compile error:
        //abstract final class WontCompile{}
        //private abstract class WontCompile{}
        abstract class SecondAbstractBaseClass extends AbstractBaseClass {
            //A third abstractmethod is introduced
            public abstract Object thirdAbstractMethod();
            //The second abstract method is implemented/overriden
            public Object secondAbstractMethod(){
                return LocalDateTime.now().plusMonths(1);
            }
            //NOTE that the first abstract method is not overriden, this is fine because it's an abstract class

            //public final abstract Object WontCompile();
            //You also cannot combine abstract and private modifiers:
            //private abstract Object WontCompile();
            //Lastly, while you can have a static abstract class (if it's nested), you cannot have a static abstract method
            //abstract public static Object WontCompile();
        }

        //However, the first concrete implementation must implement all abstract methods that have not yet gotten
        //A concrete implementation
        class SecondConcreteClass extends SecondAbstractBaseClass{
            //abstractMethod and thirdAbstractMethod must be implemented because these have not yet received one
            public Object abstractMethod() {
                return null;
            }
            public Object thirdAbstractMethod() {
                return null;
            }

            //NOTE: secondAbstractMethod need not be implemented because the SecondAbstractBaseClass already provides an
            //implementation.

        }

        //You cannot directly instantiate an abstract class:
        //AbstractBaseClass abstractBaseClass = new AbstractBaseClass();

        //It can only be instantiated through a subclass
        ImplementationClass implementationClass = new ImplementationClass();
        implementationClass.concreteMethod();
    }


    private void immutableObjects() {
        //An immutable object is an object that you cannot change the members of
        //In other words: an Immutable object cannot change state
        //Rules for creating an immutable object:
        //1) Mark the class final or make all constructors private
             //This prevents a user from making a mutable subclass
        //2) Mark all instance variables private and final
        //3) Don't have setter methods
             //Rule 2 and 3 prevent users from directly changing the variables
        //4) Don't allow mutable members objects to be directly referenced
             //This rules prevents a user from getting, for example an Array, and then changing the array by
             //Reassigning one of the indexes. Remember: Objects marked final can still be muted, it just cannot be redeclared/reassigned.
             //So if one of your fields is an ArrayList, and you want to provide a get method: return a copy of the array
        //5) Set all properties of the object in the constructor, and only in the constructor,
             //Making a defensive copy if needed
             //This prevents a user from passing an ArrayList to the constructor, and then changing the ArrayList afterwards
             //So if you pass a mutable object to the constructor, make a copy of it before you assign it to a field

        //Rule 1: mark class final
        final class ImmutableClass{
            //Rule 2: variables are marked final and private
            private final int immutableInt;
            private final ArrayList<String> immutableArrayList;

            //Rule 3: only getters, no setters
            public int getImmutableInt(){
                return immutableInt;
            }

            //Rule 4, return a defensive copy of mutable object
            public ArrayList<String> getImmutableArrayList(){
                return new ArrayList<>(immutableArrayList);
            }

            public ImmutableClass(int a, ArrayList<String> b){
                this.immutableInt = a;
                //Rule 5, when assigning a mutable object make a defensive copy
                this.immutableArrayList = new ArrayList<>(b);
            }
        }
    }
}


//You can have as many default access modified top level classes as you wish
class ThisClassCompiles {
}

class ThisClassCompilesAsWell {
}

//However, you can only have a maximum of one public top level class, and it's name must match the filename
//public class ThisClassWontCompile{}

//A top level class can't be protected or private either for obvious reasons:
//protected class ThisClassWontCompile2{}
//private class ThisClassWontCompile3{}

