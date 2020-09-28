package backend;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


enum Topology {
    CLIQUE,
    CYCLE,
    DIRECTED_CLIQUE,
    DIRECTED_CYCLE
}

public class WalkSimulator {

    private Topology topology;
    private Graph<CustomVertex, DefaultEdge> graph;
    private GraphGenerator generator;
    private Random rand;
    private int numOfNodes;
    private int numOfEdges;
    private CustomVertex actualVisited;
    private Map<String, Integer> score_graph = new HashMap<String, Integer>();

    /**
     * WalkSimulator Constructor
     */
    public WalkSimulator() {
        this.generator = new GraphGenerator();
        this.graph = this.generator.createCliqueGraph(7, false);
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
                    actualVisited = vertex;
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
     * Reset every node back to unvisited.
     * @return With a logical value -> true if every visited otherwise false.
     */
    public boolean checkAllVisited() {
        for (CustomVertex vertex : this.graph.vertexSet()) {
            if(!vertex.getVisited()){
                return false;
            }
        }
        return true;
    }

    /**
     * Create a Cycle Graph structure, with the Node number of the parameter,
     * and set it up to the simulation.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     * @param directed - logical value if it is true the graph going to directed.
     */
    public void createCycleGraph(int numberOfNodes, boolean directed) {
        this.graph = this.generator.createCycleGraph(numberOfNodes, directed);
        if(directed){
            this.topology = Topology.DIRECTED_CYCLE;
        }else{
            this.topology = Topology.CYCLE;
        }
        this.setup(numberOfNodes);
    }

    /**
     * Create a Clique Graph structure, with the Node number of the parameter,
     * and set it up to the simulation.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     */
    public void createCliqueGraph(int numberOfNodes, boolean directed) {
        this.graph = this.generator.createCliqueGraph(numberOfNodes, directed);
        if(directed){
            this.topology = Topology.DIRECTED_CLIQUE;
        }else{
            this.topology = Topology.CLIQUE;
        }
        this.setup(numberOfNodes);
    }

    /**
     * Simple Random Walk on undirected or directed graphs.
     * @param directed - Walk on a directed or not directed graph.
     */

    public void simpleRandomWalkNext(boolean directed) {
        int vertexDegree;
        double probability;
        if(!directed) {
            vertexDegree = Graphs.successorListOf(this.graph, actualVisited).size() / 2;
        }else{
            vertexDegree = this.graph.outDegreeOf(actualVisited);
        }
        probability = (double) 1 / vertexDegree;
        if(vertexDegree == 0) {
            return;
        }
        for(CustomVertex neighbor: Graphs.successorListOf(this.graph, actualVisited) ) {
            if(new Random().nextDouble() <= probability ) {
                neighbor.setVisited(true);
                System.out.println("HERE NOW: " + neighbor.getId());
                actualVisited = neighbor;
                return;
            }
        }
    }

    /**
     * Lazy Random Walk on dynamic graphs.
     */
    public void lazyRandomWalkNext() {

    }

}
