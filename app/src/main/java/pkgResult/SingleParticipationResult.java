package pkgResult;

import pkgData.Participation;

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
