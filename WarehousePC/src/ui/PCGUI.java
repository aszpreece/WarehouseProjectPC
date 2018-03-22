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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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
import rp.robotics.visualisation.GridPositionDistributionVisualisation;
import rp.robotics.visualisation.MapVisualisationComponent;
import types.Job;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * 
 * @author Osanne Gbayere, Brandon Goodwin
 *
 */
public class PCGUI extends JFrame implements Runnable {

	private static final String FRAME_TITLE = "Robot Control UI";

	private JPanel jobsPanel;
	private JPanel activeJobsPanel;
	private JPanel inactiveJobsPanel;
	private JPanel activeJobsInnerPanel;
	private JPanel inactiveJobsInnerPanel;
	private JPanel gridPanel;
	private JPanel robotDetailsPanel;

	private JScrollPane activeScrollPane;
	private JScrollPane inactiveScrollPane;

	private GridPanel gridInnerPanel;

	private RobotPanel robotDetailsInnerPanel;

	private JMenuBar menuBar;

	private JMenu toolsMenu;

	private JMenuItem addRobotMenuItem;

	private int direction = -1;

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
			activeJobsPanel.setPreferredSize(new Dimension(250, 100));
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
			inactiveJobsPanel.setPreferredSize(new Dimension(250, 375));
			inactiveJobsPanel.setLayout(new BoxLayout(inactiveJobsPanel, BoxLayout.Y_AXIS));

			inactiveJobsInnerPanel = new JPanel();
			inactiveJobsInnerPanel.setLayout(new BoxLayout(inactiveJobsInnerPanel, BoxLayout.Y_AXIS));

