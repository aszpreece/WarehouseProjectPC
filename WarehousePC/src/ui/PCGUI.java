package ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import bluetooth.Robot;
import filehandling.JobTable;
import jobmanagement.Server;
import lejos.util.Delay;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.KillMeNow;
import rp.robotics.visualisation.MapVisualisationComponent;
import types.Job;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * 
 * @author Brandon Goodwin, Osanne Gbayere
 *
 */
public class PCGUI extends JFrame implements Runnable {

	private static final String FRAME_TITLE = "Robot Control UI";

	private JPanel jobsPanel;

	private JPanel activeJobsPanel;
	private JScrollPane activeScrollPane;
	private JPanel activeJobsInnerPanel;

	private JPanel inactiveJobsPanel;
	private JScrollPane inactiveScrollPane;
	private JPanel inactiveJobsInnerPanel;

	private GridPanel gridPanel;
	private JPanel gridInnerPanel;
	private JMenuBar menuBar;
	private JMenu toolsMenu;
	private JMenuItem addRobotMenuItem;
	
	private String direction = "";

	private Server server;

	// This will be the JobTable class used to get information about the jobs
	// And will require certain methods to be added to the JobTable class
	private JobTable jobDataStore;

	private List<Robot> robots;

	public PCGUI(JobTable jobDataStore, Server server) {
		this.jobDataStore = jobDataStore;
		this.server = server;
		this.robots = server.getConnectedRobots();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(800, 600));
		setTitle(FRAME_TITLE);
		setVisible(true);
				
		jobsPanel = new JPanel(new FlowLayout());
		jobsPanel.setPreferredSize(new Dimension(250, 500));

		activeJobsPanel = new JPanel();
		{
			activeJobsPanel.setBorder(BorderFactory.createTitledBorder("Active Jobs"));
			activeJobsPanel.setPreferredSize(new Dimension(250, 200));
			activeJobsPanel.setLayout(new BoxLayout(activeJobsPanel, BoxLayout.Y_AXIS));

			activeJobsInnerPanel = new JPanel();

			activeScrollPane = new JScrollPane(activeJobsInnerPanel);
			activeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			activeJobsPanel.add(activeScrollPane);
		}
		jobsPanel.add(activeJobsPanel);

		inactiveJobsPanel = new JPanel();
		{
			inactiveJobsPanel.setBorder(BorderFactory.createTitledBorder("Inactive Jobs"));
			inactiveJobsPanel.setPreferredSize(new Dimension(250, 260));
			inactiveJobsPanel.setLayout(new BoxLayout(inactiveJobsPanel, BoxLayout.Y_AXIS));

			inactiveJobsInnerPanel = new JPanel();
			inactiveJobsInnerPanel.setLayout(new BoxLayout(inactiveJobsInnerPanel, BoxLayout.Y_AXIS));

			inactiveScrollPane = new JScrollPane(inactiveJobsInnerPanel);
			inactiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			inactiveJobsPanel.add(inactiveScrollPane);
		}
		jobsPanel.add(inactiveJobsPanel);

		add(jobsPanel, BorderLayout.WEST);

