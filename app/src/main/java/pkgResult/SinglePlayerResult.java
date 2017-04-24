package pkgResult;


import pkgData.Player;

public class SinglePlayerResult extends Result{

	private Player content;
	
	public SinglePlayerResult() {
	}

	public Player getContent() {
		return content;
	}

	public void setContent(Player player) {
		this.content = player;
	}
	
	
}
