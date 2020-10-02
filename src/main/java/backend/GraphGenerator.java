package backend;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

    /**
     * Initialize an empty Multigraph.
     * @param directed - Directed empty graph or not.
     * @return ListenableGraph<CustomVertex, DefaultEdge>
     */
    private Graph<CustomVertex, DefaultEdge> crateEmptyGraph(boolean directed){
        if(directed){
            Graph<CustomVertex, DefaultEdge> graph;
            graph = new SimpleDirectedGraph<CustomVertex, DefaultEdge>(DefaultEdge.class);
            return graph;
        }else {
            Graph<CustomVertex, DefaultEdge> graph;
            graph = new SimpleGraph<CustomVertex, DefaultEdge>(DefaultEdge.class);
            return graph;
        }
    }

    /**
     * Create a list from the the created Node objects by the parameter number,
     * which can we use to create a Graph structure.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     * @return List with the Nodes to the graph.
     */
    private List<CustomVertex> createGraphNodes(int numberOfNodes){
        List<CustomVertex> nodes = new ArrayList<CustomVertex>();

        for(int i = 0; i < numberOfNodes; ++i){
            nodes.add(new CustomVertex(String.valueOf(i)));
        }

        return nodes;
    }

    /**
     * Create a Cycle Graph structure, with the Node number of the parameter.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     * @param directed - logical value if it is true the graph going to directed.
     * @return Graph - (Undirected or Undirected CycleGraph structure)
     */
    public Graph<CustomVertex, DefaultEdge> createCycleGraph(int numberOfNodes, boolean directed){
        Graph<CustomVertex, DefaultEdge> cycleGraph = crateEmptyGraph(directed);
        List<CustomVertex> cycleNodes = createGraphNodes(numberOfNodes);

        for(CustomVertex node : cycleNodes){
            cycleGraph.addVertex(node);
        }
        for(int i = 0; i < cycleNodes.size() - 1; ++i){
            cycleGraph.addEdge(cycleNodes.get(i), cycleNodes.get(i + 1));
            if(!directed) {
                cycleGraph.addEdge(cycleNodes.get(i + 1), cycleNodes.get(i));
            }
        }
        cycleGraph.addEdge(cycleNodes.get(cycleNodes.size() - 1), cycleNodes.get(0));
        if(!directed) {
            cycleGraph.addEdge(cycleNodes.get(0), cycleNodes.get(cycleNodes.size() - 1));
        }

        return cycleGraph;
    }

    /**
     * Create a Clique Graph structure, with the Node number of the parameter.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     * @param directed - logical value if it is true the graph going to directed.
     * @return Graph - (Undirected or Undirected CliqueGraph structure)
     */
    public Graph<CustomVertex, DefaultEdge> createCliqueGraph(int numberOfNodes, boolean directed){
        Graph<CustomVertex, DefaultEdge> cliqueGraph = crateEmptyGraph(directed);
        List<CustomVertex> cliqueNodes = createGraphNodes(numberOfNodes);

        for(CustomVertex node : cliqueNodes){
            cliqueGraph.addVertex(node);
        }
        for(int i = 0; i < cliqueNodes.size(); ++i){
            CustomVertex buffer = cliqueNodes.get(i);
            for (CustomVertex node : cliqueNodes) {
                if (!buffer.getId().equals(node.getId())) {
                    if(directed && cliqueGraph.incomingEdgesOf(node).size() != 0){
                        if( new Random().nextDouble() <= 0.5 ) {
                            cliqueGraph.addEdge(buffer, node);
                        }
                    }else{
                        cliqueGraph.addEdge(buffer, node);
                    }
                }
            }
        }

        return cliqueGraph;
    }

    /**
     * Create a Dynamic Graph structure, with the Node number of the parameter.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     * @return Graph - Dynamic "CliqueGraph" structure, only the frame without dynamic change.
     */
    public Graph<CustomVertex, DefaultEdge> createDynamicGraph(int numberOfNodes){
        Graph<CustomVertex, DefaultEdge> dynamicGraph;
        dynamicGraph = new DirectedPseudograph<CustomVertex, DefaultEdge>(DefaultEdge.class);
        List<CustomVertex> dynamicNodes = createGraphNodes(numberOfNodes);

        for(CustomVertex node : dynamicNodes){
            dynamicGraph.addVertex(node);
        }
        for(int i = 0; i < dynamicNodes.size(); ++i){
            CustomVertex buffer = dynamicNodes.get(i);
            for (CustomVertex node : dynamicNodes) {
                if (!buffer.getId().equals(node.getId())) {
                    if( new Random().nextDouble() <= 0.5 ) {
                        dynamicGraph.addEdge(buffer, node);
                    }
                }
            }
        }
        // Self loops by the out degree num
        for(CustomVertex vertex : dynamicGraph.vertexSet()) {
            int vertexDegree = dynamicGraph.outDegreeOf(vertex);
            for(int i = 0; i < vertexDegree; ++i) {
                dynamicGraph.addEdge(vertex, vertex);
            }
        }

        return dynamicGraph;
    }

    /**
     * Dynamic Graph - dynamic function add random edge to the graph or delete random edge or do nothing in the step.
     * Note: after the changes we have to cover the degree nums because of the self loops and pick the max to the random lazy walk.
     * @param dynamicGraph - actual dynamic graph, where we make changes with the edges.
     */
    public void dynamicChange(Graph<CustomVertex, DefaultEdge> dynamicGraph) {
        CustomVertex firstVertex = pickRandomVertex(dynamicGraph);
        CustomVertex secVertex = pickRandomVertex(dynamicGraph);
        System.out.println("first id: " + firstVertex.getId() + " sec id: " + secVertex.getId() );
        if(new Random().nextDouble() <= 0.75) {
            if(firstVertex != null && secVertex != null) {
                if(!firstVertex.getId().equals(secVertex.getId())) {
                    if(Graphs.successorListOf(dynamicGraph, firstVertex).contains(secVertex)) {
                        System.out.println("There is a edge between this two vertices.");
                    } else {
                        dynamicGraph.addEdge(firstVertex, secVertex);
                        System.out.println("Add Edge Change");
                    }
                }
            }
        }else if(!firstVertex.getId().equals(secVertex.getId())) {
            if(Graphs.successorListOf(dynamicGraph, firstVertex).contains(secVertex)) {
                dynamicGraph.removeEdge(firstVertex, secVertex);
                System.out.println("Delete Edge Change");
            }
        }
        // self loops fix after the changes
        updateSelfLoopsAfterChange(dynamicGraph);

        for(CustomVertex vertex: dynamicGraph.vertexSet()) {
            System.out.println("Vertex id: " + vertex.getId() + " Degree num: " +  dynamicGraph.outDegreeOf(vertex));
            System.out.println("Vertex id: " + vertex.getId() + " Edges to: " +  Graphs.successorListOf(dynamicGraph, vertex));
        }
    }

    /**
     * After the dynamic changes we update the self loops on vertices. (after add or delete edge)
     * @param dynamicGraph - actual dynamic graph, where we have to update the self loops
     */
    private void updateSelfLoopsAfterChange(Graph<CustomVertex, DefaultEdge> dynamicGraph) {
        for(CustomVertex vertex : dynamicGraph.vertexSet()) {
            dynamicGraph.removeAllEdges(vertex, vertex); // remove all self loops
            int vertexDegree = dynamicGraph.outDegreeOf(vertex); // get the actual degree to recreate the self loops
            for(int i = 0; i < vertexDegree; ++i) {
                dynamicGraph.addEdge(vertex, vertex);
            }
        }
    }

    /**
     * Pick a random vertex from the parameter graph.
     * @param graph - graph, where we pick a random vertex
     */
    private CustomVertex pickRandomVertex(Graph<CustomVertex, DefaultEdge> graph) {
        CustomVertex result = null;
        int size = graph.vertexSet().size();
        int item = new Random().nextInt(size);
        int i = 0;
        for(CustomVertex vertex : graph.vertexSet())
        {
            if (i == item)
                result = vertex;
            i++;
        }
        return result;
    }

    /**
     * Print informations about the graph -> graph nodes and graph edges, plus about the structure.
     * @param g - the graph where we would like to see the informations about that.
     */
    public void consoleGraphInfo(Graph<CustomVertex, DefaultEdge> g){
        System.out.println("Graph system:");
        for(CustomVertex node : g.vertexSet()){
            System.out.println(node.toString());
            System.out.println(g.edgesOf(node));
        }
        System.out.println();
        System.out.println("Graph:");
        System.out.println(g.toString());
        System.out.println("Graph only the edges:");
        System.out.println(g.edgeSet());
    }

}
