package OCPEssentials;

import java.time.ZonedDateTime;

public class C3MakingDecisions {
    public static void main(String... args){
        statementAndBlocks();
        ifStatement();
        patternMatching();
        switchStatements();
        switchExpressions();
        whileLoops();
        forLoops();
        breakStatement();
        continueStatement();
        returnStatement();
    }

    private static void statementAndBlocks() {
        //A java statement is a unit of execution, terminated with a semicolon ;
        //Control flow statements break up execution flow by using decision-making, looping and branching
        //   This allows the application to selectively execute lines of code
        //A block of code is a group of zero or more statements between braces ({})
    }

    private static void ifStatement() {
        //The if statement allows you to execute a block of code only under certain circumstances
        //If no block is specified, it will only apply to the next statement:
        //The following is exactly the same
        if(true)
        System.out.println("if true 1");
        //If statement is terminated here
        //As this
        if(true)
        {
            System.out.println("if true 2");
            //Blocks allow you to execute multiple statements under if
        }

        //The else statements allows you to specify the behavior of the program is the if evaluation is not true
        if(false){
            System.out.println("This won't be reached");
        }else{
            System.out.println("this will be reached");
        }

        //Lastly you can use else if :
        if(false){
            System.out.println("This won't be reached");
        }else if (false){
            System.out.println("this won't be reached either");
        }else{
            System.out.println("this will be reached");
        }

        //You can only use boolean expressions. This won't compile: if(1){}
    }


    private static void patternMatching() {
        //Pattern matching can be used to shorten code where you check whether an object is an instance of a certain object
        //Before we had to do:
        Number aNumber = 10L;
        if(aNumber instanceof Integer){
            Integer anInteger = (Integer) aNumber;
            System.out.println(anInteger);
        }

        //Now you can automatically cast and assign the variable by using the following:
        if(aNumber instanceof Integer aSecondInteger){
            System.out.println(aSecondInteger);
        }

        //Please note that assigning to the variable will only occur if the if statement is actually true! This won't work:
        if(!(aNumber instanceof Integer aSecondInteger)){
            //So this won't compile: System.out.println(aSecondInteger);
        }

        //These also won't work for similar reasons:
        //if(aNumber instanceof Integer aThirdInteger | aThirdInteger==10){}
        //if(aNumber instanceof Integer aThirdInteger ^ aThirdInteger==10){}
        //if(aNumber instanceof Integer aThirdInteger || aThirdInteger==10){}
        //if(aNumber instanceof Integer aThirdInteger & aThirdInteger==10){}
        //But this will:
        if(aNumber instanceof Integer aThirdInteger && aThirdInteger==10){}
        //You can only cast and assign to a subtype of the variable type on the left:
        if(aNumber instanceof Long aLong){} //this works
        //this won't: if(aNumber instanceof Number aNumber){}

        //The casted and assigned variable can be used outside of the scope of the if statement only if it is certain it is of that type outside it:
        if(aNumber instanceof Long aLong){
            System.out.println(aLong);
        }
        //this won't work, since you are not sure it is of the type Long outside of this if statement: System.out.println(aLong);

        //But this will work:
        if(!(aNumber instanceof Long aLong)){
            //Cannot use aLong here because it specifically not in the scope of this block: System.out.println(aLong); won't compile
            return;
        }else{
            //but it will be in scope here
            System.out.println(aLong);
        }
        //And even here:
        System.out.println(aLong);
    }


