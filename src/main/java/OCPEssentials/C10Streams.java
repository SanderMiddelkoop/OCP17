package OCPEssentials;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.*;

public class C10Streams {
    public static void main(String... args) {
        new OptionalLesson().opionals();
        new CreatingStreamsLesson().creatingStreams();
        new StreamTerminalOperationsLesson().terminalOperations();
        new StreamIntermediateOperationsLesson().intermediateOperations();
        new PrimitiveStreamLesson().primitiveStreams();
        new AdvancedStreamConceptsLesson().advancedStreamConcepts();
    }
}

class OptionalLesson {
    public void opionals() {
        //An optional is a 'box' that may have an object in it, or it may be empty
        //You can create an optional by using a factory method
        Optional<String> emptyOptional = Optional.empty(); //Optional containing nothing
        Optional<String> hiOptional = Optional.of("hi"); //Optional containing the String "hi"

        //Optional that can either be empty, if the value is null, or contains the value
        String a = Math.random() > 0.5 ? null : "bye";
        Optional<String> schrodingersOptional = Optional.ofNullable(a); //ofNullable automatically makes empty optional
                                                                        //if value passed is null

        //You can check if an optional contains something/whether it is empty:
        if (emptyOptional.isPresent()) System.out.println(emptyOptional.get()); //This will never execute
        if (hiOptional.isPresent()) System.out.println(hiOptional.get()); //Prints hi
        if (schrodingersOptional.isPresent()) System.out.println(schrodingersOptional.get()); //Random chance whether this will print bye or not

        //Doing get() on an empty optional results in a NoSuchElementException

        //A quick way to automatically perform some function if the optional isPresent, is by using the method
        //ifPresent(Consumer<? super T> c) -- you can use it as follows:
        hiOptional.ifPresent(System.out::println); //prints hi via method reference
        hiOptional.ifPresent((String lambda) -> System.out.println(lambda)); //prints hi via long form lambda
        hiOptional.ifPresent((CharSequence lambda) -> System.out.println(lambda)); //prints hi via convoluted long form lambda

        //You can specify a 'default' String you want to return if the optional is empty
        //You can either do this by passing a direct value or reference to a direct value
        //This is done by the method orElse(T t) :
        String defaultString = "default";
        System.out.println(emptyOptional.orElse(defaultString)); //prints default

        //You can also do this by passing a Supplier<T> that returns the default value you want
        //This is done by the method orElseGet(Supplier<? extends T> s) :
        Supplier<String> defaultStringSupplier = String::new; //Supplier that returns a new String
        System.out.println(emptyOptional.orElseGet(defaultStringSupplier));

        //Finally, you can throw a NoSuchElementException
        //This is done by the method orElseThrow() :
        try {
            System.out.println(emptyOptional.orElseThrow());
        } catch (NoSuchElementException e){
            System.out.println("NoSuchElementException caught");
        }

        //You can also supply a Throwable by using the method orElseThrow(Supplier<? extends Throwable>)
        Supplier<RuntimeException> runtimeExceptionSupplier = RuntimeException::new;
        try {
            System.out.println(emptyOptional.orElseThrow(runtimeExceptionSupplier));
        } catch (RuntimeException e){
            System.out.println("RunTimeException caught");
        }
    }
}

class CreatingStreamsLesson {
    public void creatingStreams() {
        //A stream is a sequence of data
        //A stream pipeline is a set of operations that run on a stream, that produces a result
        //Stream can be infinite or finite, finite streams have a limit
        //A stream pipeline has 3 parts:
          //Part 1) The source -> this is where the stream originates from
          //Part 2) The intermediate operations -> a transformation that transforms the datastream into a different datastream
          //Part 3) The terminal operation -> an operation that converts the datastream to a result

        //Streams can be used only once, if the terminal operation completes, the original stream is no longer valid!
          //This means that you can only have one terminal operation, while you can have multiple intermediates
        //Intermediate operations are not mandatory, you can have a stream that directly calls a terminal operation
          //A terminal operation IS required to create a useful pipeline
        //A terminal operation does NOT return a stream, an intermediate operation always returns a stream of (different) data

        creatingStreamSources();

    }

