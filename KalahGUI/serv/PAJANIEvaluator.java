import game.StateLogic;
//do not import game.State, if you import it doesnt know serv.State anymore. use game.State in code instead
import java.util.*;

public class PAJANIEvaluator implements Evaluator{

    /**
     * Returns a full state evaluation of the player-to-move for the passed state.
     *
     * @param s The state to evaluate.
     * @return The state evaluation for this state.
     */
    @Override
    public StateEvaluation evaluateFull(State s) {
        //convert engine.State to game.State to be able to get the moves and the successor States with class StateLogic
        State.GameInfo gameInfo=s.getGameInfo();
        game.State gameState= new game.State(s.getP1Houses(),s.getP2Houses(),s.getP1Score(),s.getP2Score(),s.isP1ToMove(),gameInfo.getHouses(),gameInfo.getSeeds());
        int[] moves = StateLogic.getMoves(gameState);
        //convert int[] moves to List<Integer>
        List<Integer> movesList = new ArrayList<>();
        Collections.addAll(movesList, Arrays.stream(moves).boxed().toArray(Integer[]::new));
        // Map from move to evaluation
        Map<Integer, Double> evalMap = new HashMap<>();
        //save successor states in succStates
        List<game.State> succStates=new ArrayList<>();
        for (Integer move : moves){
            succStates.add(StateLogic.getSuc(move,gameState));
        }
        //compute evaluation to each move/succState and save in evalMap
        assert(movesList.size() == succStates.size()):"something went wrong when computing succStates, #moves != #succStates";
        for(int i=0; i < succStates.size();i++){
            //TODO try different Evaluation functions
            //change implementation here
            double e = differenceInScoreEvaluation(succStates.get(i));
            evalMap.put(movesList.get(i),e);
        }
        return new StateEvaluation(movesList, evalMap );
    }

    /**
     * Returns a flat evaluation of the state, which usually is just the evaluation function applied to the passed state.
     *
     * @param s the state to evaluate
     * @return the evaluation
     */
    @Override
    public double evaluateState(State s) {
        return 0;
    }

    /**
     * Returns the principal variation for the passed state.
     * The principal variation is the preferred sequence of moves.
     *
     * @param s The state to get the PV for
     * @return the PV for the state.
     */
    @Override
    public List<Integer> getPrincipalVariation(State s) {
        return null;
    }

    /**
     * computes the difference in Seeds in each players stores
     * @param gs the game.State to evaluate ( successor of state s given to methods inherited from Evaluator)
     * @return evaluation
     */
    private double differenceInScoreEvaluation(game.State gs){
        double result;
        if(gs.getResult() == game.State.Result.P1WIN) {
            result = 100.0;
        }else if(gs.getResult() == game.State.Result.P2WIN){
            result= -100.0;
        }else {
            int p1Score = gs.getP1Score();
            int p2Score = gs.getP2Score();
            result = p1Score - p2Score;
        }
        return result;
    }
}
