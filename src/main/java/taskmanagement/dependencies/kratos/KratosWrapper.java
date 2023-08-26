package taskmanagement.dependencies.kratos;

import sh.ory.kratos.ApiException;
import sh.ory.kratos.api.FrontendApi;
import sh.ory.kratos.model.Session;
import taskmanagement.exceptions.AuthenticationException;
import taskmanagement.service.User;
import taskmanagement.service.UserManagementService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

public class KratosWrapper implements UserManagementService {

    private final FrontendApi kratosFrontendApi;

    @Inject
    public KratosWrapper(FrontendApi kratosFrontendApi) {
        this.kratosFrontendApi = kratosFrontendApi;
    }

    public Optional<User> getUserByAuthToken(String authToken) throws IOException {
        Session session;
        try {
            session = kratosFrontendApi.toSession(authToken, null);
        } catch (ApiException e) {
            if (e.getCode() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                return Optional.empty();
            }

            throw new IOException(e);
        }

        if (session == null) {
            return Optional.empty();
        }

        if (session.getActive() == null || !session.getActive()){
            throw new AuthenticationException("Inactive session returned for the given authentication token.");
        }

        return Optional.of(new User(session.getIdentity().getId()));
    }
}
