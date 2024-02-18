

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.security.PublicKey;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Interface extends JFrame {

	private static final long serialVersionUID = -259747138688691732L;
	BufferedImage backBuffer;
	int FPS = 30;
	int janelaW = 800;
	int janelaH = 500;
	private static final String NEWSTATE = "NEW";
	private Alvo alvo1;
	private Alvo alvo2;
	private Lancador lancador1;
	private Lancador lancador2;
	private Lancador lancador3;
	private List<Lancador> shooterList = new ArrayList<>();
	private List<Alvo> targetList = new ArrayList<>();
	private HashMap<Long, Integer> calculations;
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private static int alvosAcertados;

	public Interface() {
		alvo1 = new Alvo(230, 55, 100, 470, new Date().getTime(), 30);
		alvo2 = new Alvo(520, 55, 350, 470, new Date().getTime(), 30);
		alvosAcertados = 0;

		targetList.add(alvo1);
		targetList.add(alvo2);

		lancador1 = new Lancador(350, 430);
		lancador2 = new Lancador(150, 430);
		lancador3 = new Lancador(550, 430);

		lancador1.start();
		lancador2.start();
		lancador3.start();

		shooterList.add(lancador1);
		shooterList.add(lancador2);
		shooterList.add(lancador3);

		calculations = new HashMap<>();

	}

	public Alvo createTarget() {
		int min = -100;
		int max = 200;
		int rand = (int) Math.floor(Math.random() * (max - min + 1) + min);

		Alvo a = new Alvo(230 + rand, 55, 100, 470, new Date().getTime(), 30);
		targetList.add(a);

		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return a;
	}

	public boolean chockDetection(int pontoX, int pontoY, int x, int y, int w, int h) {

		if ((pontoX >= x && pontoX <= x + w) && (pontoY >= y && pontoY <= y + h)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean colisionDetection(Alvo a, Tiro t) {
		Rectangle r1 = null;
		Rectangle r2 = null;
		if (t != null) {
			r1 = getTargetBounds(a);
			r2 = getShotBounds(t);

			if (r1.intersects(r2)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Rectangle getTargetBounds(Alvo a) {
		return new Rectangle(a.getCoordStartX(), a.getLocY(), 30, 30);
	}

	public Rectangle getShotBounds(Tiro t) {
		return new Rectangle(t.getLocX(), t.getLocY(), 25, 25);
	}

	public void desenharGraficos(List<Tiro> shotList, List<Alvo> targetList, List<Lancador> lancadorList) {

		Graphics g = getGraphics();
		Graphics bbg = backBuffer.getGraphics();

		ImageIcon spaceShip = new ImageIcon("src/spaceship.jpg");

		bbg.setColor(Color.WHITE);
		bbg.fillRect(0, 0, janelaW, janelaH);

		bbg.drawImage(spaceShip.getImage(), lancador1.getLocX(), 430, 50, 50, this);
		bbg.setColor(Color.RED);

		bbg.drawImage(spaceShip.getImage(), lancador2.getLocX(), 430, 50, 50, this);
		bbg.setColor(Color.RED);

		bbg.drawImage(spaceShip.getImage(), lancador3.getLocX(), 430, 50, 50, this);
		bbg.setColor(Color.RED);

		for (Alvo target : targetList) {
			if (target.isVisible()) {
				// Inicia a Thread se já não estiver iniciada
				if (target.getState().toString() == NEWSTATE) {
					target.start();
				}
				// Desenha os alvos na tela
				bbg.setColor(Color.BLACK);
				bbg.fillOval(target.getCoordStartX(), target.getLocY(), 25, 25);
				// Verifica se o alvo foi atingido
				target.setKilled(colisionDetection(target, findByIdTiro(target.getId(), shotList)));
				if (target.isKilled()) {
					target.setVisible(Boolean.FALSE);
					findByIdTiro(target.getId(), shotList).setContact(Boolean.TRUE);
					findByIdTiro(target.getId(), shotList).setVisibility(Boolean.FALSE);
					findLancadorByIdTiro(findByIdTiro(target.getId(), shotList), lancadorList).addAmmo();
					alvosAcertados++;
				}

				if (!target.isCalculated()) {
					posCalculation(target);
					target.setCalculated(Boolean.TRUE);
				}
			}
		}

		for (Tiro t : shotList) {
			if (t.isVisible()) {
				bbg.setColor(Color.RED);
				bbg.fillOval(t.getLocX(), t.getLocY(), 15, 15);
			}
		}

		bbg.setFont(new Font("helvica", Font.BOLD, 20));
		bbg.drawString("Alvos acertados :" + alvosAcertados, 400, 100);
		bbg.setColor(Color.RED);

		g.drawImage(backBuffer, 0, 0, this);
	}

	public Tiro findByIdTiro(long idTiro, List<Tiro> test) {
		for (Tiro t : test) {
			if (t.getIdTiro() == idTiro) {
				return t;
			}
		}
		return null;
	}

	public Lancador findLancadorByIdTiro(Tiro tiro, List<Lancador> Lancadorlist) {
		for (Lancador l : Lancadorlist) {
			if (tiro != null) {
				if (tiro.getIdLancador() == l.getId()) {
					return l;
				}
			}
		}
		return null;
	}

	public Alvo findById(long idAlvo, List<Alvo> targetList) {
		for (Alvo target : targetList) {
			if (target.getId() == idAlvo) {
				return target;
			}
		}
		return null;
	}

	public void inicializar() {
		setTitle("Exercício - Alvos Móveis");
		setSize(janelaW, janelaH);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setVisible(true);
		backBuffer = new BufferedImage(janelaW, janelaH, BufferedImage.TYPE_INT_RGB);
	}
	
	
	public void infoWatcher() {
		
		Runtime runtime = Runtime.getRuntime();
		NumberFormat format = NumberFormat.getInstance();

		StringBuilder stringBuilder = new StringBuilder();
		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

	    stringBuilder.append("Free Memory: " + format.format(freeMemory / 1024) + " ");
		stringBuilder.append("Allocated Memory: " + format.format(allocatedMemory / 1024) + " ");
		stringBuilder.append("Maximum Memory: " + format.format(maxMemory / 1024) + " ");
		stringBuilder.append("Total Free Memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "<br/>");
		stringBuilder.append("Used Memory: " + format.format((allocatedMemory- freeMemory) / 1024) + " ");
		stringBuilder.append("Percentage of use : " + format.format(100- (freeMemory*100)/allocatedMemory) + " ");
		stringBuilder.append("Processor Cores: " + runtime.availableProcessors() + " ");
		
		//System.out.println(stringBuilder);

	}

	public double posCalculation(Alvo alvo) {

		int min = 10;
		int max = 20;

		int estimatedXdist = Math.abs(350 - alvo.getCoordStartX());

		// Timestamp from when the target was launched
		long timestampTarget = alvo.getTimestamp();

		// Timestamp from when the shoot was shot
		long timestampShoot = new Date().getTime();

		// Difference between both
		long timestampDiff = (timestampShoot - timestampTarget);

		// How much pixels target has shifted
		int estimatedPixels = (int) ((timestampDiff / 30) * 5);

		// Estimated Y position of target
		int estimatedY = (alvo.getCoordStartY() + estimatedPixels) + estimatedXdist;

		calculations.put(alvo.getId(), estimatedY);
		return estimatedY;

	}

	public void executar() {
		inicializar();
		List<Tiro> shotList = new ArrayList<>();

		while (true) {
			infoWatcher();
			executor.scheduleAtFixedRate(createTarget(), 0, 10, TimeUnit.MILLISECONDS);

			for (Alvo a : targetList) {
				for (Lancador l : shooterList) {
					if (l.getState().toString() == NEWSTATE) {
						l.start();
					}
					if (!l.getLoader().isEmpty()) {
						if (!a.isCalculated()) {
							a.setRate(posCalculation(a));
							shotList.add(l.warTime(a));
						}
					}
				}
			}

			for (Tiro t : shotList) {
				if (t.getState().toString() == NEWSTATE) {
					t.start();
				}
			}
			desenharGraficos(shotList, targetList, shooterList);

			try {
				Thread.sleep(1000 / FPS);
			} catch (Exception e) {
				System.out.println("Thread interrompida!");
			}

		}
	}
}
