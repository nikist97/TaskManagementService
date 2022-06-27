package taskmanagement.api;


import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/tasks")
public class TaskManagementResource {

    @POST
    public Response createTask() {
        return Response.ok().build();
    }

    @GET
    public Response getTasks() {
        return Response.ok().build();
    }

    @PATCH
    @Path("/{taskID}")
    public Response updateTask(@PathParam("taskID") String taskID) {
        return Response.ok().build();
    }

    @GET
    @Path("/{taskID}")
    public Response getTask(@PathParam("taskID") String taskID) {
        return Response.ok().build();
    }

    @DELETE
    @Path("/{taskID}")
    public Response deleteTask(@PathParam("taskID") String taskID) {

        return Response.ok().build();
    }

}
