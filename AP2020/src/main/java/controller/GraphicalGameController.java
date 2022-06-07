package controller;

import controller.database.CSVInfoGetter;
import controller.database.ReadAndWriteDataBase;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import model.card.Card;
import model.card.spell_traps.Spell;
import model.card.spell_traps.SpellType;
import model.enums.VoiceEffects;
import model.exceptions.GameException;
import model.exceptions.WinnerException;
import model.game.Cell;
import model.game.Game;
import model.game.State;
import view.duelMenu.Phase;
import view.graphics.Menu;
import view.graphics.duelgraphics.ChangeBetweenThreeRounds;
import view.graphics.duelgraphics.EndPhaseMenuGraphical;
import view.graphics.duelgraphics.WinnerExceptionHolder;
import view.responses.GameMenuResponse;
import view.responses.GameMenuResponsesEnum;

import java.util.ArrayList;
import java.util.Arrays;


public class GraphicalGameController {

    private final Button ATTACK = new Button("Attack Monster !");
    private final Button DIRECT_ATTACK = new Button("Direct Attack !");
    private final Button NEXT_PHASE = new Button("Next phase!");
    private final Button ACTIVE_EFFECT = new Button("Active Effect!");
    private final Button SET = new Button("Set!");
    private final Button FLIP_SUMMON = new Button("Flip Summon!");
    private final Button SHOW_CARD = new Button("Show Card!");
    private final Button SHOW_GRAVEYARD = new Button("Show GraveYard!");
    private final Button SET_ATTACK_POSITION = new Button("Set Attack!");
    private final Button SET_DEFENSE_POSITION = new Button("Set Defense!");
    private final Button SURRENDER = new Button("Surrender!");
    private final Button SUMMON = new Button("Summon!");
    private final Pane pane;
    private final ImageView[] playerMonsters;
    private final ImageView[] playerSpells;
    private final ImageView[] rivalMonsters;
    private final ImageView[] rivalSpells;
    private final HBox playerCards;
    private final HBox rivalCards;
    private final VBox options;
    private final ImageView playerFieldSpell;
    private final ImageView playerGraveYard;
    private final ImageView rivalFieldSpell;
    private final ImageView rivalGraveYard;
    private final ImageView background;
    private final Label playerName;
    private final Label playerHealth;
    private final Label rivalName;
    private final Label rivalHealth;
    private final Label phase;
    private final Game game;
    private final Circle playerPic;
    private final Circle rivalPic;
    private Phase currentPhase = Phase.DRAW_PHASE;

    {
        ATTACK.setOnMouseClicked(this::attack);
        DIRECT_ATTACK.setOnMouseClicked(this::directAttack);
        NEXT_PHASE.setOnMouseClicked(this::nextPhase);
        ACTIVE_EFFECT.setOnMouseClicked(this::activeEffect);
        SET.setOnMouseClicked(this::set);
        FLIP_SUMMON.setOnMouseClicked(this::flipSummon);
        SHOW_CARD.setOnMouseClicked(this::showCard);
        SHOW_GRAVEYARD.setOnMouseClicked(this::showGraveYard);
        SET_ATTACK_POSITION.setOnMouseClicked(this::setAttackPosition);
        SET_DEFENSE_POSITION.setOnMouseClicked(this::setDefensePosition);
        SURRENDER.setOnMouseClicked(this::surrender);
        SUMMON.setOnMouseClicked(this::summon);
        adjustButton(ATTACK);
        adjustButton(DIRECT_ATTACK);
        adjustButton(NEXT_PHASE);
        adjustButton(ACTIVE_EFFECT);
        adjustButton(SET);
        adjustButton(FLIP_SUMMON);
        adjustButton(SHOW_CARD);
        adjustButton(SHOW_GRAVEYARD);
        adjustButton(SET_ATTACK_POSITION);
        adjustButton(SET_DEFENSE_POSITION);
        adjustButton(SURRENDER);
        adjustButton(SUMMON);
    }

    public GraphicalGameController(ImageView[] playerMonsters, ImageView[] playerSpells,
                                   ImageView[] rivalMonsters, ImageView[] rivalSpells,
                                   HBox playerCards, HBox rivalCards, VBox options,
                                   ImageView playerFieldSpell, ImageView playerGraveYard,
                                   ImageView rivalFieldSpell, ImageView rivalGraveYard, Game game, Pane pane,
                                   Label playerName, Label playerHealth, Label rivalName, Label rivalHealth, Label phase, ImageView background,
                                   Circle playerPic, Circle rivalPic) {
        this.pane = pane;
        this.playerMonsters = playerMonsters;
        this.playerSpells = playerSpells;
        this.options = options;
        this.rivalMonsters = rivalMonsters;
        this.rivalCards = rivalCards;
        this.rivalSpells = rivalSpells;
        this.playerCards = playerCards;
        this.playerFieldSpell = playerFieldSpell;
        this.playerGraveYard = playerGraveYard;
        this.rivalFieldSpell = rivalFieldSpell;
        this.rivalGraveYard = rivalGraveYard;
        this.playerName = playerName;
        this.playerHealth = playerHealth;
        this.rivalName = rivalName;
        this.rivalHealth = rivalHealth;
        this.background = background;
        this.playerPic = playerPic;
        this.rivalPic = rivalPic;
        this.phase = phase;
        this.game = game;
//        game.getPlayerHandCards().add(CSVInfoGetter.getCardByName("Monster Reborn"));
//        game.getRivalHandCards().add(CSVInfoGetter.getCardByName("Monster Reborn"));
        setImageFunctionality();
        loadNames();
        initReceiveDrag();
        updateWithoutTransition();
    }

    public void initReceiveDrag() {
        for (ImageView playerMonster : playerMonsters) {
            playerMonster.setOnDragOver(dragEvent -> {
                if (dragEvent.getDragboard().hasImage()) dragEvent.acceptTransferModes(TransferMode.ANY);
                dragEvent.consume();
            });
            playerMonster.setOnDragDropped(dragEvent -> {
                if (dragEvent.getDragboard().hasImage()) {
                    summonWithDrag(dragEvent);
                }
            });
        }
        for (ImageView playerSpell : playerSpells) {
            playerSpell.setOnDragOver(dragEvent -> {
                if (dragEvent.getDragboard().hasImage()) dragEvent.acceptTransferModes(TransferMode.ANY);
                dragEvent.consume();
            });
            playerSpell.setOnDragDropped(dragEvent -> {
                if (dragEvent.getDragboard().hasImage()) {
                    summonWithDrag(dragEvent);
                }
            });
        }
    }

    private void loadNames() {
        playerName.setText(game.getPlayer().getNickname());
        playerHealth.setText(game.getPlayerLP() + "");
        rivalName.setText(game.getRival().getNickname());
        rivalHealth.setText(game.getRivalLP() + "");
        playerPic.setFill(new ImagePattern(Menu.getProfilePhoto(game.getPlayer().getProfilePhoto())));
        rivalPic.setFill(new ImagePattern(Menu.getProfilePhoto(game.getRival().getProfilePhoto())));
    }

    // doesnt work for hand cards ..
    private void setImageFunctionality() {
        ArrayList<ImageView> images = new ArrayList<>(Arrays.asList(playerMonsters));
        images.addAll(Arrays.asList(playerSpells));
        images.addAll(Arrays.asList(rivalMonsters));
        images.addAll(Arrays.asList(rivalSpells));
        images.add(playerFieldSpell);
        images.add(rivalFieldSpell);
        images.add(playerGraveYard);
        images.add(rivalGraveYard);
        for (ImageView image : images) setImageSelectable(image);
    }

    private void setImageSelectable(ImageView image) {
        image.setOnMouseClicked(mouseEvent -> {
            resetImageEffects();
            if (((ImageView) mouseEvent.getSource()).getImage() != null)
                ((ImageView) mouseEvent.getSource()).setEffect(new DropShadow(40, Color.RED));
            updateButtons();
        });
    }

