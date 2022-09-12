package org.chimado;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics2D;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.chimado.TrackScreen.height;

public class Car extends Thread implements KeyListener {
    TrackScreen panel;

    private final Set<Character> pressedKeys = new HashSet<Character>();
    private final int playerWidth = 50, playerHeight = 70;
    private final float timeModifier = 1f, baseAcceleration = 0.0001f, baseAngleChange = 0.1f;
    public float playerX = 0, playerY = height / 2, velocity = 0, angle = 0;
    public Boolean throttle = false, breaks = false, left = false, right = false;
    private long deltaTime = 0;
    Instant start, finish;
    Image Texture;

    public Car(TrackScreen panel)
    {
        this.panel = panel;
        Texture = new ImageIcon("src/main/resources/carStationary.png").getImage();

        start();
    }

    public void run()
    {
        while(true)
        {
            start = Instant.now();
            updatePlayer();
            panel.repaint();

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}

            finish = Instant.now();
            deltaTime = Duration.between(start, finish).toMillis();
            System.out.println(deltaTime);
        }
    }

    private void updatePlayer() {
        playerX += Math.sin(Math.toRadians(angle)) * velocity * deltaTime;
        playerY -= Math.cos(Math.toRadians(angle)) * velocity * deltaTime;

        if(throttle) velocity += baseAcceleration * deltaTime;
        else if(breaks) velocity -= baseAcceleration * deltaTime;
        else velocity -= baseAcceleration * deltaTime / 5;

        if(velocity < 0) velocity = 0;
        else if(velocity >= 20) velocity = 20;

        if(right) angle += baseAngleChange * deltaTime;
        else if(left) angle -= baseAngleChange * deltaTime;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Point p = new Point((int) (playerX+playerWidth/2), (int) (playerY+playerHeight/2));
        g2.rotate(Math.toRadians(angle), p.x,p.y);
        g2.drawImage(Texture, (int) playerX, (int) playerY, playerWidth, playerHeight, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        throttle = false;
        breaks = false;
        left = false;
        right = false;

        pressedKeys.add((char) e.getKeyCode());
        Point offset = new Point();
        if (!pressedKeys.isEmpty()) {
            for (Iterator<Character> it = pressedKeys.iterator(); it.hasNext();) {
                switch (it.next()) {
                    case KeyEvent.VK_W:
                        throttle = true;
                        break;
                    case KeyEvent.VK_A:
                        left = true;
                        break;
                    case KeyEvent.VK_S:
                        breaks = true;
                        break;
                    case KeyEvent.VK_D:
                        right = true;
                        break;
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove((char) e.getKeyCode());

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                throttle = false;
                break;
            case KeyEvent.VK_A:
                left = false;
                break;
            case KeyEvent.VK_S:
                breaks = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
        }
    }
}
