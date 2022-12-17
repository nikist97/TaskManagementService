package taskmanagement.api;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.StringWriter;

@Path("/metrics")
public class MetricsResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String metrics() throws IOException {
        StringWriter writer = new StringWriter();

        TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());

        return writer.toString();
    }

}