    private void updateButtons() {
        removeOptions();
        if (playerMonsterIsSelected()) {
            playerMonsterButtons();
        } else if (playerSpellIsSelected()) {
            playerSpellsButtons();
        } else if (rivalMonsterIsSelected()) {
            rivalMonstersButtons();
        } else if (rivalSpellIsSelected()) {
            rivalSpellsButtons();
        } else if (playerFieldSpellIsSelected()) {
            playerFieldSpellButtons();
        } else if (playerGraveYardIsSelected()) {
            playerGraveYardButtons();
        } else if (rivalFieldSpellIsSelected()) {
            rivalFieldSpellButtons();
        } else if (rivalGraveYardIsSelected()) {
            rivalGraveYardButtons();
        } else if (playerHandCardIsSelected()) {
            playerHandCardsButtons();
        } else {
            options.getChildren().addAll(NEXT_PHASE, SURRENDER);
        }
    }

    private boolean playerHandCardIsSelected() {
        for (Node child : playerCards.getChildren()) {
            if (child.getEffect() != null) return true;
        }
        return false;
    }

    private void removeOptions() {
        for (int i = options.getChildren().size() - 1; i >= 0; i--) {
            options.getChildren().remove(i);
        }
    }

    private boolean rivalGraveYardIsSelected() {
        return rivalGraveYard.getImage() != null && rivalGraveYard.getEffect() != null;
    }

    private boolean rivalFieldSpellIsSelected() {
        return rivalFieldSpell.getImage() != null && rivalFieldSpell.getEffect() != null;
    }

    private boolean playerGraveYardIsSelected() {
        return playerGraveYard.getImage() != null && playerGraveYard.getEffect() != null;
    }

    private boolean playerFieldSpellIsSelected() {
        return playerFieldSpell.getImage() != null && playerFieldSpell.getEffect() != null;
    }

    private boolean rivalSpellIsSelected() {
        for (ImageView imageView : rivalSpells) {
            if (imageView.getImage() != null && imageView.getEffect() != null) return true;
        }
        return false;
    }

    private boolean rivalMonsterIsSelected() {
        for (ImageView imageView : rivalMonsters) {
            if (imageView.getImage() != null && imageView.getEffect() != null) return true;
        }
        return false;
    }

    private boolean playerSpellIsSelected() {
        for (ImageView imageView : playerSpells) {
            if (imageView.getImage() != null && imageView.getEffect() != null) return true;
        }
        return false;
    }

    private boolean playerMonsterIsSelected() {
        for (ImageView imageView : playerMonsters) {
            if (imageView.getImage() != null && imageView.getEffect() != null) return true;
        }
        return false;
    }

    public void playerMonsterButtons() {
        int i = 0;
        for (int i1 = 0; i1 < playerMonsters.length; i1++) {
            if (playerMonsters[i1].getImage() != null && playerMonsters[i1].getEffect() != null) {
                i = i1;
                break;
            }
        }
        Cell cell = game.getPlayerBoard().getMonsterZone(i);
        if (!cell.isOccupied()) {
            options.getChildren().addAll(NEXT_PHASE, SURRENDER);
            return;
        }
        State cellState = cell.getState();
        if (currentPhase == Phase.BATTLE_PHASE && cellState == State.FACE_UP_ATTACK) {
            options.getChildren().addAll(ATTACK, DIRECT_ATTACK);
        }
        if (cellState != State.FACE_DOWN_DEFENCE && (currentPhase == Phase.MAIN_PHASE1 || currentPhase == Phase.MAIN_PHASE2)) {
            options.getChildren().addAll(SET_ATTACK_POSITION, SET_DEFENSE_POSITION);
        }
        if (cellState == State.FACE_DOWN_DEFENCE) {
            options.getChildren().add(FLIP_SUMMON);
        }
        options.getChildren().addAll(SHOW_CARD, NEXT_PHASE, SURRENDER);
    }

    public void playerSpellsButtons() {
        int i = 0;
        for (int i1 = 0; i1 < playerSpells.length; i1++) {
            if (playerSpells[i1].getImage() != null && playerSpells[i1].getEffect() != null) {
                i = i1;
                break;
            }
        }
        Cell cell = game.getPlayerBoard().getSpellZone(i);
        if (!cell.isOccupied()) {
            options.getChildren().addAll(NEXT_PHASE, SURRENDER);
            return;
        }
        State cellState = cell.getState();
        if (currentPhase == Phase.MAIN_PHASE1 || currentPhase == Phase.MAIN_PHASE2) {
            options.getChildren().add(ACTIVE_EFFECT);
        }
        options.getChildren().addAll(SHOW_CARD, NEXT_PHASE, SURRENDER);
    }

    public void rivalMonstersButtons() {
        int i = 0;
        for (int i1 = 0; i1 < rivalMonsters.length; i1++) {
            if (rivalMonsters[i1].getImage() != null && rivalMonsters[i1].getEffect() != null) {
                i = i1;
                break;
            }
        }
        Cell cell = game.getRivalBoard().getMonsterZone(i);
        if (!cell.isOccupied()) {
            options.getChildren().addAll(NEXT_PHASE, SURRENDER);
            return;
        }
        State cellState = cell.getState();
        if (cellState != State.FACE_DOWN_DEFENCE) options.getChildren().add(SHOW_CARD);
        options.getChildren().addAll(NEXT_PHASE, SURRENDER);
    }

    public void rivalSpellsButtons() {
        int i = 0;
        for (int i1 = 0; i1 < rivalSpells.length; i1++) {
            if (rivalSpells[i1].getImage() != null && rivalSpells[i1].getEffect() != null) {
                i = i1;
                break;
            }
        }
        Cell cell = game.getRivalBoard().getSpellZone(i);
        if (!cell.isOccupied()) {
            options.getChildren().addAll(NEXT_PHASE, SURRENDER);
            return;
        }
        State cellState = cell.getState();
        if (cellState == State.FACE_UP_SPELL) options.getChildren().add(SHOW_CARD);
        options.getChildren().addAll(NEXT_PHASE, SURRENDER);
    }

    public void playerFieldSpellButtons() {
        if (game.getPlayerBoard().getFieldZone().isOccupied()) options.getChildren().add(SHOW_CARD);
        options.getChildren().addAll(NEXT_PHASE, SURRENDER);
    }

    public void playerGraveYardButtons() {
        options.getChildren().addAll(SHOW_GRAVEYARD, NEXT_PHASE, SURRENDER);
    }

    public void rivalGraveYardButtons() {
        playerGraveYardButtons();
    }

    public void rivalFieldSpellButtons() {
        if (game.getRivalBoard().getFieldZone().isOccupied()) options.getChildren().add(SHOW_CARD);
        options.getChildren().addAll(NEXT_PHASE, SURRENDER);
    }

    public void playerHandCardsButtons() {
        int i = -1;
        for (int i1 = 0; i1 < playerCards.getChildren().size(); i1++) {
            if (playerCards.getChildren().get(i1).getEffect() != null) {
                i = i1;
                break;
            }
        }
        if (i == -1) {
            options.getChildren().addAll(NEXT_PHASE, SURRENDER);
            return;
        }
        Card card = game.getPlayerHandCards().get(i);
        if (card.isSpell()) {
            if (((Spell) card).getSpellType() == SpellType.FIELD) {
                options.getChildren().addAll(SUMMON, SHOW_CARD, NEXT_PHASE, SURRENDER);
                return;
            }
        }
        options.getChildren().addAll(SUMMON, SET, SHOW_CARD, NEXT_PHASE, SURRENDER);
    }

    public void summonWithDrag(DragEvent dragEvent) {
        ImageView imageView;
        try {
            imageView = (ImageView) dragEvent.getGestureSource();
        } catch (Exception e) {return;}
        resetImageEffects();
        imageView.setEffect(new DropShadow(0, Color.GREEN));
        summon(null);
    }

