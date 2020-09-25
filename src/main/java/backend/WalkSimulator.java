package backend;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

enum Topology {
    CLIQUE,
    CYCLE
}

public class WalkSimulator {

    private Topology topology;
    private Graph<CustomVertex, DefaultEdge> graph;
    private GraphGenerator generator;
    private Random rand;
    private int numOfNodes;
    private int numOfEdges;
    private Map<String, Integer> score_graph = new HashMap<String, Integer>();

    /**
     * WalkSimulator Constructor
     */
    public WalkSimulator() {
        this.generator = new GraphGenerator();
        this.graph = this.generator.createCliqueGraph(7);
        this.topology = Topology.CLIQUE;
        this.numOfNodes = graph.vertexSet().size();
        this.numOfEdges = graph.edgeSet().size();
        this.rand = new Random();
        this.randomizeGraph();
    }

    /**
     * Getter to the graph.
     * @return With tha actual graph.
     */
    public Graph<CustomVertex, DefaultEdge> getGraph() {
        return this.graph;
    }

    /**
     * Select the random start vertex from the graph.
     */
    public void randomizeGraph() {
        try {
            int num = (int) (Math.random() * this.numOfNodes);
            for (CustomVertex vertex : this.graph.vertexSet()) {
                if (--num < 0) {
                    vertex.setVisited(true);
                    return;
                }
            }
        }catch(AssertionError ignored) {}
    }

    /**
     * Setup the created graph. Handle the number of nodes and pick the start node.
     * @param numberOfNodes - number of the graph nodes.
     */
    public void setup(int numberOfNodes) {
        this.numOfNodes = numberOfNodes;
        for (Map.Entry<String, Integer> entry : this.score_graph.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }
        this.score_graph = new HashMap<String,Integer>();
        this.randomizeGraph();
    }

    /**
     * Reset every node back to unvisited.
     */
    public void resetVisited() {
        for (CustomVertex vertex : this.graph.vertexSet()) {
            vertex.setVisited(false);
        }
    }

    /**
     * Create a Cycle Graph structure, with the Node number of the parameter,
     * and set it up to the simulation.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     */
    public void createCycleGraph(int numberOfNodes) {
        this.graph = this.generator.createCycleGraph(numberOfNodes);
        this.topology = Topology.CYCLE;
        this.setup(numberOfNodes);
    }

    /**
     * Create a Clique Graph structure, with the Node number of the parameter,
     * and set it up to the simulation.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     */
    public void createCliqueGraph(int numberOfNodes) {
        this.graph = this.generator.createCliqueGraph(numberOfNodes);
        this.topology = Topology.CLIQUE;
        this.setup(numberOfNodes);
    }

    // Kellenek a szimulációs algoritmusok
}
