package taskmanagement.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.time.Instant;

@Priority(2)
@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LogManager.getLogger(LoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) {
        long processingStartTime = Instant.now().toEpochMilli();
        requestContext.setProperty("requestProcessingStartTime", processingStartTime);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        long processingStartTime = (long) requestContext.getProperty("requestProcessingStartTime");
        long processingEndTime = Instant.now().toEpochMilli();
        long processingLatency = processingEndTime - processingStartTime;

        requestContext.setProperty("requestProcessingTime", processingLatency);

        LOGGER.info("{} {} {} {}ms",
                requestContext.getMethod(),
                requestContext.getUriInfo().getRequestUri().getPath(),
                responseContext.getStatus(),
                processingLatency);
    }
}
