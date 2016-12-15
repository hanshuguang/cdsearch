<%@ page language="java" import="java.util.*,java.sql.*,mendeley.pe.pitt.edu.*" pageEncoding="utf-8"%>

<head>
	<title>Login Page</title>
	<script src="include/util.js"></script>
	<script language="javascript">
		function submit(){
		    // examines whether a user has filled the form
			var username = document.getElementById('username').value;
			var password = document.getElementById('password').value;
			if (username == null || username == "" || password == "" || password == null){
				document.getElementById('message').innerHTML = "Username or password cannot be empty.";
				return;
			}
			// Examines whether the username and password is correct.
			var url = "api.jsp?type=login&username=" + username + "&password=" + password;
			sendRequest(url, redirect);
		}
		
		function redirect(text) {
		    if(text.includes("success")) {
			    window.location.href = "index.jsp";
			}
			
			if(text.includes("fail")) {
			    document.getElementById('message').innerHTML = "Either your username or password is incorrect.";
			}
		}
	</script>
</head>

<body>
	<div style="width:100%; height:50px; font-size: 20px; font-weight:bold; background:#eaeaea; ">
		 <div style="float:left; margin: 10px"><strong>Login Page</strong> </div>
	</div>
	<div style="margin:5px">
	  <table>
	      <tr height="20px">
		     <td colspan="2" id="message" style="color:red"></td> </tr>
		  <tr height="50px"> 
			 <td align="right"> Username:</td>
			 <td>&nbsp;&nbsp;<input type="text" id="username" value="" style="height:30px;width:300px;font-size:15px"/> </td>
		  </tr>
		  <tr height="50px"> 
			 <td align="right"> Password:</td>
			 <td>&nbsp;&nbsp;<input type="password" id="password" value="" style="height:30px;width:300px;font-size:15px"/> </td>
		  </tr>			  
		   <tr height="50px"> 
			 <td align="right"> </td>
			 <td align="right"><input type="button" name="submit" value="Submit" style="height:35px;font-size:15px" onclick="submit()"/> </td>
		  </tr>
	   </table>		
	</div>
</body>

</html>

