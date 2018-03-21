package ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import filehandling.JobTable;
import lejos.util.Delay;
import types.Job;
import types.RobotPC;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;

/**
 * 
 * @author Brandon Goodwin, Osanne Gbayere
 *
 */
public class PCGUI extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 902776736269552333L;

	private static final String FRAME_TITLE = "Robot Control UI";

	private JMenuBar menuBar;
	
	private JPanel jobsPanel;
	
	private JPanel activeJobsPanel;
	private JScrollPane activeScrollPane;
	private JPanel activeJobsInnerPanel;
	
	private JPanel inactiveJobsPanel;
	private JScrollPane inactiveScrollPane;
	private JPanel inactiveJobsInnerPanel;
	
	private JPanel robotPanel;

	// This will be the JobTable class used to get information about the jobs
	// And will require certain methods to be added to the JobTable class
	private JobTable jobDataStore;

	private ArrayList<RobotPC> robots;
	
	private String itemFileLocation = "/Resources/items.csv";
	
	private String locationFileLocation = "/Resources/locations.csv";
	
	private String jobFileLocation = "/Resources/jobs.csv";
	
	private String cancellationFileLocation = "/Resources/cancellations.csv";

	public PCGUI(JobTable jobDataStore, ArrayList<RobotPC> robots) {
		this.jobDataStore = jobDataStore;
		this.robots = robots;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(500, 500));
		setTitle(FRAME_TITLE);
		setVisible(true);
		
		menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        // Menu items to load in the frequencies file and the cipher file
        JMenuItem loadJobsItem = new JMenuItem("Load Jobs File");
        loadJobsItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				  JFileChooser fileChooser = new JFileChooser();
			        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			        fileChooser.setDialogTitle("Open File");
			        // Only allow csv files
			        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (.csv)", "csv"));

			        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			            jobFileLocation = fileChooser.getSelectedFile().getAbsolutePath();
			        }
			        else{
			            // If no file is found return null
			            jobFileLocation = null;
			        }
			}
		});
        fileMenu.add(loadJobsItem);

        JMenuItem loadItemsItem = new JMenuItem("Load items File");
        loadItemsItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				  JFileChooser fileChooser = new JFileChooser();
			        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			        fileChooser.setDialogTitle("Open File");
			        // Only allow csv files
			        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (.csv)", "csv"));

			        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			            itemFileLocation = fileChooser.getSelectedFile().getAbsolutePath();
			        }
			        else{
			            // If no file is found return null
			            itemFileLocation = null;
			        }
			}
		});
        fileMenu.add(loadItemsItem);
        
        JMenuItem loadCancellationsItem = new JMenuItem("Load Cancellations File");
        loadCancellationsItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				  JFileChooser fileChooser = new JFileChooser();
			        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			        fileChooser.setDialogTitle("Open File");
			        // Only allow csv files
			        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (.csv)", "csv"));

			        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			            cancellationFileLocation = fileChooser.getSelectedFile().getAbsolutePath();
			        }
			        else{
			            // If no file is found return null
			            cancellationFileLocation = null;
			        }
			}
		});
        fileMenu.add(loadCancellationsItem);
        
        setJMenuBar(menuBar);

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
			inactiveJobsPanel.setPreferredSize(new Dimension(250, 243));
			inactiveJobsPanel.setLayout(new BoxLayout(inactiveJobsPanel, BoxLayout.Y_AXIS));

			inactiveJobsInnerPanel = new JPanel();
			inactiveJobsInnerPanel.setLayout(new BoxLayout(inactiveJobsInnerPanel, BoxLayout.Y_AXIS));

			inactiveScrollPane = new JScrollPane(inactiveJobsInnerPanel);
			inactiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			inactiveJobsPanel.add(inactiveScrollPane);
		}
		jobsPanel.add(inactiveJobsPanel);

		add(jobsPanel, BorderLayout.WEST);

		robotPanel = new JPanel();
		{
			robotPanel.setBorder(BorderFactory.createTitledBorder("Robots"));
			robotPanel.setPreferredSize(new Dimension(245, 500));
			robotPanel.setLayout(new BoxLayout(robotPanel, BoxLayout.Y_AXIS));
		}
		
		updateUI();
		
		add(robotPanel, BorderLayout.EAST);

		pack();

	}
	
	public String getItemFileLocation() {
		return itemFileLocation;
	}
	
	public String getLocationFileLocation() {
		return locationFileLocation;
	}
	
	public String getJobFileLocation() {
		return jobFileLocation;
	}
	
	public String getCancellationFileLocation() {
		return cancellationFileLocation;
	}
	
	public void updateUI() {
		activeJobsInnerPanel.removeAll();
		inactiveJobsInnerPanel.removeAll();
		for(String jobID : jobDataStore.getJobTable().keySet()) {
			Job j = jobDataStore.getJobTable().get(jobID);
			float percentageComplete = j.getPercentageComplete();
				
			if(j.getActive()) {
				activeJobsInnerPanel.add(new JobPanel(jobID,percentageComplete));
			} else {
				inactiveJobsInnerPanel.add(new JobPanel(jobID));
			}
			
			
		}
		revalidate();
	}

	@Override
	public void run() {
	
		while(true) {
			// Maybe set a delay
			Delay.msDelay(1000);
			updateUI();
		}
	}
}

// Wont take the same form and more
// Will have cancel button, % complete if it is an active job and JobID
class JobPanel extends JPanel {

	private static final long serialVersionUID = -6584237164987663902L;

	private JLabel jobLabel;

	private JButton cancelButton;

	private JobTable jobDataStore;
	
	private JLabel percentageCompleteLabel;

	/*
	 * Active Job Panel Constructor
	 */
	public JobPanel(String jobID, Float percentageComplete){
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