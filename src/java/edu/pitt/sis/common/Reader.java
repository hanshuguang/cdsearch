package edu.pitt.sis.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

public class Reader {

	public static String readByUrl(String url, String userAgent, String charset)
			throws IOException{
            StringBuilder htmlSource = new StringBuilder("");

            HttpClient client = new HttpClient();
            HttpMethod get = new GetMethod(url);

            setBroswerInfo(get, userAgent);
            client.executeMethod(get);

            int statusCode = get.getStatusCode();
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY 
              || statusCode == HttpStatus.SC_MOVED_TEMPORARILY 
                 || statusCode ==  HttpStatus.SC_SEE_OTHER 
                    || statusCode == HttpStatus.SC_TEMPORARY_REDIRECT) {

                    Header header = get.getResponseHeader("location");
            if (header != null) {
                String newurl = header.getValue();
                if (header != null && !header.equals("") && newurl.equals("")) {
                    HttpMethod method = new GetMethod(newurl);	            	
                            setBroswerInfo(method, userAgent);
                            client.executeMethod(method);
                            htmlSource.append(readHTMLContent(method, charset));
                            method.releaseConnection();
                }
            }
          } else {	
              htmlSource.append(readHTMLContent(get, charset));
          }		
              get.releaseConnection();

          return htmlSource.toString();
	}
	
	// Reads HTML content.
	public static String readHTMLContent(HttpMethod get, String charset) throws IOException {
		StringBuilder htmlSource = new StringBuilder("");
		InputStream ism = get.getResponseBodyAsStream();
	    Scanner in = new Scanner(ism, charset);
	    while(in.hasNextLine()) {
	    	htmlSource.append(in.nextLine() + " ");
	    } 
	    in.close();
	    ism.close();
	    return htmlSource.toString();
	}
    
    // Sets meta data information for Browser.
    public static void setBroswerInfo(HttpMethod get, String userAgent) {
    	get.setRequestHeader("User-Agent", userAgent);
		get.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);	
    }
    
    public static Properties readProperty(String fileName) {
        Properties prop = new Properties();
	InputStream input = null;
        
	try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            input = classLoader.getResourceAsStream(fileName);
            //input = new FileInputStream(fileName);
            prop.load(input);
	} catch (IOException ex) {
            ex.printStackTrace();
	} finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
	}
        return prop;
    }

}
