/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.VeritabaniHandler;
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
public class DBOlusturController implements Initializable {

    @FXML
    private TextField txt_isim;

    public Connection con;

    Image image = new Image("/Kaynaklar/database_37021.png");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ButtonOlustur(ActionEvent event) throws IOException, SQLException {
        String isim = txt_isim.getText();

        if (isim.equals("")) {
            JOptionPane.showMessageDialog(null, "Boş Bırakılamaz");
        } else {
            VeritabaniHandler vt = new VeritabaniHandler();
            vt.VeritabaniEkle(con, isim);

            ((Node) (event.getSource())).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DBGoruntule.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Veritabanları");
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

            DBGoruntuleController dbgc = (DBGoruntuleController) loader.getController();
            dbgc.Goster(con);
        }

    }

    @FXML
    private void ButtonIptal(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AnaEkran.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setTitle("Ana Ekran");
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

        AnaEkranController aec = (AnaEkranController) loader.getController();
        aec.con = con;
    }

}
