package Files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FileTest {

	public static void main(String[] args) throws IOException {
		JobTable table = new JobTable();
		System.out.println(table.popQueue());
		ItemTable itemTable = new ItemTable();;
		itemTable.getItem("AA");
		//System.out.println(table.queue.peek().getTotalReward());
		//System.out.println(table.getReward("1001"));
		//table.itemTable.get("a");
	}

}
