<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>

<%   	
    String pageUrl = request.getParameter("pageUrl");
    if(pageUrl == null) pageUrl = "";
//    pageUrl = new String(pageUrl.getBytes("ISO-8859-1"),"utf-8");

    String events = request.getParameter("event");
    if(events == null) events = "";

    String touchHtml = request.getParameter("touchHtml");
    if(touchHtml == null) touchHtml = "";
//    touchHtml = new String(touchHtml.getBytes("ISO-8859-1"),"utf-8");

    String pageType = request.getParameter("pageType");
    if(pageType == null) pageType = "";

    String logType = request.getParameter("logType");
    if(logType == null) logType = "mtievent";

    String eventType = request.getParameter("eventType");
    if(eventType == null) eventType = "";

    String interval = request.getParameter("interval");
    if(interval == null) interval = "0";
    
    String title = request.getParameter("title");
    if(title == null) title = "0";

    ArrayList<String> paras = new ArrayList<String>();
    paras.add(username);
    paras.add(currentTime + "");
    paras.add(currentSessionId + "");
    paras.add(device);
    paras.add(userAgent);
    paras.add(pageType);
    paras.add(pageUrl);
    paras.add(query);
    paras.add(title);
      
    String sql = "";
    if(logType.equals("pageevent")) {
       sql = "INSERT INTO pageevents(username, timestamp, sessionid, device, userAgent, pageType, pageUrl, query, title, eventType, timeinterval)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";      
      paras.add(eventType);
      paras.add(interval);
    } else if(logType.equals("mtievent")) {
       sql = "INSERT INTO mtievents(username, timestamp, sessionid, device, userAgent, pageType, pageUrl, query, title, html, events)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";        
        paras.add(touchHtml);
        paras.add(events);
    }
    
    if(!sql.equals("")) {
        Mysql.executeUpdatewithPreparedStaement(db, sql, paras);
    }
%>