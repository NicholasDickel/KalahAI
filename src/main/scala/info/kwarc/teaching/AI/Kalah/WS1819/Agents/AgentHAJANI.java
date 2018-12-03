package info.kwarc.teaching.AI.Kalah.WS1819.Agents;

import java.util.ArrayList;
import java.util.List;

import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.Agents.Agent;

public class AgentHAJANI extends Agent {

	private String name="AgentHAJANI";
	private Board board;
	private boolean playerOne;

	public AgentHAJANI() {}
	public AgentHAJANI(String name) {
		this.name=name;
	}
	
	@Override
	public Iterable<String> students() {
		List<String> studs=new ArrayList();
		studs.add("Hanna");
		studs.add("Jan Urf");
		studs.add("Nicholas Dickel");
		return studs;
	}

	@Override
	public void init(Board board, boolean playerOne) {
		this.board=board;
		this.playerOne=playerOne;

	}

	@Override
	public int move() {
		//Iterable<Object> houses = board.getHouses(this);
		return 2;
	}

	@Override
	public String name() {
		return name;
	}

}
