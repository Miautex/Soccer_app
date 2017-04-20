package pkgResult;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;
import pkgData.Game;

@XmlRootElement
public class GameResult extends Result{

	private ArrayList<Game> content;
	
	public GameResult() {}

	public ArrayList<Game> getContent() {
		return content;
	}

	public void setContent(ArrayList<Game> content) {
		this.content = content;
	}


	
	
}
	