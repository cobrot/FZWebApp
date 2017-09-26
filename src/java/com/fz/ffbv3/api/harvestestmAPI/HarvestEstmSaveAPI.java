/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fz.ffbv3.api.harvestestmAPI;

import com.fz.ffbv3.service.harvestestm.HarvestEstm;
import com.fz.generic.Db;
import com.fz.generic.ReturnValue;
import com.fz.util.FZUtil;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Eri Fizal
 */
@Path("hvsEstmSave")
public class HarvestEstmSaveAPI {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of HarvestEstmSaveAPI
     */
    public HarvestEstmSaveAPI() {
    }

    /**
     * Retrieves representation of an instance of com.fz.ffbv3.service.harvestestm.HarvestEstmSaveAPI
     * @return an instance of java.lang.String
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postLoginJson(@QueryParam("json") String jsonInput, @Context HttpServletRequest request)
    {
        return Response.status(200).entity("Sukses").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(
            @QueryParam("json") String jsonInput
            , @Context HttpServletRequest request) {

        // initialize standard return value
        ReturnValue returnValue = new ReturnValue();
        
        // create json parser using Gson library from Google
        Gson gson = new Gson();
        
        try {
            
            // parse JSON input into java object
            HarvestEstm he = gson.fromJson(jsonInput, HarvestEstm.class);
            
            // open db connection and 1 statement to insert header
            String sql = "insert into fbHvsEstm(hvsDt, status, createdBy)"
                    + " values(?,?,?)";
            try (
                Connection con = (new Db()).getConnection("jdbc/fz");
                PreparedStatement psHdr = con.prepareStatement(sql
                        , Statement.RETURN_GENERATED_KEYS);
                )  {
                
                // start database transaction,
                // transaction needed because we have several sql 
                con.setAutoCommit(false);
                
                // insert header to db
                psHdr.setDate(1, java.sql.Date.valueOf(he.harvestDate));
                psHdr.setString(2, "NEW");
                psHdr.setString(3, "");
                psHdr.executeUpdate();
                
                // get generated auto increment key from db
                int key = -1;
                try (ResultSet rs = psHdr.getGeneratedKeys()){
                    rs.next();
                    key = rs.getInt(1);
                }

                // create child sql
                sql = "insert into fbHvsEstmDtl"
                        + "(taskType, blocks, size1, hvsEstmID)"
                        + " values(?,?,?,?)";
                try(PreparedStatement ps = con.prepareStatement(sql)){

                    // insert each rows
                    for (String[] ss : he.estimateRows){

                        // set the values
                        ps.clearParameters();
                        ps.setString(1, ss[0]); // type
                        ps.setString(2, ss[1]); // blocks
                        ps.setDouble(3, Double.parseDouble(ss[2])); // kg
                        ps.setInt(4, key); // parent
                        
                        // add to sql batch
                        ps.addBatch();

                    }
                    
                    // execute sql batch
                    ps.clearParameters();
                    int[] results = ps.executeBatch();
                }
                
                // commit transaction
                con.setAutoCommit(true);
                
            }
            
            // create success returnValue 
            returnValue.success = true;
            returnValue.msg = "Success!";
        }
        catch(Exception e){
            
            // convert stack trace to string
            String msg = FZUtil.toStackTraceText(e);
            
            // create error returnValue 
            returnValue.success = false;
            returnValue.msg = msg;
            
            // print to console for debug
            System.out.println(msg);
        }

        // convert returnValue to json, then return it
        String jsonOutput = gson.toJson(returnValue, ReturnValue.class);
        return jsonOutput;
    }

}
