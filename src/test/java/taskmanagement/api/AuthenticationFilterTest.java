package taskmanagement.api;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import taskmanagement.exceptions.AuthenticationException;
import taskmanagement.service.User;

import javax.ws.rs.container.ContainerRequestContext;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ContainerRequestContext requestContext;

    @Test
    public void testRequestFilter() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        String authToken = "token";

        when(requestContext.getHeaderString("Authorization")).thenReturn("Bearer " + authToken);

        authenticationFilter.filter(requestContext);

        verify(requestContext).getHeaderString("Authorization");
        verify(requestContext).setProperty(eq("user"), eq(new User(authToken)));
        verifyNoMoreInteractions(requestContext);
    }

    @Test
    public void testRequestFilterNullAuthHeader() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();

        authenticationFilter.filter(requestContext);

        verify(requestContext).getHeaderString("Authorization");
        verifyNoMoreInteractions(requestContext);
    }

    @Test
    public void testRequestFilterNonBearerAuthHeader() {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();

        when(requestContext.getHeaderString("Authorization")).thenReturn("Basic test123");

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("invalid authentication scheme - please use a Bearer token");

        try {
            authenticationFilter.filter(requestContext);
        } finally {
            verify(requestContext).getHeaderString("Authorization");
            verifyNoMoreInteractions(requestContext);
        }
    }
}