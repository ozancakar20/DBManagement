/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handlers;

import Models.Sutun;
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
public class SutunHandler {

    public List<Sutun> SutunlariGetir(Connection con, String vt_ismi, String tbl_ismi) {

        String sql = "DESC " + vt_ismi + "." + tbl_ismi;
        List<Sutun> liste = new ArrayList<Sutun>();
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sutun s = new Sutun();
                s.setAd(rs.getString(1));
                s.setTip(rs.getString(2));
                s.setNotNull(rs.getBoolean(3));
                s.setKey(rs.getString(4));
                s.setDefaultDeger(rs.getString(5));
                s.setAutoInc(rs.getString(6));
                liste.add(s);
            }
        } catch (SQLException ex) {
            System.out.println("sutun çekmede hata" + "-----" + ex);
        }

        return liste;
    }

    public void SutunEkle(Connection con, String vt_isim, String tbl_isim, String stn_isim, String tip, String notNull, String defDeger) {
        String sql = "ALTER TABLE " + vt_isim + "." + tbl_isim
                + " ADD COLUMN " + stn_isim + " " + tip + " " + notNull + " " + defDeger;
        System.out.println(sql);
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("eklemede hata" + "-----" + e);
        }

    }

    public void SutunSil(Connection con, String vt_isim, String tbl_isim, String stn_isim) {
        String sql = "ALTER TABLE " + vt_isim + "." + tbl_isim
                + " DROP COLUMN " + stn_isim;
        System.out.println(sql);
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("silmede hata" + "-----" + e);
        }
    }

}
