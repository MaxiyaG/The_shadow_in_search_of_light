package Main;

import Entity.Player;
import Map.mapManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // Configuration
    final int Size = 16; //16x16
    final int scale = 3; // 16x3x scale
    public final int titeSize = Size * scale; // 48x48 tite
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = titeSize * maxScreenCol;
    public final int ScreenHeight = titeSize * maxScreenRow;


    // Variable déroulement du jeu
    int FPS = 60;
    public Thread gameThread;
    KeyHandler keyH = new KeyHandler();

    // Creation de la map
    public mapManager mapM = new mapManager(this);
    //public Collision collision = new Collision(this);
    // Creation joueur
    Player player = new Player(this,keyH);

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startMusic();
    }

    public void startMusic(){
        player.playMusic("/src/Music/backgroundMusic.wav", true);
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / FPS;
        double delta = 0;

        while (gameThread != null) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                update(); // Update
                delta--;
            }

            repaint(); // Draw

            try {
                // Pour limiter le FPS à 60
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        mapM.draw(g2);
        player.draw(g2);

        g2.dispose();
    }

}
