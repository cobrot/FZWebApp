/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.api;

import com.fz.ffbv3.service.usermgt.UserLogic;
import com.fz.ffbv3.service.usermgt.UserModel;
import com.fz.generic.DBConnector;
import com.fz.generic.ResponseMessege;
import com.fz.generic.StatusHolder;
import com.fz.util.FixMessege;
import com.fz.util.FixValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Ignat
 */
@Path("users")
public class UsersApi {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of UsersApi
     */
    public UsersApi() {
    }

  /**
   * Retrieves representation of an instance of com.fz.ffbv3.api.TestResource
   * @return an instance of java.lang.String
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getJson()
  {
    //TODO return proper representation object
    throw new UnsupportedOperationException();
  }

  /**
   * PUT method for updating or creating an instance of TestResource
   * @param content representation for the resource
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public void putJson(String content)
  {
  }

  @POST
  @Path("login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postLoginJson(String content)
  {
		// Get Gson object and parse json string to object
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserModel userModel = gson.fromJson(content, UserModel.class);

    DBConnector dBConnector = new DBConnector();
    Connection conn = dBConnector.ConnectToDatabase();

    StatusHolder statusHolder = new StatusHolder();
    
    if(conn != null)
    {
      UserLogic userLogic = new UserLogic(conn, userModel.getUserData().getUsername()); 
      statusHolder = userLogic.Login(userModel.getUserData().getPassword());
    }
    else
    {
      statusHolder.setCode(FixValue.intResponError);
      statusHolder.setRsp(new ResponseMessege().CoreMsgResponse(FixValue.intFail, FixMessege.strLoginFailed));
    }
    
    dBConnector.CloseDatabase(conn);    
    return Response.status(statusHolder.getCode()).entity(statusHolder.getRsp()).build();
  }
}
