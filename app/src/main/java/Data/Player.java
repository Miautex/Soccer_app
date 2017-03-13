package Data;

/**
 * Created by Martin on 13.03.2017.
 */

public class Player {
    private String name;
    private int id;

    public Player (String name){
        this.name = name;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
