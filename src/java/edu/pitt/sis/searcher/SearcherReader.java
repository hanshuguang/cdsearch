package edu.pitt.sis.searcher;

import edu.pitt.sis.common.Reader;
import java.util.Properties;

public class SearcherReader {
    public static final String CONFIG = "../../property/config.properties";
    public static Properties PROP = Reader.readProperty(CONFIG);
}
