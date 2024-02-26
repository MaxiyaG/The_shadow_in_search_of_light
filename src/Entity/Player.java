package Entity;

import Main.GamePanel;
import Main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import java.io.File;

import static Main.Main.main;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyH;

    public Player(GamePanel gamePanel, KeyHandler keyH) {
        this.gamePanel = gamePanel;
        this.keyH = keyH;

        direction = "down";
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.height = 32;
        solidArea.width = 32;

        setDefaultConfig();
        getPlayerImage();
    }

    public void setDefaultConfig() {
        x = 40;
        y = 40;
        speed = 4;
    }

    public void getPlayerImage() {
        try {
            right1 = ImageIO.read(getClass().getResourceAsStream("/Image/player/r1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/Image/player/r2.png"));
            up1 = ImageIO.read(getClass().getResourceAsStream("/Image/player/u1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/Image/player/u2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/Image/player/l1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/Image/player/l2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/Image/player/d1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/Image/player/d2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void update() {
        boolean isMoving = false; // Vérifier si le personnage est en mouvement

        int nextX = x;
        int nextY = y;

        if (keyH.upPressed) {
            direction = "up";
            nextY -= speed;
            isMoving = true;
        } else if (keyH.downPressed) {
            direction = "down";
            nextY += speed;
            isMoving = true;
        } else if (keyH.leftPressed) {
            direction = "left";
            nextX -= speed;
            isMoving = true;
        } else if (keyH.rightPressed) {
            direction = "right";
            nextX += speed;
            isMoving = true;
        }

        // Vérifier la collision avec les murs
        if (checkCollision(nextX, nextY)) {
            // Si pas de collision, mettre à jour la position du joueur
            x = nextX;
            y = nextY;
        }

        // Réinitialiser l'animation si le personnage ne bouge pas
        if (!isMoving) {
            spriteCounter = 0;
        } else {
            // Incrémenter le compteur de sprite uniquement lorsque le personnage est en mouvement
            spriteCounter++;
            if (spriteCounter > 12) {
                if (sprintNum == 1) {
                    sprintNum = 2;
                } else if (sprintNum == 2) {
                    sprintNum = 1;
                }
                spriteCounter = 0;
            }
        }

        // Vérifier la collision avec un arbre
        if (checkTreeCollision(nextX, nextY)) {
            winPlayer(); // Appeler la méthode winPlayer si une collision avec un arbre est détectée
        }
    }

    private boolean checkTreeCollision(int nextX, int nextY) {
        Rectangle playerRect = new Rectangle(nextX + solidArea.x, nextY + solidArea.y, solidArea.width, solidArea.height);

        // Vérifier la collision avec chaque arbre
        for (int row = 0; row < gamePanel.maxScreenRow; row++) {
            for (int col = 0; col < gamePanel.maxScreenCol; col++) {
                int numTile = gamePanel.mapM.tileNum[row][col];
                if (numTile == 2) { // Vérifier si c'est un arbre
                    Rectangle treeRect = new Rectangle(col * gamePanel.titeSize, row * gamePanel.titeSize, gamePanel.titeSize, gamePanel.titeSize);
                    if (playerRect.intersects(treeRect)) {
                        return true; // Collision avec un arbre détectée
                    }
                }
            }
        }
        return false; // Pas de collision avec un arbre
    }

    private void winPlayer() {

        gamePanel.gameThread = null;

        String filePath = "/src/Music/winfantasia.wav";

        playMusic(filePath, false);

        JOptionPane.showMessageDialog(gamePanel, "Félicitations! Vous avez gagné!", "Victoire", JOptionPane.INFORMATION_MESSAGE);

        // Afficher les boutons "Quitter" et "Rejouer" dans une boîte de dialogue
        int choice = JOptionPane.showOptionDialog(gamePanel, "Que voulez-vous faire maintenant?", "Choix", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Quitter", "Rejouer"}, "Quitter");

        // Traiter le choix de l'utilisateur
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0); // Quitter le programme
        } else if (choice == JOptionPane.NO_OPTION) {
            // Rejouer en appelant à nouveau la méthode main
            main(new String[]{});
        }
    }

    private Clip currentClip;
    private boolean isLooping = false;

    public void playMusic(String musicLoc, boolean loop) {
        try {
            if (currentClip != null && currentClip.isRunning()) {
                currentClip.stop();
            }

            String currentDir = System.getProperty("user.dir");
            File musicPath = new File(currentDir, musicLoc);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.loop(0);
                }
                currentClip = clip;
                isLooping = loop;
            } else {
                System.out.println("Impossible de trouver le fichier audio : " + musicLoc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void stopMusic() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
    }

    public void resumeMusic() {
        if (currentClip != null && !currentClip.isRunning()) {
            currentClip.start();
        }
    }



    private boolean checkCollision(int nextX, int nextY) {
        Rectangle playerRect = new Rectangle(nextX + solidArea.x, nextY + solidArea.y, solidArea.width, solidArea.height);

        for (int row = 0; row < gamePanel.maxScreenRow; row++) {
            for (int col = 0; col < gamePanel.maxScreenCol; col++) {
                int numTile = gamePanel.mapM.tileNum[row][col];
                if (numTile == 1) { // Vérifier si c'est un mur
                    Rectangle wallRect = new Rectangle(col * gamePanel.titeSize, row * gamePanel.titeSize, gamePanel.titeSize, gamePanel.titeSize);
                    if (playerRect.intersects(wallRect)) {
                        return false; // Collision détectée
                    }
                }
            }
        }
        return true; // Pas de collision
    }




    public void draw(Graphics2D g2){
        BufferedImage image = null;
        switch (direction){
            case "up":
                if(sprintNum == 1){
                    image = up1;
                } else if (sprintNum == 2) {
                    image = up2;
                }
                break;
            case "down":
                if(sprintNum == 1){
                    image = down1;
                } else if (sprintNum == 2) {
                    image = down2;
                }
                break;
            case "left":
                if(sprintNum == 1){
                    image = left1;
                } else if (sprintNum == 2) {
                    image = left2;
                }
                break;
            case "right":
                if(sprintNum == 1){
                    image = right1;
                } else if (sprintNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, x, y, gamePanel.titeSize, gamePanel.titeSize, null);
    }
}
