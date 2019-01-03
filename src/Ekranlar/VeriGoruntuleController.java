/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ekranlar;

import Handlers.VeriHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
public class VeriGoruntuleController implements Initializable {

    @FXML
    private TextField txt_vtad;
    @FXML
    private TextField txt_tblad;
    @FXML
    private TableView<ObservableList> tbl_veri;

    Image image = new Image("/Kaynaklar/database_37021.png");
    Connection con;
    String vt_ismi;
    String tbl_ismi;
    int sutunSayisi;

    ObservableList<ObservableList> satirlarinListesi = FXCollections.observableArrayList();
    @FXML
    private TextField txt_filter;

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

        tbl_veri.setRowFactory(new Callback<TableView<ObservableList>, TableRow<ObservableList>>() {

            @Override
            public TableRow<ObservableList> call(TableView<ObservableList> param) {
                final TableRow<ObservableList> tblrow = new TableRow<>();
                final ContextMenu cm = new ContextMenu();
                final MenuItem sil = new MenuItem("Veriyi Sil");
                final MenuItem guncelle = new MenuItem("Veriyi Güncelle");

                sil.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //silme işlemleri
                        int cevap = JOptionPane.showConfirmDialog(null, "Emin Misiniz?", "Sil", JOptionPane.YES_NO_OPTION);
                        if (cevap == JOptionPane.YES_OPTION) {
                            VeriHandler vh = new VeriHandler();
                            vh.VeriSil(con, vt_ismi, tbl_ismi, tblrow.getTableView().getColumns().get(0).textProperty().getValue(), tblrow.getItem().get(0).toString());
                        }

                        TabloyuDoldur();
                        TabloyuFiltrele();
                    }
                });

                guncelle.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        //düzenleme işlemleri
                        Stage st = (Stage) tbl_veri.getScene().getWindow();
                        st.hide();
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("VeriEkleDuzenle.fxml"));
                            Parent root = (Parent) loader.load();
                            Stage stage = new Stage();
                            stage.setTitle("Veri Düzenle");
                            stage.setResizable(false);
                            stage.getIcons().add(image);
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();

                            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                                @Override
                                public void handle(WindowEvent event) {
                                    Stage st = (Stage) tbl_veri.getScene().getWindow();
                                    st.hide();
                                    try {
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("VeriGoruntule.fxml"));
                                        Parent root = (Parent) loader.load();
                                        Stage stage = new Stage();
                                        stage.setTitle("Veriler");
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

                                        VeriGoruntuleController vgc = (VeriGoruntuleController) loader.getController();
                                        vgc.Goster(con, vt_ismi, tbl_ismi);
                                    } catch (IOException ex) {
                                        System.out.println("hata");
                                    }
                                }
                            });

                            VeriEkleDuzenleController vedc = (VeriEkleDuzenleController) loader.getController();
                            vedc.VerileriAlDuzenle(con, vt_ismi, tbl_ismi, tblrow.getTableView().getColumns().get(0).textProperty().getValue(), tblrow.getItem().get(0).toString());

                        } catch (IOException ex) {
                            System.out.println("hata");
                        }
                    }
                });

                cm.getItems().add(sil);
                cm.getItems().add(guncelle);
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

        satirlarinListesi.clear();

        String sql = "SELECT * FROM " + vt_ismi + "." + tbl_ismi;
        try {
            PreparedStatement ps = (PreparedStatement) con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            sutunSayisi = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                ObservableList<String> satir = FXCollections.observableArrayList();
                for (int i = 1; i <= sutunSayisi; i++) {
                    satir.add(rs.getString(i));
                }
                satirlarinListesi.add(satir);
            }
            tbl_veri.setItems(satirlarinListesi);
            tbl_veri.getColumns().clear();
            for (int i = 0; i < sutunSayisi; i++) {
                String sutunAdi = rs.getMetaData().getColumnName(i + 1);
                final int j = i;
                TableColumn col = new TableColumn(sutunAdi);
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                col.setMinWidth(100);
                tbl_veri.getColumns().addAll(col);
            }

        } catch (SQLException ex) {
            System.out.println("veri çekmede hata");
        }
    }

    public void TabloyuFiltrele() {
        FilteredList<ObservableList> filtreData = new FilteredList<>(satirlarinListesi, t -> true);
        txt_filter.textProperty().addListener((observable, eskiDeger, yeniDeger) -> {
            filtreData.setPredicate(veri -> {
                if (yeniDeger == null || yeniDeger.isEmpty()) {
                    return true;
                }

                String kucukHarfVeri = yeniDeger.toLowerCase();

                for (int i = 0; i < sutunSayisi; i++) {
                    if (veri.get(i).toString().toLowerCase().contains(kucukHarfVeri)) {
                        return true;
                    }
                }
                return false;
            });
        });
        SortedList<ObservableList> siraliVeri = new SortedList<>(filtreData);
        siraliVeri.comparatorProperty().bind(tbl_veri.comparatorProperty());
        tbl_veri.setItems(siraliVeri);
    }

    @FXML
    private void ButtonYeniVeri(ActionEvent event) throws IOException {
        ((Node) (event.getSource())).getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VeriEkleDuzenle.fxml"));
        Parent root = (Parent) loader.load();
        Stage stage = new Stage();
        stage.setTitle("Veri Ekle");
        stage.setResizable(false);
        stage.getIcons().add(image);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                Stage st = (Stage) tbl_veri.getScene().getWindow();
                st.hide();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("VeriGoruntule.fxml"));
                    Parent root = (Parent) loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Veriler");
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

                    VeriGoruntuleController vgc = (VeriGoruntuleController) loader.getController();
                    vgc.Goster(con, vt_ismi, tbl_ismi);
                } catch (IOException ex) {
                    System.out.println("hata");
                }
            }
        });

        VeriEkleDuzenleController vedc = (VeriEkleDuzenleController) loader.getController();
        vedc.VerileriAlEkle(con, vt_ismi, tbl_ismi);

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
