import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CRMServiceTest {
    private final LocalDate today = LocalDate.of(2026, 9, 26);
    private CRMService service;

    @BeforeEach
    void setUp() {
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
        // 10,000*.80 + 5,000*.60 + 2,000*.50 = 12,000
        assertEquals(12_000.0, service.totalWeightedPipelineValue(), 0.001);
    }

    @Test
    void priorityScoreMatchesNextBestActionRules() {
        Lead james = service.getLeads().get(0);
        // overdue 40 + value>=5000 25 + Negotiation 20
        assertEquals(85, service.priorityScore(james, today));
        assertEquals("Contact Now", service.suggestedNextAction(james, today));
    }

    @Test
    void prioritiesAreSortedByScoreThenWeightedValue() {
        List<CRMService.PriorityItem> priorities = service.todayPriorities(today, 3);

        assertEquals("James", priorities.get(0).lead().getName());
        assertEquals("Maria", priorities.get(1).lead().getName());
        assertEquals("David", priorities.get(2).lead().getName());
    }
}
