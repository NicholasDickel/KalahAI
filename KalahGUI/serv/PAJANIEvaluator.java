import game.StateLogic;
//do not import game.State, if you import it doesnt know serv.State anymore. use game.State in code instead
import java.util.*;
import java.util.stream.Collectors;


public class PAJANIEvaluator implements Evaluator{
    //TODO try different Evaluation functions
    //change implementation here
    //save currently used implementation here -> no need to change evaluateFull, evaluateState,getPrincipalVariation
    private EvaluationImplementation evalImplementation = new differenceInScoreEvaluation();

    /**
     * Returns a full state evaluation of the player-to-move for the passed state.
     *
     * @param s The state to evaluate.
     * @return The state evaluation for this state. (map from move to evaluation of successor State with move)
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
            double e = evalImplementation.compute(gameState);
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
        State.GameInfo gameInfo=s.getGameInfo();
        game.State gs = new game.State(s.getP1Houses(),s.getP2Houses(),s.getP1Score(),s.getP2Score(),s.isP1ToMove(),gameInfo.getHouses(),gameInfo.getSeeds());
        return evalImplementation.compute(gs);
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
        StateEvaluation stateEv = evaluateFull(s);
        Map<Integer, Double> evalMap = stateEv.getEvaluation();
        //sort evalMap by value ascending (=min first)
        List<Integer> sorted = evalMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        // if p1 is to move we need max first -> reverse list
        if(s.isP1ToMove()){
            Collections.reverse(sorted);
        }
        return sorted;
    }

    /**
     * parent class for implementations
     */
    private abstract class EvaluationImplementation{
        public abstract double compute(game.State gs);
    }

    private class differenceInScoreEvaluation extends  EvaluationImplementation{
        /**
         * computes the difference in Seeds in each players stores
         * @param gs the game.State to evaluate ( successor of state s given to methods inherited from Evaluator)
         * @return evaluation
         */
        public double compute(game.State gs){
            Double result;
            if(gs.getResult() == game.State.Result.P1WIN) {
                result = 100.0;
            }else if(gs.getResult() == game.State.Result.P2WIN){
                result= -100.0;
            }else {
                int p1Score = gs.getP1Score();
                int p2Score = gs.getP2Score();
                result = (double) p1Score - p2Score;
            }
            return result;
        }
    }
}
