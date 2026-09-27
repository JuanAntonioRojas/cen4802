import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusinessMetricsTest {

    @Test
    void metricsSummarizeCustomerAndRevenueIntelligence() {
        CRMService service = new CRMService(List.of(
                new Lead(1, "A", "A Co", "Negotiation", 10_000, 0.50, LocalDate.now()),
                new Lead(2, "B", "B Co", "Qualified", 20_000, 0.25, LocalDate.now()),
                new Lead(3, "C", "C Co", "Won/Closed", 7_000, 1.00, null),
                new Lead(4, "D", "D Co", "Lost/Closed", 4_000, 0.00, null)
        ));

        BusinessMetrics metrics = BusinessMetrics.from(service);

        assertEquals(2, metrics.getActiveOpportunities());
        assertEquals(30_000.0, metrics.getTotalPipelineValue(), 0.001);
        assertEquals(10_000.0, metrics.getWeightedPipelineValue(), 0.001);
        assertEquals(15_000.0, metrics.getAverageActiveDealValue(), 0.001);
        assertEquals(1, metrics.getWonCount());
        assertEquals(1, metrics.getLostCount());
    }

    @Test
    void zeroActiveDealsProduceZeroAverage() {
        CRMService service = new CRMService(List.of(
                new Lead(1, "A", "A Co", "Won/Closed", 10_000, 1.0, null)
        ));

        assertEquals(0.0, BusinessMetrics.from(service).getAverageActiveDealValue(), 0.001);
    }
}
