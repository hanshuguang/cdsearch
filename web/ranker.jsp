<%@ page language="java" import="edu.pitt.sis.recommender.*" pageEncoding="utf-8" %>

<%@include file="common.jsp"%>

<%
  final int LEN = 5;
  final int MAX = 3;
  // Uses the current and the past sessions to rank query terms, queries and clicks
  int maxSessionid = currentSessionId;
  int minSessionid = currentSessionId - LEN + 1;
  
  ArrayList<Result> queries = new ArrayList<Result>();
  ArrayList<Result> clicks = new ArrayList<Result>();
  HashMap<String, String> titles = new HashMap<String, String>();
  
  Ranker.rank(minSessionid, maxSessionid, username, db, queries, clicks);
  PageParser.PageSummarizer(clicks, titles, db);
  
  int qLen = queries.size() > MAX ? MAX : queries.size();
  int cLen = clicks.size() > MAX ? MAX : clicks.size();
  
  if(qLen == 0 && cLen == 0) {
      
  }
  
  
%>
<div style="height:250px;border: 3px solid #eaeaea;">
<div style="background-color:#E6E6FA;font-size:20px;font-weight:bold;padding:5px;">
Recent queries:
 <%
   for(int i = 0; i < qLen; i++) {
 %>
 <a href="index.jsp?username=<%=username%>&query=<%=queries.get(i).item%>" style="font-szie:17px"><%=queries.get(i).item%></a>&nbsp;&nbsp;&nbsp;
 <%
   }
 %>
</div>
<div style="position:absolute;left:15px;top:55px;font-size:20px;line-height: 200%;">
<strong>Recent clicks</strong>: <br/>
<%
   for(int i = 0; i < cLen; i++) {
%>
&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=clicks.get(i).item%>">
   <% 
   String url = clicks.get(i).item;
   if(titles.containsKey(url)) {
      out.println(titles.get(url));
   } else {
      out.println("Untitled document");
   }
   %></a><br/>
<%
   }
%>
</div>
</div>	  	    