		gridPanel = new GridPanel(server);
		{
			gridPanel.setBorder(BorderFactory.createTitledBorder("Robots"));
			gridPanel.setPreferredSize(new Dimension(245, 500));
			gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));

			gridInnerPanel = new JPanel();
			gridInnerPanel.setLayout(new BoxLayout(gridInnerPanel, BoxLayout.X_AXIS));
		}

		add(gridPanel, BorderLayout.CENTER);
		
		menuBar = new JMenuBar();
		toolsMenu = new JMenu("Tools");
		menuBar.add(toolsMenu);
		addRobotMenuItem = new JMenuItem("Add Robot Menu Item");
		addRobotMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame addRobotFrame = new JFrame("Add Robot");
				JPanel addRobotPanel = new JPanel();
				JPanel addRobotCoordinatePanel = new JPanel();
				JPanel addRobotDirectionPanel = new JPanel();
				JLabel xLabel = new JLabel("X: ");
				JLabel yLabel = new JLabel("Y: ");
				JLabel dirLabel = new JLabel("Direction");
				JButton okButton = new JButton("OK");
				
				JTextField xTextField = new JTextField();
				xTextField.setPreferredSize(new Dimension(25,20));
				JTextField yTextField = new JTextField();
				yTextField.setPreferredSize(new Dimension(25,20));
				
				JRadioButton northButton = new JRadioButton("N");
				northButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						direction = "N";
					}
					
				});
				
				JRadioButton southButton = new JRadioButton("S");
				southButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						direction = "S";						
					}
					
				});
				
				JRadioButton eastButton = new JRadioButton("E");
				eastButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						direction = "E";
						
					}
					
				});
				
				JRadioButton westButton = new JRadioButton("W");
				
				ButtonGroup buttonGroup = new ButtonGroup();
				buttonGroup.add(northButton);
				buttonGroup.add(southButton);
				buttonGroup.add(eastButton);
				buttonGroup.add(westButton);
				
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String xCoordinate = xTextField.getText();
						String yCoordinate = yTextField.getText();
						
						if((xCoordinate.length()>0) && (yCoordinate.length()>0) && (!(direction).equals(""))) {
							gridPanel.addRobot(Integer.parseInt(xCoordinate), Integer.parseInt(yCoordinate), direction);
							direction = "";
							addRobotFrame.setVisible(false);
						}
					}
					
				});
				
				addRobotCoordinatePanel.setLayout(new BoxLayout(addRobotCoordinatePanel, BoxLayout.X_AXIS));
				addRobotCoordinatePanel.add(xLabel);
				addRobotCoordinatePanel.add(xTextField);
				addRobotCoordinatePanel.add(yLabel);
				addRobotCoordinatePanel.add(yTextField);
				addRobotDirectionPanel.setLayout(new BoxLayout(addRobotDirectionPanel, BoxLayout.X_AXIS));
				addRobotDirectionPanel.add(dirLabel);
				addRobotDirectionPanel.add(northButton);
				addRobotDirectionPanel.add(eastButton);
				addRobotDirectionPanel.add(southButton);
				addRobotDirectionPanel.add(westButton);
				addRobotPanel.add(addRobotCoordinatePanel,BorderLayout.NORTH);
				addRobotPanel.add(addRobotDirectionPanel, BorderLayout.CENTER);
				addRobotPanel.add(okButton, BorderLayout.SOUTH);
				addRobotFrame.add(addRobotPanel);
				addRobotFrame.setPreferredSize(new Dimension(250,120));
				addRobotFrame.pack();
				addRobotFrame.setVisible(true);
				
			}
			
		});
		toolsMenu.add(addRobotMenuItem);
		
		this.setJMenuBar(menuBar);

		updateUI();

		pack();

	}

	public void updateUI() {
		activeJobsInnerPanel.removeAll();
		inactiveJobsInnerPanel.removeAll();
		for (String jobID : jobDataStore.getJobTable().keySet()) {
			Job j = jobDataStore.getJobTable().get(jobID);
			float percentageComplete = j.getPercentageComplete();

			if (j.getActive()) {
				activeJobsInnerPanel.add(new JobPanel(jobID, percentageComplete));
			} else {
				inactiveJobsInnerPanel.add(new JobPanel(jobID));
			}

		}
		revalidate();
	}

	@Override
	public void run() {

		while (true) {
			// Maybe set a delay
			Delay.msDelay(1000);
			updateUI();
		}
	}
}

// Wont take the same form and more
// Will have cancel button, % complete if it is an active job and JobID
class JobPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2180129182481579585L;

	private JLabel jobLabel;

	private JButton cancelButton;

	private JobTable jobDataStore;

	private JLabel percentageCompleteLabel;

	/*
	 * Active Job Panel Constructor
	 */
	public JobPanel(String jobID, Float percentageComplete) {
		setLayout(new BorderLayout());
		// setPreferredSize(new Dimension(100, 100));

		jobLabel = new JLabel("Job ID: " + jobID);
		jobLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(jobLabel, BorderLayout.NORTH);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Send Cancel message up
				jobDataStore.cancel(jobID);
			}
		});
		add(cancelButton, BorderLayout.SOUTH);

		percentageCompleteLabel = new JLabel(percentageComplete + "% Complete");
		add(percentageCompleteLabel, BorderLayout.EAST);

	}

	/*
	 * Inactive Job Panel constructor
	 */
	public JobPanel(String jobID) {
		setLayout(new BorderLayout());
		jobLabel = new JLabel("Job ID: " + jobID);
		jobLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(jobLabel, BorderLayout.CENTER);
	}

}

class GridPanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 892150393382176613L;
	private GridMap gridMap;
	private MapBasedSimulation sim;
	private ArrayList<MobileRobotWrapper<MovableRobot>> wrapperList;
	private List<Robot> robotList;
	private HashMap<String, MobileRobotWrapper<MovableRobot>> robotTable;
	
	private Server server;

	public GridPanel(Server server) {
		gridMap = MapUtils.createRealWarehouse();
		sim = new MapBasedSimulation(gridMap);
		wrapperList = new ArrayList<MobileRobotWrapper<MovableRobot>>();
		this.robotTable = new HashMap<>();
		this.robotList = server.getConnectedRobots();
		
	}
	
	public void addRobot(int x, int y, String direction) {
		
		GridPose gridStart;
		
		switch(direction) {
		case "N":
			gridStart = new GridPose(x, y, Heading.PLUS_Y);
			break;
		case "E":
			gridStart = new GridPose(x, y, Heading.PLUS_X);
			break;
		case "S":
			gridStart = new GridPose(x, y, Heading.MINUS_Y);
			break;
		case "W":
			gridStart = new GridPose(x, y, Heading.MINUS_X);
			break;
		default:
				gridStart = new GridPose(x, y, Heading.PLUS_Y);
		}
		
		MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
				gridMap.toPose(gridStart));
		
		wrapperList.add(wrapper);
		
		GridPositionDistribution dist = new GridPositionDistribution(gridMap);
		GridPositionDistributionVisualisation mapVis = new GridPositionDistributionVisualisation(dist, gridMap);
				MapVisualisationComponent.populateVisualisation(mapVis, sim);
		removeAll();
		add(mapVis);
	}

	@Override
	public void run() {
		
	}
}