    public void summon(MouseEvent event) {
        int i = -1;
        for (int i1 = 0; i1 < playerCards.getChildren().size(); i1++) {
            if (playerCards.getChildren().get(i1).getEffect() != null) {
                i = i1;
                break;
            }
        }
        if (i == -1) return;
        Card card = game.getPlayerHandCards().get(i);
        GameMenuResponse gameMenuResponse;
        try {
            gameMenuResponse = GameMenuController.summon(game, i + 1, false);
        } catch (WinnerException winnerException) {
            gameFinished(winnerException);
            return;
        }
        if (gameMenuResponse.getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL)
            updateSummonWithTransition(card, i);
        else response(gameMenuResponse);
    }

    private void updateSummonWithTransition(Card card, int index) {
        boolean isField = false;
        int x, y;
        if (card.isMonster()) {
            y = 358;
            int i = -1;
            Cell[] cells = game.getPlayerBoard().getMonsterZone();
            for (int i1 = cells.length - 1; i1 >= 0; i1--) {
                if (cells[i1].getCard() == card) {
                    i = i1;
                    break;
                }
            }
            if (i == -1) return;
            if (i == 0) x = 169 + 75 * 2;
            else if (i == 1) x = 169 + 75;
            else if (i == 2) x = 169 + 75 * 3;
            else if (i == 3) x = 169;
            else x = 169 + 75 * 4;
        } else {
            if (card.isSpell()) {
                if (((Spell) card).getSpellType() == SpellType.FIELD) {
                    x = 67;
                    y = 355;
                    isField = true;
                } else {
                    y = 457;
                    int i = -1;
                    Cell[] cells = game.getPlayerBoard().getSpellZone();
                    for (int i1 = cells.length - 1; i1 >= 0; i1--) {
                        if (cells[i1].getCard() == card) {
                            i = i1;
                            break;
                        }
                    }
                    if (i == -1) return;
                    if (i == 0) x = 169 + 75 * 2;
                    else if (i == 1) x = 169 + 75;
                    else if (i == 2) x = 169 + 75 * 3;
                    else if (i == 3) x = 169;
                    else x = 169 + 75 * 4;
                }
            } else {
                y = 458;
                int i = -1;
                Cell[] cells = game.getPlayerBoard().getSpellZone();
                for (int i1 = cells.length - 1; i1 >= 0; i1--) {
                    if (cells[i1].getCard() == card) {
                        i = i1;
                        break;
                    }
                }
                if (i == -1) return;
                if (i == 0) x = 169 + 75 * 2;
                else if (i == 1) x = 169 + 75;
                else if (i == 2) x = 169 + 75 * 3;
                else if (i == 3) x = 169;
                else x = 169 + 75 * 4;
            }
        }
        final boolean isFieldfinal = isField;
        ImageView imageView = Menu.getImageWithSizeForGame(card.getCardName(), 100 + index * 75, 590);
        pane.getChildren().add(imageView);
        MoveTransition moveTransition = new MoveTransition(x, y, 100 + index * 75, 590, imageView, 1000, false);
        moveTransition.setOnFinished(actionEvent -> {
            if (isFieldfinal) updateBackGroundForField();
            pane.getChildren().remove(imageView);
            updateWithoutTransition();
        });
        moveTransition.play();
    }

    private void updateBackGroundForField() {
        Card card = game.getPlayerBoard().getFieldZone().getCard();
        if (card == null) {
            background.setImage(Menu.getImage("solid brown", "png"));
            background.setOpacity(0.6);
        } else {
            background.setImage(Menu.getImage(GameMenuController.trimName(card.getCardName()), "jpg"));
            background.setOpacity(0.4);
        }
    }

    public void attack(MouseEvent mouseEvent) {
        int attacker = -1;
        for (int i = 0; i < 5; i++) {
            if (playerMonsters[i].getEffect() != null) {
                attacker = i;
            }
        }
        if (attacker == -1) return;
        if (game.getRivalBoard().getNumberOfMonstersInMonsterZone() == 0) {
            Menu.showAlert("Rival Monster Zone is empty!");
            return;
        }
        final int attackFinal = attacker;
        ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("Attack!");
        Rectangle rectangle = (Rectangle) nodes.get(0);
        pane.getChildren().add(rectangle);
        ImageView[] images = new ImageView[5];
        boolean[] playerBackUpCells = new boolean[5];
        for (int i = 0; i < 5; i++) playerBackUpCells[i] = game.getPlayerBoard().getMonsterZone()[i].isOccupied();
        boolean[] rivalBackUpCells = new boolean[5];
        for (int i = 0; i < 5; i++) rivalBackUpCells[i] = game.getRivalBoard().getMonsterZone()[i].isOccupied();
        for (int i = 0; i < 5; i++) {
            images[i] = Menu.getImageWithSizeForGame(rivalMonsters[i].getImage(), 75 * i + 200, 200);
            pane.getChildren().add(images[i]);
        }
        Button attackBtn = (Button) nodes.get(1);
        pane.getChildren().add(attackBtn);
        rectangle.setOnMouseClicked(mouseEvent1 -> {
            pane.getChildren().removeAll(Arrays.asList(images));
            pane.getChildren().removeAll(rectangle, attackBtn);
        });
        attackBtn.setDisable(true);
        for (ImageView image : images) {
            image.setOnMouseClicked(mouseEvent1 -> {
                attackBtn.setDisable(false);
                for (ImageView imageView : images) {
                    imageView.setEffect(null);
                }
                if (((ImageView) mouseEvent1.getSource()).getImage() != null)
                    ((ImageView) mouseEvent1.getSource()).setEffect(new DropShadow(40, Color.GREEN));
            });
        }
        attackBtn.setOnMouseClicked(mouseEvent1 -> {
            int defender = -1;
            for (int i = 0; i < 5; i++) {
                if (images[i].getEffect() != null) defender = i;
            }
            if (defender == -1) return;
            final int defenderFinal = defender;
            if (!game.getRivalBoard().getMonsterZone(defender).isOccupied()) return;
            pane.getChildren().removeAll(Arrays.asList(images));
            pane.getChildren().removeAll(rectangle, attackBtn);
            getSelectedImageView().setEffect(null);
            removeOptions();

            // moving to graveyard
            Runnable runnable = () -> {
                try {
                    GameMenuResponse gameMenuResponse = GameMenuController.attack(game, attackFinal + 1, defenderFinal + 1, false);
                    response(gameMenuResponse);
                } catch (GameException gameException) {
                    if (gameException instanceof WinnerException) gameFinished((WinnerException) gameException);
                    return;
                }
                Cell[] playerCells = game.getPlayerBoard().getMonsterZone();
                Cell[] rivalCells = game.getRivalBoard().getMonsterZone();
                for (int i = 0; i < 5; i++) {
                    if (rivalBackUpCells[i] && !rivalCells[i].isOccupied()) {
                        int x, y = 210;
                        if (i == 0) x = 155 + 75 * 2;
                        else if (i == 1) x = 155 + 75 * 3;
                        else if (i == 2) x = 155 + 75;
                        else if (i == 3) x = 155 + 75 * 4;
                        else x = 155;
                        ImageView imageView = Menu.getImageWithSizeForGame(rivalMonsters[i].getImage(), x, y);
                        MoveTransition moveTransition = new MoveTransition(23, 111, imageView.getX(), imageView.getY(), imageView, 1500, true);
                        pane.getChildren().add(imageView);
                        moveTransition.setOnFinished(actionEvent -> pane.getChildren().remove(imageView));
                        moveTransition.play();
                    }
                }
                for (int i = 0; i < 5; i++) {
                    if (playerBackUpCells[i] && !playerCells[i].isOccupied()) {
                        int x, y = 355;
                        if (i == 0) x = 155 + 75 * 2;
                        else if (i == 1) x = 155 + 75;
                        else if (i == 2) x = 155 + 75 * 3;
                        else if (i == 3) x = 155;
                        else x = 155 + 75 * 4;
                        ImageView imageView = Menu.getImageWithSizeForGame(playerMonsters[i].getImage(), x, y);
                        MoveTransition moveTransition = new MoveTransition(23, 471, imageView.getX(), imageView.getY(), imageView, 1500, true);
                        pane.getChildren().add(imageView);
                        moveTransition.setOnFinished(actionEvent -> pane.getChildren().remove(imageView));
                        moveTransition.play();
                    }
                }
                removeOptions();
                resetImageEffects();
                updateWithoutTransition();
            };


            if (game.getRivalBoard().getMonsterZone(defender).getState() == State.FACE_DOWN_DEFENCE) {
                int x, y = 218;
                if (defender == 0) x = 169 + 75 * 2;
                else if (defender == 1) x = 169 + 75 * 3;
                else if (defender == 2) x = 169 + 75;
                else if (defender == 3) x = 169 + 75 * 4;
                else x = 169;
                rivalMonsters[defender].setImage(null);
                ImageView imageView = Menu.getImageWithSizeForGame("back", x, y);
                pane.getChildren().add(imageView);
                String cardName = game.getRivalBoard().getMonsterZone(defender).getCard().getCardName();
                FlipTransition flipTransition = new FlipTransition(imageView, x, y, cardName, 1000);
                flipTransition.setOnFinished(event -> {
                    pane.getChildren().remove(imageView);
                    rivalMonsters[defenderFinal].setImage(Menu.getCard(cardName));
                    runnable.run();
                });
                flipTransition.play();
            } else {
                runnable.run();
            }
        });
    }