    private void creatingStreamSources() {
        //You can create finite streams with factory methods
        //empty() to create an empty Stream:
        Stream<String> emptyStream = Stream.empty();
        //of(T... ts) to create a Stream of the input
        List<String> stringList = new ArrayList<>(List.of("1","2","3"));
        Stream<String> finiteStringStream = Stream.of(stringList.toArray(new String[0]));
        Stream<String> finiteStringStream2 = Stream.of("1","2","3");

        //You can also create a Stream directly from a Collection:
        Stream<String> fineStringStream3 = stringList.stream();

        //You can also create infinite streams with 2 factory methods:
        //generate(Supplier<? extends T>) to create an infinite stream of objects that result from the call to the supplier
        Supplier<Double> doubleSupplier = Math::random;
        Stream<Number> infiniteNumberStream = Stream.generate(doubleSupplier);
        //iterate(T seed, UnaryOperator<T> operator) will create a stream starting with the seed,
        //and endlessly perform the UnaryOperator to update the seed
        UnaryOperator<String> stringUnaryOperator = (x -> {
            char a = x.charAt(x.length()-1);
            return x + ++a;
        });
        Stream<String> infiniteStringStream = Stream.iterate("a",stringUnaryOperator);
        //Performs the iteration 26 times, and then returns the result of the last iteration and prints ifPresent
        infiniteStringStream.limit(26).reduce((a,b) -> b).ifPresent(System.out::println); //prints alphabet

    }
}

class StreamTerminalOperationsLesson {
    public void terminalOperations() {
        //A reduction reduces all of the contents of the Stream to a single datatype (single primitive or Object)
        //A simple reduction is counting the number of data entries in a stream
        //This is done by count()
        System.out.println(Stream.of("1","2","3").count()); //3
        //Another simple reduction is using min(Comparator<? super T>) and
        //max(Comparator<? super T>) to get the minimum or maximum value in a stream
        System.out.println(Stream.of("Z","x","c").min(Comparator.naturalOrder()).get());
        System.out.println(Stream.of("Z","x","c").max(Comparator.naturalOrder()).get());
        System.out.println(Stream.of("Z","x","c").min((x,y) -> x.compareTo(y)).get()); //Same as natural order
        System.out.println(Stream.of("zzz","xx","y").min((x,y) -> x.length() - y.length()).get()); //Returns lowest length string

        //Beware that calling count/max/ming on an infinite String will result in a neverending operation:
        //Stream.generate(Math::random).count();

        //If you want to return the first element of a Stream, you can use findFirst():
        System.out.println(Stream.of("Z","x","c").findFirst().get()); //Z
        //If you just want to return any element, you can use findAny()
        System.out.println(Stream.of("Z","x","c").findAny().get()); //Probably Z

        //You can check if any data entry in your stream matches a certain predicate
        //This is done by : anyMatch(Predicate <? super T>) :
        System.out.println(Stream.of("Z","x","c").anyMatch((String::isBlank))); //false
        //Similarly you can check if all data matches : allMatch(Predicate) or no data matches : noneMatch(Predicate)

        //You can perform an operation defined in a Consumer on the elements in a stream
        //This is done by : forEach(Consumer<? super T>) :
        Stream.of("Z","x","c").forEach(System.out::print); //Zxc
        System.out.println();
        //This is particularly handy since Streams aren't iterable, you cant loop over them via a normal for loop

        //A reduce() method combines all data in the stream into a single object
        //You can reduce in 3 way
        //    A) reduce(T identity, BinaryOperator<T> accumulator)
        //       This starts out with the identity Object, and continously performs the operation defined in te accumulator
        //       on the current result (starting with identity) and the next item in the stream
        //       for example, the below starts out with an empty String (identity)
        //       and concats the current result with the next item in the stream
        //       it starts out with "" -> "Z" -> "Zx" -> "Zxc" -> DONE
        System.out.println(Stream.of("Z","x","c").reduce("",String::concat)); //Zxc
        System.out.println(Stream.of("Z","x","c").reduce("1",String::concat)); //1Zxc
        System.out.println(Stream.of("Z","x","c").reduce("1",(first, second)->first.concat(second))); //Different syntax, same thing

        //   B) reduce(BinaryOperator<T> accumulator)
        //      this does the same thing but starts with the first element in a stream instead of the identity
        //      it returns an Optional because the result is empty if the stream is empty
        System.out.println(Stream.of("Z","x","c").reduce(String::concat).get()); //Zxc

        //  C) reduce(U identity, BiFunction(U, T, U) accumulator, BinaryOperator<U> combiner
        //     this allows you to first convert the data to another intermediate type using the accumulator,
        //     and then combine the results of the intermediate operations
        //     for example, the below starts with an int 0, and for each String element adds the length of the String to the result
        //     the combiner is used for example if the Stream is handled in parallel
        //     in this example we start with 0 -> 0 + Z.length() [1] -> 1 + x.length() [2] -> 2 + c.length() [3] --> 3
        //     if we processed in parallel it could be that one pipeline ends at 2, and the other ends at 1
        //     the BinaryOperator is then used to combine these results
        System.out.println(Stream.of("Z","x","c").reduce(0, (first, second) -> first + second.length(), Integer::sum)); //3

        //Lastly there is the collect() method. This is useful for reducing a stream to a collection.
        //It is a mutable reduction because it uses a mutable object to perpetually change this object while accumulating
        //Collecting can be done in 2 ways:
        //  A) collect(Supplier<U> supplier, BiConsumer<U, ? super T> accumulator, BiConsumer<U,U> combiner
        //     the supplier creates the object that stores the result, for example a new ArrayList or a stringbuilder
        //     the accumulator is the operation that is used to mutate the result of the supplier for each entry in the stream
        //     the combiner is used to combine intermediate results similarly to with reduce(), it is useful for parallel processing
        System.out.println(Stream.of("a","b","c")
                .collect(() -> new StringBuilder("START"), StringBuilder::append, StringBuilder::append)); //STARTabc

        //This wouldn't work with an immutable object because we use consumers and not functions, the result of the function is ignored
        System.out.println(Stream.of("a","b","c")
                .collect(() -> new String("START"), String::concat, String::concat)); //START

        //  B) collect(Collector)
        //     this is an implementation provided by Java which automatically creates Collectors that are commonly used:
        Stream.of("a","b","c")
                .collect(Collectors.toCollection(HashSet::new)) //Converts to HashSet
                .forEach(System.out::print); //Prints each element of HashSet

        Stream.of("a","b","c")
                .collect(Collectors.toSet()) //Converts to Set
                .forEach(System.out::print); //Prints each element of HashSet
        System.out.println();
    }
}

