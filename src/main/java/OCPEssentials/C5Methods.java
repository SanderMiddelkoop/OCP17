package OCPEssentials;


import java.sql.SQLOutput;
import java.util.Arrays;

//You can have a static import, which imports a static member (field/method) from a class
import static java.util.Arrays.asList;

//You can use a wildcard to import all members:
import static java.util.Arrays.*;

//You must import A member or use a wildcard this doesnt work:
//import static java.util.Arrays;
//Also syntax must be import static, this doesn't work either:
//static import java.util.Arrays.*

class UseStaticImports {
    //static imports can then be used as such:
    void useStaticImport(){
        //instead of
        Arrays.asList(1,2,3);
        //You can do:
        asList(1,2,3);

        //Methods of the class take precedence over imported static methods
        //This calls the sort method of the class, and not the imported Arrays static sort method
        sort(new int[]{3, 2, 1});
    }

    void sort(int[] a){

    }
}

public class C5Methods {
    //A method can contain an access modifier, return type optional specifiers, a method name, parameters/arguments and a throws clause
    //Exception list, access modifer and optional specifiers are not required
    //Optional specifiers and access modifier have to be at the beginning, but can appear in any order
    //Return type has to be before the method name

    //Static and final are optional specifiers
    //public is the access modifier (final static public, public static final or even static public final are allowed)
    //void is the return type
    //main is the method name 
    //// - name may only contain number, currency, letters or _, also not start with number
    //(String... args) is the parameter list
    //// - parameter list must be at least () , must be seperated by comma's e.g. name(int a, int b)
    //main(String... args) is the method signature
    //// - signature is about the name of the method, it's parameter types and order. 
    //// - there cannot be two methods with the same signature in a class. Parameter names do not matter for this.
    //throws Exception is the exception list
    //// - multiple exceptions can be declared, comma-separated
    //{} is the method body
    //// - body may be empty, but code block {} is mandatory
    static public final void main(String... args) throws Exception {
        C5Methods instanced = new C5Methods();
        instanced.accessModifiers();
        instanced.optionalSpecifiers();
        instanced.returnType();
        instanced.localAndInstanceVariables();
        instanced.workingWithVarargs();
        instanced.passingData();
        instanced.autoboxing();
    }

    private void accessModifiers() {
        //There are 4 possible access modifiers, 3 keywords: private, protected, public
        //private access:
        ////The method can only be called from within the same class
        //None specified , also called 'default' : package access
        ////The method can only be called from within the same package
        //protected:
        ////The method can be called from within the same package and by children of the class (subclasses)
        //public:
        ////The method can be called by anyone
    }


    private void optionalSpecifiers() {
        //There are 7 possible optional specifiers:
        //static:
        ////The method is a member of the shared class object, requires no instance
        //abstract:
        ////used in abstract class or interface, method without body
        //final:
        ////Method cannot be overriden
        //default:
        ////Used in interfaces to write a method with a body
        //synchronized:
        ////Used for multithreading
        //native - not important to know
        //strictfp - not important to know
    }


    private void returnType() {
        //The return type is a declaration of the type of object the method returns
        //If a method declares a return type, it MUST return an object if that type IN ANY CASE
        ////Null is allowed
        ////Any switch / if/else outcome has to return the correct type
        //A return type can return a smaller type, but not a bigger type:
        ////e.g. long getLong(){return 10;} works, but int getInt(){return 10L;} doesn't
        //Return type Long can return long, type long can return Long/int,
        //but return type Long cannot return int (double conversion)
    }

    //instance variables are defined as a member of the class:

    int instanceVariableA = 15;
    //instance variables be named final, this means they can only be assigned once, and they must be assigned before first use.
    //must be assigned either immediately

    final int finalInstanceVariableA = 10;
    //or in an initializer code block

    final int finalInstanceVariableB;
    { finalInstanceVariableB = 10; }
    //or in constructor

