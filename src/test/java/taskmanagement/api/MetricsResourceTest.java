package taskmanagement.api;

import io.prometheus.client.Counter;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MetricsResourceTest {

    @Test
    public void testMetrics() throws Exception {
        Counter counter = Counter.build().name("test_counter").help("test counter help message").register();
        counter.inc();
        counter.inc();

        MetricsResource resource = new MetricsResource();

        String metrics = resource.metrics();

        assertTrue(metrics.startsWith("# HELP test_counter_total test counter help message\n" +
                "# TYPE test_counter_total counter\n" +
                "test_counter_total 2.0\n" +
                "# HELP test_counter_created test counter help message\n" +
                "# TYPE test_counter_created gauge\n" +
                "test_counter_created "));

    }

}