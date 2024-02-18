

import java.util.ArrayList;

import javax.swing.plaf.synth.SynthOptionPaneUI;


public class Tiro extends Thread{
	
	private static int idCount = 1;
	private int id;
	private long idTiro; 
	private Municao ammunition;
	private long xAlvo;
	private long yAlvo;
	private double rateAlvo;
	private int coordStartX;
	private int coordStartY;
	private int coordEndX;
	private int coordEndY;
	private int locX;
	private int locY;
	private long timestamp;
	private long posRate;
	private boolean contact;
	private boolean isVisible;
	private int idLancador; 
	private Alvo alvo;
	Reconciliation rec;
	private ArrayList<Double>recValues = new ArrayList<>();
	
	public Tiro(Municao ammunition, int coordStartX, int coordStartY,
			int coordEndX,int coordEndY, long timestamp,
			long posRate, long idTiro, long xAlvo, long yAlvo, Alvo alvo, int idLancador) {
		
		this.id = idCount;
		this.idTiro = idTiro;
		this.ammunition = ammunition;
		this.coordStartX = coordStartX;
		this.coordStartY = coordStartY;
		this.coordEndX = coordEndX;
		this.coordEndY = coordEndY;
		this.locX = coordStartX;
		this.locY = coordStartY;
		this.timestamp = timestamp;
		this.posRate = posRate; 
		this.contact = false;
		this.xAlvo = xAlvo;
		this.yAlvo = yAlvo;
		this.alvo = alvo;
		isVisible = true;
		this.idLancador = idLancador;
		double[] y = new double[] { 3, 3, 4, 4, 4, 3 ,3};

		double[] v = new double[] { 0.503, 0.503, 0.505, 0.5041, 0.50312, 0.503,0.503};

		double[][] A = new double[][] { { 5, 1, 1, 1, 1, 1 ,1}};
		rec = new Reconciliation(y, v, A);
		for (int i = 0; i < rec.getReconciledFlow().length; i++) {
			recValues.add(rec.getReconciledFlow()[i]);
		}
	
		idCount++;

	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getIdLancador() {
		return idLancador;
	}
	
	public void setIdTiro(long idTiro) {
		this.idTiro = idTiro;
	}
	
	public long getIdTiro() {
		return idTiro;
	}

	public int getCoordStartX() {
		return coordStartX;
	}

	public void setCoordStartX(int coordStartX) {
		this.coordStartX = coordStartX;
	}

	public int getCoordStartY() {
		return coordStartY;
	}

	public void setCoordStartY(int coordStartY) {
		this.coordStartY = coordStartY;
	}

	public int getCoordEndX() {
		return coordEndX;
	}

	public void setCoordEndX(int coordEndX) {
		this.coordEndX = coordEndX;
	}

	public int getCoordEndY() {
		return coordEndY;
	}

	public void setCoordEndY(int coordEndY) {
		this.coordEndY = coordEndY;
	}

	public int getLocX() {
		return locX;
	}

	public void setLocX(int locX) {
		this.locX = locX;
	}

	public int getLocY() {
		return locY;
	}

	public void setLocY(int locY) {
		this.locY = locY;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getPosRate() {
		return posRate;
	}

	public void setPosRate(long posRate) {
		this.posRate = posRate;
	}

	public boolean isContact() {
		return contact;
	}

	public void setContact(boolean contact) {
		this.contact = contact;
	}

	public Municao getAmmunition() {
		return ammunition;
	}

	public void setAmmunition(Municao ammunition) {
		this.ammunition = ammunition;
	}
	
	public boolean isVisible () {
		return isVisible;
	}
	
	public void setVisibility(Boolean visible) {
		isVisible = visible;
	}

	@Override
	public void run() {

		while (!isContact() && isVisible()) {
			
			double dx = xAlvo - this.getCoordStartX();
			double dy = alvo.getLocY() - this.getCoordStartY();

			double moveX = (dx / 100) * 1;
			double moveY = (dy / 100) * 1;
			
			double meanRec = 0;
			double sumRec = 0;
			
			double bigger = Math.max(Math.abs(moveX), Math.abs(moveY));

			for (Double recValue: recValues) {
				sumRec+=recValue;
			}
			meanRec = sumRec/recValues.size();
			this.locX += moveX*(5/bigger)*(meanRec);
			this.locY += moveY*(5/bigger)*(meanRec);
		
			setLocX(locX);
			setLocY(locY);
			try {
				Thread.sleep(posRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}
