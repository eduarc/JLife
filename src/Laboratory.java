/*
 * @(#) Laboratory.java
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Eduar C. Velilla
 */
public class Laboratory extends JFrame implements ActionListener, Runnable {

    /** */
    Grid grid;
    /** */
    GraphicalGrid graphGrid;
    /** */
    Thread simulator;
    /** */
    int velocity;
    /** */
    int generation;
    /** */
    int skipFrames;
    /** */
    boolean running;
    /**
     * GUI. Componentes de interfaz
     */
    JToolBar toolbar;
    JButton bNew;
    JButton bOpen;
    JButton bSave;
    JButton bStartStop;
    JButton bResizeIn;
    JButton bResizeOut;
    JButton bSpeedUp;
    JButton bSpeedDown;
    JButton bAbout;
    JLabel status;
    ImageIcon iNew;
    ImageIcon iOpen;
    ImageIcon iSave;
    ImageIcon iStart;
    ImageIcon iStop;
    ImageIcon iResizeIn;
    ImageIcon iResizeOut;
    ImageIcon iSpeedUp;
    ImageIcon iSpeedDown;
    ImageIcon iAbout;
    JFileChooser fChooser;
    /**
     * 
     */
    public Laboratory() {
        super("JLife - Simulator of the Comway's Game of Life algorithm");

        iNew = new ImageIcon(getClass().getResource("images/new.png"));
        iOpen = new ImageIcon(getClass().getResource("images/open.png"));
        iSave = new ImageIcon(getClass().getResource("images/save.png"));
        iStart = new ImageIcon(getClass().getResource("images/start.png"));
        iStop = new ImageIcon(getClass().getResource("images/stop.png"));
        iResizeIn = new ImageIcon(getClass().getResource("images/zoom-in.png"));
        iResizeOut = new ImageIcon(getClass().getResource("images/zoom-out.png"));
        iSpeedUp = new ImageIcon(getClass().getResource("images/speed-up.png"));
        iSpeedDown = new ImageIcon(getClass().getResource("images/speed-down.png"));
        iAbout = new ImageIcon(getClass().getResource("images/about.png"));

        bNew = new JButton(iNew);
        bNew.setFocusPainted(false);
        bNew.setToolTipText("New Simulation");

        bOpen = new JButton(iOpen);
        bOpen.setFocusPainted(false);
        bOpen.setToolTipText("Open Existing Simulation");

        bSave = new JButton(iSave);
        bSave.setFocusPainted(false);
        bSave.setToolTipText("Save current Simulation");

        bStartStop = new JButton(iStart);
        bStartStop.setFocusPainted(false);
        bStartStop.setToolTipText("Start Simulation");

        bResizeIn = new JButton(iResizeIn);
        bResizeIn.setFocusPainted(false);
        bResizeIn.setToolTipText("Resize In");

        bResizeOut = new JButton(iResizeOut);
        bResizeOut.setFocusPainted(false);
        bResizeOut.setToolTipText("Resize Out");

        bSpeedUp = new JButton(iSpeedUp);
        bSpeedUp.setFocusPainted(false);
        bSpeedUp.setToolTipText("Speed Up");

        bSpeedDown = new JButton(iSpeedDown);
        bSpeedDown.setFocusPainted(false);
        bSpeedDown.setToolTipText("Speed Down");

        bAbout = new JButton(iAbout);
        bAbout.setFocusPainted(false);
        bAbout.setToolTipText("About JLife");

        toolbar = new JToolBar();
        toolbar.setFloatable(false);

        toolbar.add(bNew);
        toolbar.add(bOpen);
        toolbar.add(bSave);
        toolbar.addSeparator();
        toolbar.add(bSpeedDown);
        toolbar.add(bStartStop);
        toolbar.add(bSpeedUp);
        toolbar.addSeparator();
        toolbar.add(bResizeIn);
        toolbar.add(bResizeOut);
        toolbar.addSeparator();
        toolbar.add(bAbout);

        status = new JLabel("Status Bar");
        status.setFont(new Font("auto", Font.BOLD, 15));

        bNew.addActionListener(this);
        bOpen.addActionListener(this);
        bSave.addActionListener(this);
        bSpeedUp.addActionListener(this);
        bStartStop.addActionListener(this);
        bSpeedDown.addActionListener(this);
        bResizeIn.addActionListener(this);
        bResizeOut.addActionListener(this);
        bAbout.addActionListener(this);

        fChooser = new JFileChooser();
        fChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fChooser.setAcceptAllFileFilterUsed(false);

        fChooser.setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                return file.getName().toLowerCase().endsWith(".jlife") || file.isDirectory();
            }

