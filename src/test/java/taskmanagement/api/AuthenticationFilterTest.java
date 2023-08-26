package taskmanagement.api;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import taskmanagement.exceptions.AuthenticationException;
import taskmanagement.service.User;
import taskmanagement.service.UserManagementService;

import javax.ws.rs.container.ContainerRequestContext;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private UserManagementService userManagementService;

    @Test
    public void testRequestFilter() throws IOException {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userManagementService);
        String authToken = "token";
        User user = new User("test-user-id");

        when(requestContext.getHeaderString(anyString())).thenReturn("Bearer " + authToken);
        when(userManagementService.getUserByAuthToken(anyString())).thenReturn(Optional.of(user));

        authenticationFilter.filter(requestContext);

        verify(requestContext).getHeaderString("Authorization");
        verify(requestContext).setProperty("user", user);
        verify(userManagementService).getUserByAuthToken(authToken);
        verifyNoMoreInteractions(requestContext, userManagementService);
    }

    @Test
    public void testRequestFilterNullAuthHeader() throws IOException {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userManagementService);

        authenticationFilter.filter(requestContext);

        verify(requestContext).getHeaderString("Authorization");
        verifyNoMoreInteractions(requestContext, userManagementService);
    }

    @Test
    public void testRequestFilterNonBearerAuthHeader() throws IOException {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(userManagementService);

        when(requestContext.getHeaderString("Authorization")).thenReturn("Basic test123");

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("invalid authentication scheme - please use a Bearer token");

        try {
            authenticationFilter.filter(requestContext);
        } finally {
            verify(requestContext).getHeaderString("Authorization");
            verifyNoMoreInteractions(requestContext, userManagementService);
        }
    }
}