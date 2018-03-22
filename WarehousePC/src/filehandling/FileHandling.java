package filehandling;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Minhal - Job Selection
 */
public class FileHandling {
	
	public final static String TRAINING_NAME = "training_jobs";
	public final static String CANCELLATION_NAME = "cancellations";
	public final static String ITEM_FILE_NAME = "items";
	public final static String JOBS_FILE_NAME = "jobs";
	public final static String ITEM_LOCATION_FILE_NAME = "locations";
	private final static String FOLDER_PATH = "/WarehousePC/Resources/";
	private final static String DOT_CSV = ".csv";
	public final static String cvsSplitBy = ","; //split by a comma
	
	public static FileReader getFileReader(String fileName) throws FileNotFoundException{
		String directory = System.getProperty("user.dir") + FOLDER_PATH + fileName + DOT_CSV; //   .../FOLDER_NAME/'fileName'.csv
		return new FileReader(directory);
	}
	
	
}
