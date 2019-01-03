/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.TabloHandler;
import Models.Tablo;
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
public class TabloGoruntuleController implements Initializable {

    @FXML
    private TextField txt_vtadi;
    @FXML
    private TableView<Tablo> tbl_tablolar;
    @FXML
    private TableColumn<Tablo, String> clm_adi;
    @FXML
    private TableColumn<Tablo, String> clm_satir;
    @FXML
    private TableColumn<Tablo, String> clm_data;
    @FXML
    private TextField txt_filter;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;
    String vt_ismi;

    ObservableList<Tablo> tablo_listesi = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void Goster(Connection con, String vt_ismi) {
        this.con = con;
        this.vt_ismi = vt_ismi;
        txt_vtadi.setText(vt_ismi);

        TabloyuDoldur();

        clm_adi.setCellValueFactory(new PropertyValueFactory<Tablo, String>("adi"));
        clm_satir.setCellValueFactory(new PropertyValueFactory<Tablo, String>("satirSayisi"));
        clm_data.setCellValueFactory(new PropertyValueFactory<Tablo, String>("dataLength"));

        tbl_tablolar.setRowFactory(new Callback<TableView<Tablo>, TableRow<Tablo>>() {

            @Override
            public TableRow<Tablo> call(TableView<Tablo> param) {
                final TableRow<Tablo> tblrow = new TableRow<>();
                final ContextMenu cm = new ContextMenu();
                final MenuItem sil = new MenuItem("Tabloyu Sil");
                final MenuItem goruntule = new MenuItem("Sütunları Görüntüle");

                sil.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //silme işlemleri
                        int cevap = JOptionPane.showConfirmDialog(null, "Emin Misiniz?", "Sil", JOptionPane.YES_NO_OPTION);
                        if (cevap == JOptionPane.YES_OPTION) {
                            TabloHandler th = new TabloHandler();
                            th.TabloSil(con, vt_ismi, tblrow.getItem().getAdi());
                        }

                        TabloyuDoldur();
                        TabloyuFiltrele();
                    }
                });

                goruntule.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //düzenleme işlemleri
                        Stage st = (Stage) tbl_tablolar.getScene().getWindow();
                        st.hide();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("SutunGoruntule.fxml"));
                            Parent root = (Parent) loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Sutunlar");
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

                            SutunGoruntuleController sgc = (SutunGoruntuleController) loader.getController();
                            sgc.Goster(con, vt_ismi, tblrow.getItem().getAdi());

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

        tbl_tablolar.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    if (tbl_tablolar.getSelectionModel().getSelectedItem().getAdi().isEmpty()) {
                        //
                    } else {
                        //çift tıklayarak verileri görme ekranına geçecek
                        Stage st = (Stage) tbl_tablolar.getScene().getWindow();
                        st.hide();
                        try {
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
                            vgc.Goster(con, vt_ismi, tbl_tablolar.getSelectionModel().getSelectedItem().getAdi());

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
        tablo_listesi.clear();
        List<Tablo> templist = new TabloHandler().TablolariGetir(con, vt_ismi);
        for (Tablo t : templist) {
            tablo_listesi.add(t);
        }
        tbl_tablolar.setItems(tablo_listesi);

    }

    public void TabloyuFiltrele() {

        FilteredList<Tablo> filtreData = new FilteredList<>(tablo_listesi, t -> true);
        txt_filter.textProperty().addListener((observable, eskiDeger, yeniDeger) -> {
            filtreData.setPredicate(tablo -> {
                if (yeniDeger == null || yeniDeger.isEmpty()) {
                    return true;
                }

                String kucukHarfVeri = yeniDeger.toLowerCase();

                if (tablo.getAdi().toLowerCase().contains(kucukHarfVeri)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Tablo> siraliVeri = new SortedList<>(filtreData);
        siraliVeri.comparatorProperty().bind(tbl_tablolar.comparatorProperty());
        tbl_tablolar.setItems(siraliVeri);
    }

    @FXML
    private void ButtonYeniTablo(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TabloOlustur.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setTitle("Tablo Olustur");
        stage.setResizable(false);
        stage.getIcons().add(image);
        Scene scene = new Scene(root);
        stage.hide();
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Stage st = (Stage) txt_vtadi.getScene().getWindow();
                st.hide();
                try {
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
                } catch (IOException ex) {
                    System.out.println("hata");
                }

            }
        });

        TabloOlusturController toc = (TabloOlusturController) loader.getController();
        toc.VerileriAl(con, vt_ismi);
    }

    @FXML
    private void ButtonGeri(ActionEvent event) throws SQLException, IOException {
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
