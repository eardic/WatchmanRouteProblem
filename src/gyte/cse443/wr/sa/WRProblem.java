/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr.sa;

import gyte.cse443.metaheuristics.Solution;
import gyte.cse443.metaheuristics.sa.SAProblem;
import gyte.cse443.metaheuristics.sa.SASolution;

/**
 *
 * @author Emre
 */
public class WRProblem extends SAProblem {

    public WRProblem(SASolution initSol) {
        super(initSol);
    }

    @Override
    public double eval(Solution s) {
        return s.getCost();
    }

}
