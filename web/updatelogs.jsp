<%@ page language="java" import="java.util.*,edu.pitt.sis.searcher.*,edu.pitt.sis.common.*,java.net.URLEncoder" pageEncoding="utf-8" %>
<%@include file="include/common.jsp"%>
<%
   	
	String pageUrl = request.getParameter("pageUrl");
	if(pageUrl == null) pageUrl = "";
    pageUrl = new String(pageUrl.getBytes("ISO-8859-1"),"utf-8");
	
	String events = request.getParameter("event");
	if(events == null) events = "";
	
	String touchHtml = request.getParameter("touchHtml");
	if(touchHtml == null) touchHtml = "";
	touchHtml = new String(touchHtml.getBytes("ISO-8859-1"),"utf-8");
	
	String pageType = request.getParameter("pageType");
	if(pageType == null) pageType = "";
	
	String logType = request.getParameter("logType");
	if(logType == null) logType = "mtievent";
	
	String eventType = request.getParameter("eventType");
	if(eventType == null) eventType = "";
	
	String interval = request.getParameter("interval");
	if(interval == null) interval = "0";
	
	if(logType.equals("pageevent")) {
	
	  String sql = "INSERT INTO pageevents(username, timestamp, sessionid, device, userAgent, pageType, pageUrl, query, eventType, timeinterval)"
	    + " VALUES ('" + username + "', " + currentTime + ", " + currentSessionId + ", '" + device + "','" + userAgent + "','"
	    + pageType + "', '" + pageUrl.replace("'", "''") + "','" + query.replace("'", "''") + "', '" + eventType + "', " + interval + ")";
	  out.println(sql);
	  BingLogger.log(databasename, sql);
	  
    } else if(logType.equals("mtievent")) {
	
	  String sql = "INSERT INTO mtievents(username, timestamp, sessionid, device, userAgent, pageType, pageUrl, query, html, events)"
	    + " VALUES ('" + username + "', " + currentTime + ", " + currentSessionId + ", '" + device + "','" + userAgent + "','"
	    + pageType + "', '" + pageUrl.replace("'", "''") + "','" + query.replace("'", "''") + "', ?, '" + events + "')";
	  BingLogger.logWithPreparedStatement(databasename, sql, touchHtml);
	  
    }
%>