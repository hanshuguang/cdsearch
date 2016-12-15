<%@include file="common.jsp"%>

<html>
<head>
<title>Start Page</title>
<script src="include/util.js"></script>
</head>
<body>
	<form method="get" action="index.jsp" id="form-query">	
	<input type = "hidden" name = "username" value = "<%=username%>" id = "hidden-username"/>
	<%@include file="include/start-header.jsp"%>	
	</form>
	
	<center>
	  <iframe src="ranker.jsp?username=<%=username%>" style="width:700px;height:300px;border:0px" />
	</center>
</body>
</html>