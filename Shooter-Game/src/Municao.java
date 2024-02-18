

public class Municao {

	private static int idCount = 1;
	private int id;
	private boolean used;

	public Municao() {
		this.id = idCount;
		this.used = false;
		idCount++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
