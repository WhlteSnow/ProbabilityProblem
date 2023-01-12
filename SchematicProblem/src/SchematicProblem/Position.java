package finalproj;

import java.util.Objects;

public class Position {

	int x;
	int y;
	Position predecessor;
	private int hashCode;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
		this.hashCode = Objects.hash(x, y);
	}
	
	public void setValue(int x, int y) {
		this.x = x;
		this.y = y;
		this.hashCode = Objects.hash(x, y);
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y +")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Position)) {
			return false;
		}

		Position c = (Position) obj;

		return this.x == c.x && this.y == c.y;
	}
	
	@Override
    public int hashCode() {
        return this.hashCode;
    }
}
