package edu.pitt.sis.recommender;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.pitt.sis.common.Mysql;
import edu.pitt.sis.common.Result;

public class PageParser {

	public static void PageSummarizer(ArrayList<Result> urls,
			HashMap<String, String> titles, String dbname) {
		StringBuilder sb = new StringBuilder();
		for(Result url : urls) {
			sb.append("'" + url.item + "',");
		}
		sb.append("''");
		
		String sql = "SELECT pageUrl, html FROM htmls WHERE pageUrl IN (" + sb.toString() + ")";
		System.out.println(sql);
		readFromDb(sql, dbname, titles);
	}
	
    private static void readFromDb(String sql, String dbname,
    		HashMap<String, String> records) {
		// Reads content from database.
    	ArrayList<String> cols = new ArrayList<String>();
    	cols.add("html");
    	cols.add("pageUrl");
    	
    	ArrayList<ArrayList<String>> results = Mysql.executeQuery(dbname, sql, cols);
    	for(ArrayList<String> result : results) {
    		Document doc = Jsoup.parse(result.get(0));
			records.put(result.get(1), doc.title());
    	}
	}

}