    public void directAttack(MouseEvent mouseEvent) {
        int attacker = -1;
        for (int i = 0; i < 5; i++) {
            if (playerMonsters[i].getEffect() != null) {
                attacker = i;
            }
        }
        if (attacker == -1) return;
        if (game.getRivalBoard().getNumberOfMonstersInMonsterZone() != 0) {
            Menu.showAlert("Monster Zone is not Empty!");
            return;
        }
        try {
            GameMenuResponse gameMenuResponse = GameMenuController.directAttack(game, attacker + 1);
            if (gameMenuResponse.getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL)
                Menu.playMedia(VoiceEffects.EXPLODE);
            response(gameMenuResponse);
        } catch (WinnerException winnerException) {
            gameFinished(winnerException);
            return;
        }
        updateWithoutTransition();
    }

    public void set(MouseEvent mouseEvent) {
        int i = -1;
        for (int i1 = 0; i1 < playerCards.getChildren().size(); i1++) {
            if (playerCards.getChildren().get(i1).getEffect() != null) {
                i = i1;
                break;
            }
        }
        if (i == -1) return;
        Card card = game.getPlayerHandCards().get(i);
        GameMenuResponse gameMenuResponse;
        if (card.isMonster()) {
            gameMenuResponse = GameMenuController.setMonsterCard(game, i + 1);
        } else {
            gameMenuResponse = GameMenuController.setSpellAndTrap(game, i + 1);
        }
        if (gameMenuResponse.getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL) {
            int x, y;
            if (card.isMonster()) {
                y = 360;
                int j = -1;
                Cell[] cells = game.getPlayerBoard().getMonsterZone();
                for (int i1 = cells.length - 1; i1 >= 0; i1--) {
                    if (cells[i1].getCard() == card) {
                        j = i1;
                        break;
                    }
                }
                if (j == -1) return;
                if (j == 0) x = 165 + 75 * 2;
                else if (j == 1) x = 165 + 75;
                else if (j == 2) x = 165 + 75 * 3;
                else if (j == 3) x = 165;
                else x = 165 + 75 * 4;
            } else {
                y = 460;
                int j = -1;
                Cell[] cells = game.getPlayerBoard().getSpellZone();
                for (int i1 = cells.length - 1; i1 >= 0; i1--) {
                    if (cells[i1].getCard() == card) {
                        j = i1;
                        break;
                    }
                }
                if (j == -1) return;
                if (j == 0) x = 165 + 75 * 2;
                else if (j == 1) x = 165 + 75;
                else if (j == 2) x = 165 + 75 * 3;
                else if (j == 3) x = 165;
                else x = 165 + 75 * 4;
            }
            ImageView imageView = Menu.getImageWithSizeForGame("back", 100 + i * 75, 590);
            pane.getChildren().add(imageView);
            MoveTransition moveTransition = new MoveTransition(x, y, imageView.getX(), imageView.getY(), imageView, 1000, false);
            moveTransition.setOnFinished(actionEvent -> {
                pane.getChildren().remove(imageView);
                updateWithoutTransition();
            });
            moveTransition.play();
        } else {
            response(gameMenuResponse);
        }
    }

    public void nextPhase(MouseEvent mouseEvent) {
        try {
            if (currentPhase.equals(Phase.STANDBY_PHASE))
                goToMainPhase1();
            else if (currentPhase.equals(Phase.END_PHASE)) {
                goToDrawPhase();
                GameMenuResponse gameMenuResponse;
                if ((gameMenuResponse = GameMenuController.draw(game)).getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL) {
                    Menu.showAlert("New card added to your hand!");
                }
            } else if (currentPhase.equals(Phase.DRAW_PHASE))
                goToStandByPhase();
            else if (currentPhase.equals(Phase.MAIN_PHASE2))
                goToEndPhase();
            else if (currentPhase.equals(Phase.BATTLE_PHASE))
                goToMainPhase2();
            else if (currentPhase.equals(Phase.MAIN_PHASE1))
                goToBattlePhase();
        } catch (WinnerException e) {
            gameFinished(e);
            return;
        }
        updateWithoutTransition();
    }

    public void goToStandByPhase() throws WinnerException {
        GameMenuController.goToStandByPhase(game);
        setCurrentPhase(Phase.STANDBY_PHASE);
        updatePhase();
    }

    public void goToMainPhase1() {
        setCurrentPhase(Phase.MAIN_PHASE1);
        updatePhase();
    }

    public void goToDrawPhase() throws WinnerException {
        resetImageEffects();
        if (game.getPlayerHandCards().size() > 6) new EndPhaseMenuGraphical(game).run();
        game.changeTurn();
        resetImageEffects();
        setCurrentPhase(Phase.DRAW_PHASE);
        updatePhase();
    }

    public void goToMainPhase2() {
        setCurrentPhase(Phase.MAIN_PHASE2);
        updatePhase();
    }

    public void goToBattlePhase() {
        setCurrentPhase(Phase.BATTLE_PHASE);
        updatePhase();
    }

    public void goToEndPhase() {
        setCurrentPhase(Phase.END_PHASE);
        updatePhase();
    }

