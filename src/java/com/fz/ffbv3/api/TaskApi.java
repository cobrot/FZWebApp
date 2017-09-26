/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.api;

import com.fz.ffbv3.service.taskmgt.TaskLogic;
import com.fz.ffbv3.service.taskmgt.TaskPlanModel;
import com.fz.ffbv3.service.taskmgt.UploadModel;
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
@Path("tasks")
public class TaskApi
{

  @Context
  private UriInfo context;

  /**
   * Creates a new instance of TaskApi
   */
  public TaskApi()
  {
  }

  /**
   * Retrieves representation of an instance of com.fz.ffbv3.api.TaskApi
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
   * PUT method for updating or creating an instance of TaskApi
   * @param content representation for the resource
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public void putJson(String content)
  {
  }
  
  @POST
  @Path("synchronize")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postJsonSynchronize(String content)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    TaskPlanModel taskModel = gson.fromJson(content, TaskPlanModel.class);

    DBConnector dBConnector = new DBConnector();
    Connection conn = dBConnector.ConnectToDatabase();

    StatusHolder statusHolder = new StatusHolder();
    
    if(conn != null)
    {
      TaskLogic taskLogic = new TaskLogic(conn, taskModel.getTaskListData().getVehicleID());

      Integer JobID = taskLogic.getJobID();
      
      if(JobID == -1) 
      {
        statusHolder.setCode(FixValue.intResponError);
        statusHolder.setRsp(new ResponseMessege().CoreMsgResponse(FixValue.intFail, FixMessege.strTaskEmpty));
      }
      else
        statusHolder = taskLogic.TaskList(JobID);
    }
    else
    {
      statusHolder.setCode(FixValue.intResponError);
      statusHolder.setRsp(new ResponseMessege().CoreMsgResponse(FixValue.intFail, FixMessege.strTaskFailed));
    }   
   
    dBConnector.CloseDatabase(conn);    
    return Response.status(statusHolder.getCode()).entity(statusHolder.getRsp()).build(); 
  }

  @POST
  @Path("upload")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response postJsonUpload(String content)
  {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    UploadModel uploadModel = gson.fromJson(content, UploadModel.class);

    DBConnector dBConnector = new DBConnector();
    Connection conn = dBConnector.ConnectToDatabase();

    StatusHolder statusHolder = new StatusHolder();
    
    if(conn != null)
    {
      TaskLogic taskLogic = new TaskLogic(conn, 0);
      statusHolder = taskLogic.TaskListUpload(uploadModel);
    }
    else
    {
      statusHolder.setCode(FixValue.intResponError);
      statusHolder.setRsp(new ResponseMessege().CoreMsgResponse(FixValue.intFail, FixMessege.strUploadFailed));
    }   
    
    dBConnector.CloseDatabase(conn);    
    return Response.status(statusHolder.getCode()).entity(statusHolder.getRsp()).build(); 
  }
}
