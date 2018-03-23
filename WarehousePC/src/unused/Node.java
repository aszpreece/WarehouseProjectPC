package unused;

public class Node {
	int x;
	int y;
	
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
	 
	 public String toString() {
		 return "(" + x + "," + y + ")";
	 }
}
