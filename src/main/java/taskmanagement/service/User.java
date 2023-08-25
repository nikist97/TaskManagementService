package taskmanagement.service;

import java.util.Objects;

import static taskmanagement.utils.Validator.validateArgNotNull;

public class User {

    private final String userID;

    public User(String userID) {
        validateArgNotNull("userID", userID);

        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(userID, user.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                '}';
    }
}
