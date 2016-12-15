package edu.pitt.sis.searcher;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.pitt.sis.common.Reader;

public class BingPageReader {
	
	public static String readPage(String pageURL, String charset, String userAgent, String meta) {
		StringBuilder page = new StringBuilder("");
		try {			
			String html = Reader.readByUrl(pageURL, userAgent, charset);
			page.append(cleanByJSoup(html, pageURL, meta, userAgent));
		} catch(IOException ioe) {
			// Should log in the backend
		}
		return page.toString();
	}
	
	public static String cleanByJSoup(String html, String url, String meta, String userAgent) {
		Document doc = Jsoup.parse(html, url);
		fixedURLs(doc, meta, userAgent);
		return doc.outerHtml();
	}
	
	private static void fixedURLs(Document doc, String meta, String userAgent) {		
		Elements links = doc.select("a[href]");
		for(int index = 0; index < links.size(); index++){
			String urlnew = links.get(index).attr("abs:href");
			String newLinkUrl = "view.jsp?" + meta + "&cdsearchurl=" + urlnew;
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
	
	public static void main(String args[]) {
		String url = "http://www.thefreedictionary.com/mobile";
		String html = BingPageReader.readPage(url, "utf-8", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36", "");
		System.out.println(html);
	}

}
