import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    private static final List<String> PIPELINE_STAGES = List.of(
            "New Lead", "Contacted", "Qualified", "Proposal Sent",
            "Negotiation", "Won/Closed", "Lost/Closed"
    );





    public static void main(String[] args) throws IOException {
        //  Start SSCRM with a small group of sample leads.
        CRMService crmService = new CRMService(sampleLeads(LocalDate.now()));

        //  Java's small built-in HTTP server is enough for this class project, for now.
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new CRMHandler(crmService));
        server.setExecutor(null);

        System.out.println("SSCRM server started at http://localhost:8080/");
        server.start();
    }





    static List<Lead> sampleLeads(LocalDate today) {
        List<Lead> leads = new ArrayList<>();

        //  These are demo records so the page always has something useful to show.
        leads.add(new Lead(1, "James Smith", "Apex Solutions", "Negotiation", 130_000, 0.80, today.minusDays(2)));
        leads.add(new Lead(2, "Maria Johnson", "BlueSky Media", "Proposal Sent", 95_000, 0.60, today.minusDays(1)));
        leads.add(new Lead(3, "Patricia Garcia", "Falcon Security", "Negotiation", 72_000, 0.80, today));
        leads.add(new Lead(4, "David Williams", "Cedar Logistics", "Qualified", 4_800, 0.45, today));
        leads.add(new Lead(5, "Linda Brown", "Delta Manufacturing", "Contacted", 850, 0.25, today.plusDays(2)));
        leads.add(new Lead(6, "Betty Martin", "Titan Auto", "Won/Closed", 3_800, 1.00, null));
        leads.add(new Lead(7, "Mark Thompson", "Willow Care", "Lost/Closed", 1_200, 0.00, null));

        return leads;
    }





    static String renderDashboard(CRMService service, LocalDate today) {
        BusinessMetrics metrics = BusinessMetrics.from(service);
        List<CRMService.ActionItem> actions = service.needsAction(today, 5);
        NumberFormat money = NumberFormat.getCurrencyInstance(Locale.US);
        StringBuilder html = new StringBuilder();

        addPageStart(html);
        addHeader(html);
        addMetrics(html, metrics, money);
        addNeedsAction(html, actions, money);
        addSalesFunnel(html, service, money);
        addPageEnd(html);

        return html.toString();
    }





    private static void addPageStart(StringBuilder html) {
        //  Keeping the CSS here makes this little project easy to package as one JAR.
        html.append("<!DOCTYPE html><html><head>");
        html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        html.append("<title>SSCRM - Today</title>");
        html.append("<style>");
        html.append("*{box-sizing:border-box}");
        html.append("body{font-family:Arial,sans-serif;margin:0;background:#10151d;color:#e9eef5}");
        html.append("main{max-width:1100px;margin:auto;padding:28px}");
        html.append(".muted{color:#9aa8b8}");
        html.append(".grid{display:grid;grid-template-columns:repeat(4,1fr);gap:14px}");
        html.append(".card,.panel{background:#18212c;border:1px solid #2b3948;border-radius:12px;padding:16px}");
        html.append(".value{font-size:1.55rem;font-weight:700;margin-top:8px}");
        html.append(".priority{display:grid;grid-template-columns:1.5fr 1fr 1fr .7fr;gap:10px;padding:12px 0;border-bottom:1px solid #2b3948}");
        html.append("table{border-collapse:collapse;width:100%;background:#18212c;margin-top:18px}");
        html.append("th,td{padding:11px;border:1px solid #2b3948;text-align:left}");
        html.append("th{background:#223044}");
        html.append("select{background:#10151d;color:#e9eef5;border:1px solid #44586e;border-radius:5px;padding:6px}");
        html.append("h1{margin-bottom:4px}h2{margin-top:0}.score{font-weight:700}.action{color:#b8d9ff}");
        html.append("@media(max-width:760px){.grid{grid-template-columns:1fr 1fr}.priority{grid-template-columns:1fr 1fr}}");
        html.append("</style>");
        html.append("</head><body><main>");
    }





    private static void addHeader(StringBuilder html) {
        html.append("<h1>SSCRM &mdash; Today</h1>");
        html.append("<p class=\"muted\">A quick CRM view of the customers, opportunities, and follow-ups that need action.</p>");
    }





    private static void addMetrics(StringBuilder html, BusinessMetrics metrics, NumberFormat money) {
        html.append("<section class=\"panel\">");
        html.append("<h2>Customer &amp; Revenue Intelligence</h2>");
        html.append("<div class=\"grid\">");
        html.append(metricCard("Active Opportunities", String.valueOf(metrics.getActiveOpportunities())));
        html.append(metricCard("Pipeline Value", money.format(metrics.getTotalPipelineValue())));
        html.append(metricCard("Weighted Pipeline", money.format(metrics.getWeightedPipelineValue())));
        html.append(metricCard("Average Active Deal", money.format(metrics.getAverageActiveDealValue())));
        html.append("</div></section>");
    }





    private static void addNeedsAction(StringBuilder html, List<CRMService.ActionItem> actions, NumberFormat money) {
        html.append("<section class=\"panel\" style=\"margin-top:16px\">");
        html.append("<h2>Needs Action</h2>");
        html.append("<p class=\"muted\">These customers need more than attention.  They need a sales action.</p>");
        html.append("<div class=\"priority muted\"><div>Customer</div><div>Stage / Value</div><div>Action</div><div>Score</div></div>");

        for (CRMService.ActionItem item : actions) addActionRow(html, item, money);

        html.append("</section>");
    }





    private static void addActionRow(StringBuilder html, CRMService.ActionItem item, NumberFormat money) {
        Lead lead = item.getLead();

        html.append("<div class=\"priority\">");
        html.append("<div><strong>" + escape(lead.getName()) + "</strong><br><span class=\"muted\">" + escape(lead.getCompany()) + "</span></div>");
        html.append("<div>" + escape(lead.getStatus()) + "<br><span class=\"muted\">" + money.format(lead.getEstimatedValue()) + "</span></div>");
        html.append("<div class=\"action\">" + escape(item.getAction()) + "</div>");
        html.append("<div class=\"score\">" + item.getScore() + "</div>");
        html.append("</div>");
    }





    private static void addSalesFunnel(StringBuilder html, CRMService service, NumberFormat money) {
        html.append("<h2 style=\"margin-top:24px\">Sales Funnel</h2>");
        html.append("<table><thead><tr>");
        html.append("<th>ID</th><th>Contact</th><th>Company</th><th>Stage</th>");
        html.append("<th>Est. Value</th><th>Probability</th><th>Weighted</th><th>Next Follow-Up</th>");
        html.append("</tr></thead><tbody>");

        for (Lead lead : service.getLeads()) addLeadRow(html, lead, money);

        html.append("</tbody></table>");
    }





    private static void addLeadRow(StringBuilder html, Lead lead, NumberFormat money) {
        html.append("<tr>");
        html.append("<td>" + lead.getId() + "</td>");
        html.append("<td>" + escape(lead.getName()) + "</td>");
        html.append("<td>" + escape(lead.getCompany()) + "</td>");
        html.append("<td><select>");

        for (String stage : PIPELINE_STAGES) {
            String selected = "";
            if (stage.equalsIgnoreCase(lead.getStatus())) selected = " selected";
            html.append("<option" + selected + ">" + escape(stage) + "</option>");
        }

        html.append("</select></td>");
        html.append("<td>" + money.format(lead.getEstimatedValue()) + "</td>");
        html.append("<td>" + String.format(Locale.US, "%.0f%%", lead.getProbability() * 100) + "</td>");
        html.append("<td>" + money.format(lead.getWeightedValue()) + "</td>");

        if (lead.getNextFollowUp() == null) html.append("<td>&mdash;</td>");
        else html.append("<td>" + lead.getNextFollowUp() + "</td>");

        html.append("</tr>");
    }





    private static void addPageEnd(StringBuilder html) {
        html.append("</main></body></html>");
    }





    private static String metricCard(String label, String value) {
        return "<div class=\"card\"><div class=\"muted\">" + escape(label)
                + "</div><div class=\"value\">" + escape(value) + "</div></div>";
    }





    private static String escape(String input) {
        String safe = input;
        safe = safe.replace("&", "&amp;");
        safe = safe.replace("<", "&lt;");
        safe = safe.replace(">", "&gt;");
        safe = safe.replace("\"", "&quot;");
        return safe;
    }





    //  This is the small bridge between the browser and the Java code.
    static class CRMHandler implements HttpHandler {
        private final CRMService service;





        CRMHandler(CRMService service) {
            this.service = service;
        }





        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String page = renderDashboard(service, LocalDate.now());
            byte[] response = page.getBytes(StandardCharsets.UTF_8);

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.length);

            OutputStream output = exchange.getResponseBody();
            output.write(response);
            output.close();
        }
    }
}