class StreamIntermediateOperationsLesson {
    public void intermediateOperations() {
        //Intermediate operations result in a Stream of (probably) different data
        //filter<Predicate<? super T> filter the data in the stream based on a predicate
        //it KEEPS entries where the predicate evaluates to TRUE
        Predicate<Integer> integerPredicate = integer -> integer>5; //filters values over 5
        Stream.of(1,2,3,4,5,6,7,8).filter(integerPredicate).forEach(System.out::print); //678

        System.out.println();
        //distinct() removes duplicates based on the equals() method
        Stream.of(10,10,10,5,5,7).distinct().forEach(System.out::print); //1057

        System.out.println();
        //limit(x) only processes x number of entries
        //skip(x) skips the first x number of entries
        Stream.iterate(1,x->x+1).skip(5).limit(3).forEach(System.out::print); //6 7 8
        Stream.iterate(1,x->x+1).limit(3).skip(5).forEach(System.out::print); //no output

        System.out.println();
        //map() can be used to convert each element in a stream to a new element
        //the elements can even be converted to a different type
        Stream.of(1,2,3).map(x->x+1).forEach(System.out::print); //maps each integer to original value +1 -> 234
        System.out.println();
        Stream.of("a","aa","aaa").map(String::length).forEach(System.out::print); //maps each string to int representing length -> 123

        System.out.println();
        //flatMap can be used to convert each element of the data to an element containing just the data type
        //this is handy if your stream contains Lists of data, and you just want to return a stream of what the list contains
        //not a stream of lists
        Stream.of(List.of(1,2), List.of(3,4), List.of(5,6)) //a stream of 3 lists
                .flatMap(x -> x.stream()) //convert each list to a stream
                .forEach(System.out::print); //123456

        System.out.println();
        //You can use sort() to sort the stream based on natural order :
        Stream.of(3,4,1,2).sorted().forEach(System.out::print); //1,2,3,4
        //You can also use a Comparator :
        System.out.println();
        Stream.of(3,4,1,2).sorted((x,y) -> y-x).forEach(System.out::print); //4,3,2,1
        System.out.println();
        Stream.of(3,4,1,2).sorted(Comparator.comparingInt(Integer::intValue).reversed()).forEach(System.out::print); //4,3,2,1
        System.out.println();
        Stream.of(3,4,1,2).sorted(Comparator.reverseOrder()).forEach(System.out::print); //4,3,2,1

        System.out.println();
        //Finally, you can execute a Consumer on the elements of the Stream similar to forEach, but without ending the stream
        //This is done via the peek() method :
        Stream.of(6,6,6).peek(System.out::print).forEach(System.out::print); //666666

        //Note that an intermediate operation is not done unless there is a terminating operation:
        Stream.of(6,6,6).peek(System.out::print); //Prints nothing!
    }
}

