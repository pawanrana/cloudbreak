package com.sequenceiq.cloudbreak.api.endpoint;

import java.util.Set;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sequenceiq.cloudbreak.api.model.ConstraintTemplateRequest;
import com.sequenceiq.cloudbreak.api.model.ConstraintTemplateResponse;
import com.sequenceiq.cloudbreak.doc.ContentType;
import com.sequenceiq.cloudbreak.doc.ControllerDescription;
import com.sequenceiq.cloudbreak.doc.Notes;
import com.sequenceiq.cloudbreak.doc.OperationDescriptions;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/constraints")
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/constraints", description = ControllerDescription.CONSTRAINT_TEMPLATE_DESCRIPTION, protocols = "http,https")
public interface ConstraintTemplateEndpoint {

    @POST
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.POST_PRIVATE, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "postPrivateConstraint")
    ConstraintTemplateResponse postPrivate(@Valid ConstraintTemplateRequest constraintTemplateRequest);

    @POST
    @Path("account")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.POST_PUBLIC, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "postPublicConstraint")
    ConstraintTemplateResponse postPublic(ConstraintTemplateRequest constraintTemplateRequest);

    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.GET_PRIVATE, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "getPrivatesConstraint")
    Set<ConstraintTemplateResponse> getPrivates();

    @GET
    @Path("account")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.GET_PUBLIC, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "getPublicsConstraint")
    Set<ConstraintTemplateResponse> getPublics();

    @GET
    @Path("user/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.GET_PRIVATE_BY_NAME, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "getPrivateConstraint")
    ConstraintTemplateResponse getPrivate(@PathParam(value = "name") String name);

    @GET
    @Path("account/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.GET_PUBLIC_BY_NAME, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "getPublicConstraint")
    ConstraintTemplateResponse getPublic(@PathParam(value = "name") String name);

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.GET_BY_ID, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "getConstraint")
    ConstraintTemplateResponse get(@PathParam(value = "id") Long id);

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.DELETE_BY_ID, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "deleteConstraint")
    void delete(@PathParam(value = "id") Long id);

    @DELETE
    @Path("account/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.DELETE_PUBLIC_BY_NAME, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "deletePublicConstraint")
    void deletePublic(@PathParam(value = "name") String name);

    @DELETE
    @Path("user/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = OperationDescriptions.ConstraintOpDescription.DELETE_PRIVATE_BY_NAME, produces = ContentType.JSON, notes = Notes.CONSTRAINT_NOTES,
            nickname = "deletePrivateConstraint")
    void deletePrivate(@PathParam(value = "name") String name);
}
