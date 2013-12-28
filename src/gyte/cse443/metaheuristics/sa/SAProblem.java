/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.sa;

import gyte.cse443.metaheuristics.Problem;
import gyte.cse443.metaheuristics.Solution;

/**
 * Contains necessary functions for simulated annealing method.
 *
 * @author Emre
 */
public abstract class SAProblem extends Problem {

    public SAProblem(SASolution init) {
        super(init);
    }

    @Override
    public SASolution getInitialSolution() {
        return (SASolution) super.getInitialSolution();
    }

    public double calculateTemprature(double temp, double coolingRatio) {
        return temp * coolingRatio;
    }

    public double getP(double currentStateEval, double newStateEval, double temp) {
        if (newStateEval < currentStateEval) {
            return 1.0;
        }
        return Math.exp((currentStateEval - newStateEval) / temp);
    }

    public abstract double eval(Solution s);

}
