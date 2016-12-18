package edu.pitt.sis.searcher;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import edu.pitt.sis.common.Reader;
import edu.pitt.sis.common.Webpage;

public class PageReader extends SearcherReader {
    
    public static Webpage readPage(String url, String charset,
            String userAgent, String meta) {
        Webpage page = new Webpage();
        try {
            if(!shouldRedirect(url)) {
                String html =  Reader.readByUrl(url, userAgent, charset);
                cleanByJSoup(html, url, meta, userAgent, page);
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return page;
    }
    
    public static boolean shouldRedirect(String url) {        
        for(String fType : PROP.getProperty("redirectpages").toString().split(";")) {
            if(url.endsWith(fType)) {
                return true;
            }
        }        
        return false;
    }
	
    public static void cleanByJSoup(String html, String url, String meta,
            String userAgent, Webpage page) {
        Document doc = Jsoup.parse(html, url);
        fixedURLs(doc, meta, userAgent);
        page.html = doc.outerHtml();
        page.title = doc.title();
        page.url = url;
        page.timestamp = System.currentTimeMillis() + "";
    }
	
    private static void fixedURLs(Document doc, String meta, String userAgent) {		
        Elements links = doc.select("a[href]");
        for(int index = 0; index < links.size(); index++){
            String urlnew = links.get(index).attr("abs:href");

            String urlBase = PROP.getProperty("servername").toString()
               + PROP.getProperty("localviewurl").toString();
            String newLinkUrl = urlBase + meta + "&cdsearchurl=" + urlnew;
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

}
