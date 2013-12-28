/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gyte.cse443.wr;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLMetadata;
import edu.uci.ics.jung.io.GraphMLReader;
import gyte.cse443.metaheuristics.aco.AntColony;
import gyte.cse443.metaheuristics.sa.SASolution;
import gyte.cse443.metaheuristics.sa.SimulatedAnnealing;
import gyte.cse443.wr.sa.WRProblem;
import gyte.cse443.wr.sa.WRSolution;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.collections15.Factory;
import org.xml.sax.SAXException;

/**
 *
 * @author Emre
 */
public class WatchmanRoute {

    /**
     * Test of watchman route algorithms
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WatchmanRoute wr = new WatchmanRoute();
        //Create graph from file
        Graph<GuardPoint, Road> graph = wr.readGraphMLFile("D:/wrGraph3.xml");
        final int TRIAL = 100;
        for (GuardPoint sp : graph.getVertices()) {
            // Initialize solution 
            if ("n2".equals(sp.getName())) {
                for (int i = 0; i < TRIAL; ++i) {
                    wr.antSystem(graph, sp);
                    //wr.simulatedAnnealing(graph, sp);
                }
            }
        }
    }

    public void antSystem(Graph<GuardPoint, Road> graph, GuardPoint sp) {
        AntColony aco = new AntColony(new gyte.cse443.wr.aco.WRProblem(graph,sp));
        aco.findSolution();
    }

    public void simulatedAnnealing(Graph<GuardPoint, Road> graph, GuardPoint sp) {
        SASolution sol = new WRSolution(graph, sp);
        // Initialize SA
        SimulatedAnnealing sa = new SimulatedAnnealing(new WRProblem(sol));
        //Evaluate and print solution
        sa.findSolution();
    }

    /**
     * Reads given graphML file and returns UndirectedWeightedSparseGraph
     * GraphML file must contain edge weight datas
     *
     * @param filePath
     * @return
     */
    public Graph<GuardPoint, Road> readGraphMLFile(String filePath) {
        try {

            Factory vFac = new Factory<GuardPoint>() {
                @Override
                public GuardPoint create() {
                    return new GuardPoint();
                }
            };

            Factory eFac = new Factory<Road>() {
                @Override
                public Road create() {
                    return new Road();
                }
            };

            // Read graphML file and create graph
            GraphMLReader<Graph<GuardPoint, Road>, GuardPoint, Road> gReader
                    = new GraphMLReader<Graph<GuardPoint, Road>, GuardPoint, Road>(vFac, eFac);

            Graph<GuardPoint, Road> graph = new UndirectedSparseGraph<>();
            gReader.load(new FileReader(filePath), graph);

            // Get graph vertex,edge data from graphML
            Map<String, GraphMLMetadata<Road>> eMeta = gReader.getEdgeMetadata();
            Map<GuardPoint, String> vIds = gReader.getVertexIDs();
            Map<Road, String> eIds = gReader.getEdgeIDs();
            // Initialize graph vertices
            for (GuardPoint gp : graph.getVertices()) {
                gp.setName(vIds.get(gp));
                //System.err.println("V:" + gp);
            }
            // Initialize graph edges
            for (Road r : graph.getEdges()) {
                r.setLength(Double.parseDouble(eMeta.get("w").transformer.transform(r)));
                r.setId(eIds.get(r));
                //System.err.println("E:" + r);
            }
            return graph;
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WatchmanRoute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(WatchmanRoute.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WatchmanRoute.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