class PrimitiveStreamLesson {
    public void primitiveStreams() {
        //Java provided some useful utility for working with streams that have primitive numbers
        //There are 3 types of primitive streams: IntStream, DoubleStream and LongStream

        System.out.println();
        //You can easily compute the min,max,sum and average of a primitive stream:
        DoubleStream.of(1.2,1.5,1.3).min().ifPresent(System.out::println); //1.2
        DoubleStream.of(1.2,1.5,1.3).max().ifPresent(System.out::println); //1.5
        //Note how sum does not return an Optional because an empty primitive stream has a sum of 0
        System.out.println(DoubleStream.of(1.2,1.5,1.3).sum()); //4.0
        DoubleStream.of(1.2,1.5,1.3).average().ifPresent(System.out::println); //1.333

        //In addition to the normal factory methods to create streams, you can use range to create a range of primitives
        //This only works with ints and longs because it iterates based on whole numbers
        //The stream ends at the second argument -1, so range(1,10) prints all numbers from 1 to 9
        LongStream.range(50, 55).forEach(System.out::print); //5051525354
        System.out.println();
        //You can use rangeClosed if you want to include the second argument
        LongStream.rangeClosed(50, 55).forEach(System.out::print); //505152535455

        System.out.println();
        //You can use boxed() to convert the primitive stream to a wrapper stream
        LongStream.of(1,2,3).boxed() //converts stream of long to stream of Long
                .forEach(x -> System.out.print(x.byteValue())); //1,2,3

        System.out.println();
        //You can use mapToInt, mapToDouble, mapToLong to convert any existing stream to a primitive stream
        Stream.of("1","2","3").mapToDouble(Double::parseDouble) //mapping String stream to primitive stream
                .forEach(System.out::print); //1.02.03.0

        System.out.println();
        //If you use a primitive stream, the map method will only map to the same type
        //If you want to map to a different type, you can use mapToObj
        IntStream.of(1,20,300).mapToObj(Integer::toString) //Converts stream of ints to stream of strings
                .mapToInt(String::length) //Converts stream of strings back to stream of ints
                .forEach(System.out::print); //123

        System.out.println();
        //summaryStatistics can be used on a primitive stream to return a SummaryStatistics object
        IntSummaryStatistics stats = IntStream.of(1,2,3).summaryStatistics();
        //This allows you to obtain count, sum, average, max, min at once without having to call multiple streams:
        System.out.println(stats.getMax()+stats.getMin()+stats.getCount()+stats.getAverage()+stats.getSum()); //15.0
    }
}

class AdvancedStreamConceptsLesson {
    public void advancedStreamConcepts() {
        //Streams are lazily evaluated, this means that the stream is only evaluated once a terminal operation is requested
        //This means that if you change the underlying data, it will affect the stream if it is not yet terminated
        //Take the following example:
        List<Integer> intList = new ArrayList<>(List.of(1, 2, 3));
        Stream<Integer> stream1 = intList.stream();
        intList.add(4);
        stream1 = stream1.filter(x -> x > 3);
        intList.add(5);
        stream1.forEach(System.out::print); //Only now the stream is evaluated, the stream iterates over 1,2,3,4,5
        //                                    it then filters out 1,2,3, and prints 45

        System.out.println();
        //You can use map on an optional:
        Optional<String> emptyOptString = Optional.empty();
        Optional<String> hiOptString = Optional.of("hi");
        //Maps optional to A, and ifPresent prints, prints
        emptyOptString.map(x -> "A").ifPresent(System.out::println); //Prints nothing because Optional is empty
        hiOptString.map(x -> "A").ifPresent(System.out::println); //A
        hiOptString.map(String::length).ifPresent(System.out::println); //1

        //If your map returns another optional, you can use flatMap
        hiOptString.map(x -> Optional.of("A")).ifPresent(System.out::println); //Optional[A]
        hiOptString.flatMap(x -> Optional.of("A")).ifPresent(System.out::println); //A

        spliterator();
        collectingResults();

    }

