package model.exceptions;

public class StopAttackException extends GameException {
    final StopEffectState state;

    public StopAttackException(StopEffectState state) {
        this.state = state;
    }

    public StopEffectState getState() {
        return state;
    }
}
