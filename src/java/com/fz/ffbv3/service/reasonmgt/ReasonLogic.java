/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.service.reasonmgt;

import com.fz.generic.StatusHolder;
import com.fz.util.FixValue;
import com.fz.generic.ResponseMessege;
import com.fz.util.FixMessege;
import java.sql.*;

/**
 *
 * @author Agustinus Ignat
 */
public class ReasonLogic
{              
	private Statement st;
	private ResultSet res;
  private String strQuery;
  private ResponseMessege rspMsg;
  private StatusHolder sendRsp;
  private final Connection conn;

  public ReasonLogic(Connection conn)
  {
    this.conn = conn;
  }
  
  public StatusHolder ReasonList(Integer ReasonID)
  {
    Integer rows;
    rspMsg = new ResponseMessege();
    sendRsp = new StatusHolder();
   
    if(ReasonID == 1)
      strQuery = "SELECT FailID as ReasonID, FailName as ReasonName, FailDescription as ReasonDesc FROM reasonfail ORDER BY FailName DESC";
    else
    if(ReasonID == 2)
      strQuery = "SELECT LateID as ReasonID, LateName as ReasonName, LateDescription as ReasonDesc FROM reasonlate ORDER BY LateName DESC";

    try
    {
      st = conn.createStatement();
  	  res = st.executeQuery(strQuery);

      res.last();
      rows = res.getRow();
      
      if(rows == 0)
      {
        sendRsp.setCode(FixValue.intResponFail);
        sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strReasonEmpty));
      }
      else
      {
        sendRsp.setCode(FixValue.intResponSuccess);
        sendRsp.setRsp(rspMsg.ReasonMsgResponse(FixValue.intSuccess, FixMessege.strReasonSuccess, res, rows));
      }
    }
    catch (SQLException ex)
    {
      String message;
      message = ex.getMessage();
      
      sendRsp.setCode(FixValue.intResponFail);
      sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strReasonFailed));
    }

    return sendRsp;
  }    
}
