

import java.util.Date;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Lancador extends Thread {

	private static int idCount = 1;
	private int id;
	private int numberPermissions = 1;
	private Tiro tiro;
	private Alvo alvo;
	private int locX;
	private int locY;
	private Stack<Municao> loader = new Stack<>();
	private Semaphore semaphoreAcess = new Semaphore(numberPermissions);
	private Semaphore semaphoreLoad = new Semaphore(numberPermissions);
	private Semaphore semaphoreShoot = new Semaphore(numberPermissions);
	private boolean builded = false; 

	public Lancador(int locX, int locY) {
		
		this.id = idCount;
		this.locX = locX;
		this.locY = locY;
		loader.push(new Municao());
		loader.push(new Municao());
		loader.push(new Municao());
		
		idCount ++;
	
	}
	
	public long getId() {
		return id;
	}

	public Tiro getTiro() {
		return tiro;
	}
	
	public int getLocX() {
		return locX;
	}
	
	public boolean isBuilt() {
		return builded;
	}
	
	public void setBuilt(boolean builded) {
		this.builded = builded;
	}
	
	public int getLocY() {
		return locY;
	}

	public void setTiro(Tiro tiro) {
		this.tiro = tiro;
	}

	public Stack<Municao> getLoader() {
		return loader;
	}

	public void addAmmo() {
		loader.add(new Municao());
	}

	public void setLoader(Stack<Municao> loader) {
		this.loader = loader;
	}
	
	public void missedShot() {
		loader.pop();
		loader.pop();
	}

	@Override
	public void run() {
		warTime(alvo);
	}

	public void acessLoader() {
		try {
			semaphoreAcess.acquire();
			getLoader();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphoreAcess.release();
		}
	}

	public void loadShoot() {
		while (!loader.isEmpty()) {
			try {
				semaphoreLoad.acquire();
				//System.out.println("The shot is being loaded...");
				Thread.sleep(30);
				loader.pop();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				semaphoreLoad.release();
			}
			break;
		}
	}

	public void setShoot() {
		try {
			semaphoreShoot.acquire();
			Thread.sleep(30);
			//System.out.println("Calculating position...");

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphoreShoot.release();
		}
	}

	public Tiro shootForReal(int xLoc, int yLoc, long idTiro, Alvo alvo) {
		Tiro t = new Tiro(new Municao(), this.locX, this.locY, xLoc, yLoc, new Date().getTime(), 30, idTiro, xLoc, yLoc, alvo,this.id);
		return t;
	}

	public Tiro warTime(Alvo alvo) {
		loadShoot();
		setShoot();
		int coordStartX = 0;
		int locY = 0; 
		double rate = 0;
		long id = 0;
		if (alvo!= null) {
			coordStartX = alvo.getCoordStartX();
			locY = alvo.getLocY();
			rate = alvo.getRate();
			id = alvo.getId();
			
		}
		// Ao invés de receber a posição final do alvo, recebe o alvo
		Tiro t = shootForReal(coordStartX, locY, id, alvo);
		return t;
	}
}
