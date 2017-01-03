package edu.pitt.sis.searcher;

import edu.pitt.sis.common.Configer;
import edu.pitt.sis.common.Reader;
import edu.pitt.sis.common.Result;
import edu.pitt.sis.common.Serppage;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SerpReader {
    
    public static Serppage readPage(String userAgent, String charset,
            String meta, ArrayList<String> values, String query) {
        Serppage webpage = new Serppage();        
        try {
            String url = Configer.PROP.getProperty("searchurl").toString();
            String[] paras = Configer.PROP.getProperty("searchparams").split(";");
            for(int index = 0; index < paras.length; index++) {
                url += "&" + paras[index] + "=" + URLEncoder.encode(values.get(index), charset);
            }
            String html = Reader.readByUrl(url, userAgent, charset);
            cleanByJSoup(userAgent, meta, html, url, query, webpage);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }        
        return webpage;
    }
    
    
    public static void cleanByJSoup(String userAgent, String meta,
            String html, String url, String query, Serppage webpage) {
        
        Document doc = Jsoup.parse(html, url);
        doc.title("Search Results for " + query);
        fixPagination(doc, meta);
        removeElements(doc);
        
        HashMap<String, String> resultURL2Id = new HashMap<String, String>();
        fixURLs(doc, meta, userAgent, resultURL2Id);

        Element body = doc.select("body").first();
        body.html(Configer.PROP.getProperty("container").replace("$content$", body.html()));
        
        webpage.title = doc.title();
        webpage.html = doc.outerHtml();
        webpage.url = url;
        webpage.timestamp = System.currentTimeMillis() + "";
        webpage.URL2Ids = resultURL2Id;
    }

    public static void removeElements(Document doc) {
        for(String selector : Configer.PROP.getProperty("serpremovedselectors").split(";")) {
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

    private static void fixURLs(Document doc, String meta, String userAgent,
            HashMap<String, String> resultURL2Id) {
        String localURL = Configer.PROP.getProperty("servername")
            + Configer.PROP.getProperty("localpageurl");
        
        int idstart = 0;
        
        Elements links = doc.select("a[href]");
        for(int index = 0; index < links.size(); index++){
            Element cLink = links.get(index);
            String urlnew = cLink.attr("abs:href");
            
            if(!urlnew.contains(localURL)) {
                String serpId = "crystal-serp-" + (idstart++);
                cLink.attr("id", serpId);
                resultURL2Id.put(urlnew, serpId);
            }
            
            String newLinkUrl = urlnew.contains(localURL)
                ? urlnew : "view.jsp?" + meta + "&cdsearchurl=" + urlnew;
            cLink.attr("href",  newLinkUrl);

            if(userAgent.contains("Mobile") || userAgent.contains("Android")) {
                cLink.attr("onclick",  "logReading()");
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
        if(Configer.PROP.get("searchengine").equals("Bing")) {
            BingPagination(doc, meta);
        } else if (Configer.PROP.get("searchengine").equals("Google")) {
            GooglePagination(doc, meta);
        }
    }
    
    public static void BingPagination(Document doc, String meta) {
        Elements pages = doc.select(Configer.PROP.get("serppagination").toString());
        String serpsearchurl = Configer.PROP.get("servername").toString()
            + Configer.PROP.get("localsearchurl").toString();        
                
        for(int index = 0; index < pages.size(); index++){
            String urlnew = pages.get(index).attr("href");
            String pageno = (urlnew + "&").split("first=")[1].split("&")[0];
            pages.get(index).attr("href", serpsearchurl + "first=" + pageno + "&" + meta);			
        }
    }    
    
    public static void GooglePagination(Document doc, String meta) {
        Elements pages = doc.select(Configer.PROP.get("serppagination").toString());
        String serpsearchurl = Configer.PROP.get("servername").toString()
            + Configer.PROP.get("localsearchurl").toString();        
                
        for(int index = 0; index < pages.size(); index++){
            String urlnew = pages.get(index).attr("href");
            String pageno = (urlnew + "&").split("first=")[1].split("&")[0];
            pages.get(index).attr("href", serpsearchurl + "first=" + pageno + "&" + meta);			
        }
    }
}
