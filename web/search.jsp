<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>

<form method="get" action="search.jsp" id="form-query">
    <input type = "hidden" name="username" id="username" value="<%=username%>" />
    <% if(isMobile) { %>
        <%@include file="include/mobile-serp.jsp"%>
    <% } else { %>
        <%@include file="include/desktop-serp.jsp"%>
    <% } %>
</form>

<%
if(!query.equals("")) {
    String pageType = "serp";
    String meta = "username=" + username + "&query=" + URLEncoder.encode(query, "utf-8");
    ArrayList<String> values = new ArrayList<String>();
    values.add(query);
    values.add(first);

    Webpage webpage = SerpReader.readPage(userAgent, "utf-8", meta, values, query);
    out.println(webpage.html);

    // Records HTML sources.
    String sql = "INSERT INTO htmls(username, timestamp, sessionid, device, "
            + "userAgent, pageType, pageUrl, query, html, title) VALUES"
            + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    ArrayList<String> paras = new ArrayList<String>();
    paras.add(username);
    paras.add(webpage.timestamp);
    paras.add(currentSessionId + "");
    paras.add(device);
    paras.add(userAgent);
    paras.add(pageType);
    paras.add(webpage.url);
    paras.add(query);
    paras.add(webpage.html);
    paras.add(webpage.title);
    Mysql.executeUpdatewithPreparedStaement(db, sql, paras);
%>
    <input type="hidden" id="pagetype" value="<%=pageType%>" />
    <input type="hidden" id="query" value="<%=URLEncoder.encode(query, "utf-8")%>" />
    <input type="hidden" id="url" value="<%=URLEncoder.encode(webpage.url, "utf-8")%>" />
    
    <%@include file="hammer.html"%>
<%
}
%>
