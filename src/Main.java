import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // In-memory model for leads
    static class Lead {
        int id;
        String name;
        String company;
        String status;

        Lead(int id, String name, String company, String status) {
            this.id = id;
            this.name = name;
            this.company = company;
            this.status = status;
        }
    }

    private static final List<Lead> leads = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Seed initial data
        leads.add(new Lead(1, "Alice Smith", "Apex Logistics", "New"));
        leads.add(new Lead(2, "Bob Jones", "Beacon Tech", "Contacted"));

        // Start server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new CRMHandler());
        server.setExecutor(null); // default single-threaded executor
        System.out.println("SSCRM server started at http://localhost:8080/");
        server.start();
    }

    static class CRMHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html><head><title>SSCRM - Simple CRM</title>");
            html.append("<style>");
            html.append("body { font-family: sans-serif; margin: 40px; background: #f4f6f8; }");
            html.append("table { border-collapse: collapse; width: 100%; background: #fff; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }");
            html.append("th, td { padding: 12px 16px; border: 1px solid #ddd; text-align: left; }");
            html.append("th { background: #007bff; color: white; }");
            html.append("</style></head><body>");
            html.append("<h1>SSCRM &mdash; Super Simple CRM</h1>");
            html.append("<p>Current Active Pipeline Leads:</p>");
            html.append("<table><thead><tr><th>ID</th><th>Contact Name</th><th>Company</th><th>Status</th></tr></thead><tbody>");

            for (Lead lead : leads) {
                html.append("<tr>")
                        .append("<td>").append(lead.id).append("</td>")
                        .append("<td>").append(lead.name).append("</td>")
                        .append("<td>").append(lead.company).append("</td>")
                        .append("<td><strong>").append(lead.status).append("</strong></td>")
                        .append("</tr>");
            }

            html.append("</tbody></table>");
            html.append("</body></html>");

            byte[] responseBytes = html.toString().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}