            public String getDescription() {
                return "Game Of Life Simulation *.JLife";
            }
        });

		graphGrid = new GraphicalGrid(5, new Grid(3, 3));

        add(toolbar, BorderLayout.NORTH);
        add(graphGrid, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        bNew.doClick(0);

        setMinimumSize(new Dimension(358, 270));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(this);
        setVisible(true);
    }

    /**
     * 
     */
    public void startSimulation() {

        stopSimulation();
        running = true;
        simulator = new Thread(this);
        simulator.start();
    }

    /**
     * 
     */
    public void stopSimulation() {

        running = false;
        if(simulator != null) {
            simulator.interrupt();
            simulator = null;
        }
    }

    /**
     * 
     * @param e
     */
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

        if (src == bNew) {
            if (grid == null) {
				grid = new Grid(3,3);
                grid.add(new Cell(1, 0));
                grid.add(new Cell(0, 1));
                grid.add(new Cell(1, 1));
                grid.add(new Cell(2, 1));
                grid.add(new Cell(2, 2));
                graphGrid.setGrid(grid);
            } else {
                grid.clear();
            }
            generation = 0;
            velocity = 500;
            skipFrames = 0;
            status.setText("Generation: 0    |    Population: 0");
            repaint();
        } 
        else if (src == bOpen) {
            int res = fChooser.showOpenDialog(this);

            if(res == JFileChooser.APPROVE_OPTION) {
                Grid g = DataManager.load(fChooser.getSelectedFile());
                if(g != null) {
                    grid = g;
                    graphGrid.setGrid(grid);
                } else {
                    JOptionPane.showMessageDialog(this, "Error when loading data", "ERROR...!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        else if (src == bSave) {
            int res = fChooser.showSaveDialog(this);

            if (res == JFileChooser.APPROVE_OPTION) {
                File f = fChooser.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".jlife")) {
                    f = new File(f.getAbsolutePath() + ".jlife");
                }
                if (!DataManager.save(f, grid)) {
                    JOptionPane.showMessageDialog(this, "Error when saving data", "ERROR...!!!", JOptionPane.ERROR_MESSAGE);
                }
            }
        } 
        else if (src == bStartStop) {
            if (bStartStop.getToolTipText().startsWith("Start")) {
                startSimulation();
                bStartStop.setToolTipText("Stop Simulation");
                bStartStop.setIcon(iStop);
            }
            else if (bStartStop.getToolTipText().startsWith("Stop")) {
                stopSimulation();
                bStartStop.setToolTipText("Start Simulation");
                bStartStop.setIcon(iStart);
            }
        }
        else if (src == bSpeedUp) {
            if(velocity == 0) {
                skipFrames += 5;
            }
            else {
                velocity -= (velocity <= 100) ? 10 : 100;
            }
        }
        else if (src == bSpeedDown) {
            if(velocity == 0) {
                velocity += ((skipFrames -= 5) == 0) ? 10 : 0;
            }
            else {
                velocity += (velocity <= 100) ? 10 : 100;
            }
        }
        else if (src == bResizeIn) {
            if (grid.getWidth() > 1 || grid.getHeight() > 1) {
                int z = graphGrid.getZoom();
                graphGrid.setZoom(++z);
            }
        }
        else if (src == bResizeOut) {
            int z = graphGrid.getZoom();
            if (z > 2) {
                graphGrid.setZoom(--z);
            }
        }
        else if(src == bAbout) {
            JOptionPane.showMessageDialog(this, "JLife\n" +
                    "Simulator of the Conway's Game of Life\n" +
                    "algorithm. Implemented in JAVA\n\n" +
                    "By: Eduar C. Velilla\n" +
                    "Colombia - 2010", "About...", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * 
     */
    public void run() {

	int nSkips;
        while(running) {
            nSkips = -2;
            while (++nSkips < skipFrames) {
                grid.next();
                status.setText("Generation: " + generation++ + "    |    Population: " + grid.getPopulation());
            }
            repaint();
            try {
                Thread.sleep(velocity);
            } catch (InterruptedException ex) {}
        }
    }
}
