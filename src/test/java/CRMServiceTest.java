import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CRMServiceTest {
    private final LocalDate today = LocalDate.of(2026, 9, 26);
    private CRMService service;



    @BeforeEach
    void setUp() {
        //  Every test starts with the same five leads.
        service = new CRMService(List.of(
                new Lead(1, "James", "Apex", "Negotiation", 10_000, 0.80, today.minusDays(1)),
                new Lead(2, "Maria", "BlueSky", "Proposal Sent", 5_000, 0.60, today),
                new Lead(3, "David", "Cedar", "Qualified", 2_000, 0.50, today.plusDays(2)),
                new Lead(4, "Betty", "Titan", "Won/Closed", 20_000, 1.00, null),
                new Lead(5, "Mark", "Willow", "Lost/Closed", 8_000, 0.00, null)
        ));
    }


    @Test
    void activePipelineExcludesWonAndLost() {
        assertEquals(3, service.getActiveLeads().size());
        assertEquals(17_000.0, service.totalPipelineValue(), 0.001);
    }


    @Test
    void weightedPipelineUsesOnlyActiveOpportunities() {
        //  10,000 x .80 + 5,000 x .60 + 2,000 x .50 = 12,000.
        assertEquals(12_000.0, service.totalWeightedPipelineValue(), 0.001);
    }



    @Test
    void actionScoreFindsTheMostUrgentLead() {
        Lead james = service.getLeads().get(0);

        //  Overdue 40 + value 25 + Negotiation 20 = 85.
        assertEquals(85, service.actionScore(james, today));
        assertEquals("Contact Now", service.actionNeeded(james, today));
    }





    @Test
    void needsActionIsSortedByScore() {
        List<CRMService.ActionItem> actions = service.needsAction(today, 3);

        assertEquals("James", actions.get(0).getLead().getName());
        assertEquals("Maria", actions.get(1).getLead().getName());
        assertEquals("David", actions.get(2).getLead().getName());
    }
}
