package taskmanagement.api;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import taskmanagement.exceptions.InvalidTaskDataException;
import taskmanagement.service.Task;
import taskmanagement.service.TaskManagementService;
import taskmanagement.service.TaskUpdate;

import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TaskManagementResourceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateTask() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        String title = "test-title";
        String description = "test-description";
        String taskID = "test-task-id";
        TaskCreateRequest createRequest = new TaskCreateRequest(title, description);

        when(service.create(anyString(), anyString())).thenReturn(
                Task.builder(title, description)
                        .withIdentifier(taskID)
                        .build());

        Response response = resource.createTask(createRequest);

        assertEquals(201, response.getStatus());
        assertEquals("tasks/" + taskID, response.getLocation().getRawPath());

        verify(service, times(1)).create(title, description);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testCreateTaskNullRequestBody() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        thrown.expect(InvalidTaskDataException.class);
        thrown.expectMessage("task-create-request-body cannot be null");

        try {
            resource.createTask(null);
        } finally {
            verifyNoMoreInteractions(service);
        }
    }

    @Test
    public void testGetTasks() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        Task task = Task.builder("test-title", "test-description").build();
        Task anotherTask = Task.builder("another-test-title", "another-test-description").build();
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(anotherTask);
        when(service.retrieveAll()).thenReturn(tasks);

        List<TaskResponse> expectedTaskResponses = new ArrayList<>();
        expectedTaskResponses.add(new TaskResponse(task));
        expectedTaskResponses.add(new TaskResponse(anotherTask));

        List<TaskResponse> taskResponses = resource.getTasks();

        assertEquals(expectedTaskResponses, taskResponses);

        verify(service, times(1)).retrieveAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testUpdateTask() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        String taskID = "task-id-123";
        String newTitle = "new-title";
        String newDescription = "new-description";
        boolean completed = true;
        TaskUpdateRequest updateRequest = new TaskUpdateRequest(newTitle, newDescription, completed);

        Response response = resource.updateTask(taskID, updateRequest);

        assertEquals(200, response.getStatus());

        ArgumentCaptor<TaskUpdate> argumentCaptor = ArgumentCaptor.forClass(TaskUpdate.class);
        verify(service, times(1)).update(eq(taskID), argumentCaptor.capture());
        verifyNoMoreInteractions(service);

        TaskUpdate capturedUpdated = argumentCaptor.getValue();
        assertEquals(completed, capturedUpdated.getCompleted());
        assertEquals(newTitle, capturedUpdated.getTitle());
        assertEquals(newDescription, capturedUpdated.getDescription());
    }

    @Test
    public void testUpdateTaskNullRequestBody() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        thrown.expect(InvalidTaskDataException.class);
        thrown.expectMessage("task-update-request-body cannot be null");

        try {
            resource.updateTask("task-id-123", null);
        } finally {
            verifyNoMoreInteractions(service);
        }
    }

    @Test
    public void testGetTask() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        Task task = Task.builder("test-title", "test-description").build();
        when(service.retrieve(task.getIdentifier())).thenReturn(task);

        TaskResponse expectedTaskResponse = new TaskResponse(task);

        TaskResponse taskResponse = resource.getTask(task.getIdentifier());

        assertEquals(expectedTaskResponse, taskResponse);

        verify(service, times(1)).retrieve(task.getIdentifier());
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testDeleteTask() {
        TaskManagementService service = mock(TaskManagementService.class);
        TaskManagementResource resource = new TaskManagementResource(service);

        String taskID = "task-id-123";
        Response response = resource.deleteTask(taskID);

        assertEquals(204, response.getStatus());

        verify(service, times(1)).delete(taskID);
        verifyNoMoreInteractions(service);
    }
}