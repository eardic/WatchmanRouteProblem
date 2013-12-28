/*
 * STATEo change this license header, choose License Headers in Project Properties.
 * STATEo change this template file, choose STATEools | STATEemplates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.aco;

/**
 *
 * @author Emre
 * @param <STATE>
 */
public abstract class Ant<STATE extends Comparable> {

    protected ACOSolution<STATE> solution;
    protected final STATE initial;
    protected STATE current;

    public Ant(STATE initial, ACOSolution<STATE> solution) {
        this.initial = initial;
        this.solution = solution;
        initialize();
    }

    public abstract boolean isTermination();

    public void initialize() {
        //Clear list and add initial
        solution.initializeSolution().add(initial);
        this.current = this.initial;
    }

    public STATE getCurrent() {
        return current;
    }

    public void move(STATE s) {
        solution.add(s);
        this.current = s;
    }

    public ACOSolution<STATE> getSolution() {
        return solution;
    }

    public STATE getInitial() {
        return initial;
    }
}
