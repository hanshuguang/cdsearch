package edu.pitt.sis.searcher;

import edu.pitt.sis.common.Reader;
import edu.pitt.sis.common.Webpage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SerpReader extends SearcherReader {
    
    public static Webpage readPage(String userAgent, String charset,
            String meta, ArrayList<String> values, String query) {
        Webpage webpage = new Webpage();
        try {            
            String url = PROP.getProperty("searchurl").toString();
            String[] paras = PROP.getProperty("searchparams").split(";");
            for(int index = 0; index < paras.length; index++) {
                url += "&" + paras[index] + "=" + URLEncoder.encode(values.get(index), charset);
            }
            System.out.println("url=" + url);
            System.out.println("query=" + query);
            String html = Reader.readByUrl(url, userAgent, charset);
            cleanByJSoup(userAgent, meta, html, url, query, webpage);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }        
        return webpage;
    }
    
    
    public static void cleanByJSoup(String userAgent, String meta,
            String html, String url, String query, Webpage webpage) {
        
        Document doc = Jsoup.parse(html, url);
        doc.title("Search Results for " + query);
        fixPagination(doc, meta);
        removeElements(doc);
        
        fixedURLs(doc, meta, userAgent);

        Element body = doc.select("body").first();
        body.html(PROP.getProperty("container").replace("$content$", body.html()));
        
        webpage.title = doc.title();
        webpage.html = doc.outerHtml();
        webpage.url = url;
        webpage.timestamp = System.currentTimeMillis() + "";
    }

    public static void removeElements(Document doc) {
        for(String selector : PROP.getProperty("serpremovedselectors").split(";")) {
            Elements eles = doc.select(selector);
            if(eles == null) {
                return;
            }

            for(int index = 0; index < eles.size(); index++){
                Element ele = eles.get(index);
                ele.remove();
            }
        }
    }

    private static void fixedURLs(Document doc, String meta, String userAgent) {
        String localURL = PROP.getProperty("servername")
            + PROP.getProperty("localpageurl");
        
        Elements links = doc.select("a[href]");
        for(int index = 0; index < links.size(); index++){
            String urlnew = links.get(index).attr("abs:href");
            String newLinkUrl = urlnew.contains(localURL)
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
    
    public static void fixPagination(Document doc, String meta) {
        // Fixes the pagination
        if(PROP.get("searchengine").equals("Bing")) {
            BingPagination(doc, meta);
        }
    }
    
    public static void BingPagination(Document doc, String meta) {
        Elements pages = doc.select(PROP.get("serppagination").toString());
        String serpsearchurl = PROP.get("servername").toString()
            + PROP.get("localsearchurl").toString();        
                
        for(int index = 0; index < pages.size(); index++){
            String urlnew = pages.get(index).attr("href");
            String pageno = (urlnew + "&").split("first=")[1].split("&")[0];
            pages.get(index).attr("href", serpsearchurl + "first=" + pageno + "&" + meta);			
        }
    }
}
