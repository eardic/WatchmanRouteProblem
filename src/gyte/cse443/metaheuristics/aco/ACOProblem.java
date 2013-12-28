/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.aco;

import edu.uci.ics.jung.graph.Graph;
import gyte.cse443.metaheuristics.Problem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Implements some standart formulas for ant system algorithm
 * @author Emre
 * @param <STATE>
 * @param <PATH>
 */
public abstract class ACOProblem<STATE extends Comparable, PATH extends Object>
        extends Problem {

    protected final Graph<STATE, PATH> graph;
    protected Map<PATH, Double> pheromons;
    protected final Random rand;
    protected ACOSolution<STATE> best;

    public ACOProblem(Graph<STATE, PATH> graph) {
        super(null);
        this.rand = new Random();
        this.graph = graph;
        this.pheromons = new HashMap<PATH, Double>(graph.getEdgeCount());
    }

    public Graph<STATE, PATH> getGraph() {
        return graph;
    }

    /**
     * Creates a list of states which is unvisited by given ant
     *
     * @param ant
     * @return list of unvisited states
     */
    public abstract List<STATE> getNonVisitedStates(Ant<STATE> ant);

    /**
     * Place ants random nodes in graph
     *
     * @param antSize
     * @return ants created
     */
    public abstract List<Ant<STATE>> initializeAnts(final int antSize);

    public abstract void takeAntsToHome(List<Ant<STATE>> ants);

    /**
     * Ants come back to their home, initial position
     * @param ants
     */
    public void resetAnts(List<Ant<STATE>> ants) {
        for (Ant<STATE> ant : ants) {
            ant.initialize();
        }
    }

    /**
     * Chooses next state for given ant and iteration using random transition
     * probability function which is, P(i,t,alpha,beta) = n_ij^alpha * t_ij^beta
     * / (sum(n_ik^alpha * t_ik^beta)) j,k are neighbor set of state i
     *
     * @param ant
     * @param alpha
     * @param beta
     * @return the random selected state according to transition function
     */
    public STATE chooseState(Ant<STATE> ant, double alpha, double beta) {
        STATE i = ant.getCurrent();

        // First, calculate sum of probability of neighbor states
        // because it doesn't change. Denominator of trans prob function
        // sum( t_ik^beta * n_ik^alpha ) for k being neighbors of i
        double sum = 0;
        for (STATE k : getNonVisitedStates(ant)) {
            double n_ik = visibility(i, k);
            double t_ik = pheromoneQuantity(i, k);
            sum += Math.pow(t_ik, beta) * Math.pow(n_ik, alpha);
        }
        // Then, calculate numerator of transition formula
        double selectProb = rand.nextDouble();
        // calculate n_ij and t_ij
        for (STATE j : getNonVisitedStates(ant)) {
            double n_ij = visibility(i, j);
            double t_ij = pheromoneQuantity(i, j);
            double pFunc = Math.pow(t_ij, beta) * Math.pow(n_ij, alpha) / sum;
            // n_ij^alpha * t_ij^beta
            selectProb -= pFunc;
            //System.err.println(i+"->"+j+" P is:" + pFunc);
            if (selectProb <= 0) {
                return j;
            }
        }
        return null;
    }

    public ACOSolution<STATE> getBest() {
        return best;
    }

    public abstract void updateBest(ACOSolution<STATE> sol);

    public abstract double depositPheromon(Ant<STATE> ant, double q);

    public abstract void evaporatePheromons(List<Ant<STATE>> ants, double q,
            double evaporateFactor);

    public abstract void initializePheromons(double initPheromon);

    public abstract double visibility(STATE current, STATE next);

    public abstract double pheromoneQuantity(STATE current, STATE next);

}
