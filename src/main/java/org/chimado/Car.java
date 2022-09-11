package org.chimado;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static org.chimado.TrackScreen.height;

public class Car extends Thread implements KeyListener {
    TrackScreen panel;

    private final int playerWidth = 50, playerHeight = 70;;
    public int playerX = 0, playerY = height / 2, velocity = 0;
    Rectangle hitBox = new Rectangle();
    Image Texture;

    public Car(TrackScreen panel)
    {
        this.panel = panel;
        hitBox.setBounds(playerX, playerY, playerWidth, playerHeight);
        Texture = new ImageIcon("src/main/resources/carStationary.png").getImage();

        start();
    }

    public void run()
    {
        while(true)
        {
            updatePlayer();

            try {
                Thread.sleep(20);
            }catch (InterruptedException e) {}

            panel.repaint();
        }
    }

    private void updatePlayer() {
        playerY -= velocity;

        hitBox.x = playerX;
        hitBox.y = playerY;
    }

    public void draw(Graphics g) {
        g.drawImage(Texture, playerX, playerY, playerWidth, playerHeight,null);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        if (key != null) {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_W:
                    velocity += 10;
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
