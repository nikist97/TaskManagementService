package taskmanagement.api;

import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class TaskManagementResourceTest {

    @Test
    public void testCreateTask() {
        TaskManagementResource resource = new TaskManagementResource();

        Response response = resource.createTask();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetTasks() {
        TaskManagementResource resource = new TaskManagementResource();

        Response response = resource.getTasks();

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testUpdateTask() {
        TaskManagementResource resource = new TaskManagementResource();

        Response response = resource.updateTask("task-id-123");

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testGetTask() {
        TaskManagementResource resource = new TaskManagementResource();

        Response response = resource.getTask("task-id-123");

        assertEquals(200, response.getStatus());
    }

    @Test
    public void testDeleteTask() {
        TaskManagementResource resource = new TaskManagementResource();

        Response response = resource.deleteTask("task-id-123");

        assertEquals(200, response.getStatus());
    }
}