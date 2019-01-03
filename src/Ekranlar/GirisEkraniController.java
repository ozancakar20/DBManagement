/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
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
public class GirisEkraniController implements Initializable {

    @FXML
    private TextField txt_server;
    @FXML
    private TextField txt_port;
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;

    Image image = new Image("/Kaynaklar/database_37021.png");

    public Connection con;

    private String server;
    private String port;
    private String username;
    private String password;

    //private String url = "jdbc:mysql://localhost:3306/";
    private final String taslakurl = "jdbc:mysql://";
    private final String driver = "com.mysql.jdbc.Driver";
    private String url = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void ButtonBaglan(ActionEvent event) throws IOException {
        server = txt_server.getText();
        port = txt_port.getText();
        username = txt_username.getText();
        password = txt_password.getText();

        url = taslakurl + server + ":" + port + "/";

        Baglan();

        if (con != null) {
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

        } else {
            JOptionPane.showMessageDialog(null, "Veritabanı bağlantı hatasi oluştu.");
        }

    }

    public void Baglan() {
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, username, password);
            //JOptionPane.showMessageDialog(null, "Veritabanına Bağlanıldı");
        } catch (Exception ex) {
            Logger.getLogger(GirisEkraniController.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(null, "Veritabanı bağlantı hatasi oluştu.");
        }
    }

}
