package com.diario.resource.auth;

import com.diario.dto.auth.UserChangePasswordDTO;
import com.diario.dto.auth.UserInfoDTO;
import com.diario.dto.auth.UserRegisterDTO;
import com.diario.exception.AuthenticationPasswordException;
import com.diario.exception.AuthenticationUsernameException;
import com.diario.exception.model.GenericApplicationException;
import com.diario.service.UserService;
import io.quarkus.security.Authenticated;
import lombok.AllArgsConstructor;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/authentication")
@AllArgsConstructor(onConstructor = @__(@Inject))
public class UserResource {

    private final UserService userService;

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(AuthenticationRequest authRequest) throws AuthenticationUsernameException, AuthenticationPasswordException {
        final String token = userService.authenticate(authRequest);
        return Response.ok(new AuthenticationResponse(token)).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserRegisterDTO user) throws AuthenticationUsernameException, AuthenticationPasswordException, GenericApplicationException {
        if(!user.getPassword().equals(user.getConfirmPassword()))
            throw new GenericApplicationException(Response.Status.FORBIDDEN, "Senha/Confirmação", "Senha e confirmação são diferentes");
        userService.save(user);
        return login(new AuthenticationRequest(user.getEmail(), user.getPassword()));
    }

    @GET
    @Path("/my-info")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMe() {
        UserInfoDTO dto = userService.getUserByEmail(jwt.getIssuer());
        return Response.ok(dto).build();
    }

    @POST
    @Path("/update")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(UserInfoDTO userDto) {
        userService.update(userDto);
        return Response.ok().build();
    }

    @POST
    @Path("/change-password")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(UserChangePasswordDTO userDto) throws GenericApplicationException {
        if(!userDto.getPassword().equals(userDto.getConfirmPassword()))
            throw new GenericApplicationException(Response.Status.FORBIDDEN, "Senha/Confirmação", "Senha e confirmação são diferentes");
        userService.changePassword(userDto, jwt.getIssuer());
        return Response.ok().build();
    }



//    @GET
//    @Path("roles")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response checkRolesAllowed(){
//        return Response.ok(String.format("Usuario %s has the following roles: %s", jwt.getName(), String.join(", ", jwt.getGroups()))).build();
//    }

}
