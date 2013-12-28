/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.aco;

import gyte.cse443.metaheuristics.Solution;
import java.util.LinkedList;
import java.util.List;

/**
 * Manages solutions for ant system
 * @author Emre
 * @param <STATE>
 */
public abstract class ACOSolution<STATE extends Comparable> implements Solution,
        Comparable<ACOSolution<STATE>> {

    protected List<STATE> path;

    public ACOSolution() {
        this.path = new LinkedList<STATE>();
    }

    public void assignTo(ACOSolution<STATE> sol) {
        initializeSolution();
        for (STATE s : sol.getPath()) {
            path.add(s);
        }
    }

    @Override
    public ACOSolution<STATE> initializeSolution() {
        this.path.clear();
        return this;
    }

    public void add(STATE s) {
        path.add(s);
    }   

    public List<STATE> getPath() {
        return path;
    }

    @Override
    public String toString() {
        String route = "";
        for (STATE s : path) {
            route += s + "|";
        }
        return route + " : Cost :" + getCost();
    }

    @Override
    public int compareTo(ACOSolution<STATE> o) {
        if (this.getCost() < o.getCost()) {
            return -1;
        } else if (this.getCost() > o.getCost()) {
            return 1;
        } else {
            return 0;
        }
    }

}
