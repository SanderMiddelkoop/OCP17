package OCPEssentials;

import java.time.LocalDateTime;

public class C7BeyondClasses {
    public static void main(String args[]) {
        C7BeyondClasses lesson = new C7BeyondClasses();
        lesson.implementingInterfaces();
        new EnumLesson().enums();
        new SealedClassLesson().sealedClasses();
        new RecordLesson().records();
        NestedClassesLesson.nestedClasses();
        new PolymorphismLesson().understandingPolymorphism();
    }

    private void implementingInterfaces() {
        //An interface is an abtract data type that declares abstract methods that the class implementing the interface
        //must provide
        //A single class can implement multiple interfaces
        //One or more interfaces can be implemented by a class as follows (comma separated if multiple)
        class C7ConcreteImplementation1 implements C7Interface1 {
            //Implementation must provide the abstract method!
            //The access modifier must be public!
            //Rules for overriding methods apply, see C6.
            public int getInt() {
                return 10;
            }
        }

        //An interface can extend another, or multple interfaces (comma separated if multiple)
        interface C7InterfaceExtendsInterface1 extends C7Interface1 {
            long getLong();
        }

        abstract class C7AbstractClass1 implements C7InterfaceExtendsInterface1 {
            abstract double getDouble();
        }

        //All abstract methods that are being extended or implemented must be overriden/implemented by the first
        //Concrete class:
        class C7ConcreteImplementation2 extends C7AbstractClass1 {
            //abstract method from C7AbstractClass1 must be implemented
            protected double getDouble() {
                return 1.0;
            }

            //abstract interface method from C7InterfaceExtendsInterface1 must be implemented
            public long getLong() {
                return 1L;
            }

            //abstract interface method from C7Interface1 must be implemented
            public int getInt() {
                return 1;
            }
        }
        //Note an interface can ONLY EXTEND other interfaces,
        //A class can either extend another class, or implement an interface
        //An interface cannot extend a class!!
        //A class cannot extend an interface!!

        //A class cannot implement two interfaces that conflict with eachother, the return types must be covariant
//        class ConflictingConcreteClass implements C7Interface1, C7ConflictingInterface1 {
//            //there's no way to satisfy the overriding conditions of both interfaces for the getInt method,
//            //so this will never compile
//            @Override
//            public void getInt() {
//                return 0;
//            }
//        }
    }

    //This is how you define an interface
    //Access modifier for top level interface must either be public or default
    //abstract modifier is implicit, not necessary
    //interface cannot be marked final/private
    abstract interface C7Interface1 {
        //An interface field is implicitly public, static and final
        //Cannot be marked anything else!
        int INSTANCE_VARIABLE = 2;

        //An interface method is implicitly public and abstract
        //cannot be marked anything else but public!
        int getInt();

        //An interface method marked private must have a body:
        //This is because a private method cannot be abstract
        private int getInt(int a) {
            return a;
        }

        //Only an interface can define a default method
        //The default method MUST have a body and is always public!
        //Cannot be marked static, final, abstract, private, protected
        //A default method can still be overriden by an implementing class
        //A class that implements two interfaces with the same signature default methods, must override this
        //  method to remove an ambiguity
        default int getInt(double b) {
            return (int) b;
        }

        //Finally you can define a static method in an interface
        //It's implicitly public but can be marked private (not protected)
        //it must have a body, cannot be marked final or abstract
        //A static method can only be accessed by a class implementing the interface through reference to the interface name
        //In other words a static method is never inherited!!
        static int getInt(long c) {
            return 10;
        }

        //An interface is not required to contain methods, may be completely empty.
    }

    interface C7ConflictingInterface1 {
        void getInt();
    }
}

class C7ConcreteClassImplementingInterface1 implements C7BeyondClasses.C7Interface1 {
    @Override
    public int getInt() {
        //You can call super to call a default interface method,
        //but because you can inherit multiple interfaces, you must preface with the interface name:
        //returns the result of the default getInt(double) method
        return C7BeyondClasses.C7Interface1.super.getInt(1.0);
    }

    public void shout() {
        //static method can only be referenced by sub/implementing class via interface name
        System.out.println(C7BeyondClasses.C7Interface1.getInt(10));
    }
}

