/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.api;

import com.fz.ffbv3.service.reasonmgt.ReasonLogic;
import com.fz.ffbv3.service.reasonmgt.ReasonModel;
import com.fz.generic.DBConnector;
import com.fz.generic.ResponseMessege;
import com.fz.generic.StatusHolder;
import com.fz.util.FixMessege;
import com.fz.util.FixValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author Ignat
 */
@Path("reasons")
public class ReasonApi
{

  @Context
  private UriInfo context;

  /**
   * Creates a new instance of ReasonApi
   */
  public ReasonApi()
  {
  }

  /**
   * Retrieves representation of an instance of com.fz.ffbv3.service.reasonmgt.ReasonApi
   * @return an instance of java.lang.String
   */
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public String getXml()
  {
    //TODO return proper representation object
    throw new UnsupportedOperationException();
  }

  /**
   * PUT method for updating or creating an instance of ReasonApi
   * @param content representation for the resource
   */
  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public void putXml(String content)
  {
  }

  @POST
  @Path("reasonlist")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postJsonReason(String content)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    ReasonModel taskModel = gson.fromJson(content, ReasonModel.class);

    DBConnector dBConnector = new DBConnector();
    Connection conn = dBConnector.ConnectToDatabase();

    StatusHolder statusHolder = new StatusHolder();
    
    if(conn != null)
    {
      ReasonLogic reasonLogic = new ReasonLogic(conn);
      statusHolder = reasonLogic.ReasonList(taskModel.getReasonListData().getReasonID());
    }
    else
    {
      statusHolder.setCode(FixValue.intResponError);
      statusHolder.setRsp(new ResponseMessege().CoreMsgResponse(FixValue.intFail, FixMessege.strReasonFailed));
    }   
   
    dBConnector.CloseDatabase(conn);    
    return Response.status(statusHolder.getCode()).entity(statusHolder.getRsp()).build(); 
  }
}
