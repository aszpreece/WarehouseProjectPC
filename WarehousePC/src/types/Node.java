package types;

public class Node {
	public int x;
	public int y;
	
	 public Node(int _x, int _y) {
		x = _x;
		y = _y;
	}
	 
	 public void set(int _x, int _y) {
		 x = _x;
		 y = _y;
	 }
	 
	 public int getX() {
		 return x;
	 }
	 
	 public int getY() {
		 return y;
	 }
	 
	 public boolean equals(Object n) {
		 Node node = (Node)n;
		 return this.getX() == node.getX() && this.getY() == node.getY();
	 }
	 @Override
	 public String toString(){
		 return "X: " + x + " Y: " + y;
	 }
}
