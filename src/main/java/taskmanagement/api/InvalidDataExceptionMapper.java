package taskmanagement.api;

import taskmanagement.exceptions.InvalidDataException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {

    @Override
    public Response toResponse(InvalidDataException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ExceptionMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
