package view.responses;

public class GameMenuResponse {
    private final Object obj;
    private final GameMenuResponsesEnum gameMenuResponseEnum;

    public GameMenuResponse(Object obj, GameMenuResponsesEnum gameMenuResponseEnum) {
        this.obj = obj;
        this.gameMenuResponseEnum = gameMenuResponseEnum;
    }

    public GameMenuResponse(GameMenuResponsesEnum gameMenuResponseEnum){
        this.gameMenuResponseEnum = gameMenuResponseEnum;
        obj = null;
    }

    public Object getObj() {
        return obj;
    }

    public GameMenuResponsesEnum getGameMenuResponseEnum() {
        return gameMenuResponseEnum;
    }
}
