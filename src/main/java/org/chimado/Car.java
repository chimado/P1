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
import java.util.ArrayList;

import static org.chimado.TrackScreen.height;

public class Car extends Thread implements KeyListener {
    TrackScreen panel;

    private final Set<Character> pressedKeys = new HashSet<Character>(); // contains all the currently pressed keys
    private final int playerWidth = 50, playerHeight = 70; // player dimensions
    private final float baseAcceleration = 0.0001f, baseAngleChange = 0.01f, maxVelocity = 40f;
    public float playerX = 0, playerY = height / 2, velocity = 0, angle = 0;
    public Boolean throttle = false, breaks = false, left = false, right = false;
    private long deltaTime = 0;
    Instant start, finish;
    Image Stationary;
    Animation drivingAnimation;
    ArrayList<String> drivingAnimationLocations;

    public Car(TrackScreen panel)
    {
        this.panel = panel;

        drivingAnimationLocations = new ArrayList<String>();
        drivingAnimationLocations.add("src/main/resources/CarMoving1.png");
        drivingAnimationLocations.add("src/main/resources/CarMoving2.png");
        drivingAnimationLocations.add("src/main/resources/CarMoving3.png");

        Stationary = new ImageIcon("src/main/resources/carStationary.png").getImage();
        drivingAnimation = new Animation(drivingAnimationLocations, 50);

        start();
    }

    public void run()
    {
        while(true)
        {
            start = Instant.now();
            updatePlayer(); // update player location, velocity and angle
            panel.repaint(); // repaints accordingly to the updated values

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}

            finish = Instant.now();
            deltaTime = Duration.between(start, finish).toMillis();
        }
    }

    private void updatePlayer() {
        playerX += Math.sin(Math.toRadians(angle)) * velocity * deltaTime;
        playerY -= Math.cos(Math.toRadians(angle)) * velocity * deltaTime;

        // update the car's velocity according to its state
        if(throttle) velocity += baseAcceleration * deltaTime;
        else if(breaks) velocity -= baseAcceleration * deltaTime * 10;
        else velocity -= baseAcceleration * deltaTime / 5;

        if(velocity < 0) velocity = 0;
        else if(velocity >= maxVelocity) velocity = maxVelocity;

        // move the car's angle accordingly to its velocity and state
        if(right) angle += baseAngleChange * deltaTime * (velocity != 0 ? 1 : 0) * (velocity < maxVelocity / 2 ? 3 : 1);
        else if(left) angle -= baseAngleChange * deltaTime * (velocity != 0 ? 1 : 0) * (velocity < maxVelocity / 2 ? 3 : 1);

        drivingAnimation.setInterval((int) velocity);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Point p = new Point((int) (playerX+playerWidth/2), (int) (playerY+playerHeight/2));
        g2.rotate(Math.toRadians(angle), p.x,p.y); // rotate the car to angle degrees around point p

        // decide which animation to show
        if(velocity > 0){
            g2.drawImage(drivingAnimation.getImage(deltaTime), (int) playerX, (int) playerY, playerWidth, playerHeight, null);
        }

        else {
            g2.drawImage(Stationary, (int) playerX, (int) playerY, playerWidth, playerHeight, null);
        }
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
        // goes over all pressed keys and updates the car's states
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
