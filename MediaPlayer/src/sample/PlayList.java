package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.w3c.dom.stylesheets.MediaList;

import java.io.File;

public class PlayList {
    private ListView listView;
    private String Path;
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;
    @FXML
    private Slider progressBar;
    @FXML
    private Slider volumeSlidder;


    public void ChoseFileFunction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Path = file.toURI().toString();

        if (Path != null) {
            Media media = new Media(Path);
            mediaPlayer = new MediaPlayer(media);






        }
    }

    public void fileremover(){



    }
}