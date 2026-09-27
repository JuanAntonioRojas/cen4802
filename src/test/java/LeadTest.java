import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeadTest {





    @Test
    void weightedValueUsesEstimatedValueTimesProbability() {
        //  $10,000 at 45% should give a weighted value of $4,500.
        Lead lead = new Lead(1, "A", "Co", "Qualified", 10_000, 0.45, LocalDate.now());

        assertEquals(4_500.0, lead.getWeightedValue(), 0.001);
    }





    @Test
    void wonAndLostAreClosed() {
        Lead won = new Lead(1, "A", "Co", "Won/Closed", 1_000, 1.0, null);
        Lead lost = new Lead(2, "B", "Co", "Lost/Closed", 1_000, 0.0, null);

        assertTrue(won.isClosed());
        assertTrue(lost.isClosed());
    }





    @Test
    void probabilityOutsideZeroToOneIsRejected() {
        //  120% is not a valid probability.
        assertThrows(IllegalArgumentException.class,
                () -> new Lead(1, "A", "Co", "Qualified", 1_000, 1.20, LocalDate.now()));
    }
}
