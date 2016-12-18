<%@ page language="java" import="java.util.*,edu.pitt.sis.searcher.*,edu.pitt.sis.recommender.*,edu.pitt.sis.common.*,java.net.URLEncoder,javax.servlet.RequestDispatcher" pageEncoding="utf-8" %>

<%    	
    String username = (String) session.getAttribute("username");
    if(username == null || username == "") {
        response.sendRedirect("login.html");
    }

    String db = "cdsearch";

    String query = request.getParameter("query");
    if(query == null) query = "";
//	query = new String(query.getBytes("ISO-8859-1"),"utf-8");

    String first = request.getParameter("first");
    if(first == null) first = "1";

    String userAgent = request.getHeader("user-agent");
    //userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_1_1 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/55.0.2883.79 Mobile/14B100 Safari/602.1";
    boolean isMobile = userAgent.contains("Mobile") || userAgent.contains("Android");
    String device = isMobile ? "M" : "D";

    // Logs the HTML source.
    long currentTime = System.currentTimeMillis();
    int[] sessionids = Util.getSessionId(db, username, currentTime);
    int currentSessionId = sessionids[0] + sessionids[1];
    currentSessionId = currentSessionId == 0 ? 1 : currentSessionId;	
%>

