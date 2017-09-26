/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.service.taskmgt;

import com.fz.generic.StatusHolder;
import com.fz.util.FixValue;
import com.fz.generic.ResponseMessege;
import com.fz.util.FixMessege;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.*;

/**
 *
 * @author Agustinus Ignat
 */
public class TaskLogic
{              
	private Statement st;
	private ResultSet res;
  private String strQuery;
  private ResponseMessege rspMsg;
  private StatusHolder sendRsp;
  private final Connection conn;
  private final Integer TruckID;
	Gson gson;

  public TaskLogic(Connection conn, Integer TruckID)
  {
    this.conn = conn;
    this.TruckID = TruckID;
  }
  
  public Integer getJobID()
  {
    Integer rows;
    Integer JobID;
    
    strQuery = "SELECT JobID FROM fbjob WHERE assignedDt=(SELECT MIN(assignedDt) FROM fbjob WHERE ActualTruckID=" + TruckID + " AND DoneStatus='ASGN')";

    try
    {
      st = conn.createStatement();
  	  res = st.executeQuery(strQuery);

      res.last();
      rows = res.getRow();
      
      if(rows == 0)
        JobID = -1;
      else
        JobID = res.getInt("JobID");
    }
    catch (SQLException ex)
    {
      JobID = -1;
    }
   
    return JobID;
  }
  
  public StatusHolder TaskList(Integer JobID)
  {
    Integer rows;
    rspMsg = new ResponseMessege();
    sendRsp = new StatusHolder();
   
    strQuery = "SELECT a.divID, a.JobID, b.TaskID, b.From1 as 'From', b.To1 as 'To', DATE_FORMAT(b.PlanStart, " +
               "'%Y-%m-%d %H:%i:%s') as Start, DATE_FORMAT(b.PlanEnd, '%Y-%m-%d %H:%i:%s') as End, b.Tonnage, b.Blocks " +
               "FROM fbjob AS a, fbtask2 AS b, fbvehicle as c " +
               "WHERE a.JobID=b.JobID AND a.ActualTruckID=c.VehicleID AND	a.JobID=(SELECT JobID FROM fbjob WHERE " + 
               "assignedDt=(SELECT MIN(assignedDt) FROM fbjob WHERE ActualTruckID=" + TruckID + " AND DoneStatus=\"ASGN\"))";

    try
    {
      st = conn.createStatement();
  	  res = st.executeQuery(strQuery);

      res.last();
      rows = res.getRow();
      
      if(rows == 0)
      {
        sendRsp.setCode(FixValue.intResponFail);
        sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strTaskEmpty));
      }
      else
      {
        sendRsp.setCode(FixValue.intResponSuccess);
        sendRsp.setRsp(rspMsg.TaskListMsgResponse(conn, FixValue.intSuccess, FixMessege.strTaskSuccess, res, rows));
        UpdateJobByJobID("takenDt", JobID);
      }
    }
    catch (SQLException ex)
    {
      sendRsp.setCode(FixValue.intResponFail);
      sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strTaskFailed));
    }

    return sendRsp;
  }  
  
  private boolean UpdateJobByJobID(String strField, Integer mJobID)
  {
    boolean success=true;
    strQuery = "UPDATE fbjob SET " + strField + "=CURRENT_TIMESTAMP() WHERE JobID=" + mJobID;

    try
    {
      st = conn.createStatement();
  	  st.execute(strQuery);
    }
    catch (SQLException ex)
    {
      success=false;
      System.out.println(ex.getMessage());
    }
    
    return success;
  }

  public StatusHolder TaskListUpload(UploadModel uploadModel)
  {
    rspMsg = new ResponseMessege();
    sendRsp = new StatusHolder();

    boolean success=true;
    String setUpdate;
    UploadPlanData uploadPlanData = new UploadPlanData();
    Integer mJobID=0;
    
    if(uploadModel.getUploadData().isEmpty())
    {
      success=false;
      sendRsp.setCode(FixValue.intResponFail);
      sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strUploadEmpty));
    }
    else
    {
      for (int i=0; i<uploadModel.getUploadData().size(); i++)
      {
        uploadPlanData = uploadModel.getUploadData().get(i);
        setUpdate = "";
      
        setUpdate += "DoneStatus=\"" + uploadPlanData.getDoneStatus() + "\", "; 
        setUpdate += "ActualStart=\"" + uploadPlanData.getActualStart() + "\", "; 
        setUpdate += "ActualEnd=\"" + uploadPlanData.getActualEnd() + "\", "; 
        setUpdate += "ReasonState=" + uploadPlanData.getReasonState() + ", "; 
        setUpdate += "ReasonID=" + uploadPlanData.getReasonID(); 

        if(!UpdateTaskByJobIDTaskID(setUpdate, uploadPlanData.getTaskID(), uploadPlanData.getJobID()))
        {
          success=false;
          break;
        }
      
        mJobID = uploadPlanData.getJobID();
      }
    }
      
    if(success)
    {
      if(UpdateJobByJobID("doneDt", mJobID))
      {
        sendRsp.setCode(FixValue.intResponSuccess);
        sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intSuccess, FixMessege.strUploadSuccess));
      }
      else
      {
        sendRsp.setCode(FixValue.intResponFail);
        sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strUploadFailed));
      }
    }
    else
    {
      sendRsp.setCode(FixValue.intResponFail);
      sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strUploadFailed));
    }
    
    return sendRsp;
  }  

  private boolean UpdateTaskByJobIDTaskID(String query, Integer taskid, Integer jobid)
  {
    boolean success=true;
    
    strQuery = "UPDATE fbtask2 SET " + query + " WHERE JobID=" + jobid + " AND TaskID=" + taskid;

    try
    {
      st = conn.createStatement();
  	  st.execute(strQuery);
    }
    catch (SQLException ex)
    {
      success = false;
      System.out.println(ex.getMessage());
    }
    
    return success;
  }
}
