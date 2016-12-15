<%@ page language="java" import="java.util.*,edu.pitt.sis.searcher.*,edu.pitt.sis.common.*,java.net.URLEncoder" pageEncoding="utf-8" %>

<%@include file="include/common.jsp"%>

<title>Crystal - Search Page</title>

<form method="get" action="index.jsp" id="form-query">
<input type = "hidden" name = "username" value = "<%=username%>" />
<% if(isMobile) { %>
    <%@include file="include/mobile-serp.jsp"%>
<% } else { %>
    <%@include file="include/desktop-serp.jsp"%>
<% } %>
</form>

<%
if(!query.equals("")) {
	String pageUrl = "http://crystal.exp.sis.pitt.edu:8080/BeeTrace/index.jsp?first="
	  + first + "&username=" + username + "&query=" + query;
	
    String html = BingSearcher.readSERP(query, Integer.parseInt(first), "utf-8", userAgent,
	    "username=" + username + "&query=" + URLEncoder.encode(query, "utf-8"), "");	
	out.println(html);
	
	// Records data to the database.
	String sql = "INSERT INTO htmls(username, timestamp, sessionid, device, userAgent, pageType, pageUrl, query, html) VALUES"
	    + "('" + username + "', " + currentTime + ", " + currentSessionId + ", '" + device + "', '"
		+ userAgent + "', 'serp', '" + pageUrl.replace("'", "''") + "', '" + query.replace("'", "''") + "', ?)";
	BingLogger.logWithPreparedStatement(databasename, sql, html);
	
	// Tracks user behaviors on SERPs with hammer.js
	String pageType = "serp";
%>
    <%@include file="hammer.jsp"%>
<%
}
%>
