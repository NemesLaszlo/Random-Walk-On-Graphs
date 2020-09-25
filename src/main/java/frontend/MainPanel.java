package frontend;

import backend.WalkSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {

    private JFrame frame;
    private JPanel jpMain;
    private JPanel jpGraph;
    private JPanel jpContorls;
    private WalkSimulator walkSimulator;
    private GraphPrinter graphPrinter;
    private String graphType;
    private int startNodeNum = 7;
    private boolean showEdges = true;
    private final String[] graphTypes = {"Clique", "Cycle"};

    public MainPanel() {
        frame = new JFrame();
        jpMain = new JPanel();
        jpGraph = new JPanel();
        jpContorls = new JPanel();
        graphType = new String();
        walkSimulator = new WalkSimulator();
        graphPrinter = new GraphPrinter();
    }

    public void interfaceVisualization() {

        // Graph type selection
        JComboBox graphTypesCB = new JComboBox(graphTypes);
        graphTypesCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String newVal = (String)cb.getSelectedItem();

                graphType = newVal;
                graphPrinter.SimulationInit(walkSimulator, graphType, startNodeNum);
                rePaintGraph();
            }
        });
        graphTypesCB.setSelectedIndex(0);

        // Node input
        SpinnerModel nodesModel = new SpinnerNumberModel(startNodeNum, 1, 40, 1);
        JSpinner nodesSpinner = new JSpinner(nodesModel);
        nodesSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                JSpinner s = (JSpinner) e.getSource();
                String newValString = s.getValue().toString();
                int newValInteger = Integer.parseInt(newValString);

                startNodeNum = newValInteger;
                graphPrinter.SimulationInit(walkSimulator, graphType, startNodeNum);
                rePaintGraph();
            }
        });

        // Next button
        JButton next = new JButton("Next");
        next.setBounds(50,100,95,30);
        next.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // Valami

                rePaintGraph();
            }
        });

        // Auto button
        JButton auto = new JButton("Auto");
        auto.setBounds(50,100,95,30);
        auto.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // Valami
            }
        });

        // Stop button
        JButton stop = new JButton("Stop");
        stop.setBounds(50,100,95,30);
        stop.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // Valami
            }
        });

        // Enable edges
        JCheckBox edges = new JCheckBox("Show edges");
        edges.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent e) {
                showEdges = e.getStateChange() == java.awt.event.ItemEvent.SELECTED ? true : false;
                graphPrinter.enableEdges(showEdges);
            }
        });
        edges.setSelected(showEdges);

        // Graph
        jpMain.add(jpGraph);

        //Contorls
        jpContorls.add(new Label("Nodes count:"));
        jpContorls.add(nodesSpinner);
        jpContorls.add(next);
        jpContorls.add(auto);
        jpContorls.add(stop);
        jpContorls.add(edges);
        jpContorls.add(graphTypesCB);

        jpContorls.setLayout(new BoxLayout(jpContorls, BoxLayout.Y_AXIS));
        jpMain.add(jpContorls);

        frame.add(jpMain);
        frame.setTitle("WalkDynamicGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void rePaintGraph(){

        graphPrinter = new GraphPrinter();
        graphPrinter.regenerateGraphVisual(walkSimulator);

        graphPrinter.repaint();
        graphPrinter.getContentPane().repaint();

        jpGraph.removeAll();
        jpGraph.add(graphPrinter);

        frame.repaint();

        jpMain.repaint();
        jpMain.revalidate();

        jpGraph.repaint();
        jpGraph.revalidate();

        frame.pack();

        graphPrinter.enableEdges(showEdges);
    }
}
