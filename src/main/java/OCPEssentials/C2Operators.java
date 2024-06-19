package OCPEssentials;

import java.io.File;
import java.util.Date;

public class C2Operators {


    public static void main(String... args) {
      /*
       Operators are symbols that are applicable to a set of variables/literals or values.
       The variables they operators are applied to are called operands. Simple examples are + or -.

       Type operators:
       Unary: Operator applicable to a single operant, eg a++.
       Binary: Operator applicable to a set of 2 operants, eg a + b.
       Ternary: Operator applicable to a set of 3 operants, eg a ? b : c
     */
        operatorOrder();
        unaryOperators();
        binaryArithmeticOperators();
        assignmentOperators();
        comparingOperators();
        relationalOperators();
        logicalOperators();
        ternaryOperator();
    }

    public static void operatorOrder() {
        /*
          Operator order:
          Operators don't necessarily apply from left to right, the are evaluated in order of the following priority:
             1. Post-unary (a++, a--)
             2. Pre-unary (++a, --a)
             3. Unary (-a, !a, ~a, +a, (type)a )
             4. Cast ( (Type)a)
             5. Multiplication/division/mod (a*b, a/b, a%b)
             6. Addition/subtraction (a+b,a-b)
             7. Shift (a<<b, a>>b, a>>>b)
             8. Relational (a<b, a>b, a<=b, a>=b, a instanceof b)
             9. Equality (a==b, a!=b)
             10. Logical AND (a & b)
             11. Logical xOR (a ^ b)
             12. Logical OR (a | b)
             13. Conditional AND (a && b)
             14. Conditional OR (a || b)
             15. Ternary (a ? b : c)
             16. Assignment (a = b, a += b, a -= b, a *= b, a /= b, a %= b, a &= b, a ^= b, a |= c)
             17. Arrow (a -> b)
        */

        //You can use braces ( () ) to prioritize subsections of code
        //Compare this:
        var a = 12 - 6 * 5;
        System.out.println("12 - 6 * 5 = " + a);

        //To this:
        var b = (12 - 6) * 5;
        System.out.println("(12-6) * 5 = " + b);

        //The braces need to be well balanced. Each opening brace needs to be closed. You cannot use [].
    }

    public static void unaryOperators() {
        //A unary operator takes just one operant, examples listed below:
        //Logical complement, !a:
        var complementedBoolean = true;
        System.out.println("-true = " + !complementedBoolean);

        //Bitwise complement, ~a, flips all 0s and 1s (binary)
        //This is always equal to -a - 1;
        var bitwiseComplementedInteger = 8;
        System.out.println("~8 = " + ~bitwiseComplementedInteger);

        //Plus operator, +a, specifies the number is a positive, which is pretty much always redundant
        var plusOrMinusOperatedInteger = 13;
        System.out.println("+13 = " + +plusOrMinusOperatedInteger);

        //Minus operator, -a, specifies the number is negative
        System.out.println("-13 = " + -plusOrMinusOperatedInteger);

        //Beware: --a is not the same as -(-a), the first is a different (decrement) operator,
        // the second is two minus operators applied one after eachother
        System.out.println("-(-13) = " + -(-plusOrMinusOperatedInteger));
        System.out.println("--13 = " + --plusOrMinusOperatedInteger);

        //You cannot apply operators to variables of the wrong type, !13 , +true will not compile.

        //Pre-Increment operator, ++a, increases a by 1 and returns the incremented a
        var incrementAndDecrementThisInteger = 10;
        System.out.println("++10 = " + ++incrementAndDecrementThisInteger);

        //Pre-Decrement operator, --a, decreases a by 1 and returns the decremented a
        System.out.println("--11 = " + --incrementAndDecrementThisInteger);

        //Post-Increment operator, a++, returns a and increases by 1 after.
        System.out.println("10++ = " + incrementAndDecrementThisInteger++);
        System.out.println("But the following line the integer is actually: " + incrementAndDecrementThisInteger);

        //Post-Decrement operator, a--, returns a and decreases by 1 after.
        System.out.println("11-- = " + incrementAndDecrementThisInteger--);
        System.out.println("But the following line the integer is actually: " + incrementAndDecrementThisInteger);
    }

