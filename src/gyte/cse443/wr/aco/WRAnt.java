/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr.aco;

import gyte.cse443.metaheuristics.aco.ACOSolution;
import gyte.cse443.metaheuristics.aco.Ant;
import gyte.cse443.wr.GuardPoint;
import java.util.List;

/**
 *
 * @author Emre
 */
public class WRAnt extends Ant<GuardPoint> {

    public WRAnt(GuardPoint initial, ACOSolution<GuardPoint> sol) {
        super(initial, sol);
    }


    /**
     * If ant reaches starting position then it means the ant completed its path
     * Start | ... | start is a solution, begin from start and finish at start
     *
     * @return true if ant completed its path
     */
    @Override
    public boolean isTermination() {
        return solution.getPath().size() > 2//Except start point
                && current.compareTo(initial) == 0;
    }

}
