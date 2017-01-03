package edu.pitt.sis.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Mysql {
    
    public static final String DRIVER = "org.gjt.mm.mysql.Driver";
    public static final String HOST = Configer.PROP.getProperty("mysqlhost");
    public static final String PORT = Configer.PROP.getProperty("mysqlport");
    public static final String USERNAME = Configer.PROP.getProperty("mysqlusername");
    public static final String PASSWORD = Configer.PROP.getProperty("mysqlpassword");

    public static Connection getConn(String db) {
        try {
            Class.forName(DRIVER).newInstance();

            return DriverManager.getConnection("jdbc:mysql://"
                + HOST + ":" + PORT + "/"
                + db + "?user=" + USERNAME + "&password="
                + PASSWORD + "&useUnicode=true&characterEncoding=utf-8");
        } catch(Exception e) {
            e.printStackTrace();
            return null;		
        }
    }


    public static void executeUpdate(String db, String sql){
        try {
            Connection conn = Mysql.getConn(db);
            Statement st = conn.createStatement();
            st.executeUpdate(sql);
            st.close();
            conn.close();
        } catch(SQLException sqle){
            System.out.println("Errors in query.");
        }
    }

    public static void executeUpdatewithPreparedStaement(String db,
            String sql, ArrayList<String> paras){
        try {
            Connection conn = Mysql.getConn(db);
            PreparedStatement st = conn.prepareStatement(sql);
            for(int i = 1; i <= paras.size(); i++) {
                System.out.println(i);
                st.setString(i, paras.get(i - 1));
            }
            st.executeUpdate();
            st.close();
            conn.close();
        } catch(SQLException sqle){
            System.out.println("Errors in query.");
        }
    }

    public static ArrayList<ArrayList<String>> executeQuery(String db,
            String sql, ArrayList<String> cols) {
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();
        try {
            Connection conn = Mysql.getConn(db);	
            Statement st = conn.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sql);
            while(rs.next()){
                ArrayList<String> result = new ArrayList<String>();
                for(int i = 0; i < cols.size(); i++) {
                    result.add(rs.getString(cols.get(i)));
                }
                results.add(result);
            }
            rs.close();
            st.close();
            conn.close();
        } catch(SQLException sqle){
                System.out.println("Errors in query.");
        }
        return results;
    }
}
