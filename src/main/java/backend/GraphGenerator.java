package backend;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphGenerator {

    // Dinamikus + Hurkok
    // Ahogy olvastam a directed-nél nem kellenek a hurkok elhagyhatóak, de majd csekkoljuk :D

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
            ListenableGraph<CustomVertex, DefaultEdge> graph;
            graph = new DefaultListenableGraph<CustomVertex, DefaultEdge>(new Multigraph<CustomVertex, DefaultEdge>(DefaultEdge.class));
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
