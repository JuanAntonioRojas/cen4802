// We bring in special helper tools from JUnit so we can check if our toys work.
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// We bring in three different kinds of toy boxes to store our numbers.
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

// We bring in the magic whistle (assertions) that screams if something goes wrong.
import static org.junit.jupiter.api.Assertions.*;


//  This class is our playground laboratory.
//  We want to make sure putting things in and taking things out of our toy boxes
//     doesn't break them or make items magically disappear.

public class Ass3v2Test {

    //  Think of ArrayList like a row of cubbies glued side by side.
    private ArrayList arrayList;

    //  Think of LinkedList like a toy train where each train car holds one passenger and links hands with the car behind it.
    private LinkedList linkedList;

    //  Think of Hashtable like a giant post office wall with labeled mailboxes.
    private Hashtable hashtable;

    //  We decide to play with exactly 10,000 toy blocks for our test.
    private static final int TEST_SAMPLE_SIZE = 10_000;



    //  @BeforeEach means: Before we play ANY game below, clean up the floor and
    //    give us brand new, shiny, empty boxes so old messes don't confuse us.

    @BeforeEach
    void setUp() {
        arrayList = new ArrayList<>();
        linkedList = new LinkedList<>();
        hashtable = new Hashtable<>();
    }




    //   GAME 1: Testing the Cubby Box (ArrayList)

    @Test
    @DisplayName("Verify ArrayList insertion and tail deletion integrity")
    void testArrayListIntegrity() {
        //  Step 1: Put 10,000 blocks into the cubbies, one by one.
        for (int i = 0; i < TEST_SAMPLE_SIZE; i++) {
            arrayList.add(i);
        }

        //  Check: Did all 10,000 blocks actually make it into the cubbies?
        //  Reverted back to normal
        assertEquals(TEST_SAMPLE_SIZE, arrayList.size(), "ArrayList should contain exactly 10,000 elements");

        //  Deliberate Test Failure:
        //assertEquals(9999, arrayList.size());

        //  Step 2: Take blocks out from the very back of the line first.
        //    (Taking from the back is super easy because we don't have to push other blocks around!)
        for (int i = arrayList.size() - 1; i >= 0; i--) {
            arrayList.remove(i);
        }

        // Check: Are all the cubbies completely empty now?
        assertTrue(arrayList.isEmpty(), "ArrayList should be empty after tail removal");
    }




    //   GAME 2: Testing the Toy Train (LinkedList)

    @Test
    @DisplayName("Verify LinkedList head removal behavior and empty state check")
    void testLinkedListHeadRemoval() {
        //  Step 1: Hook up 10,000 train cars in a long line.
        for (int i = 0; i < TEST_SAMPLE_SIZE; i++) {
            linkedList.add(i);
        }

        //  Check: Do we have a train with 10,000 cars?
        assertEquals(TEST_SAMPLE_SIZE, linkedList.size());

        // Step 2: Unhook the very first car at the front of the train over and over until no train cars are left on the track.
        while (!linkedList.isEmpty()) {
            linkedList.removeFirst();
        }

        //  Check: Is our train track completely clear (0 cars)?
        assertEquals(0, linkedList.size(), "LinkedList size must drop to 0 after popping all elements");
    }





    //   GAME 3: Testing the Mailbox Wall (Hashtable)

    @Test
    @DisplayName("Verify Hashtable key mapping and deletion accuracy")
    void testHashtableKeyValueMapping() {
        // Step 1: Put a letter into each mailbox.
        // Mailbox number 'i' gets a letter with the number 'i * 2' written on it.
        for (int i = 0; i < TEST_SAMPLE_SIZE; i++) {
            hashtable.put(i, i * 2);
        }

        //  Check: Did we fill 10,000 mailboxes?
        assertEquals(TEST_SAMPLE_SIZE, hashtable.size());

        //  Check: If we open mailbox #10, is the secret number inside exactly 20 (10 * 2)?
        assertEquals(20, hashtable.get(10), "Hashtable key 10 should map to value 20");

        //  Step 2: Go through and empty every single mailbox.
        for (int i = 0; i < TEST_SAMPLE_SIZE; i++) {
            hashtable.remove(i);
        }

        //  Check: If we open mailbox #10 now, is it completely empty (null)?
        assertNull(hashtable.get(10), "Key 10 should no longer exist after removal");

        //  Check: Is the entire mailbox wall empty?
        assertTrue(hashtable.isEmpty(), "Hashtable should be empty after full key removal");
    }
}