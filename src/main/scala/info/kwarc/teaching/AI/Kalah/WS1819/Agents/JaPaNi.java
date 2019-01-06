package info.kwarc.teaching.AI.Kalah.WS1819.Agents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import game.Player;
import game.State;
import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.Agents.Agent;
import scala.Tuple4;

public class JaPaNi extends Agent {

	private String name = "JaPaNi";
	private Board board;
	private int maxHouses;
	private int initSeeds;
	private boolean playerOne;
	



	// to shorten the Tuple description elsewhere
	class AgentState {
		Tuple4<Iterable<Object>, Iterable<Object>, Object, Object> states;
	}

	public JaPaNi() {
		
	}

	public JaPaNi(String name) {
		
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
		AgentState State = new AgentState();
		State.states = board.getState();
		// Max houses for each player
		maxHouses = board.houses();
		initSeeds = board.initSeeds();

		// TODO evaluator server starten
	}

	@Override
	public int move() {
		AgentState Status = new AgentState();
		// states ist das komplette Board als (Board Player 1, Board Player 2, House
		// Player 1, House Player 2)
		Status.states = board.getState();

		// bestimmung der generischen Haeuser und der eigenen
		Iterable<Object> p1Houses = Status.states._1();
		Iterable<Object> p2Houses = Status.states._2();
		Iterator<Object> p1HousesIt = p1Houses.iterator();
		Iterator<Object> p2HousesIt = p2Houses.iterator();
		int[] p1HousesArray = new int[maxHouses];
		int[] p2HousesArray = new int[maxHouses];
		int i = 0;
		while (p1HousesIt.hasNext()) {
			p1HousesArray[i] = (int) p1HousesIt.next();
			p2HousesArray[i] = (int) p2HousesIt.next();
			i++;
		}

		// Anzahl der Houses herausfinden, weiss im Moment nicht ob das klueger geht.
		State tmpState = new State(p1HousesArray, p2HousesArray, (int) Status.states._3(), (int) Status.states._4(),
				playerOne, maxHouses, initSeeds);

		// utilitie Array anlegen, wo nachher drin stehen soll, wie viel einem ein Zug
		// von einem Haus bringt.
		// davon sollte dann das maximum gewaehlt werden.
		int[] utilities = new int[maxHouses];

		// voruebergehend um das Haus mit den meisten auszuwaehlen
		for (int j = 0; j < maxHouses; j++) {
			utilities[j] = board.getSeed((playerOne ? 1 : 2), j + 1);
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
		int maxindex;
		int depth=1;
		while(true) {
			maxindex = alphabeta(tmpState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, playerOne, true);
			timeoutMove_$eq(maxindex);
			depth++;
		}
	}

	@Override
	public String name() {
		return name;
	}
	

	private int alphabeta(State state, int depth, int alpha, int beta, boolean maximizer, boolean firstCall) {
		if (depth == 0) {
			return staticEvaluation(state,false);
		}
		if(state.isTerminal()) {
			return staticEvaluation(state,true);
		}
		int bestHouse = -1;
		int currentHouse = 0;
		int evaluation;
		if (maximizer == true) {
			int maxEval = Integer.MIN_VALUE;
			for (int i : state.getP1Houses()) {
				currentHouse++;
				if (i == 0) {
					continue;
				}
				// get the new board
				State newstate = game.StateLogic.getSuc(currentHouse - 1, state);
				// Check if we get another turn --> maximizer still true

				if (newstate.getPlayerToMove().isPlayerOne() == state.getPlayerToMove().isPlayerOne()) {
					evaluation = alphabeta(newstate, depth, alpha, beta, true, false);
				} else {
					evaluation = alphabeta(newstate, depth - 1, alpha, beta, false, false);
				}
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
			} else {
				return maxEval;
			}

		} else {
			int minEval = Integer.MAX_VALUE;
			for (int i : state.getP2Houses()) {
				currentHouse++;
				if (i == 0) {
					continue;
				}
				// get the new board
				State newstate = game.StateLogic.getSuc(currentHouse - 1, state);
				// Check if we get another turn --> maximizer still false
				if (newstate.getPlayerToMove().isPlayerOne() == state.getPlayerToMove().isPlayerOne()) {
					evaluation = alphabeta(newstate, depth, alpha, beta, false, false);
				} else {
					evaluation = alphabeta(newstate, depth - 1, alpha, beta, true, false);
				}
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
			} else {
				return minEval;
			}
		}

	}

	private int staticEvaluation(State state,boolean terminal) {
		int eval;
		if(terminal==false) {
			eval = state.getP1Score() - state.getP2Score();
		}
		else {
			int p1=0;
			int p2=0;
			for(int h:state.getP1Houses()) {
				p1+=h;
			}
			for(int h:state.getP2Houses()) {
				p2+=h;
			}
			eval=(state.getP1Score()+p1)-(state.getP2Score()+p2);
		}
		return eval;
	}

}
