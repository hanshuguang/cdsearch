<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="common.jsp"%>

<html>

<head>
<title>Start Page</title>
<script src="include/util.js"></script>
<link rel="stylesheet" href="include/css.css">
</head>

<body class="index-font">
    <form method="get" action="search.jsp" id="form-query">	
        <input type="hidden" name="username" id="username" value="<%=username%>" id = "hidden-username"/>
        <%@include file="include/start-header.html"%>
    </form>

    <center>
        <iframe src="ranker.jsp?username=<%=username%>" style="width:700px;height:350px;border:0px"></iframe>
    </center>
    <center>
        <div class="crystal-footer" id="crystal-qrcode">            
        </div>
        <script language="javascript">
            generateFooter('<%=device%>');
        </script> 
    </center>
</body>
   
</html>