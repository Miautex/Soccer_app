package pkgResult;

import edu.soccer.database.dto.Participation;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SingleParticipationResult extends Result{

	private Participation content;
	
	public SingleParticipationResult() {
	}

	public Participation getContent() {
		return content;
	}

	public void setContent(Participation participation) {
		this.content = participation;
	}
	
	
}
