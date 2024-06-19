package OCPEssentials;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class C9Collections {
    public static void main(String[] args){
        C9Collections c9Collections = new C9Collections();
        c9Collections.commonCollectionAPIs();
        c9Collections.usingListInterface();
        c9Collections.usingSetInterface();
        c9Collections.usingQueAndDequeInterface();
        c9Collections.usingMapInterface();
        c9Collections.sorting();
    }

    private void commonCollectionAPIs() {
        //A collection is a group of objects contained in a single object
        //The Java Collection Framework is a set of classes in java.util used for storing collections
        //The 3 Collection interfaces are:
          //List -> an ordered collection of elements that allow duplicates
          //Set -> a collection that does not allow duplicates
          //Queue -> a collection that orders its elements in a specific order for processing

        //Another collection that does not implement the Collection interface is:
          //Map -> a collection that stores key-value pairs, mapping keys to values

        //You need to specify that type of object that goes in a collection, the diamond operator <> is used to do that:
        List<String> listOfStrings = new ArrayList<String>();
        //This wont compile:
        //List<> listOfStrings2 = new ArrayList<String>();

        //The right hand identifier is unnecessary:
        List<String> listOfString3 = new ArrayList<>();

        //Technically, you don't need to use the diamond operator at all, it will implicitly use type Object
        List listOfStrings4 = new ArrayList<String>();
        //But it's a bad idea
        boolean a = listOfStrings4.add("String"); //You can add to a Collection by using add
        listOfStrings4.add(LocalDateTime.now());
        for (Object string : listOfStrings4){
            //Beware: this will compile but will throw a runtime error because there's nothing to prevent you from doing this
            //String b = (String) string;
            System.out.println(string);
        }

        //Adding to a collection with the add method will return a boolean, specifying whether something was added:
        List<String> arrayListOfStrings = new ArrayList<>();
        Set<String> setOfStrings = new HashSet<>();

        System.out.println(arrayListOfStrings.add("TEST")); //ArrayList will always add, so always returns true
        System.out.println(setOfStrings.add("TEST")); //Set will only add unique so this is true
        System.out.println(setOfStrings.add("TEST")); //Set will only add unique so this is false

        //Removing from a collection is possible through the remove(Object object) method
        //Return value is a boolean that says whether something was removed
        System.out.println(setOfStrings.remove("TEST")); //true
        System.out.println(setOfStrings.remove("TEST")); //No longer exists, so false

        //You can check if a collection contains elements by doing isEmpty()
        System.out.println(arrayListOfStrings.isEmpty()); //false
        System.out.println(setOfStrings.isEmpty()); //true

        //You can check size of a collection by doing size()
        System.out.println(arrayListOfStrings.size()); //1
        System.out.println(setOfStrings.size()); //0

        //Calling clear() on a collection completely empties it
        setOfStrings.clear();

        //Calling contains(Object object) on a collection checks if the collection contains that object
        System.out.println(setOfStrings.contains("TEST")); //False
        System.out.println(arrayListOfStrings.contains("TEST")); //True

        //Calling removeIf removes all elements that match a predicate
        Set<String> filterThisSet = new HashSet<>();
        filterThisSet.add("1A");
        filterThisSet.add("1B");
        filterThisSet.add("A");
        filterThisSet.add("B");
        //Only keep element that start with 1:
        Predicate<String> predicate = s -> s.startsWith("1");
        filterThisSet.removeIf(predicate.negate());
        for (String filtered : filterThisSet){
            System.out.println(filtered);
        }

        //Calling foreach can perform the function in a Consumer for all elements
        Consumer<String> consumer = System.out::print;
        filterThisSet.forEach(consumer.andThen(s -> System.out.print(" DONE "))); //prints 1A DONE 1B DONE
        System.out.println();

        //Calling equals(Object object) on an arraylist checks if two arrayLists contain the same elements in the same order
        List<Integer> intList1 = new ArrayList<>(List.of(1,2,3));
        List<Integer> intList2 = new ArrayList<>(List.of(1,3,2));
        List<Integer> intList3 = new ArrayList<>(List.of(1,2,3));
        System.out.println(intList1.equals(intList2)); //false
        System.out.println(intList1.equals(intList3)); //true

        //Calling equals(Object object) on a set checks if twp sets contain the same elements, regardless of order
        Set<Integer> intSet1 = new HashSet<>(List.of(1,2,3));
        Set<Integer> intSet2 = new HashSet<>(List.of(1,3,2));
        Set<Integer> intSet3 = new HashSet<>(List.of(1,2,3));
        System.out.println(intSet1.equals(intSet2)); //true
        System.out.println(intSet1.equals(intSet3)); //true
    }


    private void usingListInterface() {
        //A list is an ordered collection that can contain duplicates
        //Like an array, list entries can be muted based on index
        //Unlike an array, list can change in size
        List<Integer> intList = new ArrayList<>(); //Most common implementation of List is ArrayList
        //Change in size:
        intList.add(0,1);
        //mute based on index:
        intList.set(0, 2);
        //get based on index
        System.out.println(intList.get(0));

        //The other implementation of List is LinkedList, has addFirst and addLast functionality
        LinkedList<Integer> intLinkedList = new LinkedList<>();
        intLinkedList.addFirst(1); //1
        intLinkedList.addFirst(2); //2 - 1
        intLinkedList.addLast(3); //2 - 1 - 3
        intLinkedList.forEach(System.out::println);
        
        creatingAList();
        listMethods();

        //You can convert a list to an array by using the toArray() function
        //Standard an array of objects is returned
        intList.add(1);
        Object[] objectArray = intList.toArray();
        //You can convert to the correct type by specifying new
        Integer[] intArray = intList.toArray(new Integer[0]);
        //size doesn't matter, it will automatically create right size if it's below.
        for(Integer a: intArray) System.out.println(a); //Prints 2 1
        //However if you create a bigger size array, it will allocate null to missing values, so best to use 0
        Integer[] intArray2 = intList.toArray(new Integer[3]);
        for(Integer a: intArray2) System.out.println(a); //Prints 2 1 null

    }

    private void creatingAList() {
        //You can create a list via static factory methods:
        //First method is via Arrays.asList(varargs)
        List<String> stringList = Arrays.asList("A","B","C"); //Arrays.asList(varargs)
        stringList.forEach(System.out::println);
        //This creates a fixed size list, you can replace elements:
        stringList.set(0, "D");
        //But: adding and removing will lead to RUNTIME errors - > UnSupportedOperationException
        //stringList.add("E");
        //stringList.remove("E"); //Remove only causes runtime error if it ACTUALLY removes something

        //Second method is via List.of(varargs)
        List<String> stringList1 = List.of("A", "B", "C"); //List.of(varargs)

        //This creates an immutable list, you can not add, remove and replace
        //Doing so results in an UnSupportedOperationException

        //Final method is via List.copyOf(collection)
        List<String> stringList2 = List.copyOf(stringList1);
        //This results an immutable copy of the list passed to the method
        //Adding, removing and replacing here also leads to an UnsupportedOperationException

        //You can also create a List via contructor:
        List<String> stringList3 = new ArrayList<>(); //Creates an empty ArrayList
        List<String> stringList4 = new ArrayList<>(List.of("1","2","3")); //Creates a mutable copy of the passed list
        stringList4.forEach(System.out::println);
        //Specifically for ArrayList, you can also create a list with an allocated initial size:
        List<String> stringList5 = new ArrayList<>(10); //Creates an empty ArrayList
        //You can however store any amount of elements in such a list

        //You can use var with arraylist:
        var objectListVar = new ArrayList<>(); //var infers the type, because you don't specify a type in the <>, it used Object
        var stringListVar = new ArrayList<String>(); //var infers the type, because you specify String in the <>, it uses String
    }


    private void listMethods() {
        List<Integer> ints = new ArrayList<>();
        //add(E element) adds element e to the end of the list
        ints.add(1); ints.add(2); ints.forEach(System.out::println); //1 2
        //add(int index, E element) adds element e at index index
        ints.add(1, 5); ints.forEach(System.out::println); //1 5 2

        //get(int index) returns the element at index index
        System.out.println(ints.get(1)); //5

        //remove(int index) removes the element at index index and returns the removed element
        ints.remove(2); ints.forEach(System.out::println); // 1 5
        //Beware: calling with a wrapper type for primitive removes the requested object, not the index
        // Even if the element requested doesn't exist
        ints.remove(Integer.valueOf(5)); ints.forEach(System.out::println); // 1
        ints.remove(Integer.valueOf(0)); ints.forEach(System.out::println); // still 1



        //set(int index, E element) replaces element and returns the old element
        ints.set(0, -7); ints.forEach(System.out::println); //-7
        //replaceAll(UnaryOperator<E> op) performs the operation defined by the operator on each element:
        ints.replaceAll(Math::abs); //Replaces all values with the absolute value
        ints.forEach(System.out::println); //7

        //Using add, get, remove or set with an index that doesn't exist throws an IndexOutOfBoundsException
        //You can however use add with size(), this adds at the end
    }


    private void usingSetInterface() {
        //A set can be used when the order doesn't matter and you want to prevent duplicate entries
        //There are two implementations of set:
        //First: HashSet, elements are checked by the hashCode() methode
        HashSet<String> set = new HashSet<>(List.of("1","2"));
        //Because you don't have a specified order, you have no "get" method
        //Benefit of HashSet is that adding an element and checking whether an element is in the set is constant time
        //Trade off is that you have no order

        //Second: TreeSet it stores elements in a sorted tree structure
        TreeSet<Integer> treeSet = new TreeSet<>(List.of(3,2,1));
        treeSet.forEach(System.out::println); //1,2,3

        setMethods();
    }

    private void setMethods() {
        //You can create an immutable set via 2 methods:
        Set<String> stringSet = Set.of("A","B","C");
        TreeSet<Integer> treeSet = new TreeSet<>(List.of(3,2,1));
        Set<Integer> copy = Set.copyOf(treeSet);
        Set<String> copy2 = Set.copyOf(stringSet);
    }


    private void usingQueAndDequeInterface() {
        //A Queue is used when the order of adding and removing elements is specific
        //A Queue is First In First Out, the added elements are appended to the end of the queue
        //And the first element of the queue is processed first
        Queue<Integer> intQueue = new LinkedList<>();

        //add(E element) or offer adds element to the back of the Queue, which is the last element of the Collection
        intQueue.add(1); intQueue.offer(2); intQueue.add(3); intQueue.forEach(System.out::println); //1 2 3

        //E peek() returns the element at the front of the queue, which is the first element of the Collection
        System.out.println(intQueue.peek()); //1

        //E remove() or poll() returns the first element of the queue and removes it from the queue
        System.out.println(intQueue.remove()); intQueue.forEach(System.out::print); // 1 | 2 3
        System.out.println();

        //A Deque gives you the option of removing/adding/getting from both sides of the Queue
        Deque<Integer> intDeque = new LinkedList<>();

        //add(E element) still adds element to the back of the Queue, which is the last element of the Collection
        intDeque.add(5); intDeque.add(6); intDeque.add(7); intDeque.forEach(System.out::print); //5 6 7
        System.out.println();

        //addFirst(E element), offer or offerFirst also adds element to the front of Dequeu,
        // which is the first element of the Collection
        intDeque.addFirst(4); intDeque.forEach(System.out::print); //4 5 6 7
        System.out.println();

        //addLast(E element) or offerLast  adds element to the back of the Dequeu, which is the last element of the Collection
        intDeque.addLast(8); intDeque.forEach(System.out::print); //4 5 6 7 8
        System.out.println();

        //getFirst() , peekFirst() or peek() return the element at the front of the queue,
        // which is the first element of the Collection
        System.out.println("getFirst:"+intDeque.getFirst()+" - peekFirst:"+intDeque.peekFirst()
                + " - peek:" + intDeque.peek()); //4 - 4 - 4
        //getLast() or peekLast() return the element at the back of the queue,
        // which is the last element of the Collection
        System.out.println("getLast:"+intDeque.getLast()+" - peekLast:"+intDeque.peekLast()); //8 - 8

        //remove, removeFirst, pollFirst, poll removes and returns the element at the front of the queue
        System.out.print("removed: " + intDeque.remove() + "|"); intDeque.forEach(System.out::print); //removed 4 | 5 6 7 8
        System.out.println();
        //removeLast, pollLast removes and returns the element at the back of the queue
        System.out.print("removed: " + intDeque.removeLast() + "|"); intDeque.forEach(System.out::print); //removed 8 | 5 6 7
        System.out.println();
        //A dequeue can be used as a Last In, First Out queue by using push and pop
        //push(E element) adds an element to the front, similar to addFirst
        intDeque.push(4); intDeque.forEach(System.out::print); //4567
        System.out.println();
        //pop() removes the element at the front, similar to poll, pollFirst or removeFirst
        intDeque.pop(); intDeque.forEach(System.out::print); //567
    }


    private void usingMapInterface() {
        //There are two factory methods to create a map
        System.out.println();
        Map<String, Integer> stringIntegerMap = Map.of("First",1,"Second", 2);
        Map<String, Integer> stringIntegerMap1 = Map.copyOf(stringIntegerMap);
        //Again these factory methods return IMMUTABLE maps

        //There are also 2 implementations of Maps
        //HashMap, which is an unordered map in which keys are unique based on hashCode()
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>(Map.copyOf(stringIntegerMap1));

        //TreeMap, which is an ordered map in which keys are ordered based on natural order
        TreeMap<String, Integer> stringIntegerTreeMap = new TreeMap<>(Map.copyOf(stringIntegerMap1));

        //the method containsKey(K key) can be used to check if map contains the specified key:
        System.out.println(stringIntegerHashMap.containsKey("Second")); //true

        //the method containsValue(V value) can be used to check if map contains the specified value:
        System.out.println(stringIntegerHashMap.containsValue(3)); //false

        //the get(K key), returns the value associated with the Key, or null of none
        System.out.println(stringIntegerHashMap.get("First")); //1
        System.out.println(stringIntegerHashMap.get("NONE")); //null

        //the getOrDefault(K key, V value) method returns the value associated with the Key, or 'value' if null
        System.out.println(stringIntegerHashMap.getOrDefault("NONE", 0)); //0

        //keySet returns a Set<K> of all keys:
        Set<String> keySet = stringIntegerMap.keySet();

        //entrySet returns a Set of Map.Entry(K,V) objects of all entries in the map:
        Set<Map.Entry<String,Integer>> entrySet = stringIntegerMap.entrySet();

        //forEach(BiConsumer<K,V>) does the specified operation in the BiConsumer for each entry
        stringIntegerMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) no particular order
        stringIntegerTreeMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) ordered

        //clear() is used to clear the map, and isEmpty() to check if it contains any key value pairs:
        stringIntegerTreeMap.clear();
        System.out.println(stringIntegerTreeMap.isEmpty()); //true
        //If values are null, but key is present, map is not empty
        stringIntegerTreeMap.put("First", null);
        System.out.println(stringIntegerTreeMap.isEmpty()); //false

        //replaceAll, size, values
        //put(K key, V value) adds an entry with Key k and Value v to the map
        stringIntegerHashMap.put("Third",3);
        stringIntegerHashMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) no particular order

        //put method will overwrite an existing key if it already exists:
        stringIntegerHashMap.put("Third", 4); //key third is now 4
        stringIntegerHashMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) no particular order

        //putIfAbsent will only add an entry if it doesn't already exist, it will never overwrite
        stringIntegerHashMap.putIfAbsent("Third", 5); //key third is still 4
        stringIntegerHashMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) no particular order

        //remove will try to remove a key value pair, it returns the value if it was removed, null if nothing was removed
        System.out.println(stringIntegerHashMap.remove("DOESNOTEXIST")); //null

        //replace will try to replace the value of the specified key with the specified value
        //It returns the original value or null if nothing was replaced
        System.out.println(stringIntegerHashMap.replace("DOESNOTEXIST", 0)); //null

        //replaceAll will try to perform the operation specified in a BiFunction(K key, V value, V return)
        //on all entries of the map
        stringIntegerHashMap.replaceAll((k, v) -> k.length()*v); //replaces all values with the string length multiplied by the initial value
        stringIntegerHashMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) no particular order

        //size returns the size of the Map
        stringIntegerHashMap.put(null, 1);
        //counts all keys, null is counted as a key!
        System.out.println(stringIntegerHashMap.size()); //4
        System.out.println(stringIntegerHashMap.get(null)); //1

        //NOTE: null cannot be added as a key in treemap
        //stringIntegerTreeMap.put(null,1);
        //Can however be added as a value:
        stringIntegerTreeMap.put("THIRD",null);

        //values() returns a Collection<V> of all values:
        Collection<Integer> toCollection = stringIntegerHashMap.values();
        ArrayList<Integer> arrayList = new ArrayList<>(toCollection);

        //merge(K key, Value v, BiFunction(V oldValue, V newValue, V returnValue) does a few things:
        //if the Key doesn't exist, it adds an entry(K,V) to the map
        //if they Key does exists, but the value is null, it adds Value V to the entry with key K
        //if the Key and value do exist, and the result of the BiFunction is null, remove the entry
        //if the Key and value do exists, and the result of the BiFunction isn't null, replace oldValue with returnValue
        stringIntegerHashMap.merge("Second", 4, (v1, v2) -> v2); //replaces the value of key second with 4
        stringIntegerHashMap.merge("Second", 8, (v1, v2) -> v1); //doesn't change the value of key second
        stringIntegerHashMap.forEach((a, b) -> System.out.println("Key =" + a + "| Value =" + b)); //NB) no particular order
    }


    private void sorting() {
        ////COMPARABLE////
        //Collections.sort uses the comparable interface to compare
        //A treeset/treemap needs either a Comparator or Comparable class to function
        class AnimalNotComparable{
            int id;
            String name;
            AnimalNotComparable(int id){
                this.id = id;
            }
            public String toString(){
                return String.valueOf(id);
            }
        }

        TreeSet<AnimalNotComparable> treeSet = new TreeSet<>();
        //If you don't have a comparable object, you will get a RunTimeException (ClassCast) when adding to the tree!
        //treeSet.add(new AnimalNotComparable(1));

        //To create a comparable class, implement the Comparable interface:
        class AnimalComparable implements Comparable<AnimalComparable> {
            String name;
            //All you need is the compareTo method!
            public int compareTo(AnimalComparable comparedWith){
                //return 0 if equal
                //return <0 if this instance should come before comparedWith
                //return >0 if this instance should come after comparedWith
                //You should take null into account
                if(comparedWith == null) return 1; //null is always smaller
                if(name == null && comparedWith.name == null) return 0; //both null is equal
                if(name == null) return -1; //null is always smaller
                if(comparedWith.name == null) return 1; //null is always smaller

                //Sort reverse alphabetically based on name:
                return comparedWith.name.compareTo(name);

                //Remember: for strings this.a.compareTo(a) sorts alphabetically ascending
                //For ints: this.a - a sorts ascending
            }
            public AnimalComparable(String name){
                this.name = name;
            }
            public String toString(){
                return this.name;
            }
        }

        TreeSet<AnimalComparable> treeSet1 = new TreeSet<>();
        treeSet1.add(new AnimalComparable("A"));
        treeSet1.add(new AnimalComparable("B"));
        treeSet1.forEach(System.out::println);

        ////COMPARATOR////
        //If your class does not implement comparable, you can use a comparator
        //This one sorts ascendingly
        Comparator<AnimalNotComparable> animalNotComparableComparator = (d1, d2) -> d1.id - d2.id;
        //This does the same:
        Comparator<AnimalNotComparable> animalNotComparableComparator2 = Comparator.comparing(d -> d.id);
        TreeSet<AnimalNotComparable> treeSetComparator = new TreeSet<>(animalNotComparableComparator2);
        treeSetComparator.add(new AnimalNotComparable(1));
        treeSetComparator.add(new AnimalNotComparable(2));
        treeSetComparator.add(new AnimalNotComparable(2));
        treeSetComparator.forEach(System.out::println);

        //You can chain comparators by using thenComparing:
        Comparator<AnimalNotComparable> animalNotComparableComparator3 = Comparator.comparing((AnimalNotComparable d) -> d.id).
                thenComparing(d -> d.name == null ? "" : d.name); //Sorts by id first, and then by name if tied
        //beware comparing must account for null values

        TreeSet<AnimalNotComparable> treeSetComparator1 = new TreeSet<>(animalNotComparableComparator3);
        treeSetComparator1.add(new AnimalNotComparable(1));
        treeSetComparator1.add(new AnimalNotComparable(2));
        treeSetComparator1.add(new AnimalNotComparable(2));
        treeSetComparator1.forEach(System.out::println);

        //Comparator is useful for sorting and searching
        record Rabbit(int id){}
        List<Rabbit> rabbits = new ArrayList<>(List.of(new Rabbit(3),
                new Rabbit(1),new Rabbit(5), new Rabbit(4), new Rabbit(2), new Rabbit(7)));
        //Wont compile without comparator: Collections.sort(rabbits);
        Comparator<Rabbit> rabbitComparator = (r1, r2) -> r1.id() - r2.id();
        Comparator<Rabbit> rabbitComparator2 = Comparator.comparingInt(Rabbit::id);
        Collections.sort(rabbits, rabbitComparator);
        rabbits.forEach(r -> System.out.print(r.id)); //neatly sorted 1,2,3,4,5,7
        System.out.println();
        //Comparator can be used with binarySearch, provided the collection is ordered with the same comparator first:
        System.out.println(Collections.binarySearch(rabbits, new Rabbit(3), rabbitComparator)); //returns 2
        System.out.println(Collections.binarySearch(rabbits, new Rabbit(6), rabbitComparator)); //returns -6

        //You can also use sort directly on a list/arraylist:
        List<Rabbit> rabbits2 = new ArrayList<>(List.of(new Rabbit(3),
                new Rabbit(1),new Rabbit(5), new Rabbit(4), new Rabbit(2), new Rabbit(7)));
        rabbits2.sort(rabbitComparator);
        rabbits2.forEach(r -> System.out.print(r.id)); //neatly sorted 1,2,3,4,5,7
        System.out.println();
    }
}
