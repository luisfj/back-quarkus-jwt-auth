package com.diario.exception;

import com.diario.exception.model.GenericApplicationException;
import com.diario.exception.model.GenericFieldException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(final Exception exception) {
        exception.printStackTrace();
        if(exception instanceof GenericApplicationException){
            return Response.status(((GenericApplicationException) exception).getStatus())
                    .entity(((GenericApplicationException) exception).getErrors())
                    .build();
        } else if (exception instanceof AuthenticationPasswordException || exception instanceof AuthenticationUsernameException) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Arrays.asList(new GenericFieldException("Usuário/Senha", "Inválidos")))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exception.getMessage())
                .build();
    }

}