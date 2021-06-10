package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayList {
    private String Path;
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;
    @FXML
    private Slider progressBar;
    @FXML
    private Slider volumeSlidder;

    @FXML
    private TableView<Music> viewsongs;
    @FXML
    private TableColumn<Music,String> songcol;
    @FXML
    private TableColumn<Music,String> idcol;
    @FXML
    private TableColumn<Music,String> ducol;




    public void ChoseFileFunction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Path = file.toURI().toString();

        if (Path != null) {
            Media media = new Media(Path);
            mediaPlayer = new MediaPlayer(media);
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            songcol.setCellValueFactory(new PropertyValueFactory<>("name"));
            ducol.setCellValueFactory(new PropertyValueFactory<>("duration"));
            viewsongs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            Music my=new Music("1",media.getSource(),media.getDuration().toString());
            viewsongs.getItems().addAll(my);


            //String name=detect
        }
    }

    public void fileremover()
    {
        Music emailall =(Music)viewsongs.getSelectionModel().getSelectedItem();
        if (emailall!=null)
        {
            viewsongs.getItems().remove(emailall);
        }


    }
}