/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr.aco;

import edu.uci.ics.jung.graph.Graph;
import gyte.cse443.metaheuristics.aco.ACOProblem;
import gyte.cse443.metaheuristics.aco.ACOSolution;
import gyte.cse443.metaheuristics.aco.Ant;
import gyte.cse443.wr.GuardPoint;
import gyte.cse443.wr.Road;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Emre
 */
public class WRProblem extends ACOProblem<GuardPoint, Road> {

    /**
     * Start point of watchman
     */
    private final GuardPoint startPoint;

    /**
     * Graph and start point is necessary to run ACO.
     *
     * @param graph
     * @param startPoint
     */
    public WRProblem(Graph<GuardPoint, Road> graph, GuardPoint startPoint) {
        super(graph);
        this.startPoint = startPoint;
        this.best = new WRSolution(graph);
    }

    /**
     * Creates a list of states which is unvisited by given ant except start
     * point(start point is set to unvisited since a path'll contain two start
     * point(for start and end)) start|...|start
     *
     * @param ant
     * @return list of unvisited states
     */
    @Override
    public List<GuardPoint> getNonVisitedStates(Ant<GuardPoint> ant) {
        // Get neighbors of current state of ant
        Collection<GuardPoint> states = graph.getNeighbors(ant.getCurrent());
        List<GuardPoint> validStates = new ArrayList<GuardPoint>();
        // Delete visited ones, don't delete start point if it's contained
        for (GuardPoint s : states) {
            if (!ant.getSolution().getPath().contains(s)) {//Not visited,then add
                validStates.add(s);
            } else if (s.equals(startPoint)) {//If start point, then add
                validStates.add(s);
            }
        }
        return validStates;// Return unvisited states
    }

    /**
     * All ants start from the same startPoint
     *
     * @param antSize
     * @return
     */
    @Override
    public List<Ant<GuardPoint>> initializeAnts(final int antSize) {
        //List<GuardPoint> nodes = new ArrayList<GuardPoint>(graph.getVertices());
        //Collections.shuffle(nodes);
        List<Ant<GuardPoint>> ants = new LinkedList<Ant<GuardPoint>>();
        for (int i = 0; i < antSize; ++i) {
            ants.add(new WRAnt(startPoint, new WRSolution(graph)));
        }
        return ants;
    }

    /**
     * Puts (q/length of path) pheromone to ant's path, length of path may
     * contain penalties
     *
     * @param ant
     * @param q
     * @return
     */
    @Override
    public double depositPheromon(Ant<GuardPoint> ant, double q) {
        double delta = q / ant.getSolution().getCost();
        List<GuardPoint> path = ant.getSolution().getPath();
        // Put q/solCost pheromon on path of ant
        for (int i = 0; i < path.size() - 1; ++i) {
            Road r = graph.findEdge(path.get(i), path.get(i + 1));
            if (r != null) {
                pheromons.put(r, pheromons.get(r) + delta);
            }
        }
        return delta;
    }

    /**
     * Pheremons are evaporated at each iteration by using the formula :
     * T_ij(t+1) = (1-evaporateFactor) * T_ij(t) + delta_Tij(t)
     *
     * @param ants
     * @param q
     * @param evaporateFactor
     */
    @Override
    public void evaporatePheromons(List<Ant<GuardPoint>> ants, double q, double evaporateFactor) {
        double delta = 0;
        for (Ant<GuardPoint> ant : ants) {
            delta += q / ant.getSolution().getCost();
        }
        for (Road road : graph.getEdges()) {
            double t_ij = pheromons.get(road);
            pheromons.put(road, (1 - evaporateFactor) * t_ij + delta);
        }
    }

    /**
     * All pheremons on the edges are initialized to initPheromon
     *
     * @param initPheromon
     */
    @Override
    public void initializePheromons(double initPheromon) {
        for (Road road : graph.getEdges()) {
            pheromons.put(road, initPheromon);
        }
    }

    /**
     * Visibility is determined using distance between current and next state
     * and the number of seen nodes by next state. Visibility = (1 / d_ij) + (1
     * - 1/seenNodes_j)
     *
     * @param current
     * @param next
     * @return
     */
    @Override
    public double visibility(GuardPoint current, GuardPoint next) {
        Road edge = graph.findEdge(current, next);
        if (edge != null) {
            double d_ij = edge.getLength();
            double seenNodes_j = graph.getNeighborCount(next);
            return (1 / d_ij) + (1 - (1 / seenNodes_j));
        }
        return 0;
    }

    /**
     * Pheremon quantity of edge between current and next is fetched from
     * pheremon map (key is edge, value is pheromon)
     *
     * @param current
     * @param next
     * @return
     */
    @Override
    public double pheromoneQuantity(GuardPoint current, GuardPoint next) {
        Road edge = graph.findEdge(current, next);
        if (edge != null) {
            return pheromons.get(edge);
        }
        return 0;
    }

    /**
     * Initializes all ants, all comes back to their initial point. Their memory
     * is wiped out.
     *
     * @param ants
     */
    @Override
    public void takeAntsToHome(List<Ant<GuardPoint>> ants) {
        for (Ant<GuardPoint> ant : ants) {
            ant.initialize();
        }
    }

    /**
     * If given solution has lower cost than best, then update best
     *
     * @param sol
     */
    @Override
    public void updateBest(ACOSolution<GuardPoint> sol) {
        if (sol.compareTo(best) < 0) {
            best.assignTo(sol);
        }
    }

}
