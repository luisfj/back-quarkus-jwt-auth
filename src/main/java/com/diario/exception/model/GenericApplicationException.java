package com.diario.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GenericApplicationException extends Exception implements Serializable {
    private Response.Status status;
    private List<GenericFieldException> errors = new ArrayList<>();

    public GenericApplicationException(Response.Status status, String field, String message){
        this.status = status;
        addFieldError(field, message);
    }

    public void addFieldError(String field, String message){
        errors.add(new GenericFieldException(field, message));
    }
}

