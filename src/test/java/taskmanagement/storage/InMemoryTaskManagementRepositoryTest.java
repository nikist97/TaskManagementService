package taskmanagement.storage;


import org.junit.Test;
import taskmanagement.service.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class InMemoryTaskManagementRepositoryTest {

    @Test
    public void testSaveAndGet() {
        InMemoryTaskManagementRepository repository = new InMemoryTaskManagementRepository();

        Task task = Task.builder("test-title", "test-description").build();

        repository.save(task);

        Optional<Task> retrievedTask = repository.get(task.getIdentifier());

        assertTrue(retrievedTask.isPresent());
        assertSame(task, retrievedTask.get());
    }

    @Test
    public void testSaveAndGetAll() {
        InMemoryTaskManagementRepository repository = new InMemoryTaskManagementRepository();

        assertTrue(repository.getAll().isEmpty());

        Task task = Task.builder("test-title", "test-description").build();
        Task anotherTask = Task.builder("another-test-title", "another-test-description").build();

        repository.save(task);
        repository.save(anotherTask);

        List<Task> retrievedTasks = repository.getAll();

        Set<Task> expectedTasks = new HashSet<>();
        expectedTasks.add(task);
        expectedTasks.add(anotherTask);

        assertEquals(new HashSet<>(retrievedTasks), expectedTasks);
    }

    @Test
    public void testSaveDeleteAndGet() {
        InMemoryTaskManagementRepository repository = new InMemoryTaskManagementRepository();

        Task task = Task.builder("test-title", "test-description").build();

        repository.save(task);

        Optional<Task> retrievedTask = repository.get(task.getIdentifier());

        assertTrue(retrievedTask.isPresent());
        assertSame(task, retrievedTask.get());

        repository.delete(task.getIdentifier());

        retrievedTask = repository.get(task.getIdentifier());
        assertFalse(retrievedTask.isPresent());
    }
}