    public void setCurrentPhase(Phase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public void updatePhase() {
        if (currentPhase.equals(Phase.STANDBY_PHASE))
            phase.setText("StandBy");
        else if (currentPhase.equals(Phase.END_PHASE))
            phase.setText("End phase");
        else if (currentPhase.equals(Phase.DRAW_PHASE))
            phase.setText("Draw");
        else if (currentPhase.equals(Phase.MAIN_PHASE2))
            phase.setText("Main 2");
        else if (currentPhase.equals(Phase.BATTLE_PHASE))
            phase.setText("Battle");
        else if (currentPhase.equals(Phase.MAIN_PHASE1))
            phase.setText("Main 1");
    }

    public void activeEffect(MouseEvent mouseEvent) {
        int i = -1;
        for (int i1 = 0; i1 < playerSpells.length; i1++) {
            if (playerSpells[i1].getEffect() != null) i = i1;
        }
        if (i == -1) return;
        boolean[] playerMonstersOccupied = new boolean[5];
        boolean[] rivalMonstersOccupied = new boolean[5];
        boolean[] playerSpellsOccupied = new boolean[5];
        boolean[] rivalSpellsOccupied = new boolean[5];
        Card card = game.getPlayerBoard().getSpellZone(i).getCard();
        if (card == null) {
            return;
        }
        for (int i1 = 0; i1 < 5; i1++) {
            playerMonstersOccupied[i1] = game.getPlayerBoard().getMonsterZone(i1).isOccupied();
        }
        for (int i1 = 0; i1 < 5; i1++) {
            rivalMonstersOccupied[i1] = game.getRivalBoard().getMonsterZone(i1).isOccupied();
        }
        for (int i1 = 0; i1 < 5; i1++) {
            playerSpellsOccupied[i1] = game.getPlayerBoard().getSpellZone(i1).isOccupied();
        }
        for (int i1 = 0; i1 < 5; i1++) {
            rivalSpellsOccupied[i1] = game.getRivalBoard().getSpellZone(i1).isOccupied();
        }
        try {
            GameMenuController.activeEffect(game, game.getPlayerBoard().getSpellZone(i).getCard(), game.getPlayer(), GameMenuController.getSpeed(game.getPlayerBoard().getSpellZone(i).getCard().getFeatures()));
        } catch (GameException e) {
            if (e instanceof WinnerException) gameFinished((WinnerException)e);
            return;
        }
        Runnable runnable = () -> {
            for (int j = 0; j < 5; j++) {
                int x, y = 358;
                if (j == 0) x = 168 + 75 * 2;
                else if (j == 1) x = 168 + 75;
                else if (j == 2) x = 168 + 75 * 3;
                else if (j == 3) x = 168;
                else x = 168 + 75 * 4;
                if (!game.getPlayerBoard().getMonsterZone(j).isOccupied() && playerMonstersOccupied[j]) {
                    ImageView imageView = Menu.getImageWithSizeForGame(playerMonsters[j].getImage(), x, y);
                    MoveTransition moveTransition = new MoveTransition(23, 471, x, y, imageView, 1000, true);
                    pane.getChildren().add(imageView);
                    moveTransition.setOnFinished(actionEvent -> pane.getChildren().remove(imageView));
                    moveTransition.play();
                }
                y += 100;
                if (!game.getPlayerBoard().getSpellZone(j).isOccupied() && playerSpellsOccupied[j]) {
                    ImageView imageView = Menu.getImageWithSizeForGame(playerSpells[j].getImage(), x, y);
                    MoveTransition moveTransition = new MoveTransition(23, 471, x, y, imageView, 1000, true);
                    pane.getChildren().add(imageView);
                    moveTransition.setOnFinished(actionEvent -> pane.getChildren().remove(imageView));
                    moveTransition.play();
                }
                if (j == 0) x = 168 + 75 * 2;
                else if (j == 1) x = 168 + 75 * 3;
                else if (j == 2) x = 168 + 75;
                else if (j == 3) x = 168 + 4 * 75;
                else x = 168;
                y = 215;
                if (!game.getRivalBoard().getMonsterZone(j).isOccupied() && rivalMonstersOccupied[j]) {
                    ImageView imageView = Menu.getImageWithSizeForGame(rivalMonsters[j].getImage(), x, y);
                    MoveTransition moveTransition = new MoveTransition(23, 111, x, y, imageView, 1000, true);
                    pane.getChildren().add(imageView);
                    moveTransition.setOnFinished(actionEvent -> pane.getChildren().remove(imageView));
                    moveTransition.play();
                }
                y -= 100;
                if (!game.getRivalBoard().getSpellZone(j).isOccupied() && rivalSpellsOccupied[j]) {
                    ImageView imageView = Menu.getImageWithSizeForGame(rivalSpells[j].getImage(), x, y);
                    MoveTransition moveTransition = new MoveTransition(23, 471, x, y, imageView, 1000, true);
                    pane.getChildren().add(imageView);
                    moveTransition.setOnFinished(actionEvent -> pane.getChildren().remove(imageView));
                    moveTransition.play();
                }
            }
            removeOptions();
            resetImageEffects();
            updateWithoutTransition();
        };
        if (playerSpells[i].getImage().getUrl().contains("back")) {
            int x, y = 458;
            if (i == 0) x = 168 + 75 * 2;
            else if (i == 1) x = 168 + 75;
            else if (i == 2) x = 168 + 75 * 3;
            else if (i == 3) x = 168;
            else x = 168 + 75 * 4;
            ImageView imageView = Menu.getImageWithSizeForGame("back", x,  y);
            playerSpells[i].setImage(null);
            FlipTransition flipTransition = new FlipTransition(imageView, x, y, card.getCardName(), 1000);
            pane.getChildren().add(imageView);
            final int finalI = i;
            flipTransition.setOnFinished(actionEvent -> {
                pane.getChildren().remove(imageView);
                playerSpells[finalI].setImage(Menu.getCard(card.getCardName()));
                runnable.run();
            });
            flipTransition.play();
        } else {
            runnable.run();
        }
    }

    public void flipSummon(MouseEvent mouseEvent) {
        int i = -1;
        for (int i1 = 0; i1 < playerMonsters.length; i1++) {
            if (playerMonsters[i1].getEffect() != null) {
                i = i1;
                break;
            }
        }
        if (i == -1) return;
        GameMenuResponse gameMenuResponse;
        try {
            gameMenuResponse = GameMenuController.flipSummon(game, i + 1, false);
        } catch (GameException gameException) {
            if (gameException instanceof WinnerException) gameFinished((WinnerException) gameException);
            return;
        }
        if (gameMenuResponse.getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL) {
            int x, y = 358;
            if (i == 0) x = 168 + 75 * 2;
            else if (i == 1) x = 168 + 75;
            else if (i == 2) x = 168 + 75 * 3;
            else if (i == 3) x = 168;
            else x = 168 + 75 * 4;
            playerMonsters[i].setImage(null);
            ImageView imageView = Menu.getImageWithSizeForGame("back", x, y);
            pane.getChildren().add(imageView);
            String cardName = game.getPlayerBoard().getMonsterZone(i).getCard().getCardName();
            FlipTransition flipTransition = new FlipTransition(imageView, x, y, cardName, 1000);
            final int iFinal = i;
            flipTransition.setOnFinished(actionEvent -> {
                pane.getChildren().remove(imageView);
                playerMonsters[iFinal].setImage(Menu.getCard(cardName));
            });
            flipTransition.play();
        } else {
            response(gameMenuResponse);
        }
    }

    public void showCard(MouseEvent mouseEvent) {
        Menu.playMedia(VoiceEffects.CARD_FLIP);
        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Paint.valueOf("WHITE"));
        rectangle.setOpacity(0.4);
        rectangle.setHeight(700);
        rectangle.setWidth(700);
        rectangle.setX(0);
        rectangle.setY(0);
        if (playerSpellIsSelected() || playerMonsterIsSelected()) {
            if (getSelectedImageView().getImage().getUrl().contains("back")) {
                Card card;
                int i = -1;
                for (int i1 = 0; i1 < playerMonsters.length; i1++) {
                    if (playerMonsters[i1].getEffect() != null) i = i1;
                }
                if (i != -1) card = game.getPlayerBoard().getMonsterZone(i).getCard();
                else {
                    for (int i1 = 0; i1 < playerSpells.length; i1++) {
                        if (playerSpells[i1].getEffect() != null) i = i1;
                    }
                    card = game.getPlayerBoard().getSpellZone(i).getCard();
                }
                ImageView imageView = new ImageView(Menu.getCard(card.getCardName()));
                imageView.setX(280);
                imageView.setY(200);
                imageView.setFitWidth(140);
                imageView.setFitHeight(200);
                pane.getChildren().addAll(rectangle, imageView);
                EventHandler<MouseEvent> eventHandler = mouseEvent1 -> pane.getChildren().removeAll(imageView, rectangle);
                rectangle.setOnMouseClicked(eventHandler);
                imageView.setOnMouseClicked(eventHandler);
                return;
            }
        }
        ImageView imageView = new ImageView(getSelectedImageView().getImage());
        imageView.setX(280);
        imageView.setY(200);
        imageView.setFitWidth(140);
        imageView.setFitHeight(200);
        pane.getChildren().addAll(rectangle, imageView);
        EventHandler<MouseEvent> eventHandler = mouseEvent1 -> pane.getChildren().removeAll(imageView, rectangle);
        rectangle.setOnMouseClicked(eventHandler);
        imageView.setOnMouseClicked(eventHandler);
    }

