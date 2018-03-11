package filehandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Minhal - Job Selection
 */
public class FileHandling {
	
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
