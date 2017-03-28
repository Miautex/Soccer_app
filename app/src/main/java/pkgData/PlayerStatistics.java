package pkgData;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class PlayerStatistics implements Comparable, Serializable {
    private int numGoalsGot, numGoalsShotDefault, numGoalsShotHead, numGoalsShotHeadSnow, numGoalsShotPenalty, numNutmeg;
    private Player player;

    public PlayerStatistics(Player player, int numGoalsGot, int numGoalsShotDefault, int numGoalsShotHead,
                            int numGoalsShotHeadSnow, int numGoalsShotPenalty, int numNutmeg) {
        setPlayer(player);
        setNumGoalsGot(numGoalsGot);
        setNumGoalsShotDefault(numGoalsShotDefault);
        setNumGoalsShotHead(numGoalsShotHead);
        setNumGoalsShotHeadSnow(numGoalsShotHeadSnow);
        setNumGoalsShotPenalty(numGoalsShotPenalty);
        setNumNutmeg(numNutmeg);

    }

    public int getNumGoalsGot() {
        return numGoalsGot;
    }

    public void setNumGoalsGot(int numGoalsGot) throws IllegalArgumentException {
        if (numGoalsGot < 0) {
            throw new IllegalArgumentException("numGoalsGot must not be negative");
        }
        this.numGoalsGot = numGoalsGot;
    }

    public int getNumGoalsShotDefault() {
        return numGoalsShotDefault;
    }

    public void setNumGoalsShotDefault(int numGoalsShotDefault) throws IllegalArgumentException {
        if (numGoalsShotDefault < 0) {
            throw new IllegalArgumentException("numGoalsShotDefault must not be negative");
        }
        this.numGoalsShotDefault = numGoalsShotDefault;
    }

    public int getNumGoalsShotHead() {
        return numGoalsShotHead;
    }

    public void setNumGoalsShotHead(int numGoalsShotHead) throws IllegalArgumentException {
        if (numGoalsShotHead < 0) {
            throw new IllegalArgumentException("numGoalsShotHead must not be negative");
        }
        this.numGoalsShotHead = numGoalsShotHead;
    }

    public int getNumGoalsShotHeadSnow() {
        return numGoalsShotHeadSnow;
    }

    public void setNumGoalsShotHeadSnow(int numGoalsShotHeadSnow) throws IllegalArgumentException {
        if (numGoalsShotHeadSnow < 0) {
            throw new IllegalArgumentException("numGoalsShotHeadSnow must not be negative");
        }
        this.numGoalsShotHeadSnow = numGoalsShotHeadSnow;
    }

    public int getNumGoalsShotPenalty() {
        return numGoalsShotPenalty;
    }

    public void setNumGoalsShotPenalty(int numGoalsShotPenalty) throws IllegalArgumentException {
        if (numGoalsShotPenalty < 0) {
            throw new IllegalArgumentException("numGoalsShotPenalty must not be negative");
        }
        this.numGoalsShotPenalty = numGoalsShotPenalty;
    }

    public int getNumNutmeg() {
        return numNutmeg;
    }

    public void setNumNutmeg(int numNutmeg) throws IllegalArgumentException {
        if (numNutmeg < 0) {
            throw new IllegalArgumentException("numNutmeg must not be negative");
        }
        this.numNutmeg = numNutmeg;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) throws NullPointerException {
        if (player == null) {
            throw new NullPointerException("player may not be null");
        }
        this.player = player;
    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
    }

    @Override
    public String toString() {
        return getPlayer().getName() + ": GoalsGot=" + getNumGoalsGot() + ", GoalsShotDefault=" + getNumGoalsShotDefault() +
                ", GoalsShotHead=" + getNumGoalsShotHead() + ", GoalsShotHeadSnow=" + getNumGoalsShotHeadSnow() +
                ", GoalsShotPenalty=" + getNumGoalsShotPenalty() + ", Nutmeg=" + getNumNutmeg();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerStatistics that = (PlayerStatistics) o;

        return player.equals(that.player);

    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    @Override
    public int compareTo (@NonNull Object o) {
        int cp;
        PlayerStatistics tmpP = (PlayerStatistics) o;

        cp = getPlayer().compareTo(tmpP.getPlayer());

        return cp;
    }
}
