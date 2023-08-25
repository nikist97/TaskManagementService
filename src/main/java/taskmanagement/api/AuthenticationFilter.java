package taskmanagement.api;

import taskmanagement.exceptions.AuthenticationException;
import taskmanagement.service.User;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;


@Priority(3)
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null) {
            return;
        }

        authHeader = authHeader.trim();
        if (!authHeader.startsWith("Bearer")) {
            throw new AuthenticationException("invalid authentication scheme - please use a Bearer token");
        }

        String authToken = authHeader.replaceFirst("Bearer", "").trim();
        User user = getUserByAuthToken(authToken);
        requestContext.setProperty("user", user);
    }

    private User getUserByAuthToken(String authToken) {
        // FIXME for now use the auth token as user ID until integration with user service API is implemented
        return new User(authToken);
    }
}
