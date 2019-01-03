/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.TabloHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Ozan
 */
public class TabloOlusturController implements Initializable {

    @FXML
    private TextField txt_vtad;
    @FXML
    private TextField txt_tblad;
    @FXML
    private TextField txt_stnad;
    @FXML
    private CheckBox check_inc;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;
    String vt_ismi;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void VerileriAl(Connection con, String vt_ismi) {
        this.con = con;
        this.vt_ismi = vt_ismi;
        txt_vtad.setText(vt_ismi);
    }

    @FXML
    private void ButtonKaydet(ActionEvent event) throws IOException {

        String tablo_ismi = txt_tblad.getText();
        String sutun_ismi = txt_stnad.getText();
        if (tablo_ismi.equals("") || sutun_ismi.equals("")) {
            JOptionPane.showMessageDialog(null, "Boş Bırakılamaz!");
        } else {
            String autoinc = "";
            if (check_inc.isSelected()) {
                autoinc = " AUTO_INCREMENT";
            }
            TabloHandler th = new TabloHandler();
            th.TabloEkle(con, vt_ismi, tablo_ismi, sutun_ismi, autoinc);

            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SutunGoruntule.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Sutunlar");
            stage.setResizable(false);
            stage.getIcons().add(image);
            Scene scene = new Scene(root);
            stage.hide();
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

            SutunGoruntuleController sgc = (SutunGoruntuleController) loader.getController();
            sgc.Goster(con, vt_ismi, tablo_ismi);

        }

    }

    @FXML
    private void ButtonIptal(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TabloGoruntule.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setTitle("Tablolar");
        stage.setResizable(false);
        stage.getIcons().add(image);
        Scene scene = new Scene(root);
        stage.hide();
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

        TabloGoruntuleController tgc = (TabloGoruntuleController) loader.getController();
        tgc.Goster(con, vt_ismi);
    }
}
