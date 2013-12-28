/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gyte.cse443.metaheuristics;

/**
 *
 * @author Emre
 */
public interface Solution {
    /**
     * Initializes this solution
     *
     * @return
     */
    public Solution initializeSolution();
  

    /**
     * Calculates the cost of this solution
     *
     * @return
     */
    public double getCost();
}
