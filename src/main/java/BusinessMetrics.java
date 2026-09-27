/**
 * Snapshot of owner-facing Customer & Revenue Intelligence metrics.
 */
public class BusinessMetrics {
    private final int activeOpportunities;
    private final double totalPipelineValue;
    private final double weightedPipelineValue;
    private final double averageActiveDealValue;
    private final long wonCount;
    private final long lostCount;

    private BusinessMetrics(int activeOpportunities,
                            double totalPipelineValue,
                            double weightedPipelineValue,
                            double averageActiveDealValue,
                            long wonCount,
                            long lostCount) {
        this.activeOpportunities = activeOpportunities;
        this.totalPipelineValue = totalPipelineValue;
        this.weightedPipelineValue = weightedPipelineValue;
        this.averageActiveDealValue = averageActiveDealValue;
        this.wonCount = wonCount;
        this.lostCount = lostCount;
    }

    public static BusinessMetrics from(CRMService service) {
        return new BusinessMetrics(
                service.getActiveLeads().size(),
                service.totalPipelineValue(),
                service.totalWeightedPipelineValue(),
                service.averageActiveDealValue(),
                service.countByStatus("Won/Closed") + service.countByStatus("Won"),
                service.countByStatus("Lost/Closed") + service.countByStatus("Lost")
        );
    }

    public int getActiveOpportunities() {
        return activeOpportunities;
    }

    public double getTotalPipelineValue() {
        return totalPipelineValue;
    }

    public double getWeightedPipelineValue() {
        return weightedPipelineValue;
    }

    public double getAverageActiveDealValue() {
        return averageActiveDealValue;
    }

    public long getWonCount() {
        return wonCount;
    }

    public long getLostCount() {
        return lostCount;
    }
}
