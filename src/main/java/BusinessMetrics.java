//  This class holds the four numbers shown at the top of the SSCRM page.
public class BusinessMetrics {
    private final int activeOpportunities;
    private final double totalPipelineValue;
    private final double weightedPipelineValue;
    private final double averageActiveDealValue;
    private final long wonCount;
    private final long lostCount;





    private BusinessMetrics(int activeOpportunities, double totalPipelineValue,
                            double weightedPipelineValue, double averageActiveDealValue,
                            long wonCount, long lostCount) {

        this.activeOpportunities = activeOpportunities;
        this.totalPipelineValue = totalPipelineValue;
        this.weightedPipelineValue = weightedPipelineValue;
        this.averageActiveDealValue = averageActiveDealValue;
        this.wonCount = wonCount;
        this.lostCount = lostCount;
    }





    public static BusinessMetrics from(CRMService service) {
        //  Get each value separately so it is easy to see where every number comes from.
        int active = service.getActiveLeads().size();
        double pipeline = service.totalPipelineValue();
        double weighted = service.totalWeightedPipelineValue();
        double average = service.averageActiveDealValue();
        long won = service.countByStatus("Won/Closed") + service.countByStatus("Won");
        long lost = service.countByStatus("Lost/Closed") + service.countByStatus("Lost");

        return new BusinessMetrics(active, pipeline, weighted, average, won, lost);
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
