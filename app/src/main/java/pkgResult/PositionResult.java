package pkgResult;


import java.util.ArrayList;

import pkgData.PlayerPosition;

public class PositionResult extends Result{

    private ArrayList<PlayerPosition> content;

    public PositionResult() {
    }

    public ArrayList<PlayerPosition> getContent() {
        return content;
    }

    public void setContent(ArrayList<PlayerPosition> content) {
        this.content = content;
    }
}

