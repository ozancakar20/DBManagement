/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handlers;

import Models.Veritabani;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ozan
 */
public class VeritabaniHandler {

    public List<Veritabani> VeritabanlariGetir(Connection con) {

        List<Veritabani> liste = new ArrayList<Veritabani>();
        try {
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getCatalogs();
            while (rs.next()) {
                Veritabani vt = new Veritabani();
                vt.setAdi(rs.getString("TABLE_CAT"));
                liste.add(vt);
            }
        } catch (SQLException ex) {
            System.out.println("veritabanı çekmede hata" + "-----" + ex);
        }
        return liste;
    }

    public void VeritabaniEkle(Connection con, String isim) {
        String sql = "CREATE DATABASE IF NOT EXISTS " + isim;
        System.out.println(sql);
        try {
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (Exception e) {
            System.out.println("eklemede hata" + "-----" + e);
        }

    }

    public void VeritabaniSil(Connection con, String isim) {
        String sql = "DROP DATABASE " + isim;
        System.out.println(sql);
        try {
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (Exception e) {
            System.out.println("silmede hata" + "-----" + e);
        }
    }

}
