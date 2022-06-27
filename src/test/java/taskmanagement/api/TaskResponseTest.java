package taskmanagement.api;


import org.junit.Test;
import taskmanagement.service.Task;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TaskResponseTest {

    @Test
    public void testConstructor() {
        Task task = Task.builder("test-title", "test-description").build();
        TaskResponse taskResponse = new TaskResponse(task);

        assertEquals(task.getTitle(), taskResponse.getTitle());
        assertEquals(task.getDescription(), taskResponse.getDescription());
        assertEquals(task.getIdentifier(), taskResponse.getIdentifier());
        assertEquals(task.isCompleted(), taskResponse.isCompleted());
        assertEquals(task.getCreatedAt().toString(), taskResponse.getCreatedAt());
    }

    @Test
    public void testEquals() {
        Task task = Task.builder("test-title", "test-description").build();
        TaskResponse taskResponse = new TaskResponse(task);
        TaskResponse sameTaskResponse = new TaskResponse(task);
        TaskResponse anotherTaskResponse = new TaskResponse(Task.builder("another-title", "another descirpion")
                .build());

        assertEquals(taskResponse, taskResponse);
        assertEquals(taskResponse, sameTaskResponse);
        assertNotEquals(taskResponse, anotherTaskResponse);
    }

    @Test
    public void testHashCode() {
        Task task = Task.builder("test-title", "test-description").build();
        TaskResponse taskResponse = new TaskResponse(task);
        TaskResponse sameTaskResponse = new TaskResponse(task);
        TaskResponse anotherTaskResponse = new TaskResponse(Task.builder("another-title", "another descirpion")
                .build());

        Set<TaskResponse> taskResponseSet = new HashSet<>();
        taskResponseSet.add(taskResponse);
        taskResponseSet.add(taskResponse);
        taskResponseSet.add(sameTaskResponse);
        taskResponseSet.add(anotherTaskResponse);

        assertEquals(2, taskResponseSet.size());
    }
}