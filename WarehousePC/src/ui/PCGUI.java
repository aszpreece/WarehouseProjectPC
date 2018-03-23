package ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import bluetooth.Robot;
import filehandling.JobTable;
import jobmanagement.Server;
import lejos.util.Delay;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.localisation.GridPositionDistribution;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.MovableRobot;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.MapVisualisationComponent;
import types.Job;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;

/**
 * 
 * @author Osanne Gbayere, Brandon Goodwin
 * The PC display window
 *
 */
public class PCGUI extends JFrame implements Runnable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3943010528198460967L;
	
	private static final Logger logger = Logger.getLogger(PCGUI.class);

	private static final String FRAME_TITLE = "Robot Control UI";

	private JPanel activeJobsPanel;
	private JPanel inactiveJobsPanel;
	private JPanel activeJobsInnerPanel;
	private JPanel inactiveJobsInnerPanel;
	private JPanel gridPanel;
	private JPanel robotDetailsPanel;
	private JPanel mainCanvas;

	private JScrollPane activeScrollPane;
	private JScrollPane inactiveScrollPane;

	JLabel reward;

	private GridPanel gridInnerPanel;

	private RobotPanel robotDetailsInnerPanel;

	private JMenuBar menuBar;

	private JMenu toolsMenu;

	private JMenuItem pauseSimMenuItem;

	private Server server;

	// This will be the JobTable class used to get information about the jobs
	// And will require certain methods to be added to the JobTable class
	private JobTable jobDataStore;

	/**
	 * 
	 * @param jobDataStore HashMap storing job data by JobID
	 * @param server handle to the server
	 * PCGUI constructor
	 */
	public PCGUI(JobTable jobDataStore, Server server) {
		this.jobDataStore = jobDataStore;
		this.server = server;
		
		// Gets a list of the handles to the Robot objects
		server.getConnectedRobots();

		// Frame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(800, 600));
		setTitle(FRAME_TITLE);
		setVisible(true);

		// Panel holding the list of the active jobs
		activeJobsPanel = new JPanel();
		{
			logger.debug("Creating Panel holding the list of the active jobs");
			activeJobsPanel.setBorder(BorderFactory.createTitledBorder("Active Jobs"));
			activeJobsPanel.setPreferredSize(new Dimension(150, 200));
			activeJobsPanel.setLayout(new BoxLayout(activeJobsPanel, BoxLayout.Y_AXIS));

			activeJobsInnerPanel = new JPanel();
			activeJobsInnerPanel.setLayout(new BoxLayout(activeJobsInnerPanel, BoxLayout.Y_AXIS));

			activeScrollPane = new JScrollPane(activeJobsInnerPanel);
			activeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			activeJobsPanel.add(activeScrollPane);
		}
		add(activeJobsPanel, BorderLayout.EAST);

		// Panel holding the list of inactive jobs
		inactiveJobsPanel = new JPanel();
		{
			logger.debug("Creating Panel holding the list of inactive jobs");
			inactiveJobsPanel.setBorder(BorderFactory.createTitledBorder("Inactive Jobs"));
			inactiveJobsPanel.setPreferredSize(new Dimension(150, 200));
			inactiveJobsPanel.setLayout(new BoxLayout(inactiveJobsPanel, BoxLayout.Y_AXIS));

			inactiveJobsInnerPanel = new JPanel();
			inactiveJobsInnerPanel.setLayout(new BoxLayout(inactiveJobsInnerPanel, BoxLayout.Y_AXIS));

			inactiveScrollPane = new JScrollPane(inactiveJobsInnerPanel);
			inactiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			inactiveJobsPanel.add(inactiveScrollPane);
		}

		add(inactiveJobsPanel, BorderLayout.WEST);

		// The Centre of the window in the BorderLayout layout manager
		mainCanvas = new JPanel();
		{
			logger.debug("Creating the Centre of the window in the BorderLayout layout manager");
			mainCanvas.setLayout(new BoxLayout(mainCanvas, BoxLayout.Y_AXIS));
			gridPanel = new JPanel();
			gridInnerPanel = new GridPanel(server);
			gridPanel.setBorder(BorderFactory.createTitledBorder("Robot Warehouse"));
			gridPanel.add(gridInnerPanel);
			mainCanvas.add(gridPanel);
			reward = new JLabel("Total Reward: " + server.getScore());
			mainCanvas.add(reward);
		}

		add(mainCanvas, BorderLayout.CENTER);

		// Panel holding information about the robot destination, name and location
		robotDetailsPanel = new JPanel();
		{
			logger.debug("Creating panel holding information about the robot destination, name and location");
			robotDetailsInnerPanel = new RobotPanel(server);
			robotDetailsPanel.setBorder(BorderFactory.createTitledBorder("Robots"));
			robotDetailsPanel.setPreferredSize(new Dimension(245, 100));
			robotDetailsPanel.setLayout(new BoxLayout(robotDetailsPanel, BoxLayout.X_AXIS));
			robotDetailsPanel.add(robotDetailsInnerPanel);
		}

		add(robotDetailsPanel, BorderLayout.SOUTH);

		// Menubar holding the pause menu
		menuBar = new JMenuBar();
		toolsMenu = new JMenu("Tools");
		menuBar.add(toolsMenu);
		pauseSimMenuItem = new JMenuItem("Pause");
		pauseSimMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// Menu bar pauses the simulation and changes the menu item text
				if (pauseSimMenuItem.getText().equals("Pause")) {
					server.setPaused(true);
					pauseSimMenuItem.setText("Unpause");
				} else {
					server.setPaused(false);
					pauseSimMenuItem.setText("Pause");
				}
			}
		});

		toolsMenu.add(pauseSimMenuItem);

		this.setJMenuBar(menuBar);

		updateUI();

		pack();

	}

	/**
	 * Updates the UI
	 */
	public void updateUI() {
		
		// Removes all the visual components to be changed
		logger.debug("Removing all the visual components to be changed");
		activeJobsInnerPanel.removeAll();
		inactiveJobsInnerPanel.removeAll();
		robotDetailsPanel.removeAll();
		mainCanvas.remove(reward);

		// Updates the active/in active jobs list
		logger.debug("Updating the active/in active jobs list");
		for (String jobID : jobDataStore.getJobTable().keySet()) {
			Job j = jobDataStore.getJobTable().get(jobID);
			float percentageComplete = j.getPercentageComplete();

			if (j.getActive() || Math.round(percentageComplete) == 100) {

				activeJobsInnerPanel.add(new JobPanel(jobID, percentageComplete, jobDataStore));

			} else {
				inactiveJobsInnerPanel.add(new JobPanel(jobID));
			}
		}

		// Updates the total reward accumulated
		logger.debug("Updating the total reward accumulated");
		reward = new JLabel("Total Reward: " + server.getScore());
		mainCanvas.add(reward);

		robotDetailsInnerPanel = new RobotPanel(server);
		robotDetailsPanel.add(robotDetailsInnerPanel);

		// Revalidates the top level container and all of those within it
		revalidate();
	}

	@Override
	public void run() {

		while (true) {
			// sets a delay of 1 second
			Delay.msDelay(1000);
			updateUI();
		}
	}
}

class JobPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2180129182481579585L;
	
	private static final Logger logger = Logger.getLogger(JobPanel.class);

	private JLabel jobLabel;
	private JLabel percentageCompleteLabel;

	private JButton cancelButton;

	/**
	 * @param jobID
	 * @param percentageComplete
	 * @param jobDataStore
	 * Active Job Panel Constructor - the behaviour of this constructor changes 
	 * when the percentage complete value has been set to true
	 */
	public JobPanel(String jobID, Float percentageComplete, JobTable jobDataStore) {
		setLayout(new BorderLayout());
		//setPreferredSize(new Dimension(150,80));
		jobLabel = new JLabel("Job ID: " + jobID);
		
		jobLabel.setHorizontalAlignment(SwingConstants.CENTER);

		// Adds a button when percentage complete is 100%
		if (Math.round(percentageComplete) != 100) {
			add(jobLabel, BorderLayout.NORTH);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// Send Cancel message up
					logger.debug("Cancel Button Pressed");
					jobDataStore.setCancelled(jobID);
				}
			});
			add(cancelButton, BorderLayout.SOUTH);
		}

		// Indicates in the active jobs panel that the task is complete
		if (Math.round(percentageComplete) != 100) {
		percentageCompleteLabel = new JLabel(percentageComplete + "% Complete");
		} else {
			System.out.println("TASK COMPLETE (PERCENTAGE)");
			percentageCompleteLabel = new JLabel("Task Complete!");
		}
		
		add(percentageCompleteLabel, BorderLayout.EAST);

	}

	/**
	 * @param jobID
	 * Inactive Job Panel constructor
	 */
	public JobPanel(String jobID) {
		setLayout(new BorderLayout());
		jobLabel = new JLabel("Job ID: " + jobID);
		jobLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(jobLabel, BorderLayout.CENTER);
	}

}

class RobotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3025088648394354280L;
	
	// list of all the robot handles
	private List<Robot> robotList;

	/**
	 * @param server
	 * Constructor JPanel that holds the robot informations such as the position and the destination
	 */
	public RobotPanel(Server server) {
		robotList = server.getConnectedRobots();
		for (Robot r : robotList) {
			JPanel robotInnerPanel = new JPanel();
			JLabel robotNameLabel = new JLabel(r.getName());
			JLabel robotPositionLabel = new JLabel("Position: " + "(" + r.getX() + "," + r.getY() + ")");
			JLabel robotDestinationLabel = new JLabel(
					"Destination: (" + r.getDestinationX() + "," + r.getDestinationY() + ")");
			robotInnerPanel.setLayout(new BoxLayout(robotInnerPanel, BoxLayout.Y_AXIS));
			robotInnerPanel.add(robotNameLabel);
			robotInnerPanel.add(robotPositionLabel);
			robotInnerPanel.add(robotDestinationLabel);

			add(robotInnerPanel);
		}
	}
}

// Displays the robots and continuously updates their location
class GridPanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 892150393382176613L;
	
	private static final Logger logger = Logger.getLogger(GridPanel.class);
	
	private GridMap gridMap;
	private MapBasedSimulation sim;
	private List<Robot> robotList;
	private ConcurrentHashMap<String, MobileRobotWrapper<MovableRobot>> robotTable;
	GridPositionDistribution dist;
	GridPositionDistributionVisualisation mapVis;

	/**
	 * Constructor: sets up the robot simulation window based off the MarkovLocalisation example
	 * @param server
	 */
	public GridPanel(Server server) {
		setPreferredSize(new Dimension(500, 500));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		gridMap = MapUtils.createRealWarehouse();
		sim = new MapBasedSimulation(gridMap);
		this.robotTable = new ConcurrentHashMap<>();
		this.robotList = server.getConnectedRobots();

		// Loads the robot list handles into a seperate list to avoid concurrency errors
		logger.debug("Loading the robot list handles into a seperate list to avoid concurrency errors");
		List<Robot> searchList = new ArrayList<Robot>(robotList);
		
		for (Robot r : searchList) {
			// Gets a handle to the mobile robot to move it in the simulation
			MobileRobotWrapper<MovableRobot> p = addRobot(r.getCurrentX(), r.getCurrentY(), 0);
			
			// Places the handle into a HashMap
			robotTable.put(r.getName(), p);
		}

		// Starts the thread that checks the robot position
		logger.debug("Starting the thread that checks the robot position");
		Thread gridPanelThread = new Thread(this);
		gridPanelThread.start();

	}

	/**
	 * Constructor: spawns a new robot on to the simulation window
	 * @param x
	 * @param y
	 * @param direction
	 * @return
	 */
	public MobileRobotWrapper<MovableRobot> addRobot(int x, int y, int direction) {
		return addRobot(x, y, direction, true);
	}

	/**
	 * Constructor: Adds the robot to the screen. When updateScreen is set to false it doesn't spawn another robot
	 * @param x
	 * @param y
	 * @param direction
	 * @param updateScreen
	 * @return
	 */
	public MobileRobotWrapper<MovableRobot> addRobot(int x, int y, int direction, boolean updateScreen) {
		GridPose gridStart;

		// Gives a GridPose corresponding to the position and a direction
		switch (direction) {
		case 0:
			gridStart = new GridPose(x, y, Heading.PLUS_Y);
			break;
		case 90:
			gridStart = new GridPose(x, y, Heading.PLUS_X);
			break;
		case 180:
			gridStart = new GridPose(x, y, Heading.MINUS_Y);
			break;
		case 270:
			gridStart = new GridPose(x, y, Heading.MINUS_X);
			break;
		default:
			gridStart = new GridPose(x, y, Heading.PLUS_Y);
		}

		// Handle to the simulation robot object
		MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
				gridMap.toPose(gridStart));

		if (updateScreen) {
			// robot spawning code
			dist = new GridPositionDistribution(gridMap);
			mapVis = new GridPositionDistributionVisualisation(dist, gridMap);
			MapVisualisationComponent.populateVisualisation(mapVis, sim);
			removeAll();
			add(mapVis);
		}

		return wrapper;
	}

	@Override
	public void run() {
		while (true) {
			List<Robot> searchList = new ArrayList<Robot>(robotList);

			for (Robot r : searchList) {
				// Representation of the simulation grid
				GridMap myGridMap = MapUtils.createRealWarehouse();
				
				// Gets the robot handle from the robotTable and updates the grid pose
				logger.debug("Getting the robot handle from the robotTable and updates the grid pose");
				robotTable.get(r.getName()).getRobot()
						.setPose(myGridMap.toPose(new GridPose(r.getCurrentX(), r.getCurrentY(), Heading.PLUS_Y)));
			}
		}

	}
}