package taskmanagement.service;


import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {

    @Test
    public void testConstructor() {
        String userID = "test-user-id";

        User user = new User(userID);

        assertEquals(userID, user.getUserID());
    }

    @Test
    public void testEquals() {
        User user = new User("test-user-1");
        User sameUser = new User("test-user-1");
        User anotherUser = new User("test-user-2");

        assertEquals(user, user);
        assertEquals(user, sameUser);
        assertNotEquals(user, anotherUser);
    }

    @Test
    public void testHashCode() {
        Set<User> users = new HashSet<>();

        User user = new User("test-user-1");
        User anotherUser = new User("test-user-2");

        users.add(user);
        users.add(anotherUser);
        assertEquals(2, users.size());

        users.add(user);
        users.add(anotherUser);
        assertEquals(2, users.size());
    }

    @Test
    public void testToString() {
        String userID = "test-user-id";

        User user = new User(userID);

        assertEquals("User{userID='test-user-id'}", user.toString());
    }
}