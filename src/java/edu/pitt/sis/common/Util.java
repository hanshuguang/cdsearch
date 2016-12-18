package edu.pitt.sis.common;

import java.util.ArrayList;

public class Util {
    
    public static int[] getSessionId(String db, String user, long timestamp) {
        // [0] ==> current session id
        // [1] ==> increase by one
        int[] sessionids = new int[2];
    	
    	String sql = "SELECT timestamp, sessionid FROM htmls WHERE username = '"
    			+ user + "' ORDER BY timestamp DESC LIMIT 1";

    	ArrayList<String> cols = new ArrayList<String>();
    	cols.add("timestamp");
    	cols.add("sessionid");
    	ArrayList<ArrayList<String>> records = Mysql.executeQuery(db, sql, cols);
    	
    	if(records.size() >= 1) {
    		long max = Long.parseLong(records.get(0).get(0));				
		int sid = Integer.parseInt(records.get(0).get(1));
    		sessionids[0] = sid;
    		if((timestamp - max) > 1800000) {
                    sessionids[1] = 1;
		}
    	}
        return sessionids;
    }
}
