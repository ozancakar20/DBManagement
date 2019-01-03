/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

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
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Ozan
 */
public class AnaEkranController implements Initializable {

    @FXML
    private AnchorPane panel;

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
        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DBOlustur.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setTitle("Veritabanı Oluştur");
        stage.setResizable(false);
        stage.getIcons().add(image);
        Scene scene = new Scene(root);
        stage.hide();
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {

                Stage st = (Stage) panel.getScene().getWindow();
                st.hide();
                try {
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
                } catch (IOException ex) {
                    System.out.println("hata");
                }

            }
        });

        DBOlusturController dboc = (DBOlusturController) loader.getController();
        dboc.con = con;
    }

    @FXML
    private void ButtonSilDuzenle(ActionEvent event) throws IOException, SQLException {
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

    @FXML
    private void ButtonCikis(ActionEvent event) throws IOException, SQLException {
        con.close();
        Platform.exit();
    }

}
