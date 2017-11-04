/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

/**
 *
 * @author numan
 */
import java.sql.*;

public class DB 
{
    public String cS = "jdbc:mysql://localhost/finansdata";
    Connection conn;
    
    public DB()
    {
        try 
        {
            Class.forName("org.gjt.mm.mysql.Driver");
            conn = DriverManager.getConnection(cS, "root", "");
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    // Query With No Result
    public void qWNR(String sql)
    {
        try
        {
            Statement sorgu = conn.createStatement();
            sorgu.execute(sql);
            sorgu.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    // Query With 1 Result
    public ResultSet qW1R(String sql)
    {
        ResultSet rs = null;
        try
        {
            Statement sorgu = conn.createStatement();
            rs = sorgu.executeQuery(sql);
            rs.next();
        } catch (Exception e) { e.printStackTrace(); }
        return rs;
    }
    
    public ResultSet qWMR(String sql)
    {
        ResultSet rs = null;
        try
        {
            Statement sorgu = conn.createStatement();
            rs = sorgu.executeQuery(sql);
        } catch (Exception e) { e.printStackTrace(); }
        return rs;
    }
    
}

