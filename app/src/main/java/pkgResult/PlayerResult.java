package pkgResult;

import java.util.ArrayList;

import pkgData.Player;

public class PlayerResult extends Result{

	private ArrayList<Player> content;
	
	public PlayerResult() {
	}

	public ArrayList<Player> getContent() {
		return content;
	}

	public void setContent(ArrayList<Player> content) {
		this.content = content;
	}
}
