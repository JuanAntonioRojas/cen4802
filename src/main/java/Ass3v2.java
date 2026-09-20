import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Random;

public class Ass3v2 {

    //  Total number of Integers to add:
    private static final int TOT_RAND_INTS = 2_000_000;



// UTILITIES:
    //  A bunch of helper functions. That way I don't have to write the same long thing a million times.

    // Overload with an argument to print any Object, String, or number
    public static void prtLn(Object obj) {
        System.out.println(obj);
    }

    //  Calculate/give the length of time in millies:
    public static long currTime() {
        return System.currentTimeMillis();
    }

    //  This is the best one: The FOR loop that adds 2 * 10^6 random integers, ZERO based:
    public static void add2MLoop(java.util.Collection<Integer> collection) {
        for (int i = 0; i < TOT_RAND_INTS; i++) {
            //  this will get 2 million random numbers (0 to 1,999,999) and then add them to the collection
            collection.add((int) ( Math.random() * 2 * Math.pow(10, 6) ) );
        }
    }









    public static void main(String[] args) {
        prtLn("Starting Benchmark with " + TOT_RAND_INTS + " elements...\n");

        // 1. ArrayList
        testArrayList();

        // 2. LinkedList
        testLinkedList();

        // 3. Hashtable
        testHashtable();
    }






    // This here thing will Add 2*10^6 (2M) random integers to an "ArrayList" then deletes each one.

    public static void testArrayList() {

        prtLn("--- Testing ArrayList ---");
        ArrayList<Integer> aList = new ArrayList<>();

        //  start the timer to add them to the A.L.
        long startAdd = currTime();

        // Inserting the numbers to the AL:
        add2MLoop(aList);

        //  Calc the time it took: stop the timer. Prt the diff.
        long endAdd = currTime();
        prtLn("ArrayList Add Time: " + (endAdd - startAdd) + " ms");



        //  Deleting from the end (aList.size() - 1) runs in O(1) per element (O(N) total).
        //  Take a looksee: Removing from index 0 would cause O(N^2) array shifts and freeze execution.
        long startDelete = currTime();
        for (int i = aList.size() - 1; i >= 0; i--) {
            aList.remove(i);
        }
        long endDelete = currTime();
        prtLn("ArrayList Delete Time (from tail): " + (endDelete - startDelete) + " ms\n");
    }





    // This other one will Add 2M random ints to an "LinkedList" then deletes each one.

    public static void testLinkedList() {
        prtLn("--- Testing with a LinkedList ---");
        LinkedList<Integer> lList = new LinkedList<>();

        //  start the timer to add them to the L.L.
        long startAdd = currTime();

        //  Add them babies to the LL
        add2MLoop(lList);

        //  Stop the timer, and calc/print the diff
        long endAdd = currTime();
        prtLn("LinkedList Add Time: " + (endAdd - startAdd) + " ms");



        //  1. If we DELETE them using removeFirst() removes the head node in O(1) time per element; a flat constant.

        //  start the timer
        long startDelete = currTime();

        while (!lList.isEmpty()) {
            lList.removeFirst();
        }

        //  Stop the timer
        long endDelete = currTime();
        prtLn("LinkedList Delete Time (from head): O(1) per removal - " + (endDelete - startDelete) + " ms\n");


        //  2. DELETE them random numbers using a random index:
        //  Now, get a gander of this: Using lList.remove(index) BY RANDOM INDEX would require O(N) pointer traversal
        //    per item (that is: following a chain of pointers to get where you want to go). Remember: O(N) per item.
        //    In other words: if you do this removal two million times, it gets really SLLOOOWWWW because you're always
        //    walking from the start instead of just quickly removing an item (like I did above).

        //  Theretofore: this is a "NO GO" from the start: because deleting 2,000,000 items this way (by random index)
        //    will take HOURS, due to O(N^2) traversal. This old laptop will freeze/choke. Sorry, but time is money.
        /*
        while (!lList.isEmpty()) {
            int randomIndex = (int) (Math.random() * lList.size());
            lList.remove(randomIndex);
        }
        */
    }








    //  This baby will add 2,000,000 random integers to the HASH TABLE, and then deletes each one, based on its key

    public static void testHashtable() {

        prtLn("--- Testing Hashtable ---");

        Hashtable<Integer, Integer> hTable = new Hashtable<>(TOT_RAND_INTS);
        Random rand = new Random();

        //  1. Time in:
        long startAdd = currTime();

        //  2. Insertions:
        for (int i = 0; i < TOT_RAND_INTS; i++) {
            //  If we use 'i' as the key guarantees 2M unique entries without hash overwrites
            hTable.put(i, rand.nextInt());
        }
        //  3. Time out:
        long endAdd = currTime();
        //  4. Prt Difference:
        prtLn("Hashtable Add Time: " + (endAdd - startAdd) + " ms");


        //  5. Time-in for the Removals
        long startDelete = currTime();

        //  6. Deleting directly by key gives an average O(1) constant lookup per entry
        for (int i = 0; i < TOT_RAND_INTS; i++) {
            hTable.remove(i);
        }
        //  7. Time out:
        long endDelete = currTime();
        //  8. Prt Difference:
        prtLn("Hashtable Delete Time (by key): " + (endDelete - startDelete) + " ms\n");
    }
}