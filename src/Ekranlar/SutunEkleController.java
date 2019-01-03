/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.SutunHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
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
public class SutunEkleController implements Initializable {

    @FXML
    private TextField txt_vtad;
    @FXML
    private TextField txt_tblad;
    @FXML
    private CheckBox check_null;
    @FXML
    private TextField txt_stnad;
    @FXML
    private TextField txt_default;
    @FXML
    private ComboBox<String> combo_tip;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;
    String vt_ismi;
    String tbl_ismi;

    String[] tipler = {"VARCHAR(45)", "INTEGER(10)", "BOOLEAN"};

    ObservableList<String> tip_listesi = FXCollections.observableArrayList(tipler);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        combo_tip.setItems(tip_listesi);
    }

    public void Goster(Connection con, String vt_ismi, String tbl_ismi) {
        this.con = con;
        this.vt_ismi = vt_ismi;
        this.tbl_ismi = tbl_ismi;
        txt_vtad.setText(vt_ismi);
        txt_tblad.setText(tbl_ismi);
    }

    @FXML
    private void ButtonKaydet(ActionEvent event) throws IOException {
        String sutun_ismi = txt_stnad.getText();
        String tip;
        if (combo_tip.getValue() == null) {
            tip = "";
        } else {
            tip = combo_tip.getValue();
        }

        if (sutun_ismi.equals("") || tip.equals("")) {
            JOptionPane.showMessageDialog(null, "İsim ve Tip Boş Bırakılamaz!");
        } else {
            String notNull = "";
            if (check_null.isSelected()) {
                notNull = "NOT NULL";
            }
            String defDeger = "";
            if (tip.contains("VARCHAR")) {
                defDeger = "DEFAULT '" + txt_default.getText() + "'";
                if (txt_default.getText().isEmpty()) {
                    defDeger = "";
                }
            }

            SutunHandler sh = new SutunHandler();
            sh.SutunEkle(con, vt_ismi, tbl_ismi, sutun_ismi, tip, notNull, defDeger);

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
            sgc.Goster(con, vt_ismi, tbl_ismi);
        }

    }

    @FXML
    private void ButtonIptal(ActionEvent event) throws IOException {
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
        sgc.Goster(con, vt_ismi, tbl_ismi);
    }

}
