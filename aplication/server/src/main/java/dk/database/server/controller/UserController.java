package dk.database.server.controller;

import dk.database.server.domain.UserCreation;
import dk.database.server.domain.UserKeywordCreation;
import dk.database.server.domain.UserStockCreation;
import dk.database.server.entities.User;
import dk.database.server.entities.UserKeyword;
import dk.database.server.exceptions.dataconflict.DataConflictException;
import dk.database.server.exceptions.datanotfound.DataNotFoundException;
import dk.database.server.facade.DataFacadeImpl;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private final DataFacadeImpl data = new DataFacadeImpl();

    @Path("/")
    @GET
    public Response getAllUsers(@Context UriInfo uriInfo) throws SQLException, ClassNotFoundException {
        Map<Integer, User> users = data.getAllUsers();

        if(users == null)
        {
            throw new DataNotFoundException("Sorry, we were not able to handle your request.");
        }

        URI uri = uriInfo.getAbsolutePathBuilder().build();
        return Response
                .created(uri)
                .status(Response.Status.OK)
                .entity(users)
                .build();
    }

    @Path("/{userId}")
    @GET
    public Response getUserById(@PathParam("userId") int userid, @Context UriInfo uriInfo) throws SQLException, ClassNotFoundException {
        User user = data.getUserById(userid);

        if(user == null)
        {
            throw new DataNotFoundException("Sorry, we were not able to handle your request. User with id " + userid + " can't be found in our system. ");
        }

        URI uri = uriInfo.getAbsolutePathBuilder()
                .build();
        return Response
                .created(uri)
                .status(Response.Status.OK)
                .entity(user)
                .build();
    }

    @Path("/{userId}/keyword")
    @GET
    public Response getUserKeyword(@PathParam("userId") int userid, @Context UriInfo uriInfo) throws SQLException, ClassNotFoundException {
        UserKeyword user = data.getUserKeyword(userid);

        if(user == null)
        {
            throw new DataNotFoundException("Sorry, we were not able to handle your request. User with id " + userid + " can't be found in our system.");
        }

        URI uri = uriInfo.getAbsolutePathBuilder()
                .build();
        return Response
                .created(uri)
                .status(Response.Status.OK)
                .entity(user)
                .build();
    }

    @Path("/")
    @POST
    public Response addUser(@RequestBody UserCreation userCreation, @Context UriInfo uriInfo) throws SQLException, ClassNotFoundException {
        URI uri = uriInfo.getAbsolutePathBuilder()
                .build();
        User user = data.addUser(userCreation);

        if(user == null )
        {
            throw new DataConflictException("Sorry, we were not able to handle your request. " + userCreation.getUserName() + " can not be added.");
        }

        return Response
                .created(uri)
                .status(Response.Status.CREATED)
                .entity(user)
                .build();
    }

    @Path("/stock")
    @POST
    public Response applyStock(@RequestBody UserStockCreation userStockCreation, @Context UriInfo uriInfo) throws SQLException, ClassNotFoundException {
        Boolean ditInsert = data.applyStock(userStockCreation);

        if(!ditInsert)
        {
            throw new DataConflictException("Sorry, we were not able to handle your request. " + userStockCreation.getStockName() + " can not be added.");
        }

        URI uri = uriInfo.getAbsolutePathBuilder()
                .build();
        return Response
                .created(uri)
                .status(Response.Status.CREATED)
                .entity(ditInsert)
                .build();
    }

    @Path("/keyword")
    @POST
    public Response applyKeyword(@RequestBody UserKeywordCreation userKeywordCreation, @Context UriInfo uriInfo) throws SQLException, ClassNotFoundException {
        Boolean ditInsert = data.applyKeyword(userKeywordCreation);

        if(!ditInsert)
        {
            throw new DataConflictException("Sorry, we were not able to handle your request. " + userKeywordCreation.getKeywordName() + " can not be added.");
        }

        URI uri = uriInfo.getAbsolutePathBuilder()
                .build();
        return Response
                .created(uri)
                .status(Response.Status.CREATED)
                .entity(ditInsert)
                .build();
    }

}
