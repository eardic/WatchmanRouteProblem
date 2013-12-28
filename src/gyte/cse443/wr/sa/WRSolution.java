/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr.sa;

import edu.uci.ics.jung.graph.Graph;
import gyte.cse443.metaheuristics.sa.SASolution;
import gyte.cse443.wr.GuardPoint;
import gyte.cse443.wr.Road;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represents a solution for wr problem.
 *
 * @author Emre
 */
public class WRSolution implements SASolution {

    private final List<GuardPoint> guardPoints;//Watchman Tour
    private final Graph<GuardPoint, Road> graph;//Graph to tour
    private final Random rand;//For neigbor creation
    private final GuardPoint startPoint;
    private double penalty;//Penalty multipilier for unseen nodes

    /**
     * Creates WRSASolution using starting point of watchman and tour graph
     *
     * @param graph, the graph watchman'll walk
     * @param sp, start point for watchman
     */
    public WRSolution(Graph<GuardPoint, Road> graph, GuardPoint sp) {
        this.guardPoints = new LinkedList<GuardPoint>();
        this.graph = graph;
        this.rand = new Random();
        this.startPoint = sp;
        this.penalty = 0;
        // Find max weight in graph
        for (Road r : graph.getEdges()) {
            if (r.getLength() > penalty) {
                penalty = r.getLength();
            }
        }
        penalty *= graph.getEdgeCount();
        //System.out.println("Penalty:" + penalty);
        initializeSolution();
    }

    /**
     * Copy constructor which copies given WRSASolution 'sol' to this.Creates new
 random class and copies guard point list deeply.
     *
     * @param sol
     */
    public WRSolution(WRSolution sol) {
        this.guardPoints = new LinkedList<GuardPoint>(sol.getGuardPoints());
        this.graph = sol.getGraph();
        this.rand = sol.getRand();
        this.startPoint = sol.getStartPoint();
        this.penalty = sol.getPenalty();
    }

    public double getPenalty() {
        return penalty;
    }

    public GuardPoint getStartPoint() {
        return startPoint;
    }

    public Graph<GuardPoint, Road> getGraph() {
        return graph;
    }

    public List<GuardPoint> getGuardPoints() {
        return guardPoints;
    }

    public Random getRand() {
        return rand;
    }

    /**
     * Creates a random connected path begining from start point.Resulting
     * initial solution will be in <start,nodes,start> format.
     */
    @Override
    public SASolution initializeSolution() {
        //Create a random path begining from start

        for (GuardPoint p : graph.getVertices()) {
            if (!p.equals(startPoint)) {
                guardPoints.add(p);
            }
        }
        Collections.shuffle(guardPoints);
        guardPoints.add(0, startPoint);
        guardPoints.add(startPoint);
       // System.err.println(this);
        return this;
    }

    /**
     * Creates a neighbor solution using current tour by removing random vertex
     * from solution space,and adding a neighbor vertex between
     * unconnected two vertices
     *
     * @return neighbor solution
     */
    @Override
    public SASolution getNeighborSolution() {
        final WRSolution newSol = new WRSolution(this); // Copy current solution

        // Process new solution
        List<GuardPoint> gpList = newSol.getGuardPoints();// Get nodes of wr graph
        final int gpListSize = gpList.size();
        //Delete a random point from wr path list except start&end points      
        randomRemove(gpList);
        randomRemove(gpList);
        // Create connected path begining from start point
        connectNodes(gpList);
        // Adds new vertices between unconnected all nodes
        randomAdd(gpList);
        // Return new processed solution
        return newSol;
    }
    
    /**
     * Creates a connected path using nodes in gplist
     * @param gpList 
     */
    private void connectNodes(List<GuardPoint> gpList) {
        final int gpSize = gpList.size();
        for (int i = 0; i < gpSize - 1; ++i) {
            for (int j = i + 1; j < gpSize - 1; ++j) {
                if (graph.isNeighbor(gpList.get(i), gpList.get(j))) {
                    gpList.add(i + 1, gpList.remove(j));
                    break;
                }
            }
        }
    }
    
    /**
     * Adds a random neighbor vertex between unconnected vertices
     * @param gpList 
     */
    private void randomAdd(List<GuardPoint> gpList) {
        // Process solution by adding one new vertex between unconnected two node
        // Add max degreed vertex between two unconnected ones 
        for (int i = 0; i < gpList.size() - 1; ++i) {
            if (!graph.isNeighbor(gpList.get(i), gpList.get(i + 1))) {
                GuardPoint randNeighbor = getRandomNeighbor(gpList, gpList.get(i));
                if (randNeighbor != null) {
                    gpList.add(i + 1, randNeighbor);
                    //System.err.println("->>Added:"+randNeighbor);
                }
            }
        }
    }
    
    /**
     * Removes a random vertex from gplist except start nodes
     * @param gpList 
     */
    private void randomRemove(List<GuardPoint> gpList) {
        //Need minimum 3 nodes to create a cycle,like 1-2-3-1
        //Delete a random point from wr path list except start&end points  
        if (gpList.size() > 4) {// 2 start-end, 2 between = 4
            int randGp = rand.nextInt(gpList.size() - 2) + 1;
            //System.err.println("->Deleted:"+gpList.get(randGp));
            gpList.remove(randGp);
        }
    }

    /**
     * Returns a random neighbor of given vertex,Returned vertex is not in path
     * list
     *
     * @param gpInp
     * @return Random max degree neighbor for gpInp
     */
    private GuardPoint getRandomNeighbor(List<GuardPoint> gpList, GuardPoint gpInp) {
        //Init max to first element
        final Collection<GuardPoint> neighbors = graph.getNeighbors(gpInp);
        final List<GuardPoint> validNeighbors = new ArrayList<GuardPoint>();
        for (GuardPoint gp : neighbors) {
            if (!gpList.contains(gp)) {
                validNeighbors.add(gp);
            }
        }
        return validNeighbors.isEmpty() ? null
                : validNeighbors.get(rand.nextInt(validNeighbors.size()));
    }

    /**
     * Represents tour as a string like ("start|point1|point2|point3...|start")
     * The list contains nodes between start and destionation nodes
     *
     * @return string representing tour
     */
    @Override
    public String toString() {
        String route = "";
        for (GuardPoint gp : guardPoints) {
            route += gp + "|";
        }
        return route + " : Cost :" + getCost();
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
        for (int i = 0; i < guardPoints.size() - 1; ++i) {
            Road r = graph.findEdge(guardPoints.get(i), guardPoints.get(i + 1));
            c += ((r == null) ? penalty : r.getLength());
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
        for (GuardPoint node : graph.getVertices()) {
            boolean seen = false;
            for (GuardPoint path : guardPoints) {
                if (graph.isNeighbor(node, path)) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                ++c;
            }
        }
        return c * penalty;
    }

    /**
     * Returns the solution as a string in start|...|start format.
     *
     * @return
     */
    @Override
    public String getAsString() {
        return toString();
    }

}
