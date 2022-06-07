package view.graphics;

import controller.database.CSVInfoGetter;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.card.Card;
import model.enums.Cursor;
import model.enums.VoiceEffects;
import view.graphics.Menu;
import view.graphics.duelgraphics.ChooseRival;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class SearchMenu extends Menu {

    @FXML
    protected VBox searchBox;
    @FXML
    protected TextField searchField;
    @FXML
    protected Label stageCounter;

    private int minSizeSearchBox = 1;

    protected ArrayList<VBox> searchResults = new ArrayList<>();

    protected abstract void search(String searchText);

    protected abstract ArrayList<VBox> getSearchResults(ArrayList<String> searchResults);


    public void setMinSizeSearchBox(int minSizeSearchBox) {
        this.minSizeSearchBox = minSizeSearchBox;
    }

    protected int getCurrentSearchStage() {
        String currentSearchStage = stageCounter.getText().split("/")[0];
        try {
            return Integer.parseInt(currentSearchStage);
        } catch (Exception e) {
            return -1;
        }
    }

    protected void showVBox(int index) {
        if (index >= searchResults.size() || index < 0) return;
        ObservableList<Node> list = searchBox.getChildren();
        emptySearchBox();
        list.add(searchResults.get(index));
        setStageLabel(index + 1);
    }

    protected void emptySearchBox() {
        ObservableList<Node> list = searchBox.getChildren();
        if (list.size() != minSizeSearchBox) list.remove(list.get(minSizeSearchBox));
    }

    protected void setStageLabel(int i) {
        stageCounter.setText(i + "/" + searchResults.size());
    }

    public void previous() {
        int currentStage = getCurrentSearchStage();
        if (currentStage == -1 || currentStage == 1) {
            playMedia(VoiceEffects.ERROR);
            return;
        }
        playMedia(VoiceEffects.CLICK);
        showVBox(currentStage - 2);
        setStageLabel(currentStage - 1);
    }

    public void next() {
        int currentStage = getCurrentSearchStage();
        if (currentStage == -1 || currentStage == searchResults.size()) {
            playMedia(VoiceEffects.ERROR);
            return;
        }
        playMedia(VoiceEffects.CLICK);
        showVBox(currentStage);
        setStageLabel(currentStage + 1);
    }
}
