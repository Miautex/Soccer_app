package pkgData;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public final class Game implements Serializable, Comparable<Game> {

    private int id, scoreTeamA, scoreTeamB;
    private Date date;
    private String remark;
    private HashSet<Participation> participations;

    private Game () {
        id = -1;
        scoreTeamA = -1;
        scoreTeamB = -1;
        date = null;
        participations = new HashSet<>();
    }

    public Game (int id, Date date, int scoreA, int scoreB) {
        this();
        setId(id);
        setDate(date);
        setScoreTeamA(scoreA);
        setScoreTeamB(scoreB);
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String additionalInformation) {
        this.remark = additionalInformation;
    }

    private void setId (int id) {
        this.id = id;
    }

    public int getId () {
        return id;
    }

    private void setScoreTeamA (int scoreTeamA) {
        this.scoreTeamA = scoreTeamA;
    }

    public int getScoreTeamA () {
        return scoreTeamA;
    }

    private void setScoreTeamB (int scoreTeamB) {
        this.scoreTeamB = scoreTeamB;
    }

    public int getScoreTeamB () {
        return scoreTeamB;
    }

    private void setDate (Date date) {
        this.date = date;
    }

    public Date getDate () {
        return date;
    }

    public ArrayList<Participation> getParticipations() {
        return new ArrayList<>(participations);
    }

    public void addParticipation(Participation p) {
        participations.add(p);
    }

    public void removeParticipation(Participation p) {
        participations.remove(p);
    }

    @Override
    public int hashCode () {
        return id;
    }

    @Override
    public boolean equals (Object obj) {
        boolean eq = false;
        Game tmpG;
        if (obj != null) {
            if (obj instanceof Game) {
                tmpG = (Game)obj;
                eq = this.getId() == tmpG.getId();
            }
        }
        return eq;
    }

    @Override
    public String toString () {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd.MM.yyyy");
        return "Game (#" + getId() + " - " + sdf.format(getDate()) + ") A:B (" + getScoreTeamA() + ":" + getScoreTeamB() + ")";
    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
    }

    @Override
    public int compareTo (@NonNull Game game) {
        int cp;
        cp = getDate().compareTo(game.getDate());
        return cp;
    }
}
