import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//  The simple business rules for SSCRM are kept in this class.
public class CRMService {
    private final List<Lead> leads;





    public CRMService(List<Lead> leads) {
        //  Keep our own copy so another class cannot change this list by accident.
        this.leads = new ArrayList<>(leads);
    }





    public List<Lead> getLeads() {
        return new ArrayList<>(leads);
    }





    public List<Lead> getActiveLeads() {
        List<Lead> active = new ArrayList<>();

        //  In here is where we go through each lead.
        for (Lead lead : leads) if (!lead.isClosed()) active.add(lead);

        return active;
    }





    public double totalPipelineValue() {
        double total = 0;

        //  Only open opportunities belong in the current pipeline.
        for (Lead lead : getActiveLeads()) total += lead.getEstimatedValue();

        return total;
    }





    public double totalWeightedPipelineValue() {
        double total = 0;

        //  Weighted value gives more weight to deals that are more likely to close.
        for (Lead lead : getActiveLeads()) total += lead.getWeightedValue();

        return total;
    }





    public double averageActiveDealValue() {
        List<Lead> active = getActiveLeads();

        if (active.isEmpty()) return 0;

        return totalPipelineValue() / active.size();
    }





    public long countByStatus(String status) {
        long count = 0;

        for (Lead lead : leads) if (lead.getStatus().equalsIgnoreCase(status)) count++;

        return count;
    }





    public int actionScore(Lead lead, LocalDate today) {
        //  Closed leads do not need another sales action.
        if (lead.isClosed()) return -1;

        int score = 0;
        LocalDate followUp = lead.getNextFollowUp();

        //  Overdue and today's follow-ups get the most attention first.
        if (followUp != null && followUp.isBefore(today)) score += 40;
        else if (followUp != null && followUp.isEqual(today)) score += 25;

        //  Larger opportunities get a little more weight.
        if (lead.getEstimatedValue() >= 5_000) score += 25;
        else if (lead.getEstimatedValue() >= 1_000) score += 15;

        //  Leads farther down the funnel are usually closer to a sale.
        if (lead.getStatus().equalsIgnoreCase("Negotiation")) score += 20;
        else if (lead.getStatus().equalsIgnoreCase("Proposal Sent")) score += 15;
        else if (lead.getStatus().equalsIgnoreCase("Qualified")) score += 10;

        return score;
    }





    public String actionNeeded(Lead lead, LocalDate today) {
        LocalDate followUp = lead.getNextFollowUp();

        if (followUp != null && followUp.isBefore(today)) return "Contact Now";
        if (lead.getStatus().equalsIgnoreCase("Negotiation")) return "Review and Contact";
        if (lead.getStatus().equalsIgnoreCase("Proposal Sent")) return "Follow Up on Proposal";
        if (lead.getStatus().equalsIgnoreCase("Qualified")) return "Advance Opportunity";

        return "Review Customer";
    }





    public List<ActionItem> needsAction(LocalDate today, int limit) {
        List<ActionItem> items = new ArrayList<>();

        //  Turn each active lead into a small item for the Needs Action list.
        for (Lead lead : getActiveLeads()) {
            int score = actionScore(lead, today);
            String action = actionNeeded(lead, today);
            items.add(new ActionItem(lead, score, action));
        }

        sortActionItems(items);

        //  Return only the number of items requested by the page.
        List<ActionItem> result = new ArrayList<>();
        for (int i = 0; i < items.size() && i < limit; i++) result.add(items.get(i));

        return result;
    }





    private void sortActionItems(List<ActionItem> items) {
        //  There are only a few demo leads, so a simple sort is easier to read here.
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                if (shouldComeFirst(items.get(j), items.get(i))) {
                    ActionItem temp = items.get(i);
                    items.set(i, items.get(j));
                    items.set(j, temp);
                }
            }
        }
    }





    private boolean shouldComeFirst(ActionItem first, ActionItem second) {
        if (first.getScore() > second.getScore()) return true;
        if (first.getScore() < second.getScore()) return false;

        return first.getLead().getWeightedValue() > second.getLead().getWeightedValue();
    }





    //  A small regular class is enough for one row in the Needs Action list.
    public static class ActionItem {
        private final Lead lead;
        private final int score;
        private final String action;





        public ActionItem(Lead lead, int score, String action) {
            this.lead = lead;
            this.score = score;
            this.action = action;
        }





        public Lead getLead() {
            return lead;
        }





        public int getScore() {
            return score;
        }





        public String getAction() {
            return action;
        }
    }
}
