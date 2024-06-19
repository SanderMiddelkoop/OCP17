package OCPEssentials;

import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class C13Concurrency {
    public static void main(String... concurrency) {
        new A_ThreadIntro().introducingThreads();
        new B_CreatingThreadsWithConcurrencyApi().concurrencyApi();
        new C_ThreadSafeCode().threadSafeCode();
        new D_ConcurrentCollections().concurrentCollections();
        new E_IdentifyingThreadingIssues().identifyingThreadingIssues();
        new F_ParallelStreams().parallelStreams();
    }

}


class A_ThreadIntro {
    private static int counter;
    public void introducingThreads() {
        //A thread is the smallest unit of execution an operating system can schedule
        //A process is a group of associated threads that execute in the same shared environment
        //    -> A single-threaded process is a process that contains one thread
        //    -> A multi-threaded process is a process that supports multiple threads

        //A shared environment means shared memory and the ability to communicate
        //A task is a single unit of work performed by a thread

        /** Concurrency **/
        //Concurrency is the property of executing multiple threads processes at the same time
        //Operating Systems use a thread scheduler to schedule threads and allot time to them
        //When a process is not complete when the scheduled time is over a context switch occurs:
        //     A context switch means that the state of the thread is stored and restored later
        //     This is a costly operation

        //A thread can have priority over another thread.

        /** Creating Threads **/
        //A thread can be created by creating a new Thread object and passing an implementation of the Runnable interface
        //  to its constructor. The runnable interface is a functional interface with a void run() method:
        Thread firstThread = new Thread(() -> System.out.print(" Thread 1 "));
        Thread secondThread = new Thread(() -> System.out.print(" Thread 2 "));
        Thread thirdThread = new Thread(() -> System.out.print(" Thread 3 "));

        //Threads may be run by using Thread.start() :
        System.out.println("Starting all ... ");
        firstThread.start();
        secondThread.start();
        thirdThread.start();
        System.out.print(" Succesfully tried to start all threads ");
        //Note that there is no guarantee in which order threads will be started. This program will begin by printing starting all..
        //But after that it might print Thread 2 Thread 3 Succesfully tried to start all threads Thread 1
        //Or any of that in any order.
        //These threads are called asynchronous tasks -> the main thread does not wait for any of these lines to finish before
        //  continuing execution.

        //Note! Calling run() on a thread instead of start will execute the runnable, but it will do so in the main thread.
        //No new thread will be started, and it will not be asynchronous.

        //Because Thread itself implements Runnable, you can also create a Thread by making your own class that Extends Thread,
        //  and overriding the run method.

        /** Thread Types **/
        //System Threads are Threads created by the JVM, and they run in the background of the application
        //User-defined Threads are created by the developer.
        //A daemon thread is a thread that will NOT prevent the JVM from exiting once the main program has finished execution
        //You can set a thread to be a daemon thread by using:
        //thirdThread.setDaemon(true); //Now if the main method exits, and this thread would still be active, the JVM still shuts down

        /** Thread Life Cycles **/
        //A thread is initialized in a NEW STATE
        //When start() is called, it transitions into RUNNABLE STATE
        //When a Thread is RUNNABLE, it may not be running. It may transition from RUNNABLE to BLOCKED, WAITING or TIMED-WAITING
        //When a Thread is done, it transitions to TERMINATED

        //Polling is the process of intermittently checking data
        //You can use Thread.sleep(int milliseconds) to tell a THREAD to go into TIMED-WAITING for a period of time
        //This enabled you do something only every once in a while:
        Thread mainThread = Thread.currentThread(); //Get the currently active thread;
        Thread increment = new Thread(() -> {
            for(int i = 0; i<999_000_000; i++) counter++;
                //You can tell a Thread to interrupt another thread
                //Calling interupt() on a thread will transition its state from WAITING/TIMED_WAITING to RUNNABLE
                //But it will cause an InterruptedException which has to be handled when calling Thread.sleep();
                mainThread.interrupt();
        }); //Increment the counter
        increment.start();

        System.out.println(counter);
        while(counter < 900_000_000){
            System.out.println("Counter is now at:" + counter);
            try {
                Thread.sleep(10); //Try to access counter ever 10milliseconds by making the thread go asleep
            } catch (InterruptedException e) { //This is activated when a Thread is interrupted
                System.out.println("INTERRUPTED!");
                break; //You can leave the loop when interrupted;
            }
        }
    }
}

