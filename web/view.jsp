<%@ page language="java" import="java.util.*,edu.pitt.sis.searcher.*,edu.pitt.sis.common.*,java.net.URLEncoder" pageEncoding="utf-8" %>

<%@include file="include/common.jsp"%>
<%
    String pageType = "click";
	String pageUrl = "";
	String currentUrl = request.getQueryString();
	if(currentUrl.contains("&cdsearchurl=") && currentUrl.split("&cdsearchurl=").length > 1) {
	    currentUrl = currentUrl.split("&cdsearchurl=")[1];
		String refresh = "<meta http-equiv=\"refresh\" content=\"0; url=" + currentUrl + "\">";
		String html = "";
		
		if(!currentUrl.endsWith(".pdf") && !currentUrl.endsWith(".doc") && !currentUrl.endsWith(".docx")
		  && !currentUrl.endsWith(".ppt") && !currentUrl.endsWith(".pptx") && !currentUrl.endsWith(".xls")
		  && !currentUrl.endsWith(".xlsx") && !currentUrl.endsWith(".zip") && !currentUrl.endsWith(".rar")
		  && !currentUrl.endsWith(".pdf") && !currentUrl.endsWith(".csv")) {
		    html = BingPageReader.readPage(currentUrl, "utf-8", userAgent,
		        "username=" + username + "&query=" + URLEncoder.encode(query, "utf-8"));
		} else {
		    html = "";
		}
		
		// Logs clicked web page		
	    pageUrl = currentUrl;		
		String sql = "INSERT INTO htmls(username, timestamp, sessionid, device, userAgent, pageType, pageUrl, query, html) VALUES"
	        + "('" + username + "', " + currentTime + ", " + currentSessionId + ", '" + device + "', '"
		        + userAgent + "', 'click', '" + pageUrl.replace("'", "''") + "', '" + query.replace("'", "''") + "', ?)";
	    BingLogger.logWithPreparedStatement(databasename, sql, html);
		
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

<%@include file="hammer.jsp"%>

