package taskmanagement.api;

import taskmanagement.exceptions.TaskNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class TaskNotFoundExceptionMapper implements ExceptionMapper<TaskNotFoundException> {

    @Override
    public Response toResponse(TaskNotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ExceptionMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
