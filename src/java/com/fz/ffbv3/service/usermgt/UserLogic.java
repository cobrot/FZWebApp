/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.service.usermgt;

import com.fz.generic.Db;
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
public class UserLogic
{              
	private Statement st;
	private ResultSet res;
  private String strQuery;
  private ResponseMessege rspMsg;
  private StatusHolder sendRsp;
  private Connection conn;
  private String Username;

  public UserLogic(Connection conn, String Username)
  {
    this.conn = conn;
    this.Username = Username;
  }

  public StatusHolder Login(String Password)
  {
    Integer rows;
    rspMsg = new ResponseMessege();
    sendRsp = new StatusHolder();
   
    strQuery = "SELECT a.UserID, a.Name, a.Phone, a.lnkRoleID, b.Brand, b.Type, d.VehicleID, d.VehicleName FROM fbuser as a, " +
                "fbdevice as b, gbuser AS c, fbvehicle AS d WHERE c.Username=\"" + Username + "\" AND c.Password=\"" +
                Password + "\" AND a.lnkDeviceID=b.DeviceID AND a.gbUserID=c.gbUserID AND a.VehicleID=d.VehicleID";

    System.out.println(strQuery);
    try
    {
      st = conn.createStatement();
  	  res = st.executeQuery(strQuery);

      res.last();
      rows = res.getRow();
      
      if(rows == 0)
      {
        sendRsp.setCode(FixValue.intResponFail);
        sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strLoginEmpty));
      }
      else
      {
        sendRsp.setCode(FixValue.intResponSuccess);
        sendRsp.setRsp(rspMsg.LoginMsgResponse(FixValue.intSuccess, FixMessege.strLoginSuccess, res, rows));
      }
    }
    catch (SQLException ex)
    {
      sendRsp.setCode(FixValue.intResponFail);
      sendRsp.setRsp(rspMsg.CoreMsgResponse(FixValue.intFail, FixMessege.strLoginFailed));
    }

    return sendRsp;
  }  
}
