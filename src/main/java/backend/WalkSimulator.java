package backend;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;


enum Topology {
    CLIQUE,
    CYCLE,
    DIRECTED_CLIQUE,
    DIRECTED_CYCLE,
    DYNAMIC
}

public class WalkSimulator {

    private Topology topology;
    private Graph<CustomVertex, DefaultEdge> graph;
    private GraphGenerator generator;
    private Random rand;
    private int numOfNodes;
    private int numOfEdges;
    private int walkCount;
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
        this.walkCount = 0;
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
     * Getter to number of steps under the walk.
     * @return With the number of steps to cover the whole graph.
     */
    public int getWalkCount() {
        return this.walkCount;
    }

    /**
     * Reset the number of steps under the walk.
     */
    public void resetWalkCount() {
        this.walkCount = 0;
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
     * Get the maximum number from the vertex degree.
     * We need this value to the lazy random walk probability (in every step?)
     */
    private int maxDegreeNumOfTheGraph() {
        int max = this.graph.outDegreeOf(this.graph.vertexSet().iterator().next());
        for (CustomVertex vertex : this.graph.vertexSet()) {
            if (this.graph.outDegreeOf(vertex) > max) {
                max = this.graph.outDegreeOf(vertex);
            }
        }
        return max;
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
     * Create a Dynamic Graph structure, with the Node number of the parameter,
     * and set it up to the simulation. (frame without dynamic change)
     * @param numberOfNodes - number of the nodes to create a graph structure.
     */
    public void createDynamicGraph(int numberOfNodes) {
        this.graph = this.generator.createDynamicGraph(numberOfNodes);
        this.topology = Topology.DYNAMIC;
        this.setup(numberOfNodes);
    }

    /**
     * Dynamic change call. (delete random edge, add random edge or nothing)
     * With the self loops fix in every step.
     */
    public void dynamicChangeNext() {
        if(this.topology == Topology.DYNAMIC) {
            generator.dynamicChange(this.graph);
        }
    }

    /**
     * Simple Random Walk on undirected or directed graphs.
     */
    public void simpleRandomWalkNext() {
        int vertexDegree;
        double probability;

        vertexDegree = this.graph.outDegreeOf(actualVisited);
        probability = (double) 1 / vertexDegree;
        if(vertexDegree == 0) {
            return;
        }
        for(CustomVertex neighbor: Graphs.successorListOf(this.graph, actualVisited) ) {
            if(new Random().nextDouble() <= probability ) {
                neighbor.setVisited(true);
                System.out.println("HERE NOW: " + neighbor.getId());
                this.walkCount++;
                actualVisited = neighbor;
                return;
            }
        }
    }

    /**
     * Lazy Random Walk on dynamic graphs.
     * The probability of staying is 1 - (d(u)/dmax+ 1)).
     */
    public void lazyRandomWalkNext() {
        this.dynamicChangeNext();

        int vertexDegree = this.graph.outDegreeOf(actualVisited);
        int maxDegree = (this.numOfNodes -1) * 2; // declared max degree num
        int actualMaxDegree = this.maxDegreeNumOfTheGraph();
        System.out.println("Const max degree: " + maxDegree);
        System.out.println("Actual max degree: " + actualMaxDegree);
        double probability = 1 - ((double)vertexDegree / ((double)maxDegree + 1));
        System.out.println("Dynamic probability: " + probability);
        for(CustomVertex neighbor: Graphs.successorListOf(this.graph, actualVisited) ) {
            if(new Random().nextDouble() >= probability ) {
                neighbor.setVisited(true);
                System.out.println("HERE NOW: " + neighbor.getId());
                this.walkCount++;
                actualVisited = neighbor;
                return;
            }
        }
    }

}
