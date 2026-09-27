import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void renderedDashboardContainsMeaningfulNewFeature() {
        LocalDate today = LocalDate.of(2026, 9, 26);
        CRMService service = new CRMService(Main.sampleLeads(today));

        String html = Main.renderDashboard(service, today);

        assertTrue(html.contains("Customer &amp; Revenue Intelligence"));
        assertTrue(html.contains("Today's Priorities"));
        assertTrue(html.contains("Weighted Pipeline"));
        assertTrue(html.contains("Financial Health &amp; Cash Flow"));
    }
}
