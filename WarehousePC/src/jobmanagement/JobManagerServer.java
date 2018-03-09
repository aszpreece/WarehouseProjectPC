package jobmanagement;

import java.io.FileNotFoundException;
import java.io.IOException;

import filehandling.ItemTable;
import filehandling.JobTable;

public class JobManagerServer {
	
	ItemTable itemTable;
	JobTable jobTable;
	
	public JobManagerServer(){
		
		try {
			this.jobTable = new JobTable();
			this.itemTable = new ItemTable();
			
			
			
			
			
			
		} catch (FileNotFoundException i){
			//File not found
			i.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	public static void main(String[] args) {

		/*
		 * Make first actions threads so they can happen at the same time: 
		 * 
		 * Localisation takes the longest so start that first. 
		 * 
		 * Start reading and decoding files that takes less time.
		 * 
		 * Then once reading and decoding files start calculating job
		 * selection (If it doesn't require information about robot locations).
		 */

		
		
		/*
		 * Starting with localisation (This only works if implementation is complete
		 * before starting job allocation). (FOR A SINGLE ROBOT)
		 */
		//RobotLocalizer rl = new RobotLocalizer();
		//rl.start();

		// Run the JobSelector/Classifier if it doesn't require knowledge of robot location
		//JobSelecter js = new JobSelecter();
		// Or
		//JobClassifier jc = new JobClassifier();
		
//		
//		try {
//			/*
//			 * This will change in a model where the job selector keeps running
//			 * for the duration of the system
//			 */
//			// Wait for the job selector to finish as it is necessary to continue
//			js.join();
//		
//		
//			// Once localisation is complete we can continue
//			rl.join();
//		} catch (InterruptedException e) {
//			// Something went wrong abandon ship
//			e.printStackTrace();
//		}
//		
//		Integer currentX = rl.getCurrentX();
//		Integer currentY = rl.getCurrentY();
//		Integer currentDirection = rl.getCurrentDirection(); // Get current direction in degrees (0, 90, 180 or 270)
//		
	}

}