    private void spliterator() {
        //A spliterator is a utility to split a collection or stream into two halves
        //You can create one by using spliterator() on a stream or List
        List<Integer> intList2 = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        Stream<Integer> intStream = intList2.stream();
        Spliterator<Integer> spliterator1 = intList2.spliterator();
        Spliterator<Integer> spliterator2 = intStream.spliterator();

        //When calling trysplit on a spliterator it splits the original spliterator in half and returns the other half:
        Spliterator<Integer> spliterator1FirstHalf = spliterator1.trySplit();

        System.out.println(spliterator1.getExactSizeIfKnown()); //3
        System.out.println(spliterator1FirstHalf.getExactSizeIfKnown()); //3

        //tryAdvance will execute a Consumer for the next element in the spliterator if there is one
        //It will return true if it was executed, false if there was no next element:
        spliterator1.tryAdvance(System.out::print); //4
        spliterator1FirstHalf.tryAdvance(System.out::print); //1
        System.out.println();
        //tryAdvance removes the element!
        System.out.println(spliterator1.getExactSizeIfKnown()); //2
        System.out.println(spliterator1FirstHalf.getExactSizeIfKnown()); //2

        System.out.println();
        //forEachRemaining will execute a Consumer for all remaining elements in the spliterator, and remove the elements
        spliterator1.forEachRemaining(System.out::print); //56
        spliterator1FirstHalf.forEachRemaining(System.out::print); //23
        System.out.println(spliterator1.tryAdvance(System.out::println)); //false, no elements remain

        //You can use a spliterator and trysplit on an infinite stream, just don't do forEachRemaining because it will never end
    }

    private void collectingResults() {
        //There are a bunch of predefined collectors Java gives you
        //Collectors.joining(CharSequence x) allows you to reduce the stream to a string, separated by x
        //It requires a Stream of strings
        System.out.println(Stream.of(1,2,3,4).map(String::valueOf).collect(Collectors.joining("-")));
        System.out.println(Stream.of(1,2,3,4).map(Object::toString).collect(Collectors.joining("-")));

        //AveragingInt, AveragingDouble, AveragingLong can be used to calculate the average
        //It requires a function that maps the input element to a int/double/long first
        //It returns a double
        System.out.println(Stream.of(1,2,3,4,0).collect(Collectors.averagingInt(Integer::intValue))); //2.0

        //SummarizingInt, SummarazingDouble, SummarazingLong works similarly:
        System.out.println(Stream.of(1,2,3,4,0).collect(Collectors.summarizingInt(Integer::intValue)).getMax()); //4

        //Summing sums the values:
        System.out.println(Stream.of(1,2,3,4,0).collect(Collectors.summingDouble(Integer::intValue))); //10.0

        //counting returns the number of elements
        System.out.println(Stream.of("1",2,3,4).collect(Collectors.counting())); //4

        //You can collect a stream into a map using toMap, it requires you to input a function calculating the key,
        //and a function calculating the value at least:
        Map<String, Integer> stringLengthMap = Stream.of("a","aa","aaa")
                .collect(Collectors.toMap(s -> s, String::length)); //maps stream of strings to key = string, value = string.length
        System.out.println(stringLengthMap); //{a = 1, aa = 2, aaa = 3}

        //When you create a duplicate key, java will throw an IllegalStateException
        try {
            Stream.of("a", "aa", "aaa").collect(Collectors.toMap(s -> "a", String::length));
        } catch (IllegalStateException e){
            System.out.println("IllegalStateException caught");
        }

        //If this is possible, you have to provide a binaryOperator to tell java what to do with the duplicate values:
        //For example, you can tell java to ignore the second value:
        System.out.println(Stream.of("a", "aa", "aaa")
                .collect(Collectors
                        .toMap(s -> "a", String::length,(first,second)->first))); //{a=1}
        //Or to concatenate strings:
        System.out.println(Stream.of("a", "aa", "aaa","bbb")
                .collect(Collectors
                        .toMap(s -> s.length(), s->s,String::concat))); //{1=a,2=aa,3=aaabbb}

        groupingAndPartioning();
    }