    final int finalInstanceVariableC;
    C5Methods(){
        //Cannot use before assigned, won't compile: System.out.println(finalInstanceVariableC);
        //Just once, cannot be assigned in both initializer AND constructor, wont compile: finalInstanceVariableB = 10
        finalInstanceVariableC = 12;
    }
    private void localAndInstanceVariables() {
        //local variables are defined within a method or code block:
        int localVariableA = 10; //localVariableA goes out of scope when method returns
        //final is the ONLY modifier that can be used for a local variable, needs to be defined before the type:
        final int localFinalVariableA = 15;
        //final means the value may not change: won't compile localFinalVariableA = 10;

        //Final variable does not have to be assigned a value immediately, just BEFORE first use
        //This works (NOTE: it doesn't work if you explicitly assign it null);
        final Integer localFinalVariableB;
        localFinalVariableB = 15;

        //Note: you can MODIFY the object the final variable is pointing to, you just cannot REPLACE the object
        final int[] finalArrayA = {1,2,3};
        finalArrayA[0] = 3;

        //A variable can only be used if it is defined in the same scope or a bigger one:
        System.out.println(localVariableA); //local variables can be used
        System.out.println(instanceVariableA); //instance variables can be used in method

        //A variable is called 'effectively final' if it is not explicitly final, but it doesn't change after it is assigned

        {
            int localVariableB = 15;
            //All these variables can be used in a deeper code block
            System.out.println(localVariableA);
            System.out.println(localVariableB);
            System.out.println(instanceVariableA);
            //local variable B goes out of scope here!
        }

        //But a variable that was defined in a different scope cannot be used:
        //won't compile : System.out.println(localVariableB);
    }

    //Varargs can be used as a method parameter, which works just as an array but it can be used to pass multiple objects
    //Varargs must be the last parameter of the method, so there can only be ONE varargs parameter
    //only a signature like this is valid:
    private void methodThatTakesVarargs(int a, int... b){
        //the varargs argument can be accessed just like an array would:
        if(b != null && b.length > 0) System.out.println(b[0]);
    }

    private void workingWithVarargs() {
        //You can pass an array to a varargs method:
        methodThatTakesVarargs(1, new int[]{1,2,3});
        //or you can pass a number of arguments:
        methodThatTakesVarargs(1, 1, 2, 3);
        //now you can see why it must be the last parameter of the method!

        //you don't have to pass an argument to a varargs parameter, it will pass an empty array ({}, NOT NULL) if you don't:
        methodThatTakesVarargs(1);

        //you can also pass null
        methodThatTakesVarargs(1, null);
    }

    //You can apply access modifier to methods. There are 4 as said before:
    //From most restrictive to least restrictive these are:

    //Private: this method can only be used within the same class
    private void privateMethod(){

    }

    //Private method can also be used in subclass:
    private class subClass{
        void subClassMethod(){
            privateMethod();
        }
    }

    //Default access, no modifier:
    void defaultMethod(){
        //This method can be used in the same class, or from within the same package
    }

    //Protected access:
    protected void protectedMethod(){
        //this method can be used in thew same class, within the same package, or by a subclass
    }

    //Public access:
    public void publicMethod(){
        //Can be used by anyone
    }

    //A static variable is shared between all instances off a class:
    static int staticIntA = 10;
    //If it is changed, this change applied to all instances
    {
        //you can access static variables without an instance
        int a = C5Methods.staticIntA;
        C5Methods instanced = null;
        //You can access via instance, even if instance is null
        int b = instanced.staticIntA;
        //IMPORTANT: If you change the static variable of an instance, it is changed for all instances of that object!
    }

    //Because a static member can be used without an instance, it cannot reference members that require an instance:
    //static method:
    static void staticMethodA(){
        //This won't compile: since it calls a non static field: System.out.println(instanceVariableA);
        //This won't compile since it calls a static field: publicMethod();

        //It will compile if you create an instance:
        new C5Methods().publicMethod();
        int localVariableA = new C5Methods().instanceVariableA;
    }

    //A static variable can be final these must be assigned either directly:
    final static int FINAL_STATIC_INT_A = 10;
    //or in a static initializer code block

