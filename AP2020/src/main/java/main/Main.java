package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.graphics.Menu;
import view.graphics.SettingController;

public class Main extends Application {
    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        Menu.setStage(stage);
        stage.setResizable(false);
        Menu.goToMenu("Welcome");
        SettingController.playBG();
        stage.show();
    }
}
