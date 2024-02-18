

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ForkJoinTask;

import javax.management.loading.PrivateClassLoader;

public class Alvo extends Thread {

	private static int idCount = 1;
	private int id;
	private int coordStartX;
	private int coordStartY;
	private int coordEndX;
	private int coordEndY;
	private int locX;
	private int locY;
	private long timestamp;
	private long posRate;
	private boolean destiny;
	private boolean killed;
	private boolean visible;
	private boolean marked;
	private double alvoRate;
	private long totalTime;
	private ArrayList<Integer> speedList = new ArrayList<>();
	private ArrayList<Double> avgSpeed = new ArrayList<>();
	private ArrayList<Integer> posList = new ArrayList<>();
	private int estimSpeed; 

	public Alvo(int coordStartX, int coordStartY, int coordEndX, int coordEndY, long timestamp, long posRate) {

		this.id = idCount;
		this.coordStartX = coordStartX;
		this.coordStartY = coordStartY;
		this.coordEndX = coordEndX;
		this.coordEndY = coordEndY;
		this.locX = coordStartX;
		this.locY = coordStartY;
		this.timestamp = timestamp;
		this.posRate = posRate;
		this.destiny = false;
		this.killed = false;
		this.visible = true;
		this.marked = false;
		this.totalTime = 5000;
		idCount++;
		for (int i = 0; i <=10; i++) {
			posList.add(55*i);
		}

	}

	public int getCoordStartX() {
		return coordStartX;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getPosRate() {
		return posRate;
	}

	public void setPosRate(long posRate) {
		this.posRate = posRate;
	}

	public boolean isDestiny() {
		return destiny;
	}

	public void setDestiny(boolean destiny) {
		this.destiny = destiny;
	}

	public boolean isKilled() {
		return killed;
	}

	public boolean isVisible() {
		return visible;
	}

	public boolean isCalculated() {
		return marked;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void setEstimSpeed(int value) {
		this.estimSpeed = value;
	}
	
	public int getEstimSpeed() {
		return estimSpeed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}

	public void setCalculated(boolean calculated) {
		this.marked = calculated;
	}

	public void setCoordStartX(int coordStartX) {
		this.coordStartX = coordStartX;
	}

	public void setCoordStartY(int coordStartY) {
		this.coordStartY = coordStartY;
	}

	public void setCoordEndX(int coordEndX) {
		this.coordEndX = coordEndX;
	}

	public void setCoordEndY(int coordEndY) {
		this.coordEndY = coordEndY;
	}

	public void setLocY(int locY) {
		this.locY = locY;
	}

	public int getLocY() {
		return locY;
	}
	
	public int getLocX() {
		return locX;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getCoordStartY() {
		return coordStartY;
	}

	public int getCoordEndX() {
		return coordEndX;
	}

	public int getCoordEndY() {
		return coordEndY;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public double getRate() {
		return alvoRate;
	}

	public void setRate(double rate) {
		this.alvoRate = rate;
		this.marked = true;
	}
	
	public long getId() {
		return id;
	}
	
	public int speedSetter() {
		//cerca de 500 pixels para o alvo percorrer em 5 segundos
		
		/**
		 * 100p - 1s
		 * x - 0.03
		 * velocidade inicial de 3 pixels por segundo
		 */
		int min = 1;
		int max = 2;
		int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
		return random_int;
	}
	
	public double speedCalculator(long initTime, long endTime) {
		
		double estimatedSpeed = (55)/(endTime-initTime);
		return estimatedSpeed;
	}

	@Override
	public void run() {
		while (isVisible() && !isDestiny()) {
			// existe um tempo máximo de 5 segundos para o alvo chegar do outro lado
			this.setEstimSpeed(2 + speedSetter());
			speedList.add(this.getEstimSpeed());
			System.out.println("Vel_Média_Alvo: " + this.getEstimSpeed());
			locY += (this.getEstimSpeed());

			setLocY(locY);

			try {
				Thread.sleep(posRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (locY > 500) {
				setDestiny(Boolean.TRUE);
			}
		}
	}

}
