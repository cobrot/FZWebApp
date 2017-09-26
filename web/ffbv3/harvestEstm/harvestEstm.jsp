<%-- 
    Document   : test
    Created on : Sep 3, 2017, 2:24:55 PM
    Author     : Eri Fizal
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="../appGlobal/pageTop.jsp"%>
<%run(new com.fz.ffbv3.service.harvestestm.HarvestEstmLogic());%>
<!DOCTYPE html>
<html >
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Harvest Estimates</title>
</head>

<body>
  <%@include file="../appGlobal/bodyTop.jsp"%>
  <script>
  $( function() {
    $( "#harvestDate" ).datepicker();
    $( "#harvestDate" ).datepicker( "option", "dateFormat", "yy-mm-dd");
  } );
  </script>
    <form id="form1">
    <div class="container">
    <h3>Estimasi Rencana Kerja</h3>
    <br>  
        <div class="fzErrMsg" id="errMsg">
        </div>
      <label class="fzLabel">Date</label>
      <input class="fzInput" id="harvestDate" 
             name="harvestDate" value="<%=get("hvsDt")%>"/>
      <br>
      <br>
      <br>
      <div id="tbDemand" class="table-editable">
        <table class="table" border1="1">
            <tr>
                <th width="100px" class="fzCol">Tipe</th>
                <th width="100px" class="fzCol">Block</th>
                <th width="100px" class="fzCol">Kg</th>
                <th style="display:none;">Param</th>
                <th></th>
            </tr>
            <tr>
                <td class="fzCell celVal">
                  <select>
                      <option selected>TAXA</option>
                      <option>RSTN</option>
                  </select>
                </td>
                <td contenteditable="true" class="fzCell celVal">N29</td>
                <td contenteditable="true" class="fzCell celVal">10000</td>
                <td style="display:none;" class="fzCell celVal">Hidden</td>
                <td class="fzCell">
                  <span class="table-up fzTextButton">Up</span>
                  <span class="table-down fzTextButton">Down</span>
                  <!--span class="table-remove ">Del</span-->
                </td>
            </tr>
            <!-- clonable table line -->
            <tr class="hide">
                <td class="fzCell celVal">
                  <select>
                      <option selected>TAXA</option>
                      <option>RSTN</option>
                  </select>
                </td>
                <td contenteditable="true" class="fzCell celVal">Isi Block</td>
                <td contenteditable="true" class="fzCell celVal">0</td>
                <td style="display:none;" class="fzCell celVal">Hidden</td>
                <td class="fzCell">
                  <span class="table-up fzTextButton">Up</span>
                  <span class="table-down fzTextButton">Down</span>
                  <span class="table-remove fzTextButton">Del</span>
                </td>
            </tr>
        </table>
        &nbsp;&nbsp;<span class="table-add fzTextButton">Add</span>
      </div>
      <br>
        <button id="export-btn" 
                class="btn fzButton" 
                type="button">GO</button>
        <p id="export"></p>
    </div>
    <br><br>
    <br><br>
    </form>
    <script src='harvestEstm.js'></script>
    <%@include file="../appGlobal/bodyBottom.jsp"%>
    <%@include file="../appGlobal/bodyBottomLoading.jsp"%>
</body>
</html>
