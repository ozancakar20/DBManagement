/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Handlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Ozan
 */
public class VeriHandler {

    public void VeriEkle(Connection con, String vt_ismi, String tbl_ismi, List<String> veriler) {

        String sql = "INSERT INTO " + vt_ismi + "." + tbl_ismi + " VALUES(";
        for (int i = 0; i < veriler.size(); i++) {
            sql += "'" + veriler.get(i) + "'";
            if ((i + 1) == veriler.size()) {
                break;
            } else {
                sql += ",";
            }
        }
        sql += ")";
        System.out.println(sql);

        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("eklemede hata");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void VeriGuncelle(Connection con, String vt_ismi, String tbl_ismi, List<String> sutunAdlari, List<String> veriler, String pk_ad, String pk_deger) {

        String sql = "UPDATE " + vt_ismi + "." + tbl_ismi + " SET ";
        for (int i = 0; i < sutunAdlari.size(); i++) {
            sql += sutunAdlari.get(i) + " = '" + veriler.get(i) + "'";
            if ((i + 1) == veriler.size()) {
                sql += " WHERE " + pk_ad + " = " + pk_deger;
            } else {
                sql += ", ";
            }
        }
        System.out.println(sql);
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("güncellemede hata");
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public List<String> BelirliVeriyiGetir(Connection con, String vt_ismi, String tbl_ismi, String pk_ad, String pk_deger) {
        List<String> satir = new ArrayList<>();
        String sql = "SELECT * FROM " + vt_ismi + "." + tbl_ismi + " WHERE " + pk_ad + " = " + pk_deger;
        System.out.println(sql);
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    satir.add(rs.getString(i + 1));
                }
            }
        } catch (SQLException ex) {
            System.out.println("veri getirmede hata" + "-----" + ex);
        }
        return satir;
    }

    public void VeriSil(Connection con, String vt_ismi, String tbl_ismi, String pk_ad, String pk_deger) {

        String sql = "DELETE FROM " + vt_ismi + "." + tbl_ismi + " WHERE " + pk_ad + " = " + pk_deger;
        System.out.println(sql);

        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("silmede hata" + "-----" + ex);
        }
    }
}
