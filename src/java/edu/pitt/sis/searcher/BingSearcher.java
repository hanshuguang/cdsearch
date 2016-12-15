package edu.pitt.sis.searcher;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.pitt.sis.common.Reader;

public class BingSearcher {
	
	private static final String BASE_URL = "http://www.bing.com/search?";
	
	public static String readSERP(String query, int start, String charset,
			String userAgent, String meta, String bottom) {
		StringBuilder serp = new StringBuilder("");
		try {
			String serpURL = BASE_URL + "q=" + URLEncoder.encode(query, charset) + "&first=" + start;			
			String html = Reader.readByUrl(serpURL, userAgent, charset);
			serp.append(cleanByJSoup(html, serpURL, meta, query, bottom, userAgent));
		} catch(IOException ioe) {
			// Should log in the backend
		}
		return serp.toString();
	}
	
	public static String cleanByJSoup(String html, String url, String meta, String query,
			String bottom, String userAgent) {
		Document doc = Jsoup.parse(html, url);
		doc.title("Search Results for " + query);
		removeElements(doc);
		fixedURLs(doc, meta, userAgent);
		
		Element body = doc.select("body").first();
		body.html( "<div id='crystalcontainer' style='overflow-y: hidden;'>" + body.html() + bottom + "</div>");
		
		return doc.outerHtml();
	}
	
	public static void removeElements(Document doc) {

		ArrayList<String> selectors = new ArrayList<String>();
		selectors.add("header#b_header");selectors.add("footer.b_footer");
		selectors.add("ol#b_context");selectors.add("div.b_rs");
		selectors.add("div.b_float_img");selectors.add("div.b_rich");
		selectors.add("li.b_msg");selectors.add("li.b_ans");
		selectors.add("li.b_ad");selectors.add("div.slide");
		selectors.add("div.sb_hbop");
		
		for(String selector : selectors) {
			removeElements(doc, selector);
		}
	}
	
	private static void removeElements(Document doc, String selector) {
		Elements eles = doc.select(selector);
		
		if(eles == null) {
			return;
		}
		
		for(int index = 0; index < eles.size(); index++){
			Element ele = eles.get(index);
			ele.remove();
		}
	}
	
	private static void fixedURLs(Document doc, String meta, String userAgent) {		
		// Fixes the pagination
		Elements pages = doc.select("a[aria-label^=Page]");
		for(int index = 0; index < pages.size(); index++){
			String urlnew = pages.get(index).attr("href");
			String pageno = (urlnew + "&").split("first=")[1].split("&")[0];
			pages.get(index).attr("href", "http://crystal.exp.sis.pitt.edu:8080/BeeTrace/index.jsp?first=" + pageno + "&" + meta);			
		}
		
		Elements links = doc.select("a[href]");
		for(int index = 0; index < links.size(); index++){
			String urlnew = links.get(index).attr("abs:href");
			String newLinkUrl = urlnew.contains("crystal.exp.sis.pitt.edu:8080/BeeTrace")
				? urlnew : "view.jsp?" + meta + "&cdsearchurl=" + urlnew;
			links.get(index).attr("href",  newLinkUrl);
			
			if(userAgent.contains("Mobile") || userAgent.contains("Android")) {
				links.get(index).attr("onclick",  "logReading()");;
			}
		}
		
		Elements medias = doc.select("[src]");
		for(int index = 0; index < medias.size(); index++){
			String urlnew = medias.get(index).attr("abs:src");
			medias.get(index).attr("src", urlnew);
		}
		
		Elements imports = doc.select("link[href]");
		for(int index = 0; index < imports.size(); index++){
			String urlnew = imports.get(index).attr("abs:href");
			imports.get(index).attr("href", urlnew);
		}
	}
	
	public static void main(String[] args) {
//		String html = BingSearcher.readSERP("information retrieval", 1, "utf-8", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36", "");
//		Writer.write("output.html", html);
	}

}
