/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.sa;

import gyte.cse443.metaheuristics.Metaheuristics;
import gyte.cse443.metaheuristics.Solution;
import java.util.Random;

/**
 *
 * @author Emre
 */
public class SimulatedAnnealing implements Metaheuristics {

    private double initTemp, coolingRate, frozenTemp;
    private int maxIter, innerIter;
    private SAProblem problem;
    private int evalFuncCall = 0;

    public SimulatedAnnealing(SAProblem problem) {
        this(problem, 100, 1, 0.9, Integer.MAX_VALUE, 5);
    }

    public SimulatedAnnealing(SAProblem problem, double initTemp, double frozenTemp,
            double coolingRate, int maxIter, int innerIter) {
        this.initTemp = initTemp;
        this.coolingRate = coolingRate;
        this.maxIter = maxIter;
        this.problem = problem;
        this.innerIter = innerIter;
        this.frozenTemp = frozenTemp;
    }

    /**
     * Starts simulated annealing algorithm. Returns the best solution.
     *
     * @return best solution
     */
    @Override
    public SASolution findSolution() {
        SASolution current;
        SASolution best, neighbor;
        double currTemp;
        double bestEval, evalFuncCallForBest;

        // Initialize temp,init solution and sa functions
        Random rand = new Random();
        current = problem.getInitialSolution();
        best = current;
        currTemp = initTemp;
        evalFuncCall = 0;
        evalFuncCallForBest = 0;
        bestEval = Double.MAX_VALUE;

        //Until max iter and frozen temp is reached.
        for (int i = 0; i < maxIter && currTemp > frozenTemp; ++i) {

            // Iterate innerIter times
            for (int j = 0; j < innerIter; ++j) {

                // Create neighbor solution  
                neighbor = current.getNeighborSolution();

                // Evaluate current and neigbor solutions
                double neighborEval = problem.eval(neighbor);
                double currentEval = problem.eval(current);
                evalFuncCall += 2;

                // Update current if the neigbor is better than the current solution
                if (neighborEval < currentEval) {
                    current = neighbor;
                    //Evaluate the best and compare it to new solution                    
                    // Update the best if new best is found.
                    if (neighborEval < bestEval) {
                        best = neighbor;
                        bestEval = neighborEval;
                        evalFuncCallForBest = evalFuncCall;
                    }
                    /*System.out.println("New:" + current + ",T:" + currTemp + ",NEval:"
                     + neighborEval + ",CEval:" + currentEval + ",BEval:" + bestEval);*/
                }//Accept neigbor by probability function and current temprature
                else if (problem.getP(currentEval, neighborEval, currTemp) > rand.nextDouble()) {
                    current = neighbor;
                    /*System.out.println("Accepted:" + current + ",T:" + currTemp + ",NEval:"
                     + neighborEval + ",CEval:" + currentEval);*/
                }
                //System.out.println("Neigbor :" + neighbor);
            }//Inner for

            //Cool system
            // System.out.println(i+1 + "\t" + best.getCost());
            //System.out.println(evalFuncCall + "\t" + best.getCost());
            currTemp = problem.calculateTemprature(currTemp, coolingRate);

        }// Outer for

        //if(bestEval <=80)
        System.out.println(evalFuncCallForBest + "\t" + bestEval);
        return best;
    }
}
