package edu.pitt.sis.recommender;

import edu.pitt.sis.common.Configer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.pitt.sis.common.Mysql;
import edu.pitt.sis.common.Result;

public class Ranker {
    public static final double DECAY =
        Double.parseDouble(Configer.PROP.getProperty("rankerdecay"));

    public static ArrayList<ArrayList<String>> loadUserHistory(int minSessionId,
            int maxSessionId, String username, String db) {
        String sql = "SELECT title, timeinterval, pageType, "
            + "query, pageUrl, device, timestamp FROM pageevents "
            + " WHERE username = '" + username + "' AND sessionid >= "
            + minSessionId + " AND sessionid <= " + maxSessionId;

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
    
    public static void rank(int minSessionId, int maxSessionId,
            String username, String db, ArrayList<Result> queries,
            ArrayList<Result> clicks, HashMap<String, String> titles) {
  
        ArrayList<ArrayList<String>> records = loadUserHistory(minSessionId,
                maxSessionId, username, db);
        
        HashMap<String, Result> queryScores = new HashMap<String, Result>();
        HashMap<String, Result> urlScores = new HashMap<String, Result>();
        
        scoring(records, queryScores, urlScores, titles);
        
        mapToList(queries, queryScores);
        mapToList(clicks, urlScores);
        
        sort(queries);
        sort(clicks);
    }
    
    public static void scoring(ArrayList<ArrayList<String>> records,
            HashMap<String, Result> queryScores,
            HashMap<String, Result> urlScores,
            HashMap<String, String> titles) {
        
        for(ArrayList<String> cArray : records) {
            String title = cArray.get(0);
            String pageType = cArray.get(2);
            String q = cArray.get(3).toLowerCase();            
            String url = cArray.get(4);
            
            titles.put(url, title);
            
            if(pageType.equals("click")) {
                updateMeta(url, urlScores, cArray);
            } else {
                updateMeta(q, queryScores, cArray);
            }
        }
    }
    
    
    public static void updateMeta(String item,
            HashMap<String, Result> itemScores,
            ArrayList<String> cArray) {
        
        double dwell = Double.parseDouble(cArray.get(1)) / 1000.0;
        String device = cArray.get(5);
        long recentTime = Long.parseLong(cArray.get(6));        
        double score = (System.currentTimeMillis() - recentTime) / (1000.0 * 3600.0 * 24);
        
        Result cItem = itemScores.containsKey(item)
            ? itemScores.get(item) : new Result();
        cItem.item = item;
        cItem.query = cArray.get(3);
        cItem.deltaTime += dwell;
        cItem.device = device;
        cItem.recentTime = recentTime;
        cItem.score += dwell * Math.exp(-1.0 * DECAY * score);
        itemScores.put(item, cItem);
    }

    public static void sort(ArrayList<Result> itemScores) {
        Collections.sort(itemScores, new Comparator<Result>() {
            public int compare(Result o1, Result o2) {
                return o2.score - o1.score >= 0 ? 1 : -1;
            }
        });
    }
    
    public static void mapToList(ArrayList<Result> list,
          HashMap<String, Result> map) {
        for(Map.Entry<String, Result> m : map.entrySet()) {
            list.add(m.getValue());
        }
    }
}
