package edu.pitt.sis.recommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import edu.pitt.sis.common.Mysql;
import edu.pitt.sis.common.Result;

public class Ranker {
	
    public static void rank(int minSessionId, int maxSessionId,
            String username, String db, ArrayList<Result> queries,
            ArrayList<Result> clicks, HashMap<String, String> titles) {

        String sql = "SELECT title, timeinterval, pageType, query, pageUrl "
            + "FROM pageevents WHERE username = '"
            + username + "' AND sessionid >= " + minSessionId
            + " AND sessionid <= " + maxSessionId;

        ArrayList<String> cols = new ArrayList<String>();
        cols.add("title");
        cols.add("timeinterval");
        cols.add("pageType");
        cols.add("query");
        cols.add("pageUrl");

        ArrayList<ArrayList<String>> records = Mysql.executeQuery(db, sql, cols);

        HashMap<String, Double> queryScores = new HashMap<String, Double>();
        HashMap<String, Double> urlScores = new HashMap<String, Double>();
        for(ArrayList<String> cArray : records) {
            String title = cArray.get(0);
            String url = cArray.get(4);
            titles.put(url, title);
            
            double time = Double.parseDouble(cArray.get(1)) / 1000.0;
            
            // Ranking for query terms.
            for(String qContent : cArray.get(3).split(" ")) {
                double score = time;
                if(queryScores.containsKey(qContent)) {
                    score += queryScores.get(qContent);
                }
                queryScores.put(qContent, score);
            }

            // Ranking for URLs.
            if(cArray.get(2).equals("click")) {
                double score = time;
                if(urlScores.containsKey(url)) {
                    score += urlScores.get(url);
                }
                urlScores.put(url, score);
            }
        }
        
        map2SortArrayList(queries, queryScores);		
        map2SortArrayList(clicks, urlScores);
    }

    public static void sort(ArrayList<Result> itemScores) {
        Collections.sort(itemScores, new Comparator<Result>() {
            public int compare(Result o1, Result o2) {
                return o2.score - o1.score >= 0 ? 1 : -1;
            }
        });
    }

    public static void map2SortArrayList(ArrayList<Result> itemScores,
            HashMap<String, Double> scores) {
        for(Map.Entry<String, Double> m : scores.entrySet()) {
            Result result = new Result();
            result.item = m.getKey();
            result.score = m.getValue();
            itemScores.add(result);
        }
        sort(itemScores);
    }
}
