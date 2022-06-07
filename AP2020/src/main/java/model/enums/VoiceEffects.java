package model.enums;

import javafx.scene.media.Media;
import view.graphics.Menu;

public enum VoiceEffects {
    ERROR(Menu.getVoice("Error","mp3")),
    CLICK(Menu.getVoice("Click","mp3")),
    KEYBOARD_HIT(Menu.getVoice("KeyPress","mp3")),
    CARD_FLIP(Menu.getVoice("CardFlip","mp3")),
    COIN_DROP(Menu.getVoice("CoinDrop","mp3")),
    MIO(Menu.getVoice("Mio","wav")),
    EXPLODE(Menu.getVoice("Explode","mp3")),
    BG(Menu.getVoice("BackGroundMusic","mp3")),
    JINGLE(Menu.getVoice("Jingle","mp3")),
    CLEAR_CHALK(Menu.getVoice("ClearChalk","mp3")),
    SHOOW_1(Menu.getVoice("Shoow1","mp3")),
    SHOOW_2(Menu.getVoice("Shoow2","mp3"));
    javafx.scene.media.Media media;
    VoiceEffects(javafx.scene.media.Media media) {
        this.media = media;
    }

    public Media getMedia() {
        return media;
    }
}