    private static void switchStatements() {
        //A switch statements allows you to evaluate a single value and redirect flow to matching branches, called cases
        //If no matching case is found, you can call a default
        //If no default is defined, statement will be skipped entirely:

        //This is an example
        int test = (int) (Math.random()*10+1);
        switch (test){
            //The break assures that the switch is exited after the case is evaluated, so the next cases won't be executed
            case 1:System.out.println("first case:" + test); break;
            //you can collapse multiple cases into one, and you don't need to define a block
            case 2,3,4:System.out.println("second case:" + test); break;
            default: System.out.println("default case:" + test);
        }

        //You don't need cases:
        switch (test){}; //Is a perfectly valid statement.
        //Brackets are mandatory: switch (test); won't compile

        //If you don't exit with breaks, the next cases will be executed:
        switch (test){
            //If case 1 is true, the code for case2,3,4 and default will also be evaluated
            case 1:System.out.println("first case #2:" + test);
            //If case 2, 3, 4 is true, default will also be evaluated
            case 2,3,4:System.out.println("second case #2:" + test);
            default: System.out.println("default case #2:" + test);
        }

        //Default can be at the beginning/anywhere, but it makes little sense to do so,
        //it will always be evaluated last
        switch (test){
            //The default is first but will never be executed because all possible cases are covered
            default: System.out.println("default case #3:" + test); break;
            case 1:System.out.println("first case #3:" + test); break;
            case 2,3,4:System.out.println("second case #3:" + test); break;
            case 5,6,7,8,9,10:System.out.println("third case #3:" + test); break;

        }

        //You can only use a switch expression on a int, Integer, byte, Byte, short, Short, char, Character, String or Enum
        //This won't work: switch(10L){}

        //You can only use case types that are compile-time constant values, of the same type of the evaluated type

        final int caseFinalAcceptable = 2;
        final int caseFinalNotAcceptable;
        caseFinalNotAcceptable = 3;
        switch (test){
            //You can use literals:
            case 1: {
                System.out.println("You can use a literal");
                break;
            }
            //You can use final variables that have been initialized on the same line as it was assigned
            case caseFinalAcceptable:
                System.out.println("You can use this final variable");
                break;
            //Can't use a final variable that was initialized later: case caseFinalNotAcceptable:
            //You can't use a method or a final variable that has been determined by a method.

            //Finally you can also use Enum constants.
        }
    }

    private static void switchExpressions() {
        //Switch expressions are a more compact way to do switches introduced in Java 14
        int test = (int) (Math.random()*10+1);

        //Example of a switch expression:
        switch (test){
            case 1,2,3 -> System.out.println("First case:" + test);
            case 4,5,6 -> System.out.println("Second case:" + test);
            case 7,8,9 -> System.out.println("Third case:" + test);
        }

        //You don't need breaks here.It will only evaluate the first case that matches.
        //Also, you DO need {} brackets if you wan't to evaluate multiple statements

        //You can also use the switch the return a value and assign it to a variable:
        int yield = switch (test){
            case 1,2,3 -> 10;
            case 4,5,6 -> 20;
            //The datatypes returned must be consistent: this won't compile: case 7,8,9 -> 30L;
            //You can however switch longs and ints if the return type is a long,
            //because ints fit inside longs, but not the other way around
            case 7,8,9 -> 30;
            //In that case, you need to cover ALL POSSIBLE CASES. So you need a default:
            //The only case where you don't need a default, is when you use an Enum and you covered all possible cases
            default -> 40;
        }; //Don't forget the semicolon that's needed here because you assigned the value to a variable

        //A switch statement can also use a code block. If you want/need to return a value inside this block, you can use 'yield'
        int yield2 = switch (test){
            case 1,2,3 -> 10;
            case 4,5,6 -> 20;
            case 7,8,9 -> 30;
            default -> {
                if(test<100) {
                    yield 40;
                    //All branches must yield something, so else with yield is mandatory!
                } else{
                    yield 50;
                }
            }
        };
    }

    private static void whileLoops() {
        //A loop is a control flow that allows you to repeat a certain block of statements a certain number of times
        //A while loop allows you to evaluate a statement, and execute a code block if the statement is true
        //This will then repeat until the evaluated statement is not true

        //Example of the while loop:
        int i = 0;
        while(i<=10)
            //Just like with if, if no code block {} is defined, the first statement is all that will be executed
            System.out.print(i++);

        //If the expression evaluates to false the first time, the code block in the while loop will NEVER execute.
        //If you want to ensure the while loop executes AT LEAST once, you can move the evaluation to the end of the loop
        //By using a do/while statement:
        i = 0;
        do
            //Again if no code block is defined, the first statement will be executed
            System.out.print(i++);
        while (i<=10);

        //If you don't update the evaluated variable or do nothing in the block, you risk creating an infinite loop. This will
        //Crash your program at runtime. Beware of this.
    }


