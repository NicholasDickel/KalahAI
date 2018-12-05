package info.kwarc.teaching.AI.Kalah.WS1819.Agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.Agents.Agent;
import scala.Tuple4;

public class AgentPAJANI extends Agent {

	private String name = "AgentPAJANI";
	private Board board;
	private int maxHouses;
	private boolean playerOne;

	// to shorten the Tuple description elsewhere
	class AgentState {
		Tuple4<Iterable<Object>, Iterable<Object>, Object, Object> states;
	}

	public AgentPAJANI() {
	}

	public AgentPAJANI(String name) {
		this.name = name;
	}

	@Override
	public Iterable<String> students() {
		List<String> studs = new ArrayList();
		studs.add("Paula Kaiser");
		studs.add("Jan Urfei");
		studs.add("Nicholas Dickel");
		return studs;
	}

	@Override
	public void init(Board board, boolean playerOne) {
		this.board = board;
		this.playerOne = playerOne;
		AgentState State=new AgentState();
		State.states=board.getState();
		// Max houses for each player
		int numberOfHouses = 0;
		for (Object i : State.states._1()) {
			numberOfHouses++;
		}
		maxHouses=numberOfHouses;
		//TODO evaluator server starten
	}

	@Override
	public int move() {
		AgentState Status = new AgentState();
		// states ist das komplette Board als (Board Player 1, Board Player 2, House
		// Player 1, House Player 2)
		Status.states = board.getState();

		// bestimmung der generischen Haeuser und der eigenen
		Iterable<Object> ownHouses = board.getHouses(this);
		
		if (playerOne) {
			Iterable<Object> otherHouses = Status.states._2();
		} else {
			Iterable<Object> otherHouses = Status.states._1();
		}

		// Anzahl der Houses herausfinden, weiss im Moment nicht ob das klueger geht.
		int numberOfHouses = 0;
		for (Object i : ownHouses) {
			numberOfHouses++;
		}

		// utilitie Array anlegen, wo nachher drin stehen soll, wie viel einem ein Zug
		// von einem Haus bringt.
		// davon sollte dann das maximum gewaehlt werden.
		int[] utilities = new int[numberOfHouses];

		// voruebergehend um das Haus mit den meisten auszuwaehlen
		for (int i = 0; i < numberOfHouses; i++) {
			utilities[i] = board.getSeed((playerOne ? 1 : 2), i + 1);
		}

		// TODO utilities befuellen mit werten fue jeden moeglichen Zug

		// bestimmen der besten Option
//		int maxvalue = 0;
//		int maxindex = 1;
//		for (int i = 1; i <= numberOfHouses; i++) {
//			if (utilities[i - 1] > maxvalue) {
//				maxvalue = utilities[i - 1];
//				maxindex = i;
//			}
//		}
		int maxindex=alphabeta(Status,5,Integer.MIN_VALUE,Integer.MAX_VALUE,true,true);
		return maxindex;
	}

	@Override
	public String name() {
		return name;
	}

	private int alphabeta(AgentState state, int depth, int alpha, int beta, boolean maximizer, boolean firstCall) {
		if (depth == 0) {
			return staticEvaluation(state);
		}
		// TODO Add Gameover check
		int bestHouse=-1;
		int currentHouse=0;
		if (maximizer==true) {
			int maxEval = Integer.MIN_VALUE;
			for (Object i : state.states._1()) {
				currentHouse++;
				if((int)i==0) {
					continue;
				}
				// TODO Somehow get the new board
				AgentState newstate = state;
				// TODO Check if we get another turn --> maximizer still true
				int evaluation = alphabeta(newstate, depth - 1, alpha, beta, false, false);
				if (evaluation > maxEval) {
					maxEval = evaluation;
					bestHouse = currentHouse;
				}
				alpha = Math.max(alpha, evaluation);
				if (beta <= alpha) {
					break;
				}
			}
			if (firstCall) {
				return bestHouse;
			} 
			else {
				return maxEval;
			}

		} else {
			int minEval = Integer.MAX_VALUE;
			for (Object i : state.states._2()) {
				currentHouse++;
				if((int)i==0) {
					continue;
				}
				// TODO Somehow get the new board if the current house is picked
				AgentState newstate = state;
				// TODO Check if we get another turn --> maximizer still false
				int evaluation = alphabeta(newstate, depth - 1, alpha, beta, true, false);
				if (evaluation < minEval) {
					minEval = evaluation;
					bestHouse = currentHouse;
				}
				beta = Math.min(beta, evaluation);
				if (beta <= alpha) {
					break;
				}
			}
			if (firstCall) {
				return bestHouse;
			} 
			else {
				return minEval;
			}
		}

	}

	private int staticEvaluation(AgentState state) {
		// TODO replace with static evaluation
		Random rand = new Random();
		// random number from -100 to +100, will be replaced with static evaluation
		int i = rand.nextInt(201) - 100;
		return i;
	}

}
