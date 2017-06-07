package pkgMisc;

/**
 * Created by Raphael on 20.05.2017.
 */

public class PlayerWithScore {
    private String name;
    private String score;
    private String username;

    public PlayerWithScore (String name,int score, String username){
        setName(name);
        setScore(score);
        setUsername(username);
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

    public void setScore(int score) {
        this.score = String.valueOf(score);
    }
    public String getUserame() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }
}
