/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.SutunHandler;
import Models.Sutun;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Ozan
 */
public class SutunGoruntuleController implements Initializable {

    @FXML
    private TextField txt_vtad;
    @FXML
    private TextField txt_tblad;
    @FXML
    private TableView<Sutun> tbl_sutunlar;
    @FXML
    private TableColumn<Sutun, String> clm_ad;
    @FXML
    private TableColumn<Sutun, String> clm_tip;
    @FXML
    private TableColumn<Sutun, Boolean> clm_null;
    @FXML
    private TableColumn<Sutun, String> clm_inc;
    @FXML
    private TableColumn<Sutun, String> clm_default;
    @FXML
    private TableColumn<Sutun, String> clm_key;
    @FXML
    private TextField txt_filter;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;
    String vt_ismi;
    String tbl_ismi;
    ObservableList<Sutun> sutun_listesi = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void Goster(Connection con, String vt_ismi, String tbl_ismi) {
        this.con = con;
        this.vt_ismi = vt_ismi;
        this.tbl_ismi = tbl_ismi;
        txt_vtad.setText(vt_ismi);
        txt_tblad.setText(tbl_ismi);

        TabloyuDoldur();

        clm_ad.setCellValueFactory(new PropertyValueFactory<Sutun, String>("ad"));
        clm_tip.setCellValueFactory(new PropertyValueFactory<Sutun, String>("tip"));
        clm_null.setCellValueFactory(new PropertyValueFactory<Sutun, Boolean>("notNull"));
        clm_key.setCellValueFactory(new PropertyValueFactory<Sutun, String>("key"));
        clm_default.setCellValueFactory(new PropertyValueFactory<Sutun, String>("defaultDeger"));
        clm_inc.setCellValueFactory(new PropertyValueFactory<Sutun, String>("autoInc"));

        tbl_sutunlar.setRowFactory(new Callback<TableView<Sutun>, TableRow<Sutun>>() {

            @Override
            public TableRow<Sutun> call(TableView<Sutun> param) {
                final TableRow<Sutun> tblrow = new TableRow<>();
                final ContextMenu cm = new ContextMenu();
                final MenuItem sil = new MenuItem("Sütunu Sil");

                sil.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //silme işlemleri
                        int cevap = JOptionPane.showConfirmDialog(null, "Emin Misiniz?", "Sil", JOptionPane.YES_NO_OPTION);
                        if (cevap == JOptionPane.YES_OPTION) {
                            SutunHandler sh = new SutunHandler();
                            sh.SutunSil(con, vt_ismi, tbl_ismi, tblrow.getItem().getAd());
                        }

                        TabloyuDoldur();
                        TabloyuFiltrele();

                    }
                });

                cm.getItems().add(sil);
                tblrow.contextMenuProperty().bind(
                        Bindings.when(tblrow.emptyProperty())
                        .then((ContextMenu) null)
                        .otherwise(cm));

                return tblrow;
            }
        });

        TabloyuFiltrele();
    }

    public void TabloyuDoldur() {
        sutun_listesi.clear();
        List<Sutun> templist = new SutunHandler().SutunlariGetir(con, vt_ismi, tbl_ismi);
        for (Sutun s : templist) {
            sutun_listesi.add(s);
        }
        tbl_sutunlar.setItems(sutun_listesi);
    }

    public void TabloyuFiltrele() {

        FilteredList<Sutun> filtreData = new FilteredList<>(sutun_listesi, s -> true);
        txt_filter.textProperty().addListener((observable, eskiDeger, yeniDeger) -> {
            filtreData.setPredicate(sutun -> {
                if (yeniDeger == null || yeniDeger.isEmpty()) {
                    return true;
                }

                String kucukHarfVeri = yeniDeger.toLowerCase();

                if (sutun.getAd().toLowerCase().contains(kucukHarfVeri)) {
                    return true;
                } else if (sutun.getTip().toLowerCase().contains(kucukHarfVeri)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Sutun> siraliVeri = new SortedList<>(filtreData);
        siraliVeri.comparatorProperty().bind(tbl_sutunlar.comparatorProperty());
        tbl_sutunlar.setItems(siraliVeri);

    }

    @FXML
    private void ButtonYeniSutun(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SutunEkle.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setTitle("Sutun Olustur");
        stage.setResizable(false);
        stage.getIcons().add(image);
        Scene scene = new Scene(root);
        stage.hide();
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Stage st = (Stage) txt_vtad.getScene().getWindow();
                st.hide();
                try {
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
                } catch (IOException ex) {
                    System.out.println("hata");
                }
            }
        });

        SutunEkleController sedc = (SutunEkleController) loader.getController();
        sedc.Goster(con, vt_ismi, tbl_ismi);
    }

    @FXML
    private void ButtonGeri(ActionEvent event) throws IOException {
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

        TabloGoruntuleController tbc = (TabloGoruntuleController) loader.getController();
        tbc.Goster(con, vt_ismi);
    }

}
