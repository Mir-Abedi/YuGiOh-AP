package model.exceptions;

public class StopSpell extends GameException {
    final StopEffectState state;
    public StopSpell (StopEffectState state) {
        this.state = state;
    }
    public StopEffectState getState() {
        return state;
    }
}
