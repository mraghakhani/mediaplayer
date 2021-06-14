package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class PlayList implements Initializable {

    private String Path;

    @FXML
    private TableView<Music> viewsongs;
    @FXML
    private TableColumn<Music,String> songcol;
    @FXML
    private TableColumn<Music,String> idcol;
    @FXML
    private TableColumn<Music,String> ducol;

    Controller controller;



    public void ChoseFileFunction()
    {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        try {
            Path = file.toURI().toString();
        }catch(Exception e){}

        if (Path != null)
        {
            Media media = new Media(Path);
            Repository.songs.add(media);

            Music my=new Music(Repository.msongs.size(),media.getSource(),media.getDuration().toString());
            Repository.msongs.add(my);
            viewsongs.getItems().addAll(my);
        }
    }
    public void tableclick()
    {

        viewsongs.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2)
                {
                    System.out.println(viewsongs.getSelectionModel().getSelectedItem());
                    FXMLLoader loader=new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("sample.fxml"));
                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    controller=loader.getController();
                    controller.playme(viewsongs.getSelectionModel().getSelectedItem().getId());
                    viewsongs.getScene().getWindow().hide();
                }
            }
        });
    }
    public void fileremover()
    {
        Music emailall =(Music)viewsongs.getSelectionModel().getSelectedItem();
        if (emailall!=null)
        {
            viewsongs.getItems().remove(emailall);
            Repository.msongs.remove(Repository.msongs.stream()
                    .filter(e->e.getName().equals(emailall.getName())).findAny().orElse(null));
            Repository.songs.remove(Repository.songs.stream()
                    .filter(e->e.getSource().equals(emailall.getName())).findAny().orElse(null));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        songcol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ducol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        for (int i=0;i<Repository.msongs.size();i++)
        {
            viewsongs.getItems().add(Repository.msongs.get(i));
        }
        tableclick();
    }
}