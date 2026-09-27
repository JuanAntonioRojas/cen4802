import java.time.LocalDate;

//  This class holds the information for one sales lead in SSCRM.
public class Lead {
    private final int id;
    private final String name;
    private final String company;
    private final String status;
    private final double estimatedValue;
    private final double probability;
    private final LocalDate nextFollowUp;





    public Lead(int id, String name, String company, String status,
                double estimatedValue, double probability, LocalDate nextFollowUp) {

        //  A deal cannot have a negative value.
        if (estimatedValue < 0) throw new IllegalArgumentException("estimatedValue cannot be negative");

        //  Probability is stored as a decimal from 0 to 1.
        if (probability < 0 || probability > 1) throw new IllegalArgumentException("probability must be between 0 and 1");

        //  These three text fields are required.
        if (name == null) throw new IllegalArgumentException("name cannot be null");
        if (company == null) throw new IllegalArgumentException("company cannot be null");
        if (status == null) throw new IllegalArgumentException("status cannot be null");

        this.id = id;
        this.name = name;
        this.company = company;
        this.status = status;
        this.estimatedValue = estimatedValue;
        this.probability = probability;
        this.nextFollowUp = nextFollowUp;
    }





    public int getId() {
        return id;
    }





    public String getName() {
        return name;
    }





    public String getCompany() {
        return company;
    }





    public String getStatus() {
        return status;
    }





    public double getEstimatedValue() {
        return estimatedValue;
    }





    public double getProbability() {
        return probability;
    }





    public LocalDate getNextFollowUp() {
        return nextFollowUp;
    }





    public double getWeightedValue() {
        //  Example: $10,000 at 50% gives a weighted value of $5,000.
        return estimatedValue * probability;
    }





    public boolean isClosed() {
        //  Won and Lost leads are history.  They are not part of the open pipeline.
        if (status.equalsIgnoreCase("Won/Closed")) return true;
        if (status.equalsIgnoreCase("Lost/Closed")) return true;
        if (status.equalsIgnoreCase("Won")) return true;
        if (status.equalsIgnoreCase("Lost")) return true;

        return false;
    }
}
