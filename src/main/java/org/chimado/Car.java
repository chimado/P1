package org.chimado;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.chimado.TrackScreen.height;

public class Car extends Thread implements KeyListener {
    TrackScreen panel;

    private final Set<Character> pressedKeys = new HashSet<Character>();
    private final int playerWidth = 50, playerHeight = 70;;
    public int playerX = 0, playerY = height / 2, velocity = 0, angleChange = 0, angle = 0;
    public Boolean throttle = false, breaks = false, left = false, right = false;
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
            updatePlayer();

            try {
                Thread.sleep(20);
            }catch (InterruptedException e) {}

            panel.repaint();
        }
    }

    private void updatePlayer() {
        playerX -= Math.sin(angle) * velocity;
        playerY -= Math.cos(angle) * velocity;
        if(velocity > 0) velocity--;

        if(throttle){
            if(velocity < 150) velocity += 3;
        }

        else if(breaks){
            if (velocity > 0) velocity -= 3;
        }

        if(right){
            angle += 1;
        }

        else if(left){
            angle -= 1;
        }
    }

    public void draw(Graphics g) {
        angleChange = angle - angleChange;
        Graphics2D g2 = (Graphics2D)g;
        Point p = new Point(playerX+playerWidth/2,playerY+playerHeight/2);
        g2.rotate(Math.toRadians(angleChange), p.x,p.y);
        g2.drawImage(Texture, playerX, playerY, playerWidth, playerHeight, null);
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
        pressedKeys.remove(e.getKeyCode());
    }
}
