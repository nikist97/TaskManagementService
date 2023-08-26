package taskmanagement.dependencies.kratos;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sh.ory.kratos.ApiException;
import sh.ory.kratos.api.FrontendApi;
import sh.ory.kratos.model.Identity;
import sh.ory.kratos.model.Session;
import taskmanagement.exceptions.AuthenticationException;
import taskmanagement.service.User;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KratosWrapperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private FrontendApi kratosFrontendApi;

    @Test
    public void testGetUserByAuthTokenSuccess() throws Exception {
        String authToken = "test-token";
        User expectedUser = new User("test-user-id");
        Session session = new Session();
        Identity identity = new Identity();
        identity.setId(expectedUser.getUserID());
        session.setIdentity(identity);
        session.setActive(true);

        when(kratosFrontendApi.toSession(anyString(), any())).thenReturn(session);

        KratosWrapper kratosWrapper = new KratosWrapper(kratosFrontendApi);
        Optional<User> actualUser = kratosWrapper.getUserByAuthToken(authToken);

        assertTrue(actualUser.isPresent());
        assertEquals(expectedUser, actualUser.get());

        verify(kratosFrontendApi).toSession(authToken, null);
        verifyNoMoreInteractions(kratosFrontendApi);
    }

    @Test
    public void testGetUserByAuthTokenNullSession() throws Exception {
        String authToken = "test-token";

        when(kratosFrontendApi.toSession(anyString(), any())).thenReturn(null);

        KratosWrapper kratosWrapper = new KratosWrapper(kratosFrontendApi);
        Optional<User> actualUser = kratosWrapper.getUserByAuthToken(authToken);

        assertFalse(actualUser.isPresent());

        verify(kratosFrontendApi).toSession(authToken, null);
        verifyNoMoreInteractions(kratosFrontendApi);
    }

    @Test
    public void testGetUserByAuthTokenInactiveSession() throws Exception {
        String authToken = "test-token";
        User expectedUser = new User("test-user-id");
        Session session = new Session();
        Identity identity = new Identity();
        identity.setId(expectedUser.getUserID());
        session.setIdentity(identity);
        session.setActive(false);

        when(kratosFrontendApi.toSession(anyString(), any())).thenReturn(session);

        expectedException.expect(AuthenticationException.class);
        expectedException.expectMessage("Inactive session returned for the given authentication token.");

        try {
            KratosWrapper kratosWrapper = new KratosWrapper(kratosFrontendApi);
            kratosWrapper.getUserByAuthToken(authToken);
        } finally {
            verify(kratosFrontendApi).toSession(authToken, null);
            verifyNoMoreInteractions(kratosFrontendApi);
        }
    }

    @Test
    public void testGetUserByAuthTokenForAuthorizationException() throws Exception {
        String authToken = "test-token";

        when(kratosFrontendApi.toSession(anyString(), any())).thenThrow(new ApiException(Response.Status.UNAUTHORIZED.getStatusCode(), "unauthorized"));

        KratosWrapper kratosWrapper = new KratosWrapper(kratosFrontendApi);
        Optional<User> actualUser = kratosWrapper.getUserByAuthToken(authToken);

        assertFalse(actualUser.isPresent());

        verify(kratosFrontendApi).toSession(authToken, null);
        verifyNoMoreInteractions(kratosFrontendApi);
    }

    @Test
    public void testGetUserByAuthTokenForUnexpectedException() throws Exception {
        String authToken = "test-token";

        when(kratosFrontendApi.toSession(anyString(), any())).thenThrow(new ApiException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "internal server error"));

        expectedException.expect(IOException.class);
        expectedException.expectMessage("internal server error");

        try {
            KratosWrapper kratosWrapper = new KratosWrapper(kratosFrontendApi);
            kratosWrapper.getUserByAuthToken(authToken);
        } finally {
            verify(kratosFrontendApi).toSession(authToken, null);
            verifyNoMoreInteractions(kratosFrontendApi);
        }
    }
}