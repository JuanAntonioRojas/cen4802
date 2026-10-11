import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PipelineHealthTest {
    private final LocalDate today = LocalDate.of(2026, 10, 11);





    private Lead lead(int id, String status, LocalDate followUp) {
        return new Lead(id, "Customer " + id, "Company", status, 1000, 0.5, followUp);
    }





    @Test
    void healthyWhenNoActiveFollowUpsAreDue() {
        CRMService service = new CRMService(List.of(
                lead(1, "New Lead", today.plusDays(1)),
                lead(2, "Qualified", null)
        ));
        assertEquals("Healthy", service.pipelineHealth(today));
    }





    @Test
    void needsAttentionWhenFollowUpIsDueToday() {
        CRMService service = new CRMService(List.of(lead(1, "Qualified", today)));
        assertEquals("Needs Attention", service.pipelineHealth(today));
    }





    @Test
    void needsAttentionWhenExactlyOneFollowUpIsOverdue() {
        CRMService service = new CRMService(List.of(
                lead(1, "Negotiation", today.minusDays(1)),
                lead(2, "Qualified", today)
        ));
        assertEquals("Needs Attention", service.pipelineHealth(today));
    }





    @Test
    void atRiskWhenTwoFollowUpsAreOverdue() {
        CRMService service = new CRMService(List.of(
                lead(1, "Qualified", today.minusDays(2)),
                lead(2, "Negotiation", today.minusDays(1))
        ));
        assertEquals("At Risk", service.pipelineHealth(today));
    }





    @Test
    void closedLeadsDoNotCountTowardRisk() {
        CRMService service = new CRMService(List.of(
                lead(1, "Won/Closed", today.minusDays(3)),
                lead(2, "Lost/Closed", today.minusDays(2)),
                lead(3, "Qualified", today.plusDays(1))
        ));
        assertEquals("Healthy", service.pipelineHealth(today));
    }





    @Test
    void emptyPipelineIsHealthy() {
        CRMService service = new CRMService(List.of());
        assertEquals("Healthy", service.pipelineHealth(today));
    }
}