class EnumLesson {
    public void enums() {
        //An ENUM is a fixed set of constants
        //It restricts the possibilities, therefore making it impossible to create an invalid value
        //An Enum can be defined as following:
        //Top level must be either public or default
        //Enums cannot extend and you cannot extend/implement an enum
        enum Season {
            //Semicolon is optional for a simple enum like this
            //A simple enum is an enum that only contains a list of values
            WINTER, SPRING, SUMMER, FALL
        }

        //You can use a simple enum as such:
        var winter = Season.WINTER;
        //To string calls the name of the enum:
        System.out.println(winter); //Winter

        //method values() gets an array of all the Enum values (all Enum objects)
        //method name() returns a string of the name of the Enum
        //method ordinal() returns an int of the index of the Enum
        for (Season season : Season.values()) {
            System.out.print(season.name() + season.ordinal()); //WINTER0SPRING1SUMMER2FALL3
        }
        System.out.println();

        //An Enum is a separate type, you cannot directly compare Enum to string or int:
        //winter == 1; winter == "WINTER";
        //Only way to compare is:
        System.out.println(winter == Season.WINTER); //true

        //You can create an Enum by parsing a CASE-SENSITIVE string that equals the name of the enum:
        Season summer = Season.valueOf("SUMMER"); //If it doesnt match -> RuntimeException
        System.out.println(summer); //SUMMER

        enumsInSwitchStatements();
        enumMembers();

        //Enums can implement interfaces
        //Each enum member must have a concrete implementation of the abstract interface methods
        interface Temperature {
            int getTemp();
        }

        enum Weather implements Temperature {
            HOT {
                public int getTemp() {
                    return 0;
                }
            }, COLD {
                //No implementation needed because default exists
            };

            //Default implementation
            public int getTemp(){
                return 5;
            }
        }
    }

    private void enumsInSwitchStatements() {
        //Enums are handy to use in switch statements:
        enum Season {
            //Semicolon is optional for a simple enum like this
            //A simple enum is an enum that only contains a list of values
            WINTER, SPRING, SUMMER, FALL
        }

        Season spring = Season.valueOf("SPRING");
        spring = Season.FALL;
        switch (spring) {
            //Compiler will automatically detect enum, so you can use enum type here
            //Using Season.SPRING wouldn't even work
            //Using ordinal/int/string also wouldn't work.
            case SPRING:
                System.out.println("SPRING");
                break;
            case SUMMER:
                System.out.println("SUMMER");
                break;
            default:
                System.out.println("COLD");
        }

        //Same syntax
        switch (spring) {
            case SPRING -> System.out.println("SPRING");
            case SUMMER -> {
            }
            default -> System.out.println("COLD");
        }
    }


    private void enumMembers() {
        //A complex enum can contain a constructor, field and methods
        //A complex enum can be defined as such:
        enum Season {
            //You can create a call to the constructor by inputting arguments behind the values () as such:
            WINTER(1) {
                //you can define a method for each type of Enum:
                public String getLowerCaseName() {
                    return "winter";
                }

                //Overriding concrete/abstract methods follow the same rules for overriding as normally
                public double getRandom() {
                    return Math.random();
                }
            }, SPRING(2) {
                public String getLowerCaseName() {
                    return "spring";
                }

                public double getRandom() {
                    return Math.random();
                }

            }, SUMMER(3) {
                public double getRandom() {
                    return Math.random();
                }
            }, FALL(4) {
                public double getRandom() {
                    return Math.random();
                }
            }; //Semicolon is mandatory

            //Field can be public/private/static
            public final int quarter;

            //Enum constructor is always private!~Cannot be public
            Season(int quarter) {
                System.out.print("ENUM CONSTRUCTED ");
                this.quarter = quarter;
            }

            //Can use default method if Enum value doesn't implement it
            public String getLowerCaseName() {
                return "undefined";
            }

            //Can also create abstract method that all Enum values must override:
            abstract double getRandom();
        }

        //The first time you call an enum, all instances are constructed.
        //After that it's enver constructed again:
        Season season = Season.WINTER;
        System.out.println(season.quarter); //ENUM CONSTRUCTED ENUM CONSTRUCTED ENUM CONSTRUCTED ENUM CONSTRUCTED 1
        Season season2 = Season.SUMMER;
        System.out.println(season2.quarter); //3
        System.out.println(season.getLowerCaseName()); //winter
        System.out.println(season2.getLowerCaseName()); //undefined
        System.out.println(Season.WINTER.quarter); //1
        System.out.println(Season.WINTER.getRandom()); //random double
        System.out.println(Season.WINTER.getLowerCaseName()); //winter
    }
}

class SealedClassLesson {
    public void sealedClasses() {
        //A sealed class limits the number of direct subclasses it can have
        //it restricts which other classes may DIRECTLY extend it

    }
}
//A sealed class can be declared as such
//Classes that are called in permits, MUST extend the sealed class
//Classes that extend a sealed class, must either be sealed, non-sealed or final
final class Flat extends House{} //Final cannot be extended further
non-sealed class Appartment extends House {} //non-sealed can be freely extended by any class
//sealed class must be marked sealed
//Cannot be marked private
//Used permits followed by classes that can extend it
//Permits is not mandatory if the classes that extend the sealed class are top level classes in the same file
//Sealed class may be abstract
sealed class House permits Flat, Appartment {
    //permits is optional here,
    //if it's not included any top level class in the same file may extend
    //However if it is included, it is limited to what it says in permits!
    //A sealed class must be declared in the same package/module as its direct subclasses
}

