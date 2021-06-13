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

    public  String Path;
    public   static   MediaPlayer mediaPlayer;
    public    Media media;
    @FXML
    public  MediaView mediaView;
    @FXML
    public  Slider progressBar;
    @FXML
    public  Slider volumeSlidder;
    PlayList playList ;
    MediaPlayer.Status status;


    public  void playme(int index)
    {
        media=null;
        mediaPlayer=null;
        media=Repository.songs.get(index);
        mediaPlayer= new MediaPlayer(media);
        mediaPlayer.play();
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.setRate(1);
        initializetools();


    }

    public void ChoseFileFunction() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        Path = file.toURI().toString();

        if (Path != null) {
            media=(new Media(Path));
            mediaPlayer=(new MediaPlayer(media));
            mediaView.setMediaPlayer(mediaPlayer);
            Music my=new Music(Repository.msongs.size(), media.getSource(), media.getDuration().toString());
            Repository.msongs.add(my);
            Repository.songs.add(media);
        }
        status=mediaPlayer.getStatus();


    }
//index

    public void play(ActionEvent event) {
        //Media media = null;
        for (int i=0;i<Repository.msongs.size();i++)
        {
            media=null;
            mediaPlayer=null;
            if (!Repository.msongs.get(i).isPlayed())
            {
                media=(Repository.songs.get(i));
                mediaPlayer=(new MediaPlayer(media));

                mediaPlayer.play();
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.setRate(1);
                initializetools();
                break;
            }
        }

        status=mediaPlayer.getStatus();

    }
    public  void initializetools()
    {
        if (mediaPlayer !=null)
        {
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
                {
                    progressBar.setValue(newValue.toSeconds());

                }


            });

            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));

                }
            });


            mediaPlayer.setOnReady(new Runnable()
            {
                @Override
                public void run() {
                    Duration total =mediaPlayer.getMedia().getDuration();
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
    public void intit()
    {

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if (progressBar.getMax() == progressBar.getValue()) {
                    Repository.msongs.stream().filter(e -> e.getName().equals(mediaPlayer.getMedia()
                            .getSource())).findAny().ifPresent(music -> music.setPlayed(true));
                    for (int i = 0; i < Repository.msongs.size(); i++) {
                        if (!Repository.msongs.get(i).isPlayed()) {
                            media = (Repository.songs.get(i));
                            mediaPlayer = (new MediaPlayer(media));
                            mediaPlayer.play();
                            break;
                        }
                    }
                }
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        playList.thismediaview=mediaView;
        playList.thisprogressBar=progressBar;
        playList.thisvolumeSlidder=volumeSlidder;
        //status.equals(MediaPlayer.Status.DISPOSED);
        //mediaPlayer.getStatus().equals(MediaPlayer.Status.DISPOSED);
        //mediaPlayer.seton
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null&&mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
                    initializetools();
            }
        });
        thread.start();

        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();


        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

        //Width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        //Height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        //mediaPlayer.play();


    }


}
