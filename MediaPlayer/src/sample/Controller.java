package sample;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller implements Initializable {

    private String Path;
    private MediaPlayer mediaPlayer;
    private Media media;
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
            media = new Media(Path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            Music my=new Music(Repository.msongs.size(),media.getSource(),media.getDuration().toString());
            Repository.msongs.add(my);
            Repository.songs.add(media);


        }

    }
    public void playnext() {
        if (mediaPlayer != null) {
            //Media media;
            mediaPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    //Media media=null;
                    for (int i = 0; i < Repository.msongs.size(); i++) {
                        if (!Repository.msongs.get(i).isPlayed()) {
                            media = Repository.songs.get(i);
                            mediaPlayer = new MediaPlayer(media);
                            mediaPlayer.play();
                            break;
                        }
                    }
                    DoubleProperty Width = mediaView.fitWidthProperty();
                    DoubleProperty Height = mediaView.fitHeightProperty();

                    Width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                    Height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
                    //mediaPlayer.play();


                }
            });
        }
    }

    public void play(ActionEvent event) {
        //Media media = null;
        for (int i=0;i<Repository.msongs.size();i++)
        {
            if (!Repository.msongs.get(i).isPlayed())
            {
                media=Repository.songs.get(i);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                break;
            }
        }
        if (mediaPlayer!=null) {
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    progressBar.setValue(newValue.toSeconds());

                }


            });

            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));

                }
            });


            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());
                }
            });


            progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));

                }
            });


            volumeSlidder.setValue(mediaPlayer.getVolume() * 100);
            volumeSlidder.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlidder.getValue() / 100);
                }
            });
        }
        //mediaPlayer.play();
        mediaPlayer.setRate(1);

    }

    public void pause(ActionEvent event) {
        mediaPlayer.pause();

    }

    public void stop(ActionEvent event) {
        mediaPlayer.stop();
        mediaView.setMediaPlayer(null);


    }

    public void slowrate(ActionEvent event) {
        mediaPlayer.setRate(.75);

    }

    public void fastforward(ActionEvent event) {
        mediaPlayer.setRate(1.5);

    }

    public void skip10s(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));

    }

    public void back10s(ActionEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));


    }

    public void handleButtonClick (){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("PlayList.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
            stage.setTitle("PlayList");
            Image icon=null;
            icon = new Image(this.getClass().getResource("icon.png").toExternalForm(), false);
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playnext();
        DoubleProperty Width = mediaView.fitWidthProperty();
        DoubleProperty Height = mediaView.fitHeightProperty();

        //Width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        //Height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        //mediaPlayer.play();


    }
}