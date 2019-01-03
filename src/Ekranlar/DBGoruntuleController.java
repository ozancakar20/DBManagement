/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.VeritabaniHandler;
import Models.Sutun;
import Models.Veritabani;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Ozan
 */
public class DBGoruntuleController implements Initializable {

    @FXML
    private TableView<Veritabani> tbl_veritabani;
    @FXML
    private TableColumn<Veritabani, String> clm_name;
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_filter;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;

    ObservableList<Veritabani> veritabani_listesi = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO  
    }

    public void Goster(Connection con) throws SQLException {
        this.con = con;
        txt_username.setText(con.getMetaData().getUserName());

        TabloyuDoldur();

        clm_name.setCellValueFactory(new PropertyValueFactory<Veritabani, String>("adi"));

        tbl_veritabani.setRowFactory(new Callback<TableView<Veritabani>, TableRow<Veritabani>>() {

            @Override
            public TableRow<Veritabani> call(TableView<Veritabani> param) {
                final TableRow<Veritabani> tblrow = new TableRow<>();
                final ContextMenu cm = new ContextMenu();
                final MenuItem sil = new MenuItem("Veritabanını Sil");
                final MenuItem goruntule = new MenuItem("Tabloları görüntüle");

                sil.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //silme işlemleri
                        int cevap = JOptionPane.showConfirmDialog(null, "Emin Misiniz?", "Sil", JOptionPane.YES_NO_OPTION);
                        if (cevap == JOptionPane.YES_OPTION) {
                            VeritabaniHandler vt = new VeritabaniHandler();
                            vt.VeritabaniSil(con, tblrow.getItem().getAdi());
                        }

                        TabloyuDoldur();
                        TabloyuFiltrele();
                    }
                });

                goruntule.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //düzenleme işlemleri

                        Stage st = (Stage) tbl_veritabani.getScene().getWindow();
                        st.hide();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("TabloGoruntule.fxml"));
                            Parent root = (Parent) loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Tablolar");
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

                            TabloGoruntuleController tbc = (TabloGoruntuleController) loader.getController();
                            tbc.Goster(con, tblrow.getItem().getAdi());

                        } catch (IOException ex) {
                            System.out.println("hata");
                        }

                    }
                });

                cm.getItems().add(sil);
                cm.getItems().add(goruntule);
                tblrow.contextMenuProperty().bind(
                        Bindings.when(tblrow.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(cm));

                return tblrow;
            }
        });

        tbl_veritabani.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    if (tbl_veritabani.getSelectionModel().getSelectedItem().getAdi().isEmpty()) {
                        //
                    } else {
                        //çift tıklayarak verileri görme ekranına geçecek
                        Stage st = (Stage) tbl_veritabani.getScene().getWindow();
                        st.hide();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("TabloGoruntule.fxml"));
                            Parent root = (Parent) loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Tablolar");
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

                            TabloGoruntuleController tbc = (TabloGoruntuleController) loader.getController();
                            tbc.Goster(con, tbl_veritabani.getSelectionModel().getSelectedItem().getAdi());

                        } catch (IOException ex) {
                            System.out.println("hata");
                        }
                    }

                }
            }
        });

        TabloyuFiltrele();

    }

    public void TabloyuDoldur() {
        veritabani_listesi.clear();
        List<Veritabani> templist = new VeritabaniHandler().VeritabanlariGetir(con);
        for (Veritabani r : templist) {
            veritabani_listesi.add(r);
        }
        tbl_veritabani.setItems(veritabani_listesi);
    }

    public void TabloyuFiltrele() {
        FilteredList<Veritabani> filtreData = new FilteredList<>(veritabani_listesi, v -> true);
        txt_filter.textProperty().addListener((observable, eskiDeger, yeniDeger) -> {
            filtreData.setPredicate(db -> {
                if (yeniDeger == null || yeniDeger.isEmpty()) {
                    return true;
                }

                String kucukHarfVeri = yeniDeger.toLowerCase();

                if (db.getAdi().toLowerCase().contains(kucukHarfVeri)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Veritabani> siraliVeri = new SortedList<>(filtreData);
        siraliVeri.comparatorProperty().bind(tbl_veritabani.comparatorProperty());
        tbl_veritabani.setItems(siraliVeri);
    }

    @FXML
    private void ButtonGeri(ActionEvent event) throws IOException {
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
