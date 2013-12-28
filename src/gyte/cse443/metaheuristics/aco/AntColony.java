/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.metaheuristics.aco;

import gyte.cse443.metaheuristics.Metaheuristics;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Emre
 * @param <STATE>
 * @param <PATH>
 */
public class AntColony<STATE extends Comparable, PATH extends Object>
        implements Metaheuristics {

    private ACOProblem<STATE, PATH> problem;
    private final int MAX_ITER, ANT_SIZE;
    private final double ALPHA, BETA, Q, INIT_PHEROMON, EVAPORATION_FACTOR;

    private Random rand;

    public AntColony(ACOProblem<STATE, PATH> problem, int MAX_ITER,
            double ALPHA, double BETA, double Q, int ANT_SIZE, double INIT_PHEROMON,
            double EVAPORATION_FACTOR) {
        this.problem = problem;
        this.MAX_ITER = MAX_ITER;
        this.ALPHA = ALPHA;
        this.BETA = BETA;
        this.Q = Q;
        this.ANT_SIZE = ANT_SIZE;
        this.INIT_PHEROMON = INIT_PHEROMON;
        this.EVAPORATION_FACTOR = EVAPORATION_FACTOR;
        // Create ants
        this.rand = new Random();
    }

    /**
     * Max iter : 100 Alpha : 1 Beta : 2 Initial Pheromon : 1/vertex count
     * Evaporation Factor : 0.5 Ant Number : vertex count Q : 1
     *
     * @param problem
     */
    public AntColony(ACOProblem<STATE, PATH> problem) {
        // MAX_ITER, ALPHA, BETA, Q, ANT_SIZE, INIT_PHEROMON, EVAPORATION_FACTOR
        this(problem, 100, 1.0, 2.0, 1.0, problem.getGraph().getVertexCount(),
                1.0 / problem.getGraph().getVertexCount(), 0.5);
    }

    /**
     * Finds the best solution using ACO algorithm.
     *
     * @return
     */
    @Override
    public ACOSolution<STATE> findSolution() {
        List<Ant<STATE>> ants;
        problem.initializePheromons(INIT_PHEROMON);// Intialize pheromones
        ants = problem.initializeAnts(ANT_SIZE);// Place and create ants

        for (int t = 0; t < MAX_ITER; ++t) {
            // Construct a solution for all ants, and update best solution
            for (Ant<STATE> ant : ants) {
                // Get solution of ant, and update best if new best is found 
                problem.updateBest(constructSolution(ant));
                // Increase pheromons on edges the ant walked
                problem.depositPheromon(ant, Q);
            }
            // Evaporates pheromone on edges
            problem.evaporatePheromons(ants, Q, EVAPORATION_FACTOR);
            // Ants come back to home(initial point)
            problem.takeAntsToHome(ants);
        }

        System.err.println(problem.getBest());
        return problem.getBest();
    }

    private ACOSolution<STATE> constructSolution(Ant<STATE> ant) {
        while (!ant.isTermination()) {
            STATE choice = problem.chooseState(ant, ALPHA, BETA);
            if (choice != null) {// If ant has a place to go
                ant.move(choice);
            } else {// Ant stucked, nowhere to go, infeasible solution
                break;
            }
        }
        return ant.getSolution();
    }
}
