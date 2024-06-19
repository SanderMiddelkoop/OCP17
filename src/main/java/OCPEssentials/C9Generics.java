package OCPEssentials;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class C9Generics {
    public static void main(String[] anythingoesreally){
        C9Generics lesson = new C9Generics();
        lesson.creatingGenericClasses();
        lesson.writingGenericMethods();
    }

    private void creatingGenericClasses() {
        //A generic class can be created by using the diamond operator <>
        //This allows you to input any type when creating the class
        class GenericHolder<T, U, V, W> { //You can use as many generic types as you likw
            //And then the type can be used as a type for fields, methods etc
            //The type is available anywhere
            private T genericObject;
            private U generic2;
            private V generic3;
            private W generic4;

            private Supplier<T> supplier;
            public void setGeneric(T object){
                this.genericObject = object;
            }
            public void setSupplier(Supplier<T> supplier){
                this.supplier = supplier;
            }
            public T getGeneric(){
                return this.genericObject;
            }
            public T getBySupplier(){
                T object = supplier.get();
                return object;
            }
        }

        //A generic class can be used as such
           //The <> simply forces you to use the type you specified at declaration of the class consistently
           //If you don't specify <>, Object is used
           //if you use <>, you must define all types
        GenericHolder<String, Integer, Long, Double> stringHolder = new GenericHolder<>();
        stringHolder.setGeneric("Hello");
        System.out.println(stringHolder.getGeneric()); //Hello

        //At compile time, all generic types are simply converted to Object
        //Casts are then used to receive the right type from the generic class
        //Because any generic is converted to Object at compile time List<String> is considered the same argument as List<Int>
        //So generic types are considered the same in terms of method signature


        //For example:
        class Example1{
            protected List<Object> test(List<Object> a){return a;}
        }

        class Example2 extends Example1{
            //This is impossible because the generic parameter types must be the same
//            protected List<Object> test(List<Integer> a){
//                System.out.println("Overriden");
//            }

            //This is impossible because the generic return type must be the same
//            protected  List<Integer> test(List<Object> a){
//                return new ArrayList<>();
//            }

            //This is allowed, only if the generic type is the same
            @Override
            protected  ArrayList<Object> test(List<Object> a){
                return new ArrayList<>();
            }

        }
    }

    private void writingGenericMethods() {
        //You can also define Generic parameters on methods
        class UseGenericMethod<T>{
            //Use generic by using <> before return type
            //Beware that using <T> hides the generic <T> of the class if they are both named T
            public <T> String string(T t){
                return t.toString();
            }

            //Contrast the above to using the class generic:
            public T t;
            public T getT(){
                return t;
            }
        }

        UseGenericMethod<String> useGenericMethod = new UseGenericMethod<>();
        useGenericMethod.t = "hello";
        System.out.println(useGenericMethod.getT()); //hello
        System.out.println(useGenericMethod.string(LocalDateTime.now())); //datetime representation

        boundingGenericTypes();
    }

    private void boundingGenericTypes() {
        //You can 'bound' a parameter type, this restricts which types can be used
        //Any parameter means <?>
        List<?> x1 = new ArrayList<>(List.of("String"));
        System.out.println(x1.get(0)); //String

        //This is useful when used as a method parameter because you can do
        class UnboundedExample{
            public void printAnyList(List<?> anyList){
                for(Object object : anyList){
                    System.out.print(object);
                }
                System.out.println();
            }
        }

        UnboundedExample unboundedExample = new UnboundedExample();
        List<String> stringList = List.of("first","second","third");
        unboundedExample.printAnyList(stringList); //if printList took <Object> this wouldn't be possible
        List<Integer> integerList = List.of(1,2,3);
        unboundedExample.printAnyList(integerList);//printList now takes any object!

        //You can assign any type of list to List<?>
        List<?> unboundedList = integerList;

        //But not the other way around
        //List<Object> objectList = unboundedList;

        //**Upper bounded wildcard**//
        //An upper bounded wildcard means that the generic can be any type T that extends a certain type
        class UpperBoundedExample{
            //In this way, you can create a method that takes a List of any value that is a Number
            public void multiplyListBy2(List<? extends Number> a){
                for(Number n:a){
                    System.out.print(n.intValue()*2);
                }
                System.out.println();
            }
        }

        UpperBoundedExample upperBoundedExample = new UpperBoundedExample();
        ArrayList<Integer> integers = new ArrayList<>(List.of(1,2,3));
        upperBoundedExample.multiplyListBy2(integers); //2,4,6

        //When using unbounded or upper bounded wildcards, a list becomes immutable:
        List<? extends Number> numberList = new ArrayList<>(List.of(1,2,3));
        List<?> anyList = new ArrayList<>(List.of(1,2,3));
        //numberList.add(4);
        //anyList.add(5)

        //**Lower bounded wildcards**
        //Lower bounded wildcards mean that the generic can be of any type that is ? or a super class of ?
        class LowerBoundExample{
            public void addInteger(List<? super Integer> a, Integer b){
                a.add(b);
                for(Object c : a){
                    System.out.print(c);
                }
                System.out.println();
                //Ironically, the type you can ADD to the list must be of type Integer or a subtype
                Number c = 12;
                //So this will not compile: a.add(c);
            }

//            public void addInteger(List<?> a, Integer b){
//                //impossible a.add(b);
//            }
        }

        LowerBoundExample lowerBoundExample = new LowerBoundExample();
        ArrayList<Number> numbers = new ArrayList<>(List.of(1,2,3));
        ArrayList<Integer> integers2 = new ArrayList<>(List.of(1,2,3));

        //Lower bound allows you to mute the List regardless of type;
        lowerBoundExample.addInteger(numbers, 4);
        lowerBoundExample.addInteger(integers2, 5);




    }
}
