/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.sa;

import gyte.cse443.metaheuristics.Solution;

/**
 * Represents a solution space for sa method. Neighbor creation and cost
 * function must be implemented.
 *
 * @author Emre
 */
public interface SASolution extends Solution {

    /**
     * Generates a neighbor solution using this solution
     *
     * @return
     */
    public SASolution getNeighborSolution();

    /**
     * Prints this solution
     *
     * @return
     */
    public String getAsString();

}
