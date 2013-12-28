/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gyte.cse443.metaheuristics;

import gyte.cse443.metaheuristics.Solution;

/**
 *
 * @author Emre
 */
public abstract class Problem {
    private Solution init;

    public Problem(Solution init) {
        this.init = init;
    }
    
    public Solution getInitialSolution(){
        return init;
    }
}
