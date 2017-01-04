package edu.pitt.sis.recommender;

import edu.pitt.sis.common.Configer;
import edu.pitt.sis.common.Result;
import edu.pitt.sis.common.Serppage;
import edu.pitt.sis.common.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SerpRanker {
    
    public static final String WRANK = Configer.PROP.getProperty("rankerwrank");
    public static final String WKNOW = Configer.PROP.getProperty("rankerwknow");
    public static final String WDEVICE = Configer.PROP.getProperty("rankerwdevice");
    public static final String DECAY =Configer.PROP.getProperty("rankerdecay");
        
    public static String rerank(String user, String db, Serppage serp,
            String device) {
        ArrayList<ArrayList<String>> records = Util.LoadHistoryByURL(user,
            db, serp.URL2Ids);
        
        HashMap<String, Double> id2Scores = new HashMap<>();
        
        // Layer I: scores for ranking positions
        for(Map.Entry<String, String> url2id : serp.URL2Ids.entrySet()) {
            String id = url2id.getValue();
            int rank = Integer.parseInt(id.replace("crystal-serp-", ""));
            id2Scores.put(id, Double.parseDouble(WRANK) * 1.0 / Math.log(rank + 2));
        }
        
        // Layer II : relevance of visitation
        long cTime = System.currentTimeMillis();
        HashMap<String, String> lastVisits = new HashMap<>();
        records.forEach((ArrayList<String> record) -> {
            StringBuilder result = new StringBuilder("");
            String id = serp.URL2Ids.get(record.get(4));       
            result.append(record.get(3).replace("'", "")).append(","); // Query            
            long rTime = Long.parseLong(record.get(6)); // Recent Time
            result.append(Util.formatDateForRanker(rTime)).append(",");            
            result.append(record.get(5)); // Device
            lastVisits.put(id, result.toString());
            
            if(id != null) {
                double deviceFactor = record.get(5).equals(device)
                    ? Double.parseDouble(WDEVICE)
                        : (1.0 - Double.parseDouble(WDEVICE));
                double dwell = Double.parseDouble(record.get(1)) / 1000.0;
                double score = (cTime - rTime) / (1000.0 * 3600.0 * 24);
                score = Double.parseDouble(WKNOW) * dwell / 10.0
                    * Math.exp(-1.0 * Double.parseDouble(DECAY) * score)
                        * deviceFactor;
                id2Scores.put(id, id2Scores.get(id) - score);
            }
        });
        
        // Rank the HashMap
        ArrayList<Result> id2ScoresAsList = new ArrayList<>();
        id2Scores.entrySet().forEach((res) -> {
            Result result = new Result();
            result.item = res.getKey();
            result.score = res.getValue();
            id2ScoresAsList.add(result);
        });
        ReaccessRanker.sort(id2ScoresAsList);
        
        StringBuilder allResults = new StringBuilder("");
        for(int i = 0; i < id2ScoresAsList.size(); i++) {
            String id = id2ScoresAsList.get(i).item;
            String newid = "crystal-serp-" + i;
            double score = id2ScoresAsList.get(i).score;
            String lastVisit = lastVisits.containsKey(id)
                ? lastVisits.get(id) : "";
            allResults.append(newid).append(",")
                .append(id).append(",").append(score).append(",")
                .append(lastVisit).append(";");
        }
        
        return allResults.toString();
    }
}
