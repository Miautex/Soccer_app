package group2.schoolproject.a02soccer;

/**
 * Created by Raphael on 20.05.2017.
 */

public class PlayerWithScore {
    String name;
    String score;

    public PlayerWithScore (String name,int score){
        this.name = name;
        this.score = String.valueOf(score);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
