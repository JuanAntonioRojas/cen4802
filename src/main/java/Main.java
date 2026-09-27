import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class Main {
    private static final List<String> PIPELINE_STAGES = List.of(
            "New Lead",
            "Contacted",
            "Qualified",
            "Proposal Sent",
            "Negotiation",
            "Won/Closed",
            "Lost/Closed"
    );

    public static void main(String[] args) throws IOException {
        CRMService crmService = new CRMService(sampleLeads(LocalDate.now()));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new CRMHandler(crmService));
        server.setExecutor(null);

        System.out.println("SSCRM server started at http://localhost:8080/");
        server.start();
    }

    static List<Lead> sampleLeads(LocalDate today) {
        return List.of(
                new Lead(1, "James Smith", "Apex Solutions", "Negotiation",
                        130_000, 0.80, today.minusDays(2)),
                new Lead(2, "Maria Johnson", "BlueSky Media", "Proposal Sent",
                        95_000, 0.60, today.minusDays(1)),
                new Lead(3, "Patricia Garcia", "Falcon Security", "Negotiation",
                        72_000, 0.80, today),
                new Lead(4, "David Williams", "Cedar Logistics", "Qualified",
                        4_800, 0.45, today),
                new Lead(5, "Linda Brown", "Delta Manufacturing", "Contacted",
                        850, 0.25, today.plusDays(2)),
                new Lead(6, "Betty Martin", "Titan Auto", "Won/Closed",
                        3_800, 1.00, null),
                new Lead(7, "Mark Thompson", "Willow Care", "Lost/Closed",
                        1_200, 0.00, null)
        );
    }

    static String renderDashboard(CRMService service, LocalDate today) {
        BusinessMetrics metrics = BusinessMetrics.from(service);
        List<CRMService.PriorityItem> priorities = service.todayPriorities(today, 5);
        NumberFormat money = NumberFormat.getCurrencyInstance(Locale.US);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head>")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">")
                .append("<title>SSCRM - Today</title>")
                .append("<style>")
                .append("*{box-sizing:border-box}body{font-family:Arial,sans-serif;margin:0;background:#10151d;color:#e9eef5}")
                .append("main{max-width:1100px;margin:auto;padding:28px}.muted{color:#9aa8b8}.grid{display:grid;grid-template-columns:repeat(4,1fr);gap:14px}")
                .append(".card,.panel{background:#18212c;border:1px solid #2b3948;border-radius:12px;padding:16px}.value{font-size:1.55rem;font-weight:700;margin-top:8px}")
                .append(".priority{display:grid;grid-template-columns:1.5fr 1fr 1fr .7fr;gap:10px;padding:12px 0;border-bottom:1px solid #2b3948}")
                .append("table{border-collapse:collapse;width:100%;background:#18212c;margin-top:18px}th,td{padding:11px;border:1px solid #2b3948;text-align:left}th{background:#223044}")
                .append("select{background:#10151d;color:#e9eef5;border:1px solid #44586e;border-radius:5px;padding:6px}")
                .append("h1{margin-bottom:4px}h2{margin-top:0}.score{font-weight:700}.action{color:#b8d9ff}")
                .append("@media(max-width:760px){.grid{grid-template-columns:1fr 1fr}.priority{grid-template-columns:1fr 1fr}.wide{grid-column:1/-1}}").append("</style>")
                .append("</head><body><main>");

        html.append("<h1>SSCRM &mdash; Today</h1>")
                .append("<p class=\"muted\">Action-oriented CRM preview: who needs attention, why, and what should happen next.</p>");

        html.append("<section class=\"panel\"><h2>Customer &amp; Revenue Intelligence</h2><div class=\"grid\">")
                .append(metricCard("Active Opportunities", String.valueOf(metrics.getActiveOpportunities())))
                .append(metricCard("Pipeline Value", money.format(metrics.getTotalPipelineValue())))
                .append(metricCard("Weighted Pipeline", money.format(metrics.getWeightedPipelineValue())))
                .append(metricCard("Average Active Deal", money.format(metrics.getAverageActiveDealValue())))
                .append("</div></section>");

        html.append("<section class=\"panel\" style=\"margin-top:16px\"><h2>Today's Priorities</h2>")
                .append("<div class=\"priority muted\"><div>Customer</div><div>Stage / Value</div><div>Suggested Action</div><div>Score</div></div>");

        for (CRMService.PriorityItem item : priorities) {
            Lead lead = item.lead();
            html.append("<div class=\"priority\">")
                    .append("<div><strong>").append(escape(lead.getName())).append("</strong><br><span class=\"muted\">")
                    .append(escape(lead.getCompany())).append("</span></div>")
                    .append("<div>").append(escape(lead.getStatus())).append("<br><span class=\"muted\">")
                    .append(money.format(lead.getEstimatedValue())).append("</span></div>")
                    .append("<div class=\"action\">").append(escape(item.suggestedAction())).append("</div>")
                    .append("<div class=\"score\">").append(item.score()).append("</div>")
                    .append("</div>");
        }
        html.append("</section>");

        html.append("<h2 style=\"margin-top:24px\">Sales Funnel</h2>")
                .append("<table><thead><tr><th>ID</th><th>Contact</th><th>Company</th><th>Stage</th><th>Est. Value</th><th>Probability</th><th>Weighted</th><th>Next Follow-Up</th></tr></thead><tbody>");

        for (Lead lead : service.getLeads()) {
            html.append("<tr>")
                    .append("<td>").append(lead.getId()).append("</td>")
                    .append("<td>").append(escape(lead.getName())).append("</td>")
                    .append("<td>").append(escape(lead.getCompany())).append("</td>")
                    .append("<td><select>");

            for (String stage : PIPELINE_STAGES) {
                String selected = stage.equalsIgnoreCase(lead.getStatus()) ? " selected" : "";
                html.append("<option").append(selected).append(">")
                        .append(escape(stage)).append("</option>");
            }

            html.append("</select></td>")
                    .append("<td>").append(money.format(lead.getEstimatedValue())).append("</td>")
                    .append("<td>").append(String.format(Locale.US, "%.0f%%", lead.getProbability() * 100)).append("</td>")
                    .append("<td>").append(money.format(lead.getWeightedValue())).append("</td>")
                    .append("<td>").append(lead.getNextFollowUp() == null ? "&mdash;" : lead.getNextFollowUp()).append("</td>")
                    .append("</tr>");
        }

        html.append("</tbody></table>")
                .append("<p class=\"muted\" style=\"margin-top:18px\">Financial Health &amp; Cash Flow is intentionally reserved for the next revision/container rebuild.</p>")
                .append("</main></body></html>");

        return html.toString();
    }

    private static String metricCard(String label, String value) {
        return "<div class=\"card\"><div class=\"muted\">" + escape(label)
                + "</div><div class=\"value\">" + escape(value) + "</div></div>";
    }

    private static String escape(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    static class CRMHandler implements HttpHandler {
        private final CRMService service;

        CRMHandler(CRMService service) {
            this.service = service;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            byte[] responseBytes = renderDashboard(service, LocalDate.now()).getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}
