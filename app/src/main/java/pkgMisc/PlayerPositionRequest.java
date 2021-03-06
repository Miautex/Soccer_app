package pkgMisc;

public class PlayerPositionRequest {
    private boolean GOAL, MIDFIELD, ATTACK, DEFENSE;

    public PlayerPositionRequest() {
        super();
    }

    public PlayerPositionRequest(boolean isGoal, boolean isMidfield, boolean isAttack, boolean isDefense) {
        setGOAL(isGoal);
        setMIDFIELD(isMidfield);
        setATTACK(isAttack);
        setDEFENSE(isDefense);
    }

    public boolean isGOAL() {
        return GOAL;
    }

    public void setGOAL(boolean GOAL) {
        this.GOAL = GOAL;
    }

    public boolean isMIDFIELD() {
        return MIDFIELD;
    }

    public void setMIDFIELD(boolean MIDFIELD) {
        this.MIDFIELD = MIDFIELD;
    }

    public boolean isATTACK() {
        return ATTACK;
    }

    public void setATTACK(boolean ATTACK) {
        this.ATTACK = ATTACK;
    }

    public boolean isDEFENSE() {
        return DEFENSE;
    }

    public void setDEFENSE(boolean DEFENSE) {
        this.DEFENSE = DEFENSE;
    }
}
