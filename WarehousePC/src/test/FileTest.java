package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import filehandling.ItemTable;
import filehandling.JobTable;
import filehandling.TrainingJobs;
import types.Job;


public class FileTest {

	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		
		JobTable jt = new JobTable();
		
		Job x = jt.popQueue();
		while (x != null) {
			System.out.println(x.cancelPredictor +  "  " + x.getTotalReward());
			x = jt.popQueue();
		}

		//TrainingJobs tj = new TrainingJobs();
		//System.out.println("success rate: " + tj.successRate());
		
		//System.out.println(table.queue.peek().getTotalReward());
		//System.out.println(table.getReward("1001"));
		//table.itemTable.get("a");
	}

}
