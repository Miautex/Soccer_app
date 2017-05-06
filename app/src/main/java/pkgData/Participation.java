package pkgData;

import java.io.Serializable;

public final class Participation implements Comparable, Serializable {
    private int numGoalsGot, numGoalsShotDefault, numGoalsShotHead, numGoalsShotHeadSnow, numGoalsShotPenalty,
            numNutmeg;
    private Team team;
    private PlayerPosition position;
    private Player player;
    private transient Game game;

    public Participation() {
        super();
        this.numGoalsGot = 0;
        this.numGoalsShotDefault = 0;
        this.numGoalsShotHead = 0;
        this.numGoalsShotHeadSnow = 0;
        this.numGoalsShotPenalty = 0;
        this.numNutmeg = 0;
        this.team = Team.NULL;
        this.position = PlayerPosition.ATTACK;
        this.player = null;
    }

    public Participation(Player player, Team team, PlayerPosition position) throws NullPointerException {
        this();
        this.setPlayer(player);
        this.setTeam(team);
        this.setPosition(position);
    }

    public Participation(Player player, Team team, PlayerPosition position, int numGoalsGot, int numGoalsShotDefault,
                         int numGoalsShotHead, int numGoalsShotHeadSnow, int numGoalsShotPenalty, int numNutmeg)
            throws NullPointerException, IllegalArgumentException {
        this();
        this.setPlayer(player);
        this.setTeam(team);
        this.setPosition(position);
        this.setNumGoalsGot(numGoalsGot);
        this.setNumGoalsShotDefault(numGoalsShotDefault);
        this.setNumGoalsShotHead(numGoalsShotHead);
        this.setNumGoalsShotHeadSnow(numGoalsShotHeadSnow);
        this.setNumGoalsShotPenalty(numGoalsShotPenalty);
        this.setNumNutmeg(numNutmeg);
    }

    public int getNumGoalsGot() {
        return numGoalsGot;
    }

    public void setNumGoalsGot(int numGoalsGot) throws IllegalArgumentException {
        if (numGoalsGot < 0) {
            throw new IllegalArgumentException("numGoalsGot may not be negative");
        }
        this.numGoalsGot = numGoalsGot;
    }

    public int getNumGoalsShotDefault() {
        return numGoalsShotDefault;
    }

    public void setNumGoalsShotDefault(int numGoalsShotDefault) throws IllegalArgumentException {
        if (numGoalsShotDefault < 0) {
            throw new IllegalArgumentException("numGoalsShotDefault may not be negative");
        }
        this.numGoalsShotDefault = numGoalsShotDefault;
    }

    public int getNumGoalsShotHead() {
        return numGoalsShotHead;
    }

    public void setNumGoalsShotHead(int numGoalsShotHead) throws IllegalArgumentException {
        if (numGoalsShotHead < 0) {
            throw new IllegalArgumentException("numGoalsShotHead may not be negative");
        }
        this.numGoalsShotHead = numGoalsShotHead;
    }

    public int getNumGoalsShotHeadSnow() {
        return numGoalsShotHeadSnow;
    }

    public void setNumGoalsShotHeadSnow(int numGoalsShotHeadSnow) throws IllegalArgumentException {
        if (numGoalsShotHeadSnow < 0) {
            throw new IllegalArgumentException("numGoalsShotHeadSnow may not be negative");
        }
        this.numGoalsShotHeadSnow = numGoalsShotHeadSnow;
    }

    public int getNumGoalsShotPenalty() {
        return numGoalsShotPenalty;
    }

    public void setNumGoalsShotPenalty(int numGoalsShotPenalty) throws IllegalArgumentException {
        if (numGoalsShotPenalty < 0) {
            throw new IllegalArgumentException("numGoalsShotPenalty may not be negative");
        }
        this.numGoalsShotPenalty = numGoalsShotPenalty;
    }

    public int getNumNutmeg() {
        return numNutmeg;
    }

    public void setNumNutmeg(int numNutmeg) throws IllegalArgumentException {
        if (numNutmeg < 0) {
            throw new IllegalArgumentException("numNutmeg may not be negative");
        }
        this.numNutmeg = numNutmeg;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) throws NullPointerException {
        if (team == null) {
            throw new NullPointerException("team may not be null, user Team.NULL instead");
        }
        this.team = team;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) throws NullPointerException {
        if (position == null) {
            throw new NullPointerException("position may not be null");
        }
        this.position = position;
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        if (player == null) {
            throw new NullPointerException("player must not be null");
        }

        this.game = game;

        if (!game.getParticipations().contains(this)) {
            game.addParticipation(this);
        }
    }



    @Override
    public int hashCode() {
        return this.player.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        Participation tmpPart;
        if (obj != null) {
            if (obj instanceof Participation) {
                tmpPart = (Participation) obj;
                eq = this.getPlayer().equals(tmpPart.getPlayer());
            }
        }
        return eq;
    }

    @Override
    public String toString() {
        return "Participation (Player: '" + getPlayer().toString() + "' - " + getTeam() + " - " + getPosition() + ")";
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public int compareTo(Object o) {
        int cp;
        Participation tmpPart = (Participation) o;
        cp = getPlayer().compareTo(tmpPart.getPlayer());
        if (cp == 0) {
            cp = getGame().compareTo(tmpPart.getGame());
        }
        return cp;
    }
}