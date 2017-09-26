/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fz.generic;

import com.fz.util.FZUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

/**
 *
 */
public class PageTopUtils {
    
    public static String get(String paramName, PageContext pc) {
        String x = (String) pc.getRequest().getAttribute(paramName);
        if (x == null) return "";
        return x;
        
    }
    
    public static List<Object> getList(String paramName, PageContext pc){
        List<Object> x = (List<Object>) 
                pc.getRequest().getAttribute(paramName);
        if (x == null) 
            return new ArrayList<Object>();
        return x;
        
    }
    
    public static void run(BusinessLogic logic, PageContext pc)
        throws Exception {
        HttpServletRequest request = (HttpServletRequest) pc.getRequest();
        HttpServletResponse response = (HttpServletResponse) pc.getResponse();
        try {
            logic.run(request, response, pc);
        }
        catch (Exception e){
            // forward to error page to display error message
            // TODO: beautify the page
            String msg = FZUtil.toStackTraceText(e);
            System.out.println(msg);
            request.setAttribute("errMsg", msg);
            request
                    .getRequestDispatcher("../appGlobal/error.jsp")
                    .forward(request, response);
        }
    }
    
    public static void runAPI(BusinessLogic logic, PageContext pc)
        throws Exception {
        HttpServletRequest request = (HttpServletRequest) pc.getRequest();
        HttpServletResponse response = (HttpServletResponse) pc.getResponse();
        try {
            logic.run(request, response, pc);
        }
        catch (Exception e){
            
            String msg = FZUtil.toStackTraceText(e);
            System.out.println(msg);
            pc.getOut().print(
                    "{\"result\" : \"ERR\""
                    + ", \"errMsg\" :"
                    + "\"" + FZUtil.escapeText(msg) 
                    + "\"}"
            );
        }
    }
    
    public static void checkLogin(PageContext pc)
        throws Exception {
        try {
            //TODO
            
        }
        catch (Exception e){
            throw new Exception("Error checking login. ", e);
        }
    }
}
