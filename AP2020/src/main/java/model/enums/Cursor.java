package model.enums;

import javafx.scene.image.Image;
import view.graphics.Menu;

public enum Cursor {
    CHANGE_PASS(Menu.getImage("Cursors/ChangePasswordCursor","png")),
    HASHTAG(Menu.getImage("Cursors/HashtagCursor","png")),
    LEFT_ARROW(Menu.getImage("Cursors/LeftArrowCursor","png")),
    RIGHT_ARROW(Menu.getImage("Cursors/RightArrowCursor","png")),
    SEARCH(Menu.getImage("Cursors/SearchCursor","png")),
    PROFILE(Menu.getImage("Cursors/ProfileCursor","png")),
    ACCEPT(Menu.getImage("Cursors/AcceptCursor","png")),
    CANCEL(Menu.getImage("Cursors/CancelCursor","png")),
    CREATE_DECK(Menu.getImage("Cursors/CreateCursor","png")),
    ALL_CARDS(Menu.getImage("Cursors/AllCardsCursor","png")),
    TRASH(Menu.getImage("Cursors/TrashCursor","png")),
    EDIT(Menu.getImage("Cursors/EditCursor","png")),
    SWORD(Menu.getImage("Cursors/SwordCursor","png")),
    DEFAULT(null);
    private final Image image;

    Cursor(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