    public static void binaryArithmeticOperators() {
        //Arithmetic operators operate on numeric values, binary arithmetic operators operate on 2 numeric values
        //Examples are:

        //Addition operator, a + b:
        System.out.println("4 + 6 = " + (4 + 6));

        //Subtraction operator, a - b:
        System.out.println("4 - 6 = " + (4 - 6));

        //Division operator a/b:
        System.out.println("4 / 6 = " + 4 / 6);

        //Dividing using ints or longs (non floating point numbers) will just result in the number before the decimal being returned
        //So even 0.999 will return 0. This is called 'the floor value' / 'flooring'.

        //Multiplication operator a*b
        System.out.println("4 * 6 = " + 4 * 6);

        //Modulus operator %, a%b returns what remains after division
        System.out.println("4 % 6 = " + 4 % 6);
        System.out.println("32 % 5 = " + 32 % 5);

        //NUMERIC PROMOTION
        //Rules for binary operations:

        //1. If two operants have different data types, Java will promote the smallest data type to the larger one automatically
        //Given:
        int a = 1;
        long b = 2L;
        //Won't compile
        //int c = a + b;
        //Will compile:
        long c = a + b;

        //2. If one of the operants is integral and the other floating-point (decimal),
        //   Java will promote the integral value to a floating point data type
        //Given
        int d = 1;
        float e = 1.1f;
        //Won't compile
        //int f = d + e;
        //Will compile
        double f = d + e;
        float g = d + e;

        //3. Smaller data types (byte, short, char) are automatically promoted to int when used in binary operations
        //Given
        byte h = 3;
        byte k = 4;
        //Won't compile
        //byte l = h + k;

        //But it will compile when u use literals and the outcome is within the byte scope:
        byte m = 3 + 4;
        //Wont compile when the number is too large: byte noBueno = 62+67;

        //Will compile:
        int l = h + k;

        //4. The result of the operation will have the same type as it's operants (see above). int + int = int.
    }

    private static void assignmentOperators() {
        //The assignment operator modifies/assigns the variable on the left side of the equation with the outcome of the right side
        //Simples form : a = 1;

        //Java will automatically promote from smaller to larger types, but the opposite will lead to compile errors:
        long a = 12; //Is possible

        //But you cannot specify a value over int max value without using L
        // wont compile : long b = 2_222_222_222;
        long b = 2_222_222_222L;

        //The opposite, larger to smaller types, cannot be done automatically:
        // wont compile : int c = 12L;

        //Casting is explicitly converting a type to another type, so that this can be done, eg:
        int c = (int) 12L;
        short d = (short) (c + 6);

        //Casting is unary and takes precedence over binary operations
        // so this won't compile:short e = (short) c + 6;
        // Because it first casts c to a short and then does the binary operation, which makes it an int again

        //COMPOUND ASSIGNMENT
        //Compount assignment are assignments that contain an arythmic operation:
        //Addition assignment, a += 3, means a = a + 3;
        int compoundAssignedInt = 5;
        System.out.println("5 += 3 = " + (compoundAssignedInt += 3));

        //Subtraction assignment, a -= 3, means a = a - 3;
        System.out.println("8 -= 3 = " + (compoundAssignedInt -= 3));

        //Multiplication assignment, a *=3, means a = a * 3;
        System.out.println("5 *= 3 = " + (compoundAssignedInt *= 3));

        //Division assignment, a /=3, means a = a / 3;
        System.out.println("15 /= 3 = " + (compoundAssignedInt /= 3));

        //Using compounds mean you don't have to cast
        long multiplyByThisLong = 5L;
        //this won't compile : compoundAssignedInt = compoundAssignedInt * multiplyByThisLong;
        //this will:
        compoundAssignedInt = (int) (compoundAssignedInt * multiplyByThisLong);
        //But it's equal to:
        compoundAssignedInt *= multiplyByThisLong;

        //Assignments return something and can be used on the right side of an assignment as well:
        int a1 = 3;

        //They are evaluated from RIGHT TO LEFT so:
        int b1 = (a1 = 4);
        System.out.println("a1 = " + a1 + " _ b1 = " + b1);
        //is equal to :
        b1 = a1 = 4;
        System.out.println("a1 = " + a1 + " _ b1 = " + b1);
    }

    private static void comparingOperators() {
        //Equality operators determine whether values/expressions are equal or not
        //Equality : a == 10
        //For primitives: this returns true if the values represent the same value:

        int comp1 = 10;
        int comp2 = 10;
        boolean comp1iscomp2 = comp1 == comp2;
        System.out.println("comp1 == comp2 = " + comp1iscomp2);

        //For low values, Integer wrappers point to a cached reference so comp3 and comp4 point to the same reference here
        Integer comp3 = 10;
        Integer comp4 = 10;
        boolean comp3iscomp4 = comp3 == comp4;
        System.out.println("comp3 == comp4 = " + comp3iscomp4);

        //For objects, it returns true if it points to the same reference
        //This is why you should use equals for Wrappers, at high values they point to different references:
        Integer comp5 = 1234567;
        //Comp6 points to a different reference.
        Integer comp6 = 1234567;
        boolean comp5iscomp6 = comp5 == comp6;
        boolean comp5equalscomp6 = comp5.equals(comp6);
        System.out.println("comp5 == comp6 = " + comp5iscomp6);
        System.out.println("comp5 equals comp6 = " + comp5equalscomp6);

        //You cannot mix types (numeric, boolean, different Objects)
        int compInt = 1;
        boolean compBool = true;
        long compLong = 1L;
        //Won't compile: compInt == compBool;  compLong == compBool

        //But you can compare different types of numbers:
        System.out.println("compInt (1) == compLong (1L) = " + (compInt == compLong));

        //You cannot mix object types
        Integer compIntWrapper = 1;
        Long compLongWrapper = 1L;
        //won't compile: compIntWrapper == compLongWrapper;

        Date a = new Date();
        File b = new File("");
        File c = new File("");
        File d = b;
        //won't compile a == b;
        System.out.println("b == c = " + (b == c));
        System.out.println("b == d = " + (b == d));

        //You can compare any object to null, but not primitives.
        //Comparing an object to null will return true only if both objects are null.
    }


