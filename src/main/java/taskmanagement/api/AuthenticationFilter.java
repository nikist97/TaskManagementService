package taskmanagement.api;

import taskmanagement.exceptions.AuthenticationException;
import taskmanagement.service.User;
import taskmanagement.service.UserManagementService;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;


@Priority(3)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    private final UserManagementService userManagementService;

    @Inject
    public AuthenticationFilter(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null) {
            return;
        }

        authHeader = authHeader.trim();
        if (!authHeader.startsWith("Bearer")) {
            throw new AuthenticationException("invalid authentication scheme - please use a Bearer token");
        }

        String authToken = authHeader.replaceFirst("Bearer", "").trim();
        Optional<User> user = userManagementService.getUserByAuthToken(authToken);
        user.ifPresent(u -> requestContext.setProperty("user", u));
    }
}
