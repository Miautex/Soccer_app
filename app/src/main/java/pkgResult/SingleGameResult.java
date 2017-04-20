/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgResult;

import edu.soccer.database.dto.Game;
import edu.soccer.database.dto.Participation;

/**
 *
 * @author Max
 */
public class SingleGameResult extends Result {
    	private Game content;
	
	public SingleGameResult() {
	}

	public Game getContent() {
		return content;
	}

	public void setContent(Game game) {
		this.content = game;
	}
}
