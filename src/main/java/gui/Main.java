package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("cellular-automaton.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("cellular-automaton.fxml"));
        Parent root = (Parent)loader.load();
        Controller controller = loader.getController();

        primaryStage.setTitle("Cellular automaton");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();

        controller.setStage(primaryStage.getScene());
    }


    public static void main(String[] args) {
        launch(args);
    }
}