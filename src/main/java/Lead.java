import java.time.LocalDate;
import java.util.Objects;

/**
 * One sales lead/opportunity in SSCRM.
 */
public class Lead {
    private final int id;
    private final String name;
    private final String company;
    private final String status;
    private final double estimatedValue;
    private final double probability;
    private final LocalDate nextFollowUp;

    public Lead(int id,
                String name,
                String company,
                String status,
                double estimatedValue,
                double probability,
                LocalDate nextFollowUp) {

        if (estimatedValue < 0) {
            throw new IllegalArgumentException("estimatedValue cannot be negative");
        }
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("probability must be between 0 and 1");
        }

        this.id = id;
        this.name = Objects.requireNonNull(name, "name");
        this.company = Objects.requireNonNull(company, "company");
        this.status = Objects.requireNonNull(status, "status");
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
        return estimatedValue * probability;
    }

    public boolean isClosed() {
        return status.equalsIgnoreCase("Won/Closed")
                || status.equalsIgnoreCase("Lost/Closed")
                || status.equalsIgnoreCase("Won")
                || status.equalsIgnoreCase("Lost");
    }
}
