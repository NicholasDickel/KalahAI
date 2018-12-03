package info.kwarc.teaching.AI.Kalah.WS1819.Agents;

import java.util.ArrayList;
import java.util.List;

import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.Agents.Agent;
import scala.Tuple4;

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
		//states ist das komplette Board als (Board Player 1, Board Player 2, House Player 1, House Player 2)
				Tuple4<Iterable<Object>,Iterable<Object>,Object,Object> states = board.getState();

				//bestimmung der generischen Haeuser und der eigenen
				Iterable<Object> ownHouses = board.getHouses(this);
				if(playerOne) {
					Iterable<Object> otherHouses = states._2();
				}else {
					Iterable<Object> otherHouses = states._1();
				}
				
				
				//Anzahl der Houses herausfinden, weiss im Moment nicht ob das klueger geht.
				int numberOfHouses = 0;
			    for (Object i : ownHouses) {
			        numberOfHouses++;
			    }
			    
			    //utilitie Array anlegen, wo nachher drin stehen soll, wie viel einem ein Zug von einem Haus bringt. 
			    //davon sollte dann das maximum gewaehlt werden.
				int[] utilities = new int[numberOfHouses];

				
				//voruebergehend um das Haus mit den meisten auszuwaehlen
				for(int i =0;i<numberOfHouses;i++) {
					utilities[i]=board.getSeed((playerOne?1:2), i+1);
				}


				
				//TODO utilities befuellen mit werten fue jeden moeglichen Zug
				
				
				
				
				//bestimmen der besten Option
				int maxvalue=0;
				int maxindex=1;
				for(int i=1;i<=numberOfHouses;i++) {
					if(utilities[i-1]>maxvalue) {
						maxvalue=utilities[i-1];
						maxindex=i;
					}
				}
				return maxindex;
	}

	@Override
	public String name() {
		return name;
	}

}
