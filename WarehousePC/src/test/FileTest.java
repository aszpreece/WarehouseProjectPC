package test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

import filehandling.ItemTable;
import filehandling.JobTable;


public class FileTest {

	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		
		JobTable table = new JobTable();
		System.out.println(table.popQueue());
		ItemTable itemTable = new ItemTable();;
		itemTable.getItem("AA");
		//System.out.println(table.queue.peek().getTotalReward());
		//System.out.println(table.getReward("1001"));
		//table.itemTable.get("a");
	}

}
