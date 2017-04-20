package pkgResult;

import edu.soccer.database.dto.Player;
import edu.soccer.rs.Result.Result;
import java.util.ArrayList;

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
