package pkgData;

import java.io.Serializable;

public class PlayerStatistics implements Serializable {
    private Integer numWins,
            numDefeats,
            numDraws,
            numGoalsShot,
            numGoalsShotHead,
            numGoalsShotHeadSnow,
            numGoalsShotPenalty,
            numGoalsGot,
            numNutmeg,
            numGamesPlayed,
            numPosAttack,
            numPosDefense,
            numPosMidfield,
            numPosGoal;
    private Float avgGoalDifference;

    public PlayerStatistics() {
        super();
    }

    public Float getAvgGoalDifference() {
        return avgGoalDifference;
    }

    public int getNumWins() {
        return numWins;
    }

    public void setNumWins(int numWins) {
        this.numWins = numWins;
    }

    public int getNumDefeats() {
        return numDefeats;
    }

    public void setNumDefeats(int numDefeats) {
        this.numDefeats = numDefeats;
    }

    public int getNumDraws() {
        return numDraws;
    }

    public void setNumDraws(int numDraws) {
        this.numDraws = numDraws;
    }

    public int getNumGoalsShotAll() {
        return numGoalsShot +
                numGoalsShotHead +
                numGoalsShotHeadSnow +
                numGoalsShotPenalty;
    }

    public int getNumGoalsShotDefault() {
        return numGoalsShot;
    }

    public void setNumGoalsShotDefault(int numGoalsShot) {
        this.numGoalsShot = numGoalsShot;
    }

    public int getNumGoalsGot() {
        return numGoalsGot;
    }

    public void setNumGoalsGot(int numGoalsGot) {
        this.numGoalsGot = numGoalsGot;
    }

    public int getNumGoalsShotHead() {
        return numGoalsShotHead;
    }

    public void setNumGoalsShotHead(int numGoalsShotHead) {
        this.numGoalsShotHead = numGoalsShotHead;
    }

    public int getNumGoalsShotHeadSnow() {
        return numGoalsShotHeadSnow;
    }

    public void setNumGoalsShotHeadSnow(int numGoalsShotHeadSnow) {
        this.numGoalsShotHeadSnow = numGoalsShotHeadSnow;
    }

    public int getNumGoalsShotPenalty() {
        return numGoalsShotPenalty;
    }

    public void setNumGoalsShotPenalty(int numGoalsShotPenalty) {
        this.numGoalsShotPenalty = numGoalsShotPenalty;
    }

    public int getNumNutmeg() {
        return numNutmeg;
    }

    public void setNumNutmeg(int numNutmeg) {
        this.numNutmeg = numNutmeg;
    }

    public int getNumGamesPlayed() {
        return numGamesPlayed;
    }

    public void setNumGamesPlayed(int numGamesPlayed) {
        this.numGamesPlayed = numGamesPlayed;
    }

    public int getNumPosAttack() {
        return numPosAttack;
    }

    public void setNumPosAttack(int numAttack) {
        this.numPosAttack = numAttack;
    }

    public int getNumPosDefense() {
        return numPosDefense;
    }

    public void setNumPosDefense(int numDefense) {
        this.numPosDefense = numDefense;
    }

    public int getNumPosMidfield() {
        return numPosMidfield;
    }

    public void setNumPosMidfield(int numMidfield) {
        this.numPosMidfield = numMidfield;
    }

    public int getNumPosGoal() {
        return numPosGoal;
    }

    public void setNumPosGoal(int numGoal) {
        this.numPosGoal = numGoal;
    }

    public float getGoalDifference() {
        return avgGoalDifference;
    }

    public void setGoalDifference(float goalRatio) {
        this.avgGoalDifference = goalRatio;
    }
}
