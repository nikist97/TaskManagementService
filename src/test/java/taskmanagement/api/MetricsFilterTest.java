package taskmanagement.api;

import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MetricsFilterTest {
    @Test
    public void testResponseFilter() {
        ContainerRequestContext requestContext = mock(ContainerRequestContext.class);
        ContainerResponseContext responseContext = mock(ContainerResponseContext.class);
        UriInfo uriInfo = mock(UriInfo.class);


        when(requestContext.getProperty("requestProcessingTime")).thenReturn(100L);
        when(requestContext.getMethod()).thenReturn("GET");
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(URI.create("/service/api/method123"));

        when(responseContext.getStatus()).thenReturn(200);

        MetricsFilter metricsFilter = new MetricsFilter();
        metricsFilter.filter(requestContext, responseContext);

        verify(requestContext, times(1)).getProperty("requestProcessingTime");
        verify(requestContext, times(1)).getMethod();
        verify(requestContext, times(1)).getUriInfo();
        verify(responseContext, times(1)).getStatus();
    }
}