class B_CreatingThreadsWithConcurrencyApi {
    public void concurrencyApi() {
        Thread firstThread = new Thread(() -> System.out.print(" Thread 1 "));
        Thread secondThread = new Thread(() -> System.out.print(" Thread 2 "));
        Thread thirdThread = new Thread(() -> System.out.print(" Thread 3 "));

        /** Single Thread Executor **/
        //You can use the ExecutorService interface which defines services that create and manage threads
        //A Single Thread Executor only runs one thread concurrently, but it does it asynchronously still
        ExecutorService singleThreadEx = Executors.newSingleThreadExecutor();
        singleThreadEx.execute(firstThread);
        singleThreadEx.execute(secondThread);
        singleThreadEx.execute(thirdThread);
        //Prints Thread 1, Thread 2, Thread 3 in orderly fashion

        //It is important to correctly shutdown a thread, because an executor creates a non-daemon thread that will never
        //terminate otherwise
        //Shutting down an executor:
        //    first rejects any submitted tasks -> isShutdown() = true, isTerminated() = false
        //    then it will await all currently running tasks and shutdown -> isShutdown() = true, isTerminated() = true
        singleThreadEx.shutdown();
        //If you don't want to await currently running tasks, you can use shutdownNow();

        /** Submitting Tasks **/
        //You can submit a task by execute, submit or invoke.
        //executor.execute(Runnable r) -> executes the task, returns nothing
        //executor.submit(Runnable r) -> executes task and returns a Future<T> object which can be polled to see
        //  whether the task has completed, and it returns the outcome of the task
        //executor.invokeAll and invokeAny can be used to submit a list of tasks, invokeAny will wait for any task to complete
        //  and returns the result of that task, invokeAll will wait for all tasks to complete and returns a list of futures

        ExecutorService singleThreadEx2 = Executors.newSingleThreadExecutor();
        Future<?> voided = singleThreadEx2.submit(() -> System.out.println("Submitted!"));
        //A future is an object that may hold a result at some point in the future, and can be polled

        try {
            //get() waits until there is a result:
            System.out.println(voided.get()); //null
            //You can also specify a time limit:
            System.out.println(voided.get(10, TimeUnit.MILLISECONDS));
            System.out.println(voided.isCancelled()); //Check if cancelled
            System.out.println(voided.isDone()); //Check if completed, threw exception or was cancelled


            /** Callable **/
            //A callable is similar to Runnable, but it may throw an Exception and return a type
            //It is a generic class, you have to specify the type it returns:
            Callable<String> stringCallable = () -> "Called"; //Simple callable;
            Future<String> stringFuture = singleThreadEx2.submit(stringCallable);
            System.out.println(stringFuture.get()); //How to get the result of the callable


            //The cancel method may be used the cancel the execution of the task, it returns a boolean that indicates whether
            //  cancellation was succesful
            //System.out.println(voided.cancel(true)); //Check if completed, threw exception or was cancelled


        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println(e.getMessage());
        } finally {
            singleThreadEx2.shutdown();
        }


        //You can wait for an executor to finish by using the awaitTermination() method after shutting it down:
        try {
            singleThreadEx2.awaitTermination(10, TimeUnit.MILLISECONDS); //Wait max 10ms until complete termination
            if (singleThreadEx2.isTerminated()) { //Check if executor is actually terminated
                System.out.println("IS TERMINATED!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /** Scheduling Tasks **/
        //It is useful to be able to activate a process or task every now and then, or every x seconds
        //The concurrency API offers ScheduledExecutors for this, the simplest way to create one is:
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        //now you can schedule a task in the following ways:
        //1) Schedule a task to run once, after a delay of x Time Units (100 milliseconds):
        scheduledExecutorService.schedule(() -> System.out.println("1"), 100, TimeUnit.MILLISECONDS);
        //2) Schedule a task to run every x Time Units, after an initial delay of y Time Units (y = 0, x = 10ms)
        scheduledExecutorService.scheduleAtFixedRate(() -> System.out.println("2"), 0, 10, TimeUnit.MILLISECONDS);
        //3) Schedule a task to run x Time Units after completion of the last task, after an initial delay of y:
        scheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("3"), 0, 10, TimeUnit.MILLISECONDS);

        try {
            Thread.sleep(100);
            scheduledExecutorService.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /** Increasing Concurrency **/
        //All previous examples are used for scheduling and starting single-threaded processes
        //The ExecutorService can be used to create multithreaded processes:

        //Creates a thread pool that creates new threads if needed, and reuses previously constructed threads when available
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //Does the same as above, but reuses only a fixed number of threads and will wait for threads to become available
        //  when the number is reached
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

        //Same as above, but offers scheduling
        ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        //A pooled Executor will execute tasks concurrently, while a single-threaded executor will not.
    }
}

class C_ThreadSafeCode {
    private int corruptedInt = 0;
    private void incrementMyInt(){
        //it is possible that the operation that reads the corruptedInt, returns the same value in 2 concurrently running threads
        //In that case, you will do corruptedInt = corruptedInt + 1 on the same int twice, resulting in loss of data
        corruptedInt++;
        System.out.print(" - " + corruptedInt);
    }
    private ExecutorService corruptMyInt(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            C_ThreadSafeCode example = new C_ThreadSafeCode();
            for (int i = 0; i < 10; i++) executorService.submit(example::incrementMyInt);
        } finally {
            executorService.shutdown();
            return executorService;
        }
    }
    public void threadSafeCode(){

        //Thread safety is an important concept. When using multiple thread you risk the same data being accessed and written
        //to at the same time (concurrently). This means that you might corrupt variables:
        System.out.println("Example of concurrently reading and writing:");
        //The below method reads from a private variable and writes to it in multiple threads concurrently
        //The consequence of this is that numbers may be output in different order, or some numbers may be skipped entirely
        //or some may appear twice
        try {
            corruptMyInt().awaitTermination(100,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        //The volatile keyword is the first step towards writing thread-safe code.
        //Making a variable volatile, means that any writes to the variable will be IMMEDIATELY visible to other threads
        //This only fixes the issue of a possible write operation being missed in another thread
        //It doesn't fix doing concurrent read and write operations such as with a ++ operator.
        //It's still entirely possible that while one thread reads the value, another thread is incrementing it.

        /** Atomic Classes **/
        //Atomic classes offer ways for the class to be read and written to at the same time, in a thread-safe manner
        //There are three important Atomic Classes:
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        AtomicLong atomicLong = new AtomicLong(0L);

        //Atomic Classes have a few operations which can be done on them in a thread-safe manner:
        atomicInteger.set(5); //sets it to the newValue
        System.out.println(atomicInteger.getAndSet(10)); //Retrieves the old value of the int and sets it to the newValue

        //For numeric classes you have:
        System.out.println(atomicInteger.incrementAndGet()); //:: 11 - Thread safe way of saying ++i
        System.out.println(atomicInteger.getAndIncrement()); //:: 11 - Thread safe way of saying i++
        System.out.println(atomicInteger); //:: 12
        System.out.println(atomicInteger.decrementAndGet()); //:: 11 - Thread safe way of saying --i
        System.out.println(atomicInteger.getAndDecrement()); //:: 11 - Thread safe way of saying i--
        System.out.println(atomicInteger); //:: 10

        synchronizedBlocks();
        lockFramework();
        cyclicBarrier();

    }

    private void incrementMyIntSynchronized(){
        //A thread safe way to do this
        synchronized (this) { //If needed you can synchronize statically by doing synchronized(C13Concurrency.class)
            corruptedInt++;
            System.out.print(" - " + corruptedInt);
        }
    }

    //When using synchronized(this) in a method, a functionally equivalent way of doing so is by
    //  using the synchronized keyword:
    private synchronized void incrementMyIntSynchronized2(){} //This would also work with static methods

    private ExecutorService doNotCorruptMyInt(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            C_ThreadSafeCode example = new C_ThreadSafeCode();
            for (int i = 0; i < 10; i++) executorService.submit(example::incrementMyIntSynchronized);
        } finally {
            executorService.shutdown();
            return executorService;
        }
    }

    /** Synchronized blocks **/
    private void synchronizedBlocks() {
        //Atomic values are nice for simple operations, but what if you had to execute a series of operations,
        //in a thread-safe manner?
        //You can use a synchronized block to ensure that code within the block is never executed concurrently
        //It used a monitor Object to lock execution of the code for that instance
        synchronized (this) { //synchronize the code, locked to this instance
            //Any code in this block will not be ran concurrently
            System.out.println("Synchronized block started");
        }


        //A completely thread safe example of the first code, is this:
        try {
            doNotCorruptMyInt().awaitTermination(100,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

    /** The Lock Framework **/
    private void lockFramework(){
        //A synchronized block is limited in functionality, you cannot check whether a lock is available
        //Or tell the system to do something when it is not available, or prevent the system from waiting forever for a lock
        //To become available.
        //To facilitate this, Java offer the Lock Interface. This is similar to using synchronized but with more functionality.

        //A ReentrantLock  as an implementation of a lock, it can be used as follows:
        //It can be instantiated with a parameter "fairness" passed to the constructor,
        //  if you set this to true, locks will be granted in the order they are requested,
        //                                          this is costly in terms of performance
        Lock lock = new ReentrantLock();
        try {
            lock.lock(); //lock, any code after this is synchronized/threadsafe

        } finally {
            lock.unlock(); //ALWAYS unlock the SAME amount of times you locked, or the thread will NEVER become available
            //               Attempting to unlock a lock which isn't locked, will give you a Runtime Exception
        }

        //A normal lock() operation will wait until the Thread can request the lock indefinitely until succesful
        //If you want to have more control over this, you can use tryLock()

        //Try lock will attempt to retrieve the lock for the thread, if it cannot be obtained it will return false
        //Else it will return true
        if(lock.tryLock()){
            try {
            //Finally, you can also tell the Lock to wait x amount of time to try to attempt the lock, method will return false
            //   after x seconds if the lock still hasn't been obtained:
            if(lock.tryLock(10, TimeUnit.MILLISECONDS)){}
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            } finally {
                lock.unlock();
            }
        }
    }

    private void shout(CyclicBarrier cyclicBarrier, String message1, String message2){

        try {
            System.out.print(message1 + "-");
            cyclicBarrier.await(); //Will always halt the method here until x (4) number of times this has been reached
            //After that it will continue

            System.out.print(message2 + "-"); //Notice how after this, the word "random" can appear between instances of message2
            //Because we no longer await , the output here may be something like:
            //   A-A-A-A-B-B-B-RANDOM-RANDOM-B-RANDOM-RANDOM
            System.out.print("-RANDOM-");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

    }
    /** Cyclic Barrier **/
    private void cyclicBarrier() {
        System.out.println();
        //A Cyclic Barrier class can be used to tell a thread to wait until the await() method has been called an x numer of times
        //It can be instantiated as such :
        CyclicBarrier wait4Executions = new CyclicBarrier(4); //Wait for 4 thread completions
        CyclicBarrier wait4ExecutionsAndPrint = new CyclicBarrier(4,
                () -> System.out.println("Cyclic barrier reached")); //Wait for 4 completions and execute Runnable upon completion
        var exService = Executors.newFixedThreadPool(4);

        //This example will always wait until
        for (int i = 0; i < 4; i++) exService.submit(() -> shout(wait4Executions, "A", "B"));
        exService.shutdown();
        try {
            exService.awaitTermination(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("INTERRUPTED");
        }
    }
}

class D_ConcurrentCollections {
    public void concurrentCollections(){
        System.out.println();
        //A memory consistency error occurs when two threads have two different views of what should be the same data
        //A normal collection is nonconcurrent, when two threads try to modify the same nonconcurrent collection
        //   a ConcurrentModificationError may be thrown at runtime :
        var nonConcurrent1 = new HashMap<String,String>();
        var concurrent1 = new ConcurrentHashMap<String,String>();
        nonConcurrent1.put("Key1","Value1");
        nonConcurrent1.put("Key2","Value2");
        concurrent1.put("Key1","Value1");
        concurrent1.put("Key2","Value2");
        try {
            for(String key:nonConcurrent1.keySet()){
                System.out.println("removing key : "+key);
                nonConcurrent1.remove(key);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Concurrent modification error occurred");
            //This exception happens because the iterator on the keyset is not updated when the element is removed
        }

        for(String key:concurrent1.keySet()){
            concurrent1.remove(key);
        }
        System.out.println(nonConcurrent1);
        System.out.println(concurrent1);

        //If you do multithreaded operations on collections, you SHOULD use Concurrent classes to store these collections
        //                                     -> This doesn't apply to immutable collections for obvious reasons
        //A list of concurrent collections is:
        Map<String, String> concurrentMap = new ConcurrentHashMap<>(); //Concurrent map variant
        Map<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>(); //Concurrent map variant, sorted
        Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>(); //concurrent queue variant
        Set<String> concurrentSkipListSet = new ConcurrentSkipListSet<>(); //concurrent set, sorted

        //A copyOnWrite List/Set makes a copy of the collection every time a reference inside the collection
        //   is mutated (change, add, delete). It then updates the original reference to match the copy.
        //   Effectively, this means that inside an iterator the list will not be updated.
        //     It does not support remove actions.
        List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

        //Collections util also offers methods to convert collection/list/set/map to a synchronized variant
        //Better to use the concurrent versions if possible
        Collections.synchronizedCollection(concurrentQueue);
        Collections.synchronizedMap(concurrentMap);
        Collections.synchronizedSet(concurrentSkipListSet);
        Collections.synchronizedList(copyOnWriteArrayList);
    }
}

class E_IdentifyingThreadingIssues {
    public void identifyingThreadingIssues(){
        //Liveness is the ability of an application to execute in a timely manner
        //There are 3 types of liveness issues associated with threading

        /** Deadlock **/
        //Deadlock occurs when two threads are both waiting on eachother and therefore are stalled indefinitely.
        //This happens if two threads both lock resources that the other thread needs, and only release them when finished
        //   executing the complete operation.
        /** Livelock **/
        //Similar to deadlock, except that the two threads locking eachother are still active. They are constantly trying
        //to resolve the deadlock, but in an uneffective way.

        /** Starvation **/
        //Starvation occurs when a single thread is requesting a lock on a resource, but never gets it because
        //Other threads are constantly occupying the resource. The Thread is still active but never gets the resources
        //It needs


        /** Race conditions **/
        //Race conditions occur when two tasks that should run sequentially, run concurrently.
        //For example, if you have an application that allows a user to input data, checks if it already exist in a DB
        //   and then stores it, you could have unexpected outcomes when you allow two users to do this concurrently.
        //   They might both pass the uniqueness check, and you will accidentally allow 2 insertions.
    }
}

class F_ParallelStreams {
    private int returnInt(int a){
        return a;
    }

    public void parallelStreams(){
        //Streams have built-in concurrency support, meaning you can process streams in parallel
        //You can create a parallel stream by:
        List<Integer> integers = new ArrayList<>(List.of(1,2,3));
        Stream<Integer> integerStream = integers.stream().parallel();
        Stream<Integer> integerStream2 = integers.parallelStream();

        //A parellel decomposition is the process of taking a task, breaking it into smaller pieces
        //   and performing those pieces of work concurrently
        //You can create a parallel piece of work by doing:
        integerStream.map(this::returnInt).forEach(s -> System.out.println("C:"+s));
        //The consequence is the work is done in random order
        //There is a useful utility to at least output the results in the same order as it was inputted:
        integerStream2.map(this::returnInt).forEachOrdered(s -> System.out.println("D:"+s));

        //Non-parallel example which does output in orderly fashion
        integers.stream().map(this::returnInt).forEach(System.out::println);

        /** Parallel reductions **/
        //Operations such as findFirst(), skip() and limit() do still work on parallel streams!

        //A parallel reduction is a reduction operation used on a parallel stream. It used the combiner
        //  to combine the results of intermediate parallel processes.
        //  For example, the following example might have used two threads and the accumulator has produced one result
        //  of "AB", and one result of "CD",
        //  in that case, the combiner will be used to process the intermediate results
        //  the streaming API makes sure that the intermediate results are processed in the right order!
        String result = List.of("A","B","C","D").parallelStream().reduce("",(s1, id) -> s1 + id, (s2,s3) -> s2+s3);
        System.out.println(result); //:: ABCD

        //However, you have to make sure that the combiner always produces the same result as the accumulator
        //  for example:
        int resultInt = List.of(1,2,3,4).parallelStream().reduce(0, (a,b) -> a-b);

        System.out.println(resultInt);

        // Let's say this stream produced 2 intermediate results: the result of the operation 0-1-2 = -3
        //                                                    and the result of the operation 0-3-4 = -7
        //    now the combiner does -3 - - 7, and the result of this will be 4

        //In contrast to doing 1-2-3-4 = -8
        int resultInt2 = List.of(1,2,3,4).stream().reduce(0, (a,b) -> a-b);
        System.out.println(resultInt2); //:: -10

        //Or including the right combiner:
        int resultInt3 = List.of(1,2,3,4).parallelStream().reduce(0, (a,b) -> a-b, (a,b) -> {
            System.out.println("a:"+a +":b:"+b);return a+b;});
        System.out.println(resultInt3); //:: -10


        /** Using collect() **/
        //Collect also includes a combiner that can be used in a similar way as the reduce combiner:
        Stream<String> stringStream = Stream.of("A","B","C","D").parallel();
        Set<String> stringSet = stringStream.collect(ConcurrentSkipListSet::new, ConcurrentSkipListSet::add, ConcurrentSkipListSet::addAll);
        stringSet.forEach(System.out::println); //Combines intermediate HashSets

        //Parallel reduction requires the collector to be UNORDERED, there are two static collectors that can do this:
        //  1) Collectors.groupingByConcurrent()
        //  2) Collectors.toConcurrentMap()

        StringBuilder test = new StringBuilder();
        //Normally, end results of streams are ordered even if you do them in parallel.
        //Buf if you access variables from outside the stream, this might not be the case
        Stream<String> stringStream2 = Stream.of("A","B","C","D").parallel();
        stringStream2.forEach(test::append); //Because you used a variable from outside the stream, it can output in any order!
        System.out.println(test);
    }
}