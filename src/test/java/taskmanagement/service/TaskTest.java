package taskmanagement.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import taskmanagement.exceptions.InvalidDataException;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class TaskTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testBuilder() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = "test-user-id";
        String identifier = "id-123";
        Instant createdAt = Instant.now();
        boolean completed = true;

        Task task = Task.builder(title, description, createdBy)
                .withIdentifier(identifier)
                .withCreatedAt(createdAt)
                .withCompleted(completed)
                .build();

        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(createdBy, task.getCreatedBy());
        assertEquals(identifier, task.getIdentifier());
        assertEquals(createdAt, task.getCreatedAt());
        assertEquals(completed, task.isCompleted());
    }

    @Test
    public void testBuilderDefaults() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = "test-user-id";

        Task task = Task.builder(title, description, createdBy).build();

        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());

        assertTrue(isValidUUID(task.getIdentifier()));
        assertTrue(Instant.now().toEpochMilli() >= task.getCreatedAt().toEpochMilli());
        assertFalse(task.isCompleted());
    }

    @Test
    public void testEquals() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = "test-user-id";
        String identifier = "id-123";
        Instant createdAt = Instant.now();
        boolean completed = true;

        Task task = Task.builder(title, description, createdBy)
                .withIdentifier(identifier)
                .withCreatedAt(createdAt)
                .withCompleted(completed)
                .build();

        Task sameTask = Task.builder(title, description, createdBy)
                .withIdentifier(identifier)
                .withCreatedAt(createdAt)
                .withCompleted(completed)
                .build();

        Task anotherTask = Task.builder(title, description, createdBy)
                .withIdentifier("another-identifier")
                .withCreatedAt(createdAt)
                .withCompleted(completed)
                .build();

        assertEquals(task, task);
        assertEquals(task, sameTask);
        assertNotEquals(task, anotherTask);
    }

    @Test
    public void testUpdateNoChanges() {
        Task task = Task.builder("test-title", "test-description", "test-user")
                .withIdentifier("id-123")
                .withCreatedAt(Instant.now())
                .withCompleted(true)
                .build();

        Task updatedTask = task.update(new TaskUpdate(null, null, null));

        assertEquals(task, updatedTask);
    }

    @Test
    public void testUpdateWithChanges() {
        Task task = Task.builder("test-title", "test-description", "test-user")
                .build();

        String updatedTitle = "new-test-title";
        String updatedDescription = "new-test-description";
        Task updatedTask = task.update(new TaskUpdate(updatedTitle, updatedDescription, true));

        assertNotEquals(task, updatedTask);
        assertEquals(task.getIdentifier(), updatedTask.getIdentifier());
        assertEquals(task.getCreatedAt(), updatedTask.getCreatedAt());
        assertEquals(updatedTitle, updatedTask.getTitle());
        assertEquals(updatedDescription, updatedTask.getDescription());
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    public void testInvalidNullTitle() {
        String title = null;
        String description = "test-description";
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("title cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidEmptyTitle() {
        String title = "";
        String description = "test-description";
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("title cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidBlankTitle() {
        String title = "   ";
        String description = "test-description";
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("title cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidNullDescription() {
        String title = "test-title";
        String description = null;
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("description cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidEmptyDescription() {
        String title = "test-title";
        String description = "";
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("description cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidBlankDescription() {
        String title = "test-title";
        String description = "   ";
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("description cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidNullCreatedBy() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = null;

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("createdBy cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidEmptyCreatedBy() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = "";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("createdBy cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidBlankCreatedBy() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = "   ";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("createdBy cannot be null or blank");

        Task.builder(title, description, createdBy).build();
    }

    @Test
    public void testInvalidNullCreatedAt() {
        String title = "test-title";
        String description = "test-description";
        String createdBy = "test-user-id";

        thrown.expect(InvalidDataException.class);
        thrown.expectMessage("createdAt cannot be null");

        Task.builder(title, description, createdBy)
                .withCreatedAt(null)
                .build();
    }

    @Test
    public void testHashCode() {
        Set<Task> tasks = new HashSet<>();

        Task task = Task.builder("test-title-1", "test-description-1", "test-user-1")
                .build();
        Task anotherTask = Task.builder("test-title-2", "test-description-2", "test-user-2")
                .build();

        tasks.add(task);
        tasks.add(anotherTask);
        assertEquals(2, tasks.size());

        tasks.add(task);
        tasks.add(anotherTask);
        assertEquals(2, tasks.size());
    }

    private boolean isValidUUID(String uuid) {
        try{
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException exception){
            return false;
        }
    }
}