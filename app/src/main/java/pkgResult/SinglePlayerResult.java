package pkgResult;


import edu.soccer.database.dto.Player;
import edu.soccer.rs.Result.Result;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
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
