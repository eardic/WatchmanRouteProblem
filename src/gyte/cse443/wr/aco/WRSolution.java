/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr.aco;

import edu.uci.ics.jung.graph.Graph;
import gyte.cse443.metaheuristics.aco.ACOSolution;
import gyte.cse443.wr.GuardPoint;
import gyte.cse443.wr.Road;

/**
 *
 * @author Emre
 */
public class WRSolution extends ACOSolution<GuardPoint> {
    
    /**
     * Used for calculation costs and penalty value
     */
    private final Graph<GuardPoint, Road> graph;
    /**
     * Each infeasible solution will have penaltys
     */
    private double penalty;
    
    /**
     * Gets graph and calculates penalty value for cost evaluation
     * @param graph 
     */
    public WRSolution(Graph<GuardPoint, Road> graph) {
        this.graph = graph;
        this.penalty = 0;
        for (Road r : graph.getEdges()) {
            if (r.getLength() > penalty) {
                penalty = r.getLength();
            }
        }
        penalty *= graph.getEdgeCount();
    }

    public Graph<GuardPoint, Road> getGraph() {
        return graph;
    }

    public double getPenalty() {
        return penalty;
    }

    /**
     * Calculates cost of the tour including start-end points Adds penalty for
     * unconnected nodes in path and unseen nodes in graph
     *
     * @return double representing total distance of tour
     */
    @Override
    public double getCost() {
        double c = 0;
        // Calculate cost of tour by distance
        c += getPathCost();
        //System.out.println("Cost Edge:" + c);
        // Calculate cost of unseen nodes
        c += unseenNodeCost();
        //System.out.println("Cost Unseen:" + c);
        return c;
    }

    public double getPathCost() {
        double c = 0;

        // Calculate cost of tour by distance, if there's disconnected nodes,
        // then add a penalty
        for (int i = 0; i < path.size() - 1; ++i) {
            Road r = graph.findEdge(path.get(i), path.get(i + 1));
            c += ((r == null) ? penalty : r.getLength());
        }
        // Check if path is a cycle, first and last elements are the same
        if (!path.isEmpty() && path.get(path.size() - 1).compareTo(path.get(0)) != 0) {
            c += penalty;
        }

        return c;
    }

    /**
     * Adds penalty for each unseen node in graph for current path
     *
     * @return penalty value
     */
    public double unseenNodeCost() {
        double c = 0;
        for (GuardPoint node : this.graph.getVertices()) {
            boolean seen = false;
            for (GuardPoint gp : this.path) {
                if (this.graph.isNeighbor(node, gp)) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                ++c;
            }
        }
        return c * this.penalty;
    }

}
