package view.graphics.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import model.enums.Cursor;
import view.graphics.Menu;

public abstract class ChangeMenu extends Menu {
    @FXML
    protected Button changeButton;

    public void enterButton(MouseEvent mouseEvent) {
        enterButton(changeButton,Cursor.ACCEPT,mouseEvent);
    }

    public void exitButton(MouseEvent mouseEvent) {
        exitButton(changeButton,mouseEvent);
    }

    public abstract void change();

}