			inactiveScrollPane = new JScrollPane(inactiveJobsInnerPanel);
			inactiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			inactiveJobsPanel.add(inactiveScrollPane);
		}
		jobsPanel.add(inactiveJobsPanel);

		add(jobsPanel, BorderLayout.WEST);

		gridPanel = new JPanel();
		{
			gridInnerPanel = new GridPanel(server);
			gridPanel.setBorder(BorderFactory.createTitledBorder("Robot Warehouse"));
			gridPanel.add(gridInnerPanel);
		}

		add(gridPanel, BorderLayout.CENTER);

		robotDetailsPanel = new JPanel();
		{
			robotDetailsInnerPanel = new RobotPanel(server);
			robotDetailsPanel.setBorder(BorderFactory.createTitledBorder("Robots"));
			robotDetailsPanel.setPreferredSize(new Dimension(245, 100));
			robotDetailsPanel.setLayout(new BoxLayout(robotDetailsPanel, BoxLayout.X_AXIS));
			robotDetailsPanel.add(robotDetailsInnerPanel);
		}

		add(robotDetailsPanel, BorderLayout.SOUTH);

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
				xTextField.setPreferredSize(new Dimension(25, 20));
				JTextField yTextField = new JTextField();
				yTextField.setPreferredSize(new Dimension(25, 20));

				JRadioButton northButton = new JRadioButton("N");
				northButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						direction = 0;
					}

				});

				JRadioButton southButton = new JRadioButton("S");
				southButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						direction = 180;
					}

				});

				JRadioButton eastButton = new JRadioButton("E");
				eastButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						direction = 90;

					}

				});

				JRadioButton westButton = new JRadioButton("W");
				westButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						direction = 270;

					}

				});

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

						if ((xCoordinate.length() > 0) && (yCoordinate.length() > 0) && (direction != -1)) {
							gridInnerPanel.addRobot(Integer.parseInt(xCoordinate), Integer.parseInt(yCoordinate),
									direction);
							direction = -1;
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
				addRobotPanel.add(addRobotCoordinatePanel, BorderLayout.NORTH);
				addRobotPanel.add(addRobotDirectionPanel, BorderLayout.CENTER);
				addRobotPanel.add(okButton, BorderLayout.SOUTH);
				addRobotFrame.add(addRobotPanel);
				addRobotFrame.setPreferredSize(new Dimension(250, 120));
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
		//gridPanel.removeAll();

		for (String jobID : jobDataStore.getJobTable().keySet()) {
			Job j = jobDataStore.getJobTable().get(jobID);
			float percentageComplete = j.getPercentageComplete();

			if (j.getActive()) {
				activeJobsInnerPanel.add(new JobPanel(jobID, percentageComplete, jobDataStore));
			} else {
				inactiveJobsInnerPanel.add(new JobPanel(jobID));
			}

			//GridPanel gridInnerPanel = new GridPanel(server);
			//System.gc();
			
			//gridPanel.add(gridInnerPanel);

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

	private JLabel percentageCompleteLabel;

	/*
	 * Active Job Panel Constructor
	 */
	public JobPanel(String jobID, Float percentageComplete, JobTable jobDataStore) {
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
				jobDataStore.setCancelled(jobID);
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

class RobotPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3025088648394354280L;
	private List<Robot> robotList;

	public RobotPanel(Server server) {
		robotList = server.getConnectedRobots();
		for (Robot r : robotList) {
			JPanel robotInnerPanel = new JPanel();
			JLabel robotNameLabel = new JLabel(r.getName());
			JLabel robotPositionLabel = new JLabel("Position: " + "(" + r.getX() + "," + r.getY() + ")");
			JLabel robotWeightLabel = new JLabel("Weight: " + r.getCurrentWeight() + "/" + r.getMaxWeight());
			JLabel robotDestinationLabel = new JLabel("Destination: (" + r.getDestinationX() + "," + r.getDestinationY() + ")");
			robotInnerPanel.setLayout(new BoxLayout(robotInnerPanel, BoxLayout.Y_AXIS));
			robotInnerPanel.add(robotNameLabel);//, new BoxLayout(robotInnerPanel, BoxLayout.Y_AXIS));
			robotInnerPanel.add(robotPositionLabel);//, new BoxLayout(robotInnerPanel, BoxLayout.Y_AXIS));
			robotInnerPanel.add(robotDestinationLabel);
			robotInnerPanel.add(robotWeightLabel);//, new BoxLayout(robotInnerPanel, BoxLayout.Y_AXIS));
			
			add(robotInnerPanel);
		}
	}
}

class GridPanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	//private static final long serialVersionUID = 892150393382176613L;
	private GridMap gridMap;
	private MapBasedSimulation sim;
	private ArrayList<MobileRobotWrapper<MovableRobot>> wrapperList;
	private List<Robot> robotList;
	private ConcurrentHashMap<String, GridPilot> robotTable;
	GridPositionDistribution dist;
	GridPositionDistributionVisualisation mapVis;

	private Server server;

	public GridPanel(Server server) {
		setPreferredSize(new Dimension(500, 500));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		gridMap = MapUtils.createRealWarehouse();
		sim = new MapBasedSimulation(gridMap);
		wrapperList = new ArrayList<MobileRobotWrapper<MovableRobot>>();
		this.robotTable = new ConcurrentHashMap<>();
		this.robotList = server.getConnectedRobots();
		List<Robot> searchList = new ArrayList<Robot>(robotList);
		for (Robot r : searchList) {
			GridPilot p = addRobot(r.getCurrentX(), r.getCurrentY(), 0);
			robotTable.put(r.getName(), p);
		}
		
		Thread gridPanelThread = new Thread(this);
		gridPanelThread.start();

	}

	public GridPilot addRobot(int x, int y, int direction) {
		return addRobot(x, y, direction, true);
	}

	public GridPilot addRobot(int x, int y, int direction, boolean updateScreen) {
		GridPose gridStart;

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

		MobileRobotWrapper<MovableRobot> wrapper = sim.addRobot(SimulatedRobots.makeConfiguration(false, true),
				gridMap.toPose(gridStart));

		if (updateScreen) {
			wrapperList.add(wrapper);

			dist = new GridPositionDistribution(gridMap);
			mapVis = new GridPositionDistributionVisualisation(dist, gridMap);
			MapVisualisationComponent.populateVisualisation(mapVis, sim);
			removeAll();
			add(mapVis);
		}

		return new GridPilot(wrapper.getRobot().getPilot(), gridMap, gridStart);
	}

	@Override
	public void run() {
		while(true) {
			List<Robot> searchList = new ArrayList<Robot>(robotList);
			for (Robot r : searchList) {
				GridPilot p = addRobot(r.getCurrentX(), r.getCurrentY(), 0, false);
				GridPose prev = robotTable.get(r.getName()).getGridPose();
				GridPose post = p.getGridPose();
				if((prev.getX()!=post.getX()) || (prev.getY() != post.getY())) {
					System.out.println("Position Changed");
				}
				robotTable.get(r.getName()).setGridPose(p.getGridPose());
			}
		}
		
	}
}