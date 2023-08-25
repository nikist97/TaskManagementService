package taskmanagement.api;


import taskmanagement.service.Task;
import taskmanagement.service.TaskManagementService;
import taskmanagement.service.TaskUpdate;
import taskmanagement.service.User;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static taskmanagement.utils.Validator.validateArgNotNull;

@Path("/tasks")
public class TaskManagementResource {

    private final TaskManagementService service;

    @Inject
    public TaskManagementResource(TaskManagementService service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(TaskCreateRequest taskCreateRequest, @Context ContainerRequestContext requestContext) {
        validateArgNotNull(taskCreateRequest, "task-create-request-body");

        User user = (User) requestContext.getProperty("user");
        Task task = service.create(taskCreateRequest.getTitle(), taskCreateRequest.getDescription(), user);

        String taskID = task.getIdentifier();

        URI taskRelativeURI = URI.create("tasks/" + taskID);
        return Response.created(taskRelativeURI).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaskResponse> getTasks() {
        return service.retrieveAll().stream()
                .map(TaskResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @PATCH
    @Path("/{taskID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("taskID") String taskID, TaskUpdateRequest taskUpdateRequest) {
        validateArgNotNull(taskUpdateRequest, "task-update-request-body");

        TaskUpdate update = new TaskUpdate(taskUpdateRequest.getTitle(), taskUpdateRequest.getDescription(),
                taskUpdateRequest.isCompleted());

        service.update(taskID, update);

        return Response.ok().build();
    }

    @GET
    @Path("/{taskID}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaskResponse getTask(@PathParam("taskID") String taskID) {
        Task task = service.retrieve(taskID);
        return new TaskResponse(task);
    }

    @DELETE
    @Path("/{taskID}")
    public Response deleteTask(@PathParam("taskID") String taskID) {
        service.delete(taskID);
        return Response.noContent().build();
    }

}