class RecordLesson {
    public void records() {
        //A record is a immutable object that describes data
        //A record can be defined as such:
        //A record is implicitly final
        final record Record(int id, String name) {
        }
        ;

        //This creates an immutable object with a field id, and name. Fields can only be set via the constructor:
        Record record = new Record(1, "A"); //1A
        System.out.println(record.id + record.name()); //Fields can be accessed via record.[field] or record.[field]()
        System.out.println(record); //Record[id=1, name=A]

        //An accessor method for each field, a constructor, and implementations of equals() hashCode() and toString() are
        //provided
        //equals/hashcode checks not object equality, but the equality of all fields of the record

        //A class may not extend or inherit a record.

        final record Record2(int id, String name) {
            //If you explicitly define a constructor you can:
            //either implement a long-constructor which contains all arguments:
            public Record2(int id, String name) {
                //Now all field must be set explicitly because the default long-constructor is overruled
                this.id = id;
                this.name = name;
                //This constructor equal to the default constructor if you don't define one
            }

            //Or define a compact constructor (see Record3).

            //A different constructor must have a call to this as a first argument
            //All other lines past the have no effect on the record, it is already immutable by then
            public Record2(){
                this((int)(Math.random()*10), "RANDOM");
            }

        }
        record Record3(int id, String name) {
            //The compact constructor looks as follows
            //Notice no ()!
            public Record3 {
                //implicitly, this.id = id, this.name = name is done at the end
                //You cannot do this.id = xxx in this constructor because it will assign twice, and that is
                //not allowed for final variables
                //So in order to transform the input, you do:
                id = id + 1; //sets this.id to the id forwarded into the constructor, +1
                name = name == null ? "null" : name.toLowerCase();
            }

            //You can override the accessor method
            @Override
            public int id(){
                return 10;
            }

            //You can add methods, static fields,nested classes,interfaces, enums and other records
            //But not instance fields, these are reserved for inside the record declaration

        }

        Record3 record3 = new Record3(1, null);
        System.out.println(record3); //2, null
        System.out.println(record3.id()); //10 -- METHOD is overriden in record3
        System.out.println(record3.id); //2
    }
}

class NestedClassesLesson{ //382
    public static void nestedClasses(){
        //You can define a class within another class - this is called a nested class
        //There are 4 types
          //A) An inner class, non-static class defined at the member level of a class
          //B) A static nested class, static class defined at the member level of a class
          //C) Local class: A class defined within a method
          //D) anonymous class, class without name that's used for lambdas

        nestedInnerClass();
        staticNestedClass();
        localClass();
        anonymousClass();
    }

    private String outerMemberString = "hi";
    private int ambigousInt = 5;
    //An Inner class is defined at the member level
    //It can be either public proteced 'default' or private
    //can extend/implement
    //can be abstract or final
    private class InnerClas{
        int times = 3;
        private int ambigousInt = 10;
        public void shout(){
            for (int i = 0; i<times; i++) System.out.print(outerMemberString);
            //note: private field of upper class can be used
        }
        public void shoutInts(){
            //Calling this automatically refers to the inner class
            System.out.println(ambigousInt); //10
            System.out.println(this.ambigousInt); //10
            //If you want to explicitly refer to an outer class, you can do [CLASSNAME].this.[FIELD] :
            System.out.println(NestedClassesLesson.this.ambigousInt); //3
        }
    }

    private static void nestedInnerClass() {
        //Nested inner class can be used like this
        //you need an Instance of the inner class to use it (therefore u also need an instance of the outer class)
        //So because you need an instance, this doesn't work in a static context:
//        new InnerClas().shout();
//        InnerClas innerClas = new NestedClassesLesson.InnerClas();
//        innerClas.shout();
        //But this does:
        NestedClassesLesson nestedClassesLesson = new NestedClassesLesson();
        InnerClas innerClass2 = nestedClassesLesson.new InnerClas();
        innerClass2.shout(); //hihihi
        System.out.println();
        new NestedClassesLesson().new InnerClas().shout(); //hihihi
        System.out.println();
    }

    //A static inner class is similar to an inner class but static.
    //You can use it without an instance of the enclosing class
    public static class InnerStaticClass{
        private int privateInt = 12;
    }

