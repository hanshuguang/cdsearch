package edu.pitt.sis.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Writer {
	
	public static void write(String fileName, String content){
            try {
                FileOutputStream fos = new FileOutputStream(fileName,true);
                OutputStreamWriter osr=new OutputStreamWriter(fos,"utf-8");
                osr.write(content);
                osr.close();
                fos.close();
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
	}
}
