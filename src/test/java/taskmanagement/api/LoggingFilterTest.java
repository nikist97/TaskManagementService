package taskmanagement.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingFilterTest {

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private ContainerResponseContext responseContext;

    @Mock
    private UriInfo uriInfo;

    @Test
    public void testRequestFilter() {
        LoggingFilter loggingFilter = new LoggingFilter();

        loggingFilter.filter(requestContext);

        verify(requestContext, times(1)).setProperty(eq("requestProcessingStartTime"), anyLong());

        verifyNoMoreInteractions(requestContext);
    }

    @Test
    public void testResponseFilter() {
        LoggingFilter loggingFilter = new LoggingFilter();

        when(requestContext.getProperty("requestProcessingStartTime")).thenReturn(Instant.EPOCH.toEpochMilli());
        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(URI.create("/service/api/method123"));
        when(responseContext.getStatus()).thenReturn(200);

        loggingFilter.filter(requestContext, responseContext);

        verify(requestContext, times(1)).setProperty(eq("requestProcessingTime"), anyLong());
        verify(requestContext, times(1)).getProperty("requestProcessingStartTime");
        verify(requestContext, times(1)).getMethod();
        verify(requestContext, times(1)).getUriInfo();

        verify(responseContext, times(1)).getStatus();

        verify(uriInfo, times(1)).getRequestUri();

        verifyNoMoreInteractions(requestContext, responseContext, uriInfo);
    }
}