    public ImageView getSelectedImageView() {
        for (ImageView playerMonster : playerMonsters) {
            if (playerMonster.getEffect() != null) return playerMonster;
        }
        for (ImageView playerSpell : playerSpells) {
            if (playerSpell.getEffect() != null) return playerSpell;
        }
        for (ImageView rivalMonster : rivalMonsters) {
            if (rivalMonster.getEffect() != null) return rivalMonster;
        }
        for (ImageView rivalSpell : rivalSpells) {
            if (rivalSpell.getEffect() != null) return rivalSpell;
        }
        for (Node child : playerCards.getChildren()) {
            if (child.getEffect() != null) return (ImageView) child;
        }
        if (rivalFieldSpell.getEffect() != null) return rivalFieldSpell;
        if (rivalGraveYard.getEffect() != null) return rivalGraveYard;
        if (playerFieldSpell.getEffect() != null) return playerFieldSpell;
        return playerGraveYard;
    }

    public void showGraveYard(MouseEvent mouseEvent) {
        if (playerGraveYard.getEffect() != null) {
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Paint.valueOf("WHITE"));
            rectangle.setOpacity(0.36);
            rectangle.setHeight(700);
            rectangle.setWidth(700);
            rectangle.setX(0);
            rectangle.setY(0);
            if (game.getPlayerBoard().getGraveyard().getCards().size() == 0) {
                ImageView imageView = new ImageView(getSelectedImageView().getImage());
                imageView.setX(280);
                imageView.setY(200);
                imageView.setFitWidth(140);
                imageView.setFitHeight(200);
                pane.getChildren().addAll(rectangle, imageView);
                EventHandler<MouseEvent> eventHandler = mouseEvent1 -> pane.getChildren().removeAll(imageView, rectangle);
                rectangle.setOnMouseClicked(eventHandler);
                imageView.setOnMouseClicked(eventHandler);
            } else {
                Label label = new Label("1/" + game.getPlayerBoard().getGraveyard().getCards().size());
                label.setStyle("-fx-text-fill: green; -fx-font-size: 20px; -fx-background-color: rgba(255,255,255,0.44)");
                label.setLayoutX(340);
                label.setLayoutY(130);
                ImageView imageView = new ImageView(Menu.getCard(GameMenuController.trimName(game.getPlayerBoard().getGraveyard().getCards().get(0).getCardName())));
                imageView.setX(280);
                imageView.setY(200);
                imageView.setFitWidth(140);
                imageView.setFitHeight(200);
                pane.getChildren().addAll(rectangle, imageView, label);
                EventHandler<MouseEvent> eventHandler = mouseEvent1 -> pane.getChildren().removeAll(imageView, rectangle, label);
                rectangle.setOnMouseClicked(eventHandler);
                imageView.setOnMouseClicked(event -> {
                    imageView.setImage(Menu.getCard(game.getPlayerBoard().getGraveyard().getCards().get(Integer.parseInt(label.getText().split("/")[0]) % game.getPlayerBoard().getGraveyard().getCards().size()).getCardName()));
                    label.setText(((Integer.parseInt(label.getText().split("/")[0]) % game.getPlayerBoard().getGraveyard().getCards().size()) + 1) + "/" + game.getPlayerBoard().getGraveyard().getCards().size());
                });
            }
        } else if (rivalGraveYard.getEffect() != null) {
            Rectangle rectangle = new Rectangle();
            rectangle.setFill(Paint.valueOf("WHITE"));
            rectangle.setOpacity(0.36);
            rectangle.setHeight(700);
            rectangle.setWidth(700);
            rectangle.setX(0);
            rectangle.setY(0);
            if (game.getRivalBoard().getGraveyard().getCards().size() == 0) {
                ImageView imageView = new ImageView(getSelectedImageView().getImage());
                imageView.setX(280);
                imageView.setY(200);
                imageView.setFitWidth(140);
                imageView.setFitHeight(200);
                pane.getChildren().addAll(rectangle, imageView);
                EventHandler<MouseEvent> eventHandler = mouseEvent1 -> pane.getChildren().removeAll(imageView, rectangle);
                rectangle.setOnMouseClicked(eventHandler);
                imageView.setOnMouseClicked(eventHandler);
            } else {
                Label label = new Label("1/" + game.getRivalBoard().getGraveyard().getCards().size());
                label.setStyle("-fx-text-fill: green; -fx-font-size: 20px; -fx-background-color: rgba(255,255,255,0.44)");
                label.setLayoutX(340);
                label.setLayoutY(130);
                ImageView imageView = new ImageView(Menu.getCard(GameMenuController.trimName(game.getRivalBoard().getGraveyard().getCards().get(0).getCardName())));
                imageView.setX(280);
                imageView.setY(200);
                imageView.setFitWidth(140);
                imageView.setFitHeight(200);
                pane.getChildren().addAll(rectangle, imageView, label);
                EventHandler<MouseEvent> eventHandler = mouseEvent1 -> pane.getChildren().removeAll(imageView, rectangle, label);
                rectangle.setOnMouseClicked(eventHandler);
                imageView.setOnMouseClicked(event -> {
                    imageView.setImage(Menu.getCard(GameMenuController.trimName(game.getRivalBoard().getGraveyard().getCards().get(Integer.parseInt(label.getText().split("/")[0]) % game.getRivalBoard().getGraveyard().getCards().size()).getCardName())));
                    label.setText(((Integer.parseInt(label.getText().split("/")[0]) % game.getRivalBoard().getGraveyard().getCards().size()) + 1) + "/" + game.getRivalBoard().getGraveyard().getCards().size());
                });
            }
        }
    }

    public void setAttackPosition(MouseEvent mouseEvent) {
        int i = -1;
        for (int i1 = 0; i1 < playerMonsters.length; i1++) {
            if (playerMonsters[i1].getEffect() != null) i = i1;
        }
        if (i == -1) return;
        if (game.getPlayerBoard().getMonsterZone(i).getState() != State.FACE_UP_DEFENCE) return;
        GameMenuResponse gameMenuResponse;
        gameMenuResponse = GameMenuController.setMonsterPosition(game, i + 1, "attack");
        if (gameMenuResponse.getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL) {
            playerMonsters[i].setRotate(0);
        }
    }

    public void setDefensePosition(MouseEvent mouseEvent) {
        int i = -1;
        for (int i1 = 0; i1 < playerMonsters.length; i1++) {
            if (playerMonsters[i1].getEffect() != null) i = i1;
        }
        if (i == -1) return;
        if (game.getPlayerBoard().getMonsterZone(i).getState() != State.FACE_UP_ATTACK) return;
        GameMenuResponse gameMenuResponse;
        gameMenuResponse = GameMenuController.setMonsterPosition(game, i + 1, "defense");
        if (gameMenuResponse.getGameMenuResponseEnum() == GameMenuResponsesEnum.SUCCESSFUL) {
            playerMonsters[i].setRotate(90);

        }
    }

    public void surrender(MouseEvent mouseEvent) {
        gameFinished(new WinnerException(game.getRival(), game.getPlayer(), game.getRivalLP(), game.getPlayerLP()));
    }

    public void gameFinished(WinnerException winnerException) {
        if (WinnerExceptionHolder.gameMode == WinnerExceptionHolder.GameMode.ONE_ROUND) {
            ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("abbas");
            pane.getChildren().add(nodes.get(0));
            Label label = new Label("Player " + winnerException.getWinner().getNickname() + " Won with score 1000");
            label.setStyle("-fx-background-color: transparent;-fx-font: 22px Chalkboard;-fx-text-fill: red;");
            label.setWrapText(true);
            label.setLayoutX(225);
            label.setLayoutY(225);
            pane.getChildren().add(label);
            GameMenuController.cashOut(winnerException.getWinnerLP(), false, winnerException.getWinner(), winnerException.getLoser());
            ReadAndWriteDataBase.updateUser(game.getPlayer());
            ReadAndWriteDataBase.updateUser(game.getRival());
            nodes.get(0).setOnMouseClicked(mouseEvent -> Menu.goToMenu("Duel"));
        } else if (WinnerExceptionHolder.gameMode == WinnerExceptionHolder.GameMode.THREE_ROUND) {
            WinnerException winnerException1 = WinnerExceptionHolder.winnerException1;
            WinnerException winnerException2 = WinnerExceptionHolder.winnerException2;
            WinnerException winnerException3 = WinnerExceptionHolder.winnerException3;
            if (winnerException1 == null) {
                WinnerExceptionHolder.winnerException1 = winnerException;
                ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("abbas");
                pane.getChildren().add(nodes.get(0));
                Label label = new Label("This game has ended!");
                label.setStyle("-fx-background-color: transparent;-fx-font: 22px Chalkboard;-fx-text-fill: red;");
                label.setWrapText(true);
                label.setLayoutX(225);
                label.setLayoutY(225);
                pane.getChildren().add(label);
                nodes.get(0).setOnMouseClicked(mouseEvent -> new ChangeBetweenThreeRounds(game.getPlayer()));
            } else if (winnerException2 == null) {
                if (winnerException1.getWinner() == winnerException.getWinner()) {
                    ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("abbas");
                    pane.getChildren().add(nodes.get(0));
                    Label label = new Label("Player " + winnerException.getWinner().getNickname() + " Won with score 3000");
                    label.setStyle("-fx-background-color: transparent;-fx-font: 22px Chalkboard;-fx-text-fill: red;");
                    label.setWrapText(true);
                    label.setLayoutX(225);
                    label.setLayoutY(225);
                    pane.getChildren().add(label);
                    GameMenuController.cashOut(Math.max(winnerException.getWinnerLP(), winnerException1.getWinnerLP()), true, winnerException.getWinner(), winnerException.getLoser());
                    ReadAndWriteDataBase.updateUser(game.getPlayer());
                    ReadAndWriteDataBase.updateUser(game.getRival());
                    nodes.get(0).setOnMouseClicked(mouseEvent -> Menu.goToMenu("Duel"));
                } else {
                    WinnerExceptionHolder.winnerException2 = winnerException;
                    ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("abbas");
                    pane.getChildren().add(nodes.get(0));
                    Label label = new Label("This game has ended!");
                    label.setStyle("-fx-background-color: transparent;-fx-font: 22px Chalkboard;-fx-text-fill: red;");
                    label.setWrapText(true);
                    label.setLayoutX(225);
                    label.setLayoutY(225);
                    pane.getChildren().add(label);
                    nodes.get(0).setOnMouseClicked(mouseEvent -> new ChangeBetweenThreeRounds(game.getPlayer()));
                }
            } else if (winnerException3 == null) {
                ArrayList<Node> nodes = Menu.getRectangleAndButtonForGameMenus("abbas");
                pane.getChildren().add(nodes.get(0));
                Label label = new Label("Player " + winnerException.getWinner().getNickname() + " Won with score 3000");
                label.setStyle("-fx-background-color: transparent;-fx-font: 22px Chalkboard;-fx-text-fill: red;");
                label.setWrapText(true);
                label.setLayoutX(225);
                label.setLayoutY(225);
                pane.getChildren().add(label);
                int maxLp = winnerException1.getWinnerLP();
                maxLp = Math.max(winnerException2.getWinnerLP(), maxLp);
                maxLp = Math.max(winnerException.getWinnerLP(), maxLp);
                GameMenuController.cashOut(maxLp, true, winnerException.getWinner(), winnerException.getLoser());
                ReadAndWriteDataBase.updateUser(game.getPlayer());
                ReadAndWriteDataBase.updateUser(game.getRival());
                nodes.get(0).setOnMouseClicked(mouseEvent -> Menu.goToMenu("Duel"));
            }
        }
    }

    public void adjustButton(Button button) {
        String notClicked = "-fx-max-width: 141;-fx-min-width: 141;-fx-max-height: 40;-fx-min-height: 40;-fx-background-color: transparent;-fx-font: 15px Chalkboard;";
        String clicked = "-fx-max-width: 141;-fx-min-width: 141;-fx-max-height: 40;-fx-min-height: 40;-fx-background-color: rgba(78,80,190,0.53);-fx-font: 15px Chalkboard;";
        button.setStyle(notClicked);
        button.setOnMouseEntered(mouseEvent -> {
            button.setStyle(clicked);
            Menu.playMedia(VoiceEffects.CLICK);
        });
        button.setOnMouseExited(mouseEvent -> button.setStyle(notClicked));
    }

    private void resetImageEffects() {
        for (ImageView playerMonster : playerMonsters) {
            playerMonster.setEffect(null);
        }
        for (ImageView playerSpell : playerSpells) {
            playerSpell.setEffect(null);
        }
        for (ImageView rivalMonster : rivalMonsters) {
            rivalMonster.setEffect(null);
        }
        for (ImageView rivalSpell : rivalSpells) {
            rivalSpell.setEffect(null);
        }
        for (Node child : playerCards.getChildren()) {
            child.setEffect(null);
        }
        rivalFieldSpell.setEffect(null);
        rivalGraveYard.setEffect(null);
        playerFieldSpell.setEffect(null);
        playerGraveYard.setEffect(null);
    }

    public void updateWithoutTransition() {
        for (int i = rivalCards.getChildren().size() - 1; i >= 0; i--) {
            rivalCards.getChildren().remove(i);
        }
        for (int i = playerCards.getChildren().size() - 1; i >= 0; i--) {
            playerCards.getChildren().remove(i);
        }
        loadNames();
        ArrayList<Card> cards = game.getPlayerHandCards();
        for (Card card : cards) {
            ImageView imageView;
            playerCards.getChildren().add(imageView = Menu.getImageWithSizeForGame(card.getCardName(), 0, 0));
            imageView.setOnMouseClicked(mouseEvent -> {
                resetImageEffects();
                if (((ImageView) mouseEvent.getSource()).getImage() != null)
                    ((ImageView) mouseEvent.getSource()).setEffect(new DropShadow(40, Color.RED));
                updateButtons();
            });
            HBox.setMargin(imageView, new Insets(0, 0, 0, 9));
            addDragForHandCard(imageView);
        }
        cards = game.getRivalHandCards();
        for (Card card : cards) {
            ImageView imageView;
            rivalCards.getChildren().add(imageView = Menu.getImageWithSizeForGame("back", 0, 0));
            HBox.setMargin(imageView, new Insets(0, 0, 0, 9));
        }
        Cell[] cells = game.getPlayerBoard().getMonsterZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].isOccupied()) {
                State cellState = cells[i].getState();
                if (cellState == State.FACE_UP_ATTACK) {
                    playerMonsters[i].setImage(Menu.getCard(cells[i].getCard().getCardName()));
                    playerMonsters[i].setRotate(0);
                } else if (cellState == State.FACE_UP_DEFENCE) {
                    playerMonsters[i].setImage(Menu.getCard(cells[i].getCard().getCardName()));
                    playerMonsters[i].setRotate(90);
                } else if (cellState == State.FACE_DOWN_DEFENCE) {
                    playerMonsters[i].setImage(Menu.getCard("back"));
                    playerMonsters[i].setRotate(0);
                }
            } else {
                playerMonsters[i].setImage(null);
            }
        }
        cells = game.getPlayerBoard().getSpellZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].isOccupied()) {
                State cellState = cells[i].getState();
                if (cellState == State.FACE_UP_SPELL) {
                    playerSpells[i].setImage(Menu.getCard(cells[i].getCard().getCardName()));
                } else if (cellState == State.FACE_DOWN_SPELL) {
                    playerSpells[i].setImage(Menu.getCard("back"));
                }
            } else {
                playerSpells[i].setImage(null);
            }
        }
        cells = game.getRivalBoard().getMonsterZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].isOccupied()) {
                State cellState = cells[i].getState();
                if (cellState == State.FACE_UP_ATTACK) {
                    rivalMonsters[i].setImage(Menu.getCard(cells[i].getCard().getCardName()));
                    rivalMonsters[i].setRotate(0);
                } else if (cellState == State.FACE_UP_DEFENCE) {
                    rivalMonsters[i].setImage(Menu.getCard(cells[i].getCard().getCardName()));
                    rivalMonsters[i].setRotate(90);
                } else if (cellState == State.FACE_DOWN_DEFENCE) {
                    rivalMonsters[i].setImage(Menu.getCard("back"));
                    rivalMonsters[i].setRotate(0);
                }
            } else {
                rivalMonsters[i].setImage(null);
            }
        }
        cells = game.getRivalBoard().getSpellZone();
        for (int i = 0; i < 5; i++) {
            if (cells[i].isOccupied()) {
                State cellState = cells[i].getState();
                if (cellState == State.FACE_UP_SPELL) {
                    rivalSpells[i].setImage(Menu.getCard(cells[i].getCard().getCardName()));
                } else if (cellState == State.FACE_DOWN_SPELL) {
                    rivalSpells[i].setImage(Menu.getCard("back"));
                }
            } else {
                rivalSpells[i].setImage(null);
            }
        }
        if (game.getPlayerBoard().getFieldZone().isOccupied())
            playerFieldSpell.setImage(Menu.getCard(game.getPlayerBoard().getFieldZone().getCard().getCardName()));
        else playerFieldSpell.setImage(Menu.getCard("back"));
        if (game.getRivalBoard().getFieldZone().isOccupied())
            rivalFieldSpell.setImage(Menu.getCard(game.getRivalBoard().getFieldZone().getCard().getCardName()));
        else rivalFieldSpell.setImage(Menu.getCard("back"));
        if (game.getPlayerBoard().getGraveyard().getCards().size() == 0)
            playerGraveYard.setImage(Menu.getCard("back"));
        else
            playerGraveYard.setImage(Menu.getCard(game.getPlayerBoard().getGraveyard().getCards().get(game.getPlayerBoard().getGraveyard().getCards().size() - 1).getCardName()));
        if (game.getRivalBoard().getGraveyard().getCards().size() == 0)
            rivalGraveYard.setImage(Menu.getCard("back"));
        else
            rivalGraveYard.setImage(Menu.getCard(game.getRivalBoard().getGraveyard().getCards().get(game.getRivalBoard().getGraveyard().getCards().size() - 1).getCardName()));
        updatePhase();
        updateBackGroundForField();
    }

    private void addDragForHandCard(ImageView imageView) {
        imageView.setOnDragDetected(mouseEvent -> {
            int j = -1;
            for (int i = 0; i < playerCards.getChildren().size(); i++) {
                if (playerCards.getChildren().get(i) == imageView) j = i;
            }
            if (j == -1) return;
            Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putImage(imageView.getImage());
            db.setContent(content);
            mouseEvent.consume();
        });
    }

    public void response(GameMenuResponse gameMenuResponse) {
        GameMenuResponsesEnum response = gameMenuResponse.getGameMenuResponseEnum();
        String str;
        if (response == GameMenuResponsesEnum.INVALID_SELECTION)
            str = "Invalid Selection!";
        else if (response == GameMenuResponsesEnum.NO_CARD_FOUND)
            str = "No card found!";
        else if (response == GameMenuResponsesEnum.CARD_IS_HIDDEN)
            str = "Card Is hidden!";
        else if (response == GameMenuResponsesEnum.SPELL_AND_TRAP_ZONE_IS_FULL)
            str = "Spell zone is full!";
        else if (response == GameMenuResponsesEnum.MONSTER_ZONE_IS_FULL)
            str = "Monster Zone is full!";
        else if (response == GameMenuResponsesEnum.NOT_ENOUGH_MONSTERS)
            str = "Not enough monsters";
        else if (response == GameMenuResponsesEnum.CANT_NORMAL_SUMMON)
            str = "Cant normal summon!";
        else if (response == GameMenuResponsesEnum.CANT_ATTACK)
            str = "Cant attack!";
        else if (response == GameMenuResponsesEnum.ALREADY_SUMMONED)
            str = "Already summoned!";
        else if (response == GameMenuResponsesEnum.YOU_HAVENT_SUMMONED_YET)
            str = "You haven't summoned Yed!";
        else if (response == GameMenuResponsesEnum.ALREADY_IN_THIS_POSITION)
            str = "Already in this position!";
        else if (response == GameMenuResponsesEnum.CANT_FLIP_SUMMON)
            str = "Cant Flip Summon!";
        else if (response == GameMenuResponsesEnum.ALREADY_ATTACKED)
            str = "Already Attacked!";
        else if (response == GameMenuResponsesEnum.PLEASE_SELECT_MONSTER)
            str = "Please Select Monster!";
        else if (response == GameMenuResponsesEnum.PLEASE_SELECT_SPELL_OR_TRAP)
            str = "Please Select spell or trap!";
        else if (response == GameMenuResponsesEnum.CANT_RITUAL_SUMMON)
            str = "Cant ritual summon!";
        else if (response == GameMenuResponsesEnum.SELECTED_LEVELS_DONT_MATCH)
            str = "Selected levels don't match!";
        else if (response == GameMenuResponsesEnum.SELECTED_MONSTER_IS_NOT_RITUAL)
            str = "Select monster is not ritual!";
        else if (response == GameMenuResponsesEnum.NO_RITUAL_SPELL_IN_SPELLZONE)
            str = "No ritual spell in spellzone!";
        else if (response == GameMenuResponsesEnum.PLAYER_HAND_IS_FULL)
            str = "Player hand is full!";
        else if (response == GameMenuResponsesEnum.NO_CARDS_IN_MAIN_DECK)
            str = "No cards in main deck!";
        else if (response == GameMenuResponsesEnum.ABORTED)
            str = "Aborted!";
        else if (response == GameMenuResponsesEnum.CANT_SPECIAL_SUMMON)
            str = "Cant special Summon!";
        else if (response == GameMenuResponsesEnum.ALREADY_CHANGED)
            str = "Already changed!";
        else if (response == GameMenuResponsesEnum.CANT_CHANGE)
            str = "Can't change right now!";
        else return;
        Menu.showAlert(str);
    }

    private class MoveTransition extends Transition {
        final double xDes, yDes, xStart, yStart;
        final ImageView imageView;

        public MoveTransition(double xDes, double yDes, double xStart, double yStart, ImageView imageView, int time, boolean destoryed) {
            this.xDes = xDes;
            this.yDes = yDes;
            this.xStart = xStart;
            this.yStart = yStart;
            this.imageView = imageView;
            this.setInterpolator(Interpolator.EASE_OUT);
            this.setCycleDuration(Duration.millis(time));
            this.setCycleCount(1);
            if (destoryed) Menu.playMedia(VoiceEffects.EXPLODE);
            else Menu.playMedia(VoiceEffects.CARD_FLIP);
        }

        @Override
        protected void interpolate(double v) {
            imageView.setX(xStart + (xDes - xStart) * v);
            imageView.setY(yStart + (yDes - yStart) * v);
        }
    }

    private class FlipTransition extends Transition {
        private final ImageView imageView;
        private final int x, y;
        private final String imageName;
        private boolean isReversed = false;

        public FlipTransition(ImageView imageView, int x, int y, String imageName, int time) {
            this.imageView = imageView;
            this.x = x;
            this.y = y;
            this.imageName = imageName;
            this.setCycleDuration(Duration.millis(time));
            this.setCycleCount(1);
            this.setInterpolator(Interpolator.EASE_OUT);
            Menu.playMedia(VoiceEffects.CARD_FLIP);
        }

        @Override
        protected void interpolate(double v) {
            if (v > 0.5) {
                if (!isReversed) {
                    imageView.setImage(Menu.getCard(imageName));
                    isReversed = true;
                }
                imageView.setFitWidth(70 * (2 * v - 1));
                imageView.setX(x + 70 * (1 - v));
            } else {
                imageView.setFitWidth(70 * (1 - 2 * v));
                imageView.setX(x + 70 * v);
            }
        }
    }
}