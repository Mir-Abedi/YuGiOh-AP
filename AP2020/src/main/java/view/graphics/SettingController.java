package view.graphics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.enums.Cursor;
import model.enums.VoiceEffects;

import java.net.URL;
import java.util.ResourceBundle;



public class SettingController extends Menu implements Initializable {
    private final static MediaPlayer PLAYER = new MediaPlayer(getVoice("BackGroundMusic","mp3"));
    private static boolean isMute = false;
    private static double sfxVolume = 0.5;
    private static double volume = 0.5;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Button exitButton;
    @FXML
    private Button importExport;
    @FXML
    private Slider sfxSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ToggleButton muteToggle;

    public static double getSFX() {
        return sfxVolume;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initToggle();
        exitButton.setOnMouseEntered(mouseEvent -> changeCursor(Cursor.CANCEL,mouseEvent));
        exitButton.setOnMouseExited(mouseEvent -> changeCursor(Cursor.DEFAULT,mouseEvent));
        justifyButton(importExport,Cursor.TRASH);
        importExport.setOnMouseClicked(mouseEvent -> {
            playMedia(VoiceEffects.CLICK);
            goToImportExport();
        });
        exitButton.setOnMouseClicked(mouseEvent -> {
            playMedia(VoiceEffects.EXPLODE);
            close();
        });
        volumeSlider.valueProperty().addListener((observableValue, number, t1) -> PLAYER.setVolume(t1.doubleValue()));
        sfxSlider.valueProperty().addListener((observableValue, number, t1) -> {
            sfxVolume = t1.doubleValue();
            playMedia(VoiceEffects.KEYBOARD_HIT);
        });
        volumeSlider.setValue(PLAYER.getVolume());
        sfxSlider.setValue(sfxVolume);
    }

    private void initToggle() {
        muteToggle.setSelected(isMute);
        ToggleGroup group = new ToggleGroup();
        group.getToggles().add(muteToggle);
        onSelectToggle(muteToggle,group);
        muteToggle.setOnAction(actionEvent -> {
            playMedia(VoiceEffects.CLICK);
            onSelectToggle(muteToggle,group);
            if (muteToggle.isSelected()) mute();
            else unMute();
        });
    }

    private void close() {
        Scene scene = mainPane.getScene();
        setCurrentScene(Menu.getSceneBuffer());
        setSceneBuffer(null);
        ((Stage)scene.getWindow()).close();
    }

    public static void playBG() {
        PLAYER.setVolume(0.5);
        PLAYER.setCycleCount(-1);
        PLAYER.play();
    }

    private void mute() {
        isMute = true;
        PLAYER.setMute(true);
        PLAYER.pause();
    }

    private void unMute() {
        isMute = false;
        PLAYER.setMute(false);
        PLAYER.play();
    }

    private void goToImportExport() {
        Scene scene = new Scene(getNode("ImportExportMenu"),-1,-1,true);
        scene.setFill(Color.TRANSPARENT);
        Menu.setCurrentScene(scene);
        ((Stage)mainPane.getScene().getWindow()).setScene(scene);
    }
}
