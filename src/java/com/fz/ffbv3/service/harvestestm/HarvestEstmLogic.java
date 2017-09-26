/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.ffbv3.service.harvestestm;

import com.fz.generic.BusinessLogic;
import com.fz.util.FZUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 *
 */
public class HarvestEstmLogic implements BusinessLogic {

    @Override
    public void run(HttpServletRequest request
            , HttpServletResponse response
            , PageContext pc) throws Exception {
        // get id
        String hvsEstmID = FZUtil.getHttpParam(request, "hvsEstmID");
        
        // if id provided
        if (hvsEstmID.trim().length() > 0){

            // parse into int
            int id = Integer.parseInt(hvsEstmID);
            
            //String sql = "select "
            
        }
        else {
            request.setAttribute("hvsDt"
                    , (new java.text.SimpleDateFormat("yyyy-MM-dd"))
                        .format(new java.util.Date())
            );
        }
    }

}
