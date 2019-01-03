/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handlers;

import Models.Tablo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ozan
 */
public class TabloHandler {

    public List<Tablo> TablolariGetir(Connection con, String vt_ismi) {

        String sql = "SELECT table_name, table_rows, data_length "
                + "FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE TABLE_SCHEMA = '" + vt_ismi + "'";
        List<Tablo> liste = new ArrayList<Tablo>();
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Tablo t = new Tablo();
                t.setAdi(rs.getString(1));
                t.setSatirSayisi(rs.getString(2));
                t.setDataLength(rs.getString(3));
                liste.add(t);
            }
        } catch (SQLException ex) {
            System.out.println("tablo çekmede hata" + "-----" + ex);
        }

        return liste;
    }

    public void TabloEkle(Connection con, String vt_isim, String tbl_isim, String pk_isim, String autoinc) {
        String sql = "CREATE TABLE IF NOT EXISTS " + vt_isim + "." + tbl_isim
                + " (" + pk_isim + " INTEGER UNSIGNED NOT NULL" + autoinc
                + ", PRIMARY KEY (" + pk_isim + "))";
        System.out.println(sql);
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("eklemede hata" + "-----" + e);
        }

    }

    public void TabloSil(Connection con, String vt_isim, String tbl_isim) {
        System.out.println(con + vt_isim + tbl_isim);
        String sql = "DROP TABLE " + vt_isim + "." + tbl_isim;
        System.out.println(sql);
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("silmede hata" + "-----" + e);
        }
    }

}
