package bluetooth.threads;

public class Message {

	private final Byte command;
	private String info = "";
	
	public Message(Byte command) {
		this.command = command;
	}
	
	public Message(Byte command, String info) {
		this(command);
		this.info = info;
	}
	
	public Byte getCommand() {
		return command;
	}
	
	public String getInfo() {
		return info;
	}
}
