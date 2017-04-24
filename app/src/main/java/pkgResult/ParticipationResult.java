package pkgResult;

import java.util.ArrayList;
import pkgData.Participation;


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
