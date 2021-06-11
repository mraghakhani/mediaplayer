package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(this.getClass().getResource("sample.fxml"));
        loader.load();
        primaryStage.setTitle("YSPlayer");
        Image icon=null;
        icon = new Image(this.getClass().getResource("icon.png").toExternalForm(), false);
        primaryStage.getIcons().add(icon);


        primaryStage.setScene(new Scene(loader.getRoot(), 900, 600));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
