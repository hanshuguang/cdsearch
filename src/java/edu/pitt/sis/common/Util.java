package edu.pitt.sis.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    
    public static String formatDateForRanker(long timeInMilliseconds) {        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        Calendar today = Calendar.getInstance();
        String todayFormatted = dateFormat.format(today.getTime());
        
        today.add(Calendar.DATE, -1);
        String yesterdayFormatted = dateFormat.format(today.getTime());
        
        Calendar inputDate = Calendar.getInstance();
        inputDate.setTimeInMillis(timeInMilliseconds);
        String inputFormatted = dateFormat.format(inputDate.getTime());
        
        String formatted = "";
        if(inputFormatted.equals(todayFormatted)) {
            String formattedTime = new SimpleDateFormat("HH:mm:ss")
                    .format(new Date(timeInMilliseconds));
            formatted = "Today " + formattedTime;
        } else if (inputFormatted.equals(yesterdayFormatted)) {
            formatted = "Yesterday";
        } else {
            long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()
                    - timeInMilliseconds);
            formatted = days + " days ago";
        }        
        return formatted;
    }
    
     public static ArrayList<ArrayList<String>> loadHistoryByURL(String user,
            String db, HashMap<String, String> urls) {
        StringBuilder sb = new StringBuilder("");
        urls.entrySet().forEach((m) -> {
            sb.append("'").append(m.getKey()).append("', ");
        });
        sb.append("''");
        
        String sql = "SELECT title, timeinterval, pageType, "
            + "query, pageUrl, device, timestamp FROM pageevents "
            + " WHERE username = '" + user + "' AND pageType = 'click' "
            + " AND pageUrl in (" + sb.toString() + ")";

        ArrayList<String> cols = new ArrayList<>();
        cols.add("title");
        cols.add("timeinterval");
        cols.add("pageType");
        cols.add("query");
        cols.add("pageUrl");
        cols.add("device");
        cols.add("timestamp");

        return Mysql.executeQuery(db, sql, cols);
    }
}
