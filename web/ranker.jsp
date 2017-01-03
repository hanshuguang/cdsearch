<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>

<link rel="stylesheet" href="include/css.css">

<%
  final int MAX_SESSION_DIFF = 20;
  final int MAX_ITEMS = 3;
  // Uses the current and the past sessions to rank query terms, queries and clicks
  int maxSessionid = currentSessionId;
  int minSessionid = currentSessionId - MAX_SESSION_DIFF + 1;
  
  ArrayList<Result> queries = new ArrayList<>();
  ArrayList<Result> clicks = new ArrayList<>();
  HashMap<String, String> titles = new HashMap<>();
  
  Ranker.rank(minSessionid, maxSessionid, username, db, queries, clicks, titles);
  int qLen = queries.size() > MAX_ITEMS ? MAX_ITEMS : queries.size();
  int cLen = clicks.size() > MAX_ITEMS ? MAX_ITEMS : clicks.size();
  
  if(qLen > 0) {
%>
    <div class="ranker-container">
    <div class="ranker-title lightbg"> Important Queries</div>
    <div class="ranker-body">
        <%          
          for(int i = 0; i < qLen; i++) {
              Result result = queries.get(i);
        %>
          &nbsp;&nbsp;&nbsp;
          <a href="search.jsp?query=<%=result.item%>" target="_blank">
              <%=result.item%>
          </a>
        <%
          }
        %>
    <div class="ranker-title margin-top-small">Important Clicks</div>
    <div class="ranker-body">
        <%
           for(int i = 0; i < cLen; i++) {
               Result result = clicks.get(i);
        %>
        &nbsp;&nbsp;&nbsp;
        <a href="<%=result.item%>" target="_blank">
            <% 
                String url = result.item;
                String urlTitle = titles.containsKey(url) ? titles.get(url) : "Untitled document";
                out.println(urlTitle);
            %>
        </a>&nbsp;
        <span class="ranker-meta">
            (<%=Util.formatDateForRanker(result.recentTime)%> on <img src="images/logo-<%=result.device.toLowerCase()%>.png" width="17px"></img>)
        </span>
        <br/>
        <%
          }
        %>
    </div>
<%
}
%>
</div>
</div>