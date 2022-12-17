package taskmanagement.api;

import io.prometheus.client.Histogram;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Priority(1)
@Provider
public class MetricsFilter implements ContainerResponseFilter {

    private static final String[] LABELS = new String[]{
            "requestMethod",
            "uriPath",
            "responseCode"
    };

    private static final Histogram requestLatencyHistogram = Histogram.build()
            .name("task_management_service_requests_latency")
            .labelNames(LABELS)
            .buckets(5, 25, 50, 75, 100, 500, 1000, 5000, 10000)
            .help("Request latency in milli-seconds.")
            .register();

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String[] labelsValues = new String[3];

        String requestMethod = requestContext.getMethod();
        labelsValues[0] = requestMethod;

        String uriPath = requestContext.getUriInfo().getRequestUri().getPath();
        labelsValues[1] = maskTaskIDs(uriPath);

        int responseCode = responseContext.getStatus();
        labelsValues[2] = Integer.toString(responseCode);

        long processingTime = (long) requestContext.getProperty("requestProcessingTime");

        requestLatencyHistogram
                .labels(labelsValues)
                .observe(processingTime);
    }

    private String maskTaskIDs(String uriPath) {
        if (uriPath.matches("^/api/tasks/[a-zA-Z0-9_-]+$")) {
            return "/api/tasks/*";
        } else {
            return uriPath;
        }
    }
}
