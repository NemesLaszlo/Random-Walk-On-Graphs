package frontend;

import backend.CustomVertex;
import backend.WalkSimulator;

import com.mxgraph.layout.*;
import com.mxgraph.swing.*;
import org.jgrapht.*;
import org.jgrapht.ext.*;
import org.jgrapht.graph.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPrinter extends JApplet {

    private final Dimension DEFAULT_SIZE = new Dimension(530, 320);
    private JGraphXAdapter<CustomVertex, DefaultEdge> jgxAdapter;
    private mxGraphComponent component;
    private Object[] cells;
    private Object[] edges;
    private Graph<CustomVertex, DefaultEdge> g;

    /**
     * GraphPrinter Constructor
     */
    public GraphPrinter(){

    }

    /**
     * Visualization init of the simulation with graph topology selection and basic display of the start condition.
     * @param simulator - Simulator object with the simulation methods.
     * @param grapthType - Type of the graph to visualize and run the simulation on it.
     * @param numberOfNodes - Number of the graph nodes.
     */
    public void SimulationInit(WalkSimulator simulator, String grapthType, int numberOfNodes)
    {
        simulator.createCycleGraph(numberOfNodes, false);
        if ("Clique".equals(grapthType)) {
            simulator.createCliqueGraph(numberOfNodes, false);
        } else if("Cycle".equals(grapthType)){
            simulator.createCycleGraph(numberOfNodes, false);
        } else if("Directed Clique".equals(grapthType)) {
            simulator.createCliqueGraph(numberOfNodes, true);
        } else if("Directed Cycle".equals(grapthType)) {
            simulator.createCycleGraph(numberOfNodes, true);
        } else {
            throw new IllegalStateException("Unexpected value: " + grapthType);
        }

        regenerateGraphVisual(simulator);
    }

    /**
     * Basic graph visualization with coloring and positioning.
     * @param simulator - Simulator object with the simulation methods.
     */
     void regenerateGraphVisual(WalkSimulator simulator) {
        g = simulator.getGraph();

        // create a visualization using JGraph, via an adapter
        jgxAdapter = new JGraphXAdapter<CustomVertex, DefaultEdge>(g);
        component = new mxGraphComponent(jgxAdapter);

        // get all cells and edges
        cells = jgxAdapter.getChildVertices(jgxAdapter.getDefaultParent());
        edges = component.getGraph().getAllEdges(cells);

        ArrayList<CustomVertex> visitedNodes = new ArrayList();

        for(CustomVertex vertex: g.vertexSet()) {
            // System.out.println(vertex.getId() + ":");
            // System.out.println("Visited" + " : " + vertex.getVisited());
            if(vertex.getVisited()) visitedNodes.add(vertex);
        }

        // Converting ArrayList to Object[]
        Object[] visitedNodesObj = new Object[visitedNodes.size()];
        // Convert visitedNodes
        int i = 0;
        for(CustomVertex vertex: visitedNodes){
            int id = Integer.parseInt(vertex.getId());
            visitedNodesObj[i] = cells[id];
            i++;
        }

        // Coloring
        jgxAdapter.setCellStyles(com.mxgraph.util.mxConstants.STYLE_FILLCOLOR,"white",cells);
        jgxAdapter.setCellStyles(com.mxgraph.util.mxConstants.STYLE_FILLCOLOR,"red", visitedNodesObj);

        //Enable,disable edge labels
        jgxAdapter.setCellStyles(com.mxgraph.util.mxConstants.STYLE_NOLABEL,"1", edges);

        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        getContentPane().add(component);
        resize(DEFAULT_SIZE);

        // positioning via jgraphx layouts
        mxCircleLayout layout = new mxCircleLayout(jgxAdapter);

        // center the circle
        int radius = 300;
        layout.setRadius(radius);
        layout.setMoveCircle(true);

        layout.execute(jgxAdapter.getDefaultParent());
    }

    /**
     * Enable or Disable the edges on the graph visualization panel.
     */
    public void enableEdges(Boolean enable){
        if(!enable)jgxAdapter.removeCells(edges);
        else jgxAdapter.addCells(edges);
    }
}
