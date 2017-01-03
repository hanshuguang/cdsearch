package edu.pitt.sis.searcher;

import edu.pitt.sis.common.Serppage;
import edu.pitt.sis.common.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SerpHighlighter {

    public static String highlighter(String user, String db, Serppage serp) {
        ArrayList<ArrayList<String>> records = Util.loadHistoryByURL(user,
            db, serp.URL2Ids);
        
        HashMap<String, String> results = new HashMap<>();
        records.forEach((ArrayList<String> record) -> {
            StringBuilder result = new StringBuilder("");
            String id = serp.URL2Ids.get(record.get(4));
            // ID
            result.append(id).append(",");
            // Query
            result.append(record.get(3).replace("'", "")).append(",");
            // Recent Time
            long rTime = Long.parseLong(record.get(6));
            result.append(Util.formatDateForRanker(rTime)).append(",");
            // Device
            result.append(record.get(5));
            result.append(";");
            results.put(id, result.toString());
        });
        
        StringBuilder allResults = new StringBuilder("");
        for(Map.Entry<String, String> res : results.entrySet()) {
            allResults.append(res.getValue());
        }
        return allResults.toString();
    }
}
