import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Business rules for the small SSCRM demonstration.
 *
 * The priority rules intentionally mirror the n8n Next Best Action prototype
 * so the Java application and the visual automation experiment can be compared.
 */
public class CRMService {
    private final List<Lead> leads;

    public CRMService(List<Lead> leads) {
        this.leads = new ArrayList<>(leads);
    }

    public List<Lead> getLeads() {
        return List.copyOf(leads);
    }

    public List<Lead> getActiveLeads() {
        return leads.stream()
                .filter(lead -> !lead.isClosed())
                .toList();
    }

    public double totalPipelineValue() {
        return getActiveLeads().stream()
                .mapToDouble(Lead::getEstimatedValue)
                .sum();
    }

    public double totalWeightedPipelineValue() {
        return getActiveLeads().stream()
                .mapToDouble(Lead::getWeightedValue)
                .sum();
    }

    public double averageActiveDealValue() {
        return getActiveLeads().stream()
                .mapToDouble(Lead::getEstimatedValue)
                .average()
                .orElse(0.0);
    }

    public long countByStatus(String status) {
        return leads.stream()
                .filter(lead -> lead.getStatus().equalsIgnoreCase(status))
                .count();
    }

    public int priorityScore(Lead lead, LocalDate today) {
        if (lead.isClosed()) {
            return Integer.MIN_VALUE;
        }

        int score = 0;
        LocalDate followUp = lead.getNextFollowUp();

        if (followUp != null && followUp.isBefore(today)) {
            score += 40;
        } else if (followUp != null && followUp.isEqual(today)) {
            score += 25;
        }

        if (lead.getEstimatedValue() >= 5_000) {
            score += 25;
        } else if (lead.getEstimatedValue() >= 1_000) {
            score += 15;
        }

        if (lead.getStatus().equalsIgnoreCase("Negotiation")) {
            score += 20;
        } else if (lead.getStatus().equalsIgnoreCase("Proposal Sent")) {
            score += 15;
        } else if (lead.getStatus().equalsIgnoreCase("Qualified")) {
            score += 10;
        }

        return score;
    }

    public String suggestedNextAction(Lead lead, LocalDate today) {
        LocalDate followUp = lead.getNextFollowUp();

        if (followUp != null && followUp.isBefore(today)) {
            return "Contact Now";
        }
        if (lead.getStatus().equalsIgnoreCase("Negotiation")) {
            return "Review and Contact";
        }
        if (lead.getStatus().equalsIgnoreCase("Proposal Sent")) {
            return "Follow Up on Proposal";
        }
        if (lead.getStatus().equalsIgnoreCase("Qualified")) {
            return "Advance Opportunity";
        }
        return "Review Customer";
    }

    public List<PriorityItem> todayPriorities(LocalDate today, int limit) {
        return getActiveLeads().stream()
                .map(lead -> new PriorityItem(
                        lead,
                        priorityScore(lead, today),
                        suggestedNextAction(lead, today)))
                .sorted(Comparator
                        .comparingInt(PriorityItem::score).reversed()
                        .thenComparing(
                                Comparator.comparingDouble(
                                        (PriorityItem item) -> item.lead().getWeightedValue())
                                        .reversed()))
                .limit(limit)
                .toList();
    }

    public record PriorityItem(Lead lead, int score, String suggestedAction) {
    }
}
