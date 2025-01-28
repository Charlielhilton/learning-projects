package charlielhilton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Go
{
    public static void main(String[] args) throws InterruptedException
    {
        JFrame frame = new JFrame ("Bee Simulation");
        JPanel pInfo = new JPanel();
        JPanel pControl = new JPanel();
        Field field = new Field();
        JLayeredPane jlpLayeringPanel = new JLayeredPane();

        frame.setSize(new Dimension(1600, 800));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

		frame.setVisible(true);

        frame.add(jlpLayeringPanel, BorderLayout.CENTER);

        jlpLayeringPanel.add(field, new Integer(0), 0);
        TitledBorder pFieldTitle;
        pFieldTitle = BorderFactory.createTitledBorder("Field");
        pFieldTitle.setTitleJustification(TitledBorder.LEFT);
        jlpLayeringPanel.setBorder(pFieldTitle);

        boolean bUseGraphicsYAxis = true;
        double[][] data = HeatMap.generateData();
        HeatMap pHeatMap = new HeatMap(data, bUseGraphicsYAxis, Gradient.GRADIENT_ROY);
        pHeatMap.setOpaque(false);
        pHeatMap.setSize(new Dimension(800,600));
        jlpLayeringPanel.add(pHeatMap, new Integer(1), 0);

        pInfo.setPreferredSize(new Dimension(200,600));
        SpringLayout spLayout = new SpringLayout();
        spLayout.getConstraint(SpringLayout.WEST, pControl);

        
        
        TitledBorder pInfoTitle;
        pInfoTitle = BorderFactory.createTitledBorder("Info");
        pInfoTitle.setTitleJustification(TitledBorder.LEFT);
        pInfo.setBorder(pInfoTitle);
        pInfo.setVisible(true);
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
        frame.add(pInfo, BorderLayout.LINE_START);

        SourceOneGraph sog = new SourceOneGraph(pInfo, field.source, field.source2, field.hive);
        PopulationGraph popg = new PopulationGraph(pInfo, field);

        pControl.setPreferredSize(new Dimension(1000,100));
        pControl.setBackground(Color.decode("#Fdfde7"));
        TitledBorder pControlTitle;
        pControlTitle = BorderFactory.createTitledBorder("Control Panel");
        pControlTitle.setTitleJustification(TitledBorder.LEFT);
        pControl.setBorder(pControlTitle);
        pControl.setVisible(true);
        pControl.setLayout(new FlowLayout(FlowLayout.LEFT,10,25));
        frame.add(pControl, BorderLayout.PAGE_END);


        ChangeListener changeListener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent event)
            {
                JSlider source = (JSlider)event.getSource();
                if(!source.getValueIsAdjusting())
                {
                    int iNewPopulation = (int)source.getValue();
                    System.out.println(iNewPopulation);
                    field.populationSliderChange(iNewPopulation);
                }
            }
        };

        JLabel lblSlider = new JLabel("Bee Population: ");
        pControl.add(lblSlider);

		JSlider jsPopulation = new JSlider(JSlider.HORIZONTAL, 0, 400, 400);
        jsPopulation.setBackground(Color.decode("#Fdfde7"));
        jsPopulation.addChangeListener(changeListener);
        jsPopulation.setMinorTickSpacing(10);
        jsPopulation.setMajorTickSpacing(50);
        jsPopulation.setPaintTicks(true);
        jsPopulation.setPaintLabels(true);
        pControl.add(jsPopulation);

        Integer[] jcbSourceOneInts = {10,20,30,40,50,60,70,80,90,100};
        JComboBox<Integer> jcbSourceOneStore = new JComboBox<>(jcbSourceOneInts);

        Integer[] jcbSourceTwoInts = {10,20,30,40,50,60,70,80,90,100};
        JComboBox<Integer> jcbSourceTwoStore = new JComboBox<>(jcbSourceTwoInts);

        ActionListener actionListener = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent aEvent)
            {
                if(aEvent.getSource() == jcbSourceOneStore)
                {
                    int iSelectedStore = (int)jcbSourceOneStore.getSelectedItem();
                    field.source.storeChange(iSelectedStore);
                }
                else if(aEvent.getSource() == jcbSourceTwoStore)
                {
                    int iSelectedStoreTwo = (int)jcbSourceTwoStore.getSelectedItem();
                    field.source2.storeChange(iSelectedStoreTwo);
                }
            }
        };

        JLabel lblSourceOneStore = new JLabel("Source One Stored Food: ");
        pControl.add(lblSourceOneStore);

        
        jcbSourceOneStore.setSelectedIndex(4);
        jcbSourceOneStore.addActionListener(actionListener);
        jcbSourceOneStore.setSize(200,200);
        pControl.add(jcbSourceOneStore);

        JLabel lblSourceTwoStore = new JLabel("Source Two Stored Food: ");
        pControl.add(lblSourceTwoStore);

        
        jcbSourceTwoStore.setSelectedIndex(4);
        jcbSourceTwoStore.addActionListener(actionListener);
        jcbSourceTwoStore.setSize(200,200);
        pControl.add(jcbSourceTwoStore);

        frame.pack();
        frame.setLocationRelativeTo(null);

        while (true)
        {
			Thread.currentThread().sleep(20);
			field.repaint();
		}
    }
}