    private static void forLoops() {
        //A for loop allows you to loop through a sequence, it allows you to initialize a variable,
        //evaluate an expression and provide the update statement all at once

        //These are two examples
        int i;
        //i = 0 initializes the variable
        //i < 10 is the evaluation
        //i++ is the update statements that happens at the end of the executed block
        for(i=0; i<10; i++){
            System.out.print(i);
        }

        for(int i2=0; i2<10; i2++){
            System.out.print(i2);
        }

        //None of the statements is mandatory, you can theoretically do this if you reuse variables that you have already defined.
        for(;i<15;i++){
            System.out.print(i);
        }

        //You can even do this, but please don't it will loop infinitely without the break;
        for(;;){
            System.out.println("Oops");
            break;
        }

        //The evaluation is done before code execution, this will never execute once:
        for(int i3 = 0; i3<0; i3++){
            System.out.println("Unreachable");
        }

        //You can do multiple initializations and updates:
        for(int i4 = 0, i5=0; i4<10; i4++, i5+=2){
            System.out.print("i4:"+i4+"|i5:"+i5);
        }

        //You cannot redeclare a variable that was already declared
        //wont compile: for(int i = 0;;){}

        //You cannot use variables defined in the for loop outside of it

        //An alternative form of te for loop is the enhanced for loop, or the for-each loop.
        //it can be used to iterate over any class that implements the Iterable/Collection interface, or any java Array
        //It's used as follows
        int[] intArray = {1,2,3};
        for(int arrayMember : intArray){
            System.out.println(arrayMember);
        }

        //The types must be compatible, you cannot do this:
        //for(byte arrayMember: intArray){};

        //but you can do this:
        for(long arrayMember : intArray);
    }

    private static void breakStatement(){
        //A break statement allows you to exit a code block in a loop.
        for(int i = 1; i<=3; i++){
            System.out.println(i);
            if(i>1){
                //Exits the loop if i>1
                break;
            }
        }

        //You can add labels to loops. This allows you to exit out of an outer loop:
        System.out.println("BREAK OUT OF OUTER LOOP:");
        int count = 0;
        OUTER_LOOP: for(int i = 1; i<=3; i++){
            for(int j = 1; j<=3; j++){
                System.out.println(j);
                count++;
                if(count>6){
                    //Allows you to break out of the entire outer loop as well
                    break OUTER_LOOP;
                }
            }
        }
    }

    private static void continueStatement(){
        //Continue is similar to break, but it just stops execution of the block and goes right into the next iteration:
        for(int i = 1; i<=3; i++){
            if(i==2){
                //Ignores this execution if i==2, but still does the next iteration
                continue;
            }
            System.out.println(i);
        }

        //Continue can also be used with labels:
        System.out.println("CONTINUE WITH OUTER LOOP:");
        int count = 0;
        OUTER_LOOP: for(int i = 1; i<=3; i++){
            for(int j = 1; j<=3; j++){
                if(count==6){
                    //Allows you to skip the sixth execution
                    count++;
                    continue OUTER_LOOP;
                }
                System.out.print(j);
                count++;

            }
        }
    }

    private static void returnStatement(){
        //A return statement exits the entire method you're in.
        //This is often more elegant than using labels in combination with breaks and continue.
        int[][] matrix = { {1,2,3}, {4,5,6}, {7,8,9} };
        //Example methods to show how return works
        int[] positionOf10InMatrix = searchForValue(matrix, 10);
        int[] positionOf6InMatrix = searchForValue(matrix, 6);
        System.out.println("Position of 10:" + positionOf10InMatrix[0] + "," + positionOf10InMatrix[1]);
        System.out.println("Position of 6:" + positionOf6InMatrix[0] + "," + positionOf6InMatrix[1]);

    }

    private static int[] searchForValue(int[][] matrix, int valueToSearch){
        int posX = 0;
        for (int[] array : matrix) {
            int posY = 0;
            for (int value : array) {
                if (value == valueToSearch) {
                    //return coordinates and break out of the method
                    return new int[]{posX, posY};
                }
                posY++;
            }
            posX++;
        }
        return new int[]{-1,-1};
        //Code after return or break or continue is deemed unreachable and will not compile
        //System.out.println("This won't compile");
    }
}
