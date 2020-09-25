package backend;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.Multigraph;

import java.util.ArrayList;
import java.util.List;

public class GraphGenerator {

    // Önhurkok még nincsenek, csak irányítatlan gráfok, majd kell minden csúcsnak annyi önhurok ahány éle van.
    // Irányított gráf generálás is hiányzik.
    // Dinamikus gráf generálás is hoiányzik.

    /**
     * Initialize an empty Multigraph.
     * @return ListenableGraph<CustomVertex, DefaultEdge>
     */
    private Graph<CustomVertex, DefaultEdge> crateEmptyGraph(){
        ListenableGraph<CustomVertex, DefaultEdge> graph;
        graph = new DefaultListenableGraph<CustomVertex, DefaultEdge>(new Multigraph<CustomVertex, DefaultEdge>(DefaultEdge.class));

        return graph;
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
     * @return Graph - (Undirected CycleGraph structure)
     */
    public Graph<CustomVertex, DefaultEdge> createCycleGraph(int numberOfNodes){
        Graph<CustomVertex, DefaultEdge> cycleGraph = crateEmptyGraph();
        List<CustomVertex> cycleNodes = createGraphNodes(numberOfNodes);

        for(CustomVertex node : cycleNodes){
            cycleGraph.addVertex(node);
        }
        for(int i = 0; i < cycleNodes.size() - 1; ++i){
            cycleGraph.addEdge(cycleNodes.get(i), cycleNodes.get(i + 1));
            cycleGraph.addEdge(cycleNodes.get(i + 1), cycleNodes.get(i));
        }
        cycleGraph.addEdge(cycleNodes.get(cycleNodes.size() - 1), cycleNodes.get(0));
        cycleGraph.addEdge(cycleNodes.get(0), cycleNodes.get(cycleNodes.size() - 1));

        return cycleGraph;
    }

    /**
     * Create a Clique Graph structure, with the Node number of the parameter.
     * @param numberOfNodes - number of the nodes to create a graph structure.
     * @return Graph - (Undirected CliqueGraph structure)
     */
    public Graph<CustomVertex, DefaultEdge> createCliqueGraph(int numberOfNodes){
        Graph<CustomVertex, DefaultEdge> cliqueGraph = crateEmptyGraph();
        List<CustomVertex> cliqueNodes = createGraphNodes(numberOfNodes);

        for(CustomVertex node : cliqueNodes){
            cliqueGraph.addVertex(node);
        }
        for(int i = 0; i < cliqueNodes.size(); ++i){
            CustomVertex buffer = cliqueNodes.get(i);
            for (CustomVertex node : cliqueNodes) {
                if (!buffer.getId().equals(node.getId())) {
                    cliqueGraph.addEdge(buffer, node);
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
