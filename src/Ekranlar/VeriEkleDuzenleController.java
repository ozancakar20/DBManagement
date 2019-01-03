/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.SutunHandler;
import Handlers.VeriHandler;
import Models.Sutun;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Ozan
 */
public class VeriEkleDuzenleController implements Initializable {

    @FXML
    private GridPane panel;
    @FXML
    private TextField txt_vtad;
    @FXML
    private TextField txt_tblad;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;
    String vt_ismi;
    String tbl_ismi;
    String pk_ad;
    String pk_deger;

    List<TextField> text_listesi = new ArrayList<>();
    List<Sutun> sutunlar = new ArrayList<>();

    String ekleMiGuncelleMi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void VerileriAlDuzenle(Connection con, String vt_ismi, String tbl_ismi, String pk_ad, String pk_deger) {
        this.con = con;
        this.vt_ismi = vt_ismi;
        this.tbl_ismi = tbl_ismi;
        this.pk_ad = pk_ad;
        this.pk_deger = pk_deger;
        ekleMiGuncelleMi = pk_deger;
        txt_vtad.setText(vt_ismi);
        txt_tblad.setText(tbl_ismi);

        PanelDoldur();

        VeriHandler vh = new VeriHandler();
        List<String> satirElemenlari = vh.BelirliVeriyiGetir(con, vt_ismi, tbl_ismi, pk_ad, pk_deger);

        for (int i = 0; i < text_listesi.size(); i++) {
            text_listesi.get(i).setText(satirElemenlari.get(i));
        }

    }

    public void VerileriAlEkle(Connection con, String vt_ismi, String tbl_ismi) {
        this.con = con;
        this.vt_ismi = vt_ismi;
        this.tbl_ismi = tbl_ismi;
        ekleMiGuncelleMi = pk_deger;
        txt_vtad.setText(vt_ismi);
        txt_tblad.setText(tbl_ismi);

        PanelDoldur();

    }

    public void PanelDoldur() {

        sutunlar = new SutunHandler().SutunlariGetir(con, vt_ismi, tbl_ismi);
        panel.setHgap(1);
        panel.setVgap(sutunlar.size());
        Label l1 = new Label("Sütun Adı");
        Label l2 = new Label("Değeri");
        l1.setStyle("-fx-font: 20px Verdana");
        l2.setStyle("-fx-font: 20px Verdana");
        panel.add(l1, 0, 0);
        panel.add(l2, 1, 0);
        for (int i = 0; i < sutunlar.size(); i++) {
            Label l = new Label(sutunlar.get(i).getAd());
            l.setStyle("-fx-font: 15px Verdana");
            TextField tf = new TextField();
            tf.setPromptText("Değeri Girin");
            tf.setStyle("-fx-font: 15px Verdana");
            text_listesi.add(tf);
            panel.add(l, 0, i + 1);
            panel.add(tf, 1, i + 1);
        }
    }

    public void EkleIslemleri() {
        List<String> veriler = new ArrayList<>();
        for (int i = 0; i < text_listesi.size(); i++) {
            veriler.add(text_listesi.get(i).getText());
        }
        VeriHandler vh = new VeriHandler();
        vh.VeriEkle(con, vt_ismi, tbl_ismi, veriler);
    }

    public void GuncelleIslemleri() {
        List<String> veriler = new ArrayList<>();
        List<String> sutunAdlari = new ArrayList<>();
        for (int i = 0; i < text_listesi.size(); i++) {
            veriler.add(text_listesi.get(i).getText());
        }
        for (int i = 0; i < sutunlar.size(); i++) {
            sutunAdlari.add(sutunlar.get(i).getAd());
        }
        VeriHandler vh = new VeriHandler();
        vh.VeriGuncelle(con, vt_ismi, tbl_ismi, sutunAdlari, veriler, pk_ad, pk_deger);
    }

    @FXML
    private void ButtonKaydet(ActionEvent event) {
        if (ekleMiGuncelleMi == null) {
            EkleIslemleri();
        } else {
            GuncelleIslemleri();
        }

        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VeriGoruntule.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Veriler");
            stage.setResizable(false);
            stage.getIcons().add(image);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        System.out.println("Connection sonlandırmada hata");
                    }
                    Platform.exit();
                }
            });

            VeriGoruntuleController vgc = (VeriGoruntuleController) loader.getController();
            vgc.Goster(con, vt_ismi, tbl_ismi);

        } catch (IOException ex) {
            System.out.println("hata");
        }

    }

    @FXML
    private void ButtonIptal(ActionEvent event) {
        try {
            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VeriGoruntule.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Veriler");
            stage.setResizable(false);
            stage.getIcons().add(image);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        System.out.println("Connection sonlandırmada hata");
                    }
                    Platform.exit();
                }
            });

            VeriGoruntuleController vgc = (VeriGoruntuleController) loader.getController();
            vgc.Goster(con, vt_ismi, tbl_ismi);

        } catch (IOException ex) {
            System.out.println("hata");
        }
    }
}
