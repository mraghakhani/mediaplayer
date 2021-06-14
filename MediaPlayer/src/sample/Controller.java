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


public class Controller implements Initializable
{

    private   String Path;
    private    static   MediaPlayer mediaPlayer;
    private     Media media;
    @FXML
    private   MediaView mediaView;
    @FXML
    private   Slider progressBar;
    @FXML
    private   Slider volumeSlidder;

    public void ChoseFileFunction()
    {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        //if i did not put path in a try catch block
        //in case of not choosing a file and closing selecting file window
        // program would give a null exception pointer
        try {
            Path = file.toURI().toString();

        }catch(Exception e){}

        if (Path != null) {
            media=(new Media(Path));
            mediaPlayer=(new MediaPlayer(media));
            Music my=new Music(Repository.msongs.size(), media.getSource(), media.getDuration().toString());
            Repository.msongs.add(my);
            Repository.songs.add(media);
        }
    }

    public void play(ActionEvent event)
    {
        //this if finds the first media that has not played
        if(mediaPlayer.getStatus().equals(MediaPlayer.Status.READY)) {
            for (int i = 0; i < Repository.msongs.size(); i++) {
                media = null;
                mediaPlayer = null;
                if (!Repository.msongs.get(i).isPlayed()) {
                    media = (Repository.songs.get(i));
                    mediaPlayer = (new MediaPlayer(media));

                    mediaPlayer.play();
                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaPlayer.setRate(1);

                    initializetools();
                    break;
                }
            }
        }
        //this one resume the paused media
        else if (mediaPlayer!=null&&mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED))
        {
            mediaPlayer.currentRateProperty();
            mediaPlayer.seek(mediaPlayer.getCurrentTime());
            mediaPlayer.play();
        }
        //this method is for when media ends
        //it sets the current media played
        //then finds another media that has not been played
        endandnext();
    }

    //this method has been set in fxml file on mouse click event for the progressbar
    public void seek()
    {
        progressBar.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));

            }
        });
    }

    //this method has been set in fxml file on drag and drop event for the progressbar
    public  void draganddroponmainslider()
    {
        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));

            }
        });
    }

    //this on initializes sliders
    public  void initializetools()
    {
        if (mediaPlayer !=null)
        {
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
            {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue)
                {
                    progressBar.setValue(newValue.toSeconds());
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
            volumeSlidder.setValue(mediaPlayer.getVolume() * 100);
            volumeSlidder.valueProperty().addListener(new InvalidationListener()
            {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlidder.getValue() / 100);
                }
            });

        }
    }


    public void pause(ActionEvent event)
    {
        mediaPlayer.pause();

    }

    public void stop(ActionEvent event)
    {
        mediaPlayer.stop();
        mediaView.setMediaPlayer(null);
    }

    //decreases 0.25 rate of the mediaplayer
    public void slowrate(ActionEvent event)
    {
        mediaPlayer.setRate(mediaPlayer.getRate()-0.25);
    }

    //increases 0.25 rate of the mediaplayer
    public void fastforward(ActionEvent event)

    {
        mediaPlayer.setRate(mediaPlayer.getRate()+0.25);
    }

    public void skip10s(ActionEvent event)
    {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));

    }

    public void back10s(ActionEvent event)
    {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }

    public void endandnext()
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

    public  void handleButtonClick ()
    {

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

    //this method will be called from playlist controller
    //in case of double click on an item in the table
    public  void playme(int index)
    {
        if (mediaPlayer!=null)
        {
            mediaPlayer.stop();
            media=null;
            mediaPlayer=null;
            mediaView.setMediaPlayer(null);
        }

        media=Repository.songs.get(index);
        mediaPlayer= new MediaPlayer(media);
        //mediaView=new MediaView();
        //mediaView.setMediaPlayer(mediaPlayer);
        DoubleProperty width = mediaView.fitWidthProperty();
        DoubleProperty height = mediaView.fitHeightProperty();
        width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        mediaPlayer.play();
        mediaPlayer.setRate(1);

        volumeSlidder.setValue(mediaPlayer.getVolume() * 100);

        volumeSlidder.valueProperty().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlidder.getValue() / 100);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        try {
            DoubleProperty width = mediaView.fitWidthProperty();
            DoubleProperty height = mediaView.fitHeightProperty();
            width.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            height.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));
        }catch(Exception e){}


    }


}