    private void groupingAndPartioning() {
        grouping();
        partitioning();
        mapping();
    }

    private static void grouping() {
        //Another way to create a map is to group the stream elements by certain characteristics
        //This can be done by the groupingBy collector
        //groupingBy creates a map with a keys that are calculated by the Function you defined
        //each key is then paired to a value containing a list of elements that have that key
        Map<Integer,List<String>> grouped = Stream.of("aa","bb","c","d","eee", "aa")
                .collect(Collectors.groupingBy(String::length)); //Group based on length of the string
        System.out.println(grouped); //{1=[c,d], 2=[aa,bb,aa], 3=[eee]

        //You can explicitly transform the mapped collection by passing another collector
        Map<Integer,Set<String>> grouped2 = Stream.of("aa","bb","c","d","eee", "aa")
                .collect(Collectors.groupingBy(String::length, Collectors.toSet())); //Group based on length of the string,
        //                                                                             no duplicates, transforms to set
        System.out.println(grouped2); //{1=[c,d], 2=[aa,bb], 3=[eee]

        //Finally, you can explicitly change the type of Map returned by passing a supplier
        TreeMap<Integer,Set<String>> grouped3 = Stream.of("aa","bb","c","d","eee", "aa")
                .collect(Collectors.groupingBy(
                        String::length,
                        TreeMap::new, //I want a treemap not a regular map!!
                        Collectors.toSet()));
        //
        System.out.println(grouped3); //{1=[c,d], 2=[aa,bb], 3=[eee]
    }

    private void partitioning() {
        //partitioning is a special kind of grouping which creates two keys: true and false
        //It allows you to reduce a stream to a list of elements that satisfy a predicate, and a list of elements that don't
        //It is done by using a Collectors.partitioningBy(Predicate)
        Map<Boolean,List<Integer>> partitionedInts = Stream.of(1,2,3,4,1)
                .collect(Collectors.partitioningBy(x -> x>2)); //Divides group in ints that are <=2 and ints that are >2
        System.out.println(partitionedInts); //{false = [1,2,1], true = [3,4]

        //Again, you can change the type of collection that is shown in the Map by using another collector:
        Map<Boolean,Set<Integer>> partitionedIntsSet = Stream.of(1,2,3,4,1)
                .collect(Collectors.partitioningBy(
                        x -> x>2,
                        Collectors.toSet())); //I really want a set to remove duplicates!
        System.out.println(partitionedIntsSet); //{false = [1,2], true = [3,4]
    }


    private void mapping() {
        //You can use mapping to use a execute a function on a stream,
        // and pass the result of that function to yet another collector
        Set<Integer> ints = Stream.of(1,2,3,4,5,6,6).collect(
                Collectors.mapping(x->x*2, //First multiply each value by 2
                        Collectors.filtering(x -> x>7, //Then collect the result using yet another collector which filters the result
                                Collectors.toSet()))); //And transforms it to a set
        ints.forEach(System.out::print); //81012
        System.out.println();

        //Admittedly, this example is not particularly useful but it can by useful in combination with groupingBy/
        //partioniningBy when another Collector is requested by the method

        //Finally, if you want to return two things from one stream you can use a teeing collector
        //the teeing collector allows you to specify two collectors, and then allows you to specify a function that
        //determines what to do with the outcome of these two collectors:
        Set<String> strings = Stream.of(1,2,3,4,5,6)
                .collect(Collectors.teeing(
                        Collectors.mapping(String::valueOf,
                                Collectors.joining("-")), //First collector maps the elements to strings and joins
                                                                   //them separated by -
                        Collectors.mapping(String::valueOf,        //Second collector maps the elements to strings and joins
                                                                   //them separated by ,
                                Collectors.joining(",")),
                        (firstResult, secondResult)
                                -> new HashSet<>(Set.of(firstResult,secondResult)) //Function telling java to put
                                                                                   // the two results in a new HashSet
                ));
        strings.forEach(System.out::println); //1,2,3,4,5,6 - 1-2-3-4-5-6

    }
}