    final static int FINAL_STATIC_INT_B;
    static {
        //A static initializer is the same as an initializer code block, but static :D
        //Static initializers run ONCE when the class is first used, as opposed to normal initializers which run
        //every time an instance is created
        //Static initializers run in order of appearance
        FINAL_STATIC_INT_B = 11;
    }
    //Note how a static final variable CANNOT be assigned in the constructor because a constructor
    //is not static by definition and requires (creating) an instance

    //Interestingly, you can assign a final static variable in a static initializer before you declared the field
    //But you cannot read from it before you declared the field.
    static {
        //So this actually works:
        FINAL_STATIC_INT_C = 10;
        //But then this won't compile : System.out.println(FINAL_STATIC_INT_C);
    }
    final static int FINAL_STATIC_INT_C;


    private void passingData() {
        //When you call a method with an argument, a new reference to the object/primitive is created for the method scope
        //So any change you make to the reference in the method scope, doesn't affect the reference in the original scope:
        int localA = 1;
        String localB = "B";
        StringBuilder localC = new StringBuilder("C");
        methodThatPassingDataCalls(localA, localB, localC);
        //The original references are unchanged here:
        System.out.println(localA + localB); //1B
        //However the underlying object can be altered:
        System.out.println(localC); //CD
        //This entire concept is called pass by value: the underlying value is the same in both scopes, but the reference
        //is a new one in the method scope

        //Beware that if you call a method that returns a value, that you actually do something with the return value:
        int localF = 6;
        doIntPlustOne(localF);
        System.out.println(doIntPlustOne(localF));//7
        System.out.println(localF); //still 6
        localF = doIntPlustOne(localF);
        System.out.println(localF); //Now it's 7
    }

    private void methodThatPassingDataCalls(int localA, String localB, StringBuilder localC){
        //The references are only reassigned in this scope:
        localA = 2;
        localB = "C";
        //Notice how the append is actually done ON THE OBJECT that is referenced so this affects the original reference
        //in the upper scope
        localC.append("D");
        //But changing the actual reference to a new object only applies to this scope, and doesn't affect the original
        //reference in the upper scope
        localC = new StringBuilder("E");
        System.out.println(localA + localB + localC);  //2CE
    }

    private int doIntPlustOne(int a){
        //BEWARE, return a++ just returns the original value a and never does anything with the ++
        return ++a;
    }


    private void autoboxing() {
        //Normal assignment of a byte
        byte byteA = 123;
        //Autoboxing is when a primitive is automatically converted to it's wrapper type:
        Byte byteB = 123;
        Integer integerA = 123;

        //Unboxing is the opposite:
        byte byteC = Byte.valueOf(byteA);

        //Unboxing can combined with an implicit cast:
        int integerB = Byte.valueOf(byteA);
        long longB = Integer.valueOf(12);

        //This only works if you put it in a larger type, if you want to put it in a smaller type you would have to do:
        byte byteD = (byte)((int) integerA);

        //Autoboxing cannot convert Integer to Long, for example, this won't work:
        //Long longA = 12;
    }
}

class OtherC5Class extends C5Methods {
    void method1(){
        C5Methods instance = new C5Methods();
        //cannot use private method: instance.privateMethod()

        //Can use protected method and default method because in same package, and subclass of C5Methods
        instance.protectedMethod();
        instance.defaultMethod();

        //If we were in a different package this would compile:
        protectedMethod();

        //but this wouldn't because you'd be accessing the method of the object in the other package in this package.
        instance.protectedMethod();
        //protected access by a subclass only works from a different package if you are actually using it
        //from a reference that is a subclass of the class that has the protected method
        //NOTE: it is the reference type that matters here, not the actual type of the underlying object
        //So this wouldn't work either from a different package
        C5Methods instance2 = new OtherC5Class();
        instance2.protectedMethod();
        //But this would:
        OtherC5Class instance3 = new OtherC5Class();
        instance3.protectedMethod();


        //Can use public method:
        instance.publicMethod();

    }

}
