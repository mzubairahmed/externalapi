package com.asi.ext.api.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BadRequestException implements ExceptionMapper<Exception>{

    @Override
    public Response toResponse(Exception arg0) {
        // TODO Auto-generated method stub
        return Response.status(Response.Status.BAD_REQUEST).entity("Product Not Valid").build();
    }

}
