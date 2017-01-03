package edu.pitt.sis.common;

public class Result {
    // The two most important pieces of information.
    public String item = null;
    public double score = 0.0;
    
    // Meta information
    public double deltaTime = 0.0;
    public long recentTime = 0;
    public String device = "D";
    public String query = "";
}
