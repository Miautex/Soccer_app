package pkgResult;

import edu.soccer.database.dto.Participation;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParticipationResult extends Result{

	private ArrayList<Participation> content;
	
	public ParticipationResult() {
	}

	public ArrayList<Participation> getContent() {
		return content;
	}

	public void setContent(ArrayList<Participation> content) {
		this.content = content;
	}
	
	
}
