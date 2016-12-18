<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="common.jsp"%>

<%
    String pageType = "click";
    String url = request.getQueryString();
    String html = "";
    
    if(url.contains("&cdsearchurl=") && url.split("&cdsearchurl=").length > 1) {
        url = url.split("&cdsearchurl=")[1];        
        String meta = "username=" + username + "&query=" + URLEncoder.encode(query, "utf-8");
        Webpage webpage = PageReader.readPage(url, "utf-8", userAgent, meta);
        if(webpage.html == null) {
            html = "<meta http-equiv=\"refresh\" content=\"0; url=" + url + "\">";
        } else {
            html = webpage.html;
            url = webpage.url;
        }
        
        String sql = "INSERT INTO htmls(username, timestamp, sessionid, device, "
            + " userAgent, pageType, pageUrl, query, html, title) VALUES"
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
        
        //out.println(refresh);
        out.println(html);
    } else {
    if(isMobile) { %>
      <%@include file="include/mobile-view-notfound.jsp"%>
    <% } else { %>
      <%@include file="include/desktop-view-notfound.jsp"%>
    <% }
    }
%>

<input type = "hidden" name="username" id="username" value="<%=username%>" />
<input type="hidden" id="pagetype" value="<%=pageType%>" />
<input type="hidden" id="query" value="<%=URLEncoder.encode(query, "utf-8")%>" />
<input type="hidden" id="url" value="<%=URLEncoder.encode(url, "utf-8")%>" />

<%@include file="hammer.html"%>

