package taskmanagement.api;

import taskmanagement.exceptions.InvalidTaskDataException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidTaskDataExceptionMapper implements ExceptionMapper<InvalidTaskDataException> {

    @Override
    public Response toResponse(InvalidTaskDataException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
