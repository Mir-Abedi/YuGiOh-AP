package view.graphics;

import controller.database.CSVInfoGetter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ImportExportController extends Menu{
    @FXML
    private TextField importSearchbar;
    @FXML
    private ChoiceBox<Object> exportChoiceBox;
    @FXML
    private Button importButton;
    @FXML
    private Button exportButton;

    private String exportCardName;

    private String importText;

    public ImportExportController(){

    }
    public void initialize(){
        setImportButton();
        setExportButton();
        setImportSearchbar();
        setExportChoiceBox();
    }
    public void setExportChoiceBox(){
        exportChoiceBox.setItems(FXCollections.observableArrayList(CSVInfoGetter.getCardNames()));
        exportChoiceBox.setOnAction(actionEvent -> setExportButton());
    }
    public void setImportSearchbar(){
        importSearchbar.setOnAction(actionEvent -> setImportButton());
    }
    public void setExportButton(){
        if (exportChoiceBox.getValue() == null)
            exportButton.setDisable(true);
        else {
            exportButton.setDisable(false);
            exportButton.setOnMouseClicked(mouseEvent -> {
                exportCardName =(String) exportChoiceBox.getValue();
                try {
                    Menu.showAlert(CSVInfoGetter.getCardByName(exportCardName).getDescription());
                }
                catch (Exception exception){
                    Menu.showAlert("No file Found!");
                }
            });
        }
    }
    public void setImportButton(){
        if (importSearchbar.getText() == null)
            importButton.setDisable(true);
        else if (importSearchbar.getText().endsWith(".json") || importSearchbar.getText().endsWith(".csv")){
            importButton.setDisable(false);
            importText = importSearchbar.getText();
                File file = new File(importText);
                if (!file.exists())
                    Menu.showAlert("OPS! no file found!");
                else {
                   Menu.showAlert("File imported Successfully!");
                }
        }
        else
            importButton.setDisable(true);
    }
    public void exit() {
        Scene scene = new Scene(getNode("SettingMenu"),-1,-1,true);
        scene.setFill(Color.TRANSPARENT);
        Menu.setCurrentScene(scene);
        ((Stage)importButton.getScene().getWindow()).setScene(scene);
    }
}