    private static void relationalOperators() {
        //Relational operators compare to values and return a boolean
        //Less than operator, a < 5, returns true if a is smaller than 5, only applicable to number
        int intToCompare = 10;
        System.out.println("10 < 10 = " + (intToCompare < 10));

        //Less than, or equals, operator, a <= 5, returns true if a is smaller than or equal to 5, only applicable to number
        System.out.println("10 <= 10 = " + (intToCompare <= 10));

        //Greater than operator, a > 5, returns true if a is greater than 5, only applicable to number
        System.out.println("10 > 10 = " + (intToCompare > 10));

        //Greater than, or equals, operator, a >= 5, returns true if a is greater than, or equals, 5, only applicable to number
        System.out.println("10 >= 10 = " + (intToCompare >= 10));

        //INSTANCEOF OPERATOR
        //Determines whether a variable is an instance of a certain object. Only applies to objects.
        Integer isInstanceOfInteger = 10;
        System.out.println(isInstanceOfInteger instanceof Integer);
        System.out.println(isInstanceOfInteger instanceof Number);

        //Only possible if the object on the left hand side is compatible to the object on the right side (they need to be subtypes)
        Number isInstanceOfNumber = 10L;
        System.out.println(isInstanceOfNumber instanceof Integer);
        System.out.println(isInstanceOfNumber instanceof Number);

        //Won't compile: System.out.println(isInstanceOfNumber instanceof Date);
        //Using null on the left hand side always results into false, using null on the right hand side leads to a compiler error
    }

    private static void logicalOperators() {
        //Logical/conditional operators check two boolean expressions
        //Logical AND checks whether both expressions are true (a & b)
        boolean example1 = 10 == 10 & "bla" == "bla";
        System.out.println("10 == 10 & \"bla\" == \"bla\" = " + example1);

        //The conditional AND checks whether both expressions are true, but short circuits if the first one is FALSE
        //The first expression is false, so the second won't be done, example1 is still true:
        boolean example2 = 10 == 5 && (example1=!example1);
        System.out.println("Example2 = " + example2 + "_ example1 = " + example1);
        //Vs if the logical AND is used, example 1 will be false:
        example2 = 10 == 5 & (example1=!example1);
        System.out.println("Example2 = " + example2 + "_ example1 = " + example1);

        //Logical INCLUSIVE OR checks whether one of the expressions is true (a | b)
        boolean example3 = 10 == 10 | "bla" == "abc";
        System.out.println("10 == 10 | \"bla\" == \"abc\" = " + example3);
        //The CONDITIONAL OR (a || b) does the same check, but short circuits if the first one is TRUE, similar to the above

        //Logical EXCLUSIVE OR checks whether one of the expressions is true, and the other is false (a ^ b)
        boolean example4 = 10 == 10 ^ "bla" == "abc";
        boolean example5 = 10 == 5 ^ "bla" == "abc";
        System.out.println("Example4 = " + example4 + "_ example5 = " + example5);

        //The Logical operators can also be applied to numbers .
        //(7 | 5, 7 ^ 5, 7 & 5): These are valid statements. It will perform bitwise comparisons.
        //The output of bitwise comparisons are ints. A bitwise comparison will do a bit by bit comparison of the number.
        //It is not necessary to know how this works exactly.
    }


    private static void ternaryOperator() {
        //A ternary operator checks whether an expression a is true, and performs expression b if this is the case, and expression c if not
        //it can be written as  a ? b : c
        int outcome1 = 14 == 14 ? 20 : 14;
        int outcome2 = 14 == 12 ? 20 : 14;
        System.out.println("outcome1 = " + outcome1 + " _ outcome2 = " + outcome2);

        //types need to be equal if you want to assign it:
        //This wont compile: int outcome3 = 14 == 12 ? 8 : "willnotcompile";

        //As with conditionals, only one side of a ternary will actually be performed at runtime.
        //So with the following:
        int tracker = 0;
        int outcome3 = tracker == 0 ? 3 : tracker++;

        //The tracker won't be incremented:
        System.out.println("outcome3 = " + outcome3 + " _ tracker = " + tracker);

        //But here it will:
        int outcome4 = tracker == 2 ? 3 : ++tracker;
        System.out.println("outcome4 = " + outcome4 + " _ tracker = " + tracker);

    }
}
