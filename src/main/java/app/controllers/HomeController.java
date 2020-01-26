package app.controllers;

import app.models.VideoDownloadLink;
import app.utils.Downloader;
import app.utils.Scrapper;
import app.utils.Verifier;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static app.utils.Scrapper.getExt;

public class HomeController {

    @FXML
    private StackPane root;

    @FXML
    private VBox container;

    @FXML
    private AnchorPane titlePane;

    @FXML
    private AnchorPane linkPane;

    @FXML
    private AnchorPane links;

    @FXML
    private ImageView limg;

    @FXML
    private Button fetch;

    @FXML
    public ImageView icon;

    @FXML
    private Text urlStatus;

    @FXML
    private TextField url;

    @FXML
    public Text name;

    @FXML
    private JFXComboBox<VideoDownloadLink> options;

    @FXML
    private JFXButton downloadButton;

    @FXML
    private Text durationLabel;

    public static String videoName = "";
    public static String dur = "";
    public static String iconLocation = "";

    public ObservableList<VideoDownloadLink> linkList = FXCollections.observableArrayList();

    public void typing(KeyEvent evt){
        urlStatus.setText("");
        fetch.setDisable(false);
    }

    public void load(MouseEvent evt){
        try{
            if(new File(iconLocation).exists())
                new File(iconLocation).delete();
        }catch(Exception ex){

        }
        String urlString = url.getText();
        if(Verifier.verifyURL(urlString)==false){
            urlStatus.setText("Invalid URL!");
            url.setDisable(false);
            fetch.setDisable(false);
            url.setDisable(false);
            limg.setVisible(false);
            return;
        }
        links.setDisable(true);
        downloadButton.setDisable(true);
        linkPane.setDisable(true);
        fetch.setDisable(true);

        url.setDisable(true);


        limg.setVisible(true);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                linkList.clear();
                ArrayList<VideoDownloadLink> scrapedLinks = null;
                try {
                    scrapedLinks = Scrapper.scrapeYouTube(urlString.trim());
                }catch(Exception ex){
                    javax.swing.JOptionPane.showMessageDialog(null, "(C:111)The network seems to be to slow...");
                    return;
                }
                if(scrapedLinks==null){
                    url.setDisable(false);
                    url.setText("");
                    fetch.setDisable(false);
                    return;
                }
                linkList.addAll(scrapedLinks);
                links.setDisable(false);
                name.setText(videoName);
                limg.setVisible(false);
                url.setDisable(false);
                linkPane.setDisable(false);
                options.setItems(linkList);
                downloadButton.setDisable(false);
                setIcon();
            }
        });
        t.start();
    }

    private void setIcon() {
        System.out.println(iconLocation);
        try {
            Image im = new Image(new FileInputStream(new File(iconLocation)));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        try {
            icon.setImage(new Image(new FileInputStream(new File(iconLocation))));
        } catch (FileNotFoundException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();

        }
    }

    public void download(MouseEvent evt){

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder to download the Video");
        File folder = directoryChooser.showDialog(null);
        System.out.println(folder.getAbsolutePath());
        VideoDownloadLink dLink = options.getSelectionModel().getSelectedItem();
        File file = new File(folder.getAbsolutePath()+"/" + videoName + "." + dLink.getFormat());
        System.out.println(file.getAbsolutePath());
        root.setDisable(true);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Downloader.download(dLink.getLink(),file.getAbsolutePath());
                root.setDisable(false);
                url.setDisable(false);
            }
        });
        t.start();


        //System.out.println(file.getAbsolutePath());
        //https://www.youtube.com/watch?v=gnXRI3pHxrU
    }

}
