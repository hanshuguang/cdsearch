<%@ page language="java" import="java.util.*,edu.pitt.sis.searcher.*,edu.pitt.sis.common.*,java.net.URLEncoder" pageEncoding="utf-8" %>
<%    	
    String type = request.getParameter("type");
    if(type == null) type = "";

    String username = request.getParameter("username");
    if(username == null) username = "";

    String password = request.getParameter("password");
    if(password == null) password = "";
    
    String db = "cdsearch";
    
    String ipaddress = request.getRemoteAddr();
    if(ipaddress == null) ipaddress = "";

    String responseText = "";
    if(type.equals("login")) {
        ArrayList<String> cols = new ArrayList<String>();
        cols.add("username");
        String sql = "SELECT username FROM users WHERE username = '"
                + username + "' AND password = '" + password + "';";
        ArrayList<ArrayList<String>> results = Mysql.executeQuery(db, sql, cols);
        responseText = results.size() > 0 ? "success" : "fail";
        
        if(results.size() > 0) {
            String sqlLocation = "INSERT INTO userlocations(username, ip, logtimesession)"
                +" VALUES(?, ?, ?)";
            ArrayList<String> paras = new ArrayList<>();
            paras.add(username);
            paras.add(ipaddress);
            paras.add(System.currentTimeMillis() + "");
            Mysql.executeUpdatewithPreparedStaement(db, sqlLocation, paras);
        }
        
        session.setAttribute("username", username);
    }
    
    if(type.equals("logout")) {
        session.invalidate();
    }

    out.println(responseText);
%>

