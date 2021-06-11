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

    private static String Path;
    private static MediaPlayer mediaPlayer;
    private static  Media media;
    @FXML
    private static MediaView mediaView;
    @FXML
    private static Slider progressBar;
    @FXML
    private static Slider volumeSlidder;


    public void ChoseFileFunction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Path = file.toURI().toString();

        if (Path != null) {
            setMedia(new Media(Path));
            setMediaPlayer(new MediaPlayer(getMedia()));
            getMediaView().setMediaPlayer(getMediaPlayer());
            Music my=new Music(Repository.msongs.size(), getMedia().getSource(), getMedia().getDuration().toString());
            Repository.msongs.add(my);
            Repository.songs.add(getMedia());
        }

    }
    public void playnext() {
        if (getMediaPlayer() != null) {
            if (getProgressBar().getMax()== getProgressBar().getValue())
            {
                Repository.msongs.stream().filter(e->e.getName()
                        .equals(getMediaPlayer().getMedia().getSource())).findAny().orElse(null).setPlayed(true);
                for (int i = 0; i < Repository.msongs.size(); i++) {
                    if (!Repository.msongs.get(i).isPlayed()) {
                        setMedia(Repository.songs.get(i));
                        setMediaPlayer(new MediaPlayer(getMedia()));
                        getMediaPlayer().play();
                        break;
                    }
                }

            }
        }
    }

    public void play(ActionEvent event) {
        //Media media = null;
        for (int i=0;i<Repository.msongs.size();i++)
        {
            if (!Repository.msongs.get(i).isPlayed())
            {
                setMedia(Repository.songs.get(i));
                setMediaPlayer(new MediaPlayer(getMedia()));
                getMediaPlayer().play();
                break;
            }
        }
        if (getMediaPlayer() !=null) {
            getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    getProgressBar().setValue(newValue.toSeconds());

                }


            });

            getProgressBar().setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    getMediaPlayer().seek(Duration.seconds(getProgressBar().getValue()));

                }
            });


            getMediaPlayer().setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration total = getMedia().getDuration();
                    getProgressBar().setMax(total.toSeconds());
                }
            });


            getProgressBar().setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    getMediaPlayer().seek(Duration.seconds(getProgressBar().getValue()));

                }
            });


            getVolumeSlidder().setValue(getMediaPlayer().getVolume() * 100);
            getVolumeSlidder().valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    getMediaPlayer().setVolume(getVolumeSlidder().getValue() / 100);
                }
            });
        }
        //mediaPlayer.play();
        getMediaPlayer().setRate(1);

    }

    public void pause(ActionEvent event) {
        getMediaPlayer().pause();

    }

    public void stop(ActionEvent event) {
        getMediaPlayer().stop();
        getMediaView().setMediaPlayer(null);


    }

    public void slowrate(ActionEvent event) {
        getMediaPlayer().setRate(.75);

    }

    public void fastforward(ActionEvent event) {
        getMediaPlayer().setRate(1.5);

    }

    public void skip10s(ActionEvent event) {
        getMediaPlayer().seek(getMediaPlayer().getCurrentTime().add(Duration.seconds(10)));

    }

    public void back10s(ActionEvent event) {
        getMediaPlayer().seek(getMediaPlayer().getCurrentTime().add(Duration.seconds(-10)));


    }


    public  void handleButtonClick (){

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
    public void initialize(URL location, ResourceBundle resources)
    {

        playnext();
        //DoubleProperty Width = getMediaView().fitWidthProperty();
        //DoubleProperty Height = getMediaView().fitHeightProperty();

        //Width.bind(Bindings.selectDouble(getMediaView().sceneProperty(), "width"));
        //Height.bind(Bindings.selectDouble(getMediaView().sceneProperty(), "height"));

        //Width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        //Height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        //mediaPlayer.play();


    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        Controller.mediaPlayer = mediaPlayer;
    }

    public static MediaView getMediaView() {
        return mediaView;
    }

    public static void setMediaView(MediaView mediaView) {
        Controller.mediaView = mediaView;
    }

    public static Slider getProgressBar() {
        return progressBar;
    }

    public static void setProgressBar(Slider progressBar) {
        Controller.progressBar = progressBar;
    }

    public static Slider getVolumeSlidder() {
        return volumeSlidder;
    }

    public static void setVolumeSlidder(Slider volumeSlidder) {
        Controller.volumeSlidder = volumeSlidder;
    }

    public static Media getMedia() {
        return media;
    }

    public static void setMedia(Media media) {
        Controller.media = media;
    }
}