    private static void staticNestedClass() {
        //You don't need an instance of the enclosing class, so you can simply call it as such:
        InnerStaticClass innerStaticClass = new InnerStaticClass();
    }

    private static void localClass() {
        //A local class has no access modifier
           //It can be marked as final or abstract
        //It as a class used inside of a method
           //It has access to all fields and methods of the enclosing class
        //It can access local variables provided they are final or effectively final
        final int finalInt = 1;
        int effectivelyFinalInt = 2;
        int justAnInt = 3;
        justAnInt = 4;

        final class LocalClass{
            void printInts(){
                System.out.print(finalInt);
                System.out.print(effectivelyFinalInt);
                System.out.println();
                //Cannot access not effectively final local variable:
                //System.out.println(justAnInt);
            }
        }

        new LocalClass().printInts(); //12
    }


    private static void anonymousClass() {
        //An anonymous class is an implicit implementation of an interface, or extension of an existing class
        class Animal{
            String name = "Animal";
            int getInt(){return 5;}
            public int getIntSuper(){return 5;}
            public String getName(){return this.name;}
        }

        //Implemented by doing new() {}
        final Animal animal = new Animal() {
            String name = "Gorilla";
            public int getInt(){return 10;}
            public int getIntSuper(){return super.getInt();}
            public String getName(){return this.name;}
        };

        System.out.println(animal.name); //Animal
        System.out.println(animal.getName()); //Gorilla
        System.out.println(animal.getInt()); //10
        System.out.println(animal.getIntSuper()); //5

        //Anonymous class cannot have access modifiers, or extend another class/implement interface
        //It can also not have final/abstract modifier
    }
}

class PolymorphismLesson{
    public void understandingPolymorphism(){
        interface InterfaceA{
            int a = 10;
        }
        class ClassA implements InterfaceA{
            int a = 15;
            int b = 35;
            void shout(){
                System.out.println("a = " + this.a);
            }
        }
        class ClassB extends ClassA{
            int a = 20;
            void shout(){
                System.out.println("a = " + this.a);
            }
            int c = 40;
        }
        //You can access a Java object in 3 ways

        //A) through a reference of the same type as the object
        //A example: ClassB is the reference and the Object (new) is also of type ClassB
        ClassB classB1 = new ClassB();

        //B) through a reference of a supertype of the object
        //B example: Class A is a supertype of Class B. Class A is the reference type, and the Object (new) of type ClassB
        ClassA classB2 = classB1;

        //C) through a reference to an interface that the object implements
        //C example: ClassB implements InterfaceA (through ClassA), Interface A is the reference type, ClassB the Object
        InterfaceA classB3 = classB1;

        //You can only access method and field that the reference type actually has!
        System.out.println(classB1.b); //reference type ClassB has member b through inheritance
        //Wont compile:
        //System.out.println(classB2.c); //reference type ClassA has no knowledge of member c

        //When accessing a field : the REFERENCE TYPE determines which field is used:
        System.out.println(classB1.a); //20
        System.out.println(classB2.a); //15

        //However, when accessing a method, the method can be overriden
        //If the method is overriden, the method of the OBJECT TYPE will be used:
        classB1.shout(); // a = 20
        classB2.shout(); // a = 20

        //You can always assign an Object type to a reference that is a supertype:
        ClassA supertypeReference = new ClassB();

        //However, if you do it the other way around you need a cast
        ClassB castBackReference = (ClassB) supertypeReference;

        //Beware: if you cast something that cannot be cast, you get a ClassCastException
        try {
            ClassB subtypeRef = (ClassB) new ClassA();
        } catch (ClassCastException e){
            System.out.println("Cannot cast Object Type to subtype Reference Type");
        }

        //If two types are completely unrelated, the compiler prevents you from even trying:
        //LocalDateTime nonsensicalCast = (LocalDateTime) new ClassB();

        //Casting to an interface will always be allowed by the compiler, but still throws a classcastexception
        interface CompletelyUnrelatedInterface{}
        try {
            CompletelyUnrelatedInterface error = (CompletelyUnrelatedInterface) new ClassB();
        } catch (ClassCastException e){
            System.out.println("Cannot cast Object Type to completely unrelated interface Reference Type");
        }

        //You can check what type the Object is actually is by using the instanceof operator:
        System.out.println(classB1 instanceof ClassB); //true
        System.out.println(classB2 instanceof ClassB); //true
        System.out.println(classB3 instanceof ClassB); //true

        //Again, this doesn't work with completely unrelated types and will always work with interfaces
        //boolean error = classB1 instanceof LocalDateTime;
        System.out.println(classB1 instanceof CompletelyUnrelatedInterface); //false

    }
}

