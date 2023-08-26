package taskmanagement.service;

import java.io.IOException;
import java.util.Optional;

public interface UserManagementService {
    Optional<User> getUserByAuthToken(String authToken) throws IOException;
}
