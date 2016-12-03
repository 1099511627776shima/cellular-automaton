package gui;

import gui.controllers.CreateNewAutomatonStageController;
import gui.controllers.MainStageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader mainSceneLoader = new FXMLLoader(getClass().getResource("cellular-automaton-nomenu.fxml"));
        Parent mainSceneView = mainSceneLoader.load();

        FXMLLoader newAutomatonSceneLoader = new FXMLLoader(getClass().getResource("controllers/new-automaton.fxml"));
        Parent newAutomatonSceneView = newAutomatonSceneLoader.load();

        MainStageController mainSceneController = mainSceneLoader.getController();
        CreateNewAutomatonStageController createNewAutomatonSceneController = newAutomatonSceneLoader.getController();

        mainStage.initModality(Modality.NONE);
        mainStage.initOwner(primaryStage);
        mainStage.setResizable(true);
        
        createNewAutomatonStage.initModality(Modality.NONE); // FIXME [BUG] When closing createNewAutomatonStage with APPLICATION_MODAL using (x) it makes the main window unmaximizable
        createNewAutomatonStage.initOwner(mainStage);
        createNewAutomatonStage.setResizable(false);

        final Scene mainScene = new Scene(mainSceneView, 1280, 720);
        final Scene newAutomatonScene = new Scene(newAutomatonSceneView, 600,450);

        mainStage.setTitle("Cellular automaton");
        mainStage.setScene(mainScene);
        mainStage.show();

        createNewAutomatonStage.setTitle("Create New Automaton");
        createNewAutomatonStage.setScene(newAutomatonScene);
        createNewAutomatonStage.show();

        mainSceneController.setStage(mainStage.getScene(), createNewAutomatonStage);
        createNewAutomatonSceneController.setStage(createNewAutomatonStage);
        createNewAutomatonSceneController.setMasterController(mainSceneController);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void openCreateNewAutomatonWindow() {
        createNewAutomatonStage.show();
    }
    
    private final Stage mainStage = new Stage();
    private final Stage createNewAutomatonStage = new Stage();
}