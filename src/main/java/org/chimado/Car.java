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
import static org.chimado.TrackScreen.width;

public class Car extends Thread implements KeyListener {
    TrackScreen panel;

    private final Set<Character> pressedKeys = new HashSet<Character>(); // contains all the currently pressed keys
    private final int playerWidth = 50, playerHeight = 70; // player dimensions
    private final float baseAcceleration = 0.0001f, baseAngleChange = 0.02f, maxVelocity = 40f;
    public float playerX = 0, playerY = height / 2, velocity = 0, angle = 0;
    public Boolean throttle = false, breaks = false, left = false, right = false; // player state info
    private long deltaTime = 0, turnTime = 0; // various timers
    public Collider carCollider;
    // timestamps for the start and finish of an update
    Instant start, finish;
    // images for the stationary car
    Image Stationary, stationarySlightlyRightImage, stationaryRightImage, stationaryVeryRightImage,
            stationarySlightlyLeftImage, stationaryLeftImage, stationaryVeryLeftImage, outImage;
    // animations for the driving car
    Animation drivingAnimation, drivingSlightlyRightAnimation, drivingRightAnimation, drivingVeryRightAnimation,
            drivingSlightlyLeftAnimation, drivingLeftAnimation, drivingVeryLeftAnimation;
    // the locations for the animation's images
    ArrayList<String> drivingAnimationLocations, drivingSlightlyRightAnimationLocations, 
            drivingRightAnimationLocations, drivingVeryRightAnimationLocations,
            drivingSlightlyLeftAnimationLocations, drivingLeftAnimationLocations, drivingVeryLeftAnimationLocations;

    public Car(TrackScreen panel)
    {
        this.panel = panel;
        
        // image imports
        drivingAnimationLocations = new ArrayList<String>();
        drivingAnimationLocations.add("src/main/resources/CarMoving1.png");
        drivingAnimationLocations.add("src/main/resources/CarMoving2.png");
        drivingAnimationLocations.add("src/main/resources/CarMoving3.png");

        drivingSlightlyRightAnimationLocations = new ArrayList<String>();
        drivingSlightlyRightAnimationLocations.add("src/main/resources/CarMoving1SlightlyRight.png");
        drivingSlightlyRightAnimationLocations.add("src/main/resources/CarMoving2SlightlyRight.png");
        drivingSlightlyRightAnimationLocations.add("src/main/resources/CarMoving3SlightlyRight.png");
        drivingRightAnimationLocations = new ArrayList<String>();
        drivingRightAnimationLocations.add("src/main/resources/CarMoving1Right.png");
        drivingRightAnimationLocations.add("src/main/resources/CarMoving2Right.png");
        drivingRightAnimationLocations.add("src/main/resources/CarMoving3Right.png");
        drivingVeryRightAnimationLocations = new ArrayList<String>();
        drivingVeryRightAnimationLocations.add("src/main/resources/CarMoving1VeryRight.png");
        drivingVeryRightAnimationLocations.add("src/main/resources/CarMoving2VeryRight.png");
        drivingVeryRightAnimationLocations.add("src/main/resources/CarMoving3VeryRight.png");

        drivingSlightlyLeftAnimationLocations = new ArrayList<String>();
        drivingSlightlyLeftAnimationLocations.add("src/main/resources/CarMoving1SlightlyLeft.png");
        drivingSlightlyLeftAnimationLocations.add("src/main/resources/CarMoving2SlightlyLeft.png");
        drivingSlightlyLeftAnimationLocations.add("src/main/resources/CarMoving3SlightlyLeft.png");
        drivingLeftAnimationLocations = new ArrayList<String>();
        drivingLeftAnimationLocations.add("src/main/resources/CarMoving1Left.png");
        drivingLeftAnimationLocations.add("src/main/resources/CarMoving2Left.png");
        drivingLeftAnimationLocations.add("src/main/resources/CarMoving3Left.png");
        drivingVeryLeftAnimationLocations = new ArrayList<String>();
        drivingVeryLeftAnimationLocations.add("src/main/resources/CarMoving1VeryLeft.png");
        drivingVeryLeftAnimationLocations.add("src/main/resources/CarMoving2VeryLeft.png");
        drivingVeryLeftAnimationLocations.add("src/main/resources/CarMoving3VeryLeft.png");

        Stationary = new ImageIcon("src/main/resources/carStationary.png").getImage();
        
        stationarySlightlyRightImage = new ImageIcon("src/main/resources/carSlightlyRight.png").getImage();
        stationaryRightImage = new ImageIcon("src/main/resources/carRight.png").getImage();
        stationaryVeryRightImage = new ImageIcon("src/main/resources/carVeryRight.png").getImage();

        stationarySlightlyLeftImage = new ImageIcon("src/main/resources/carSlightlyLeft.png").getImage();
        stationaryLeftImage = new ImageIcon("src/main/resources/carLeft.png").getImage();
        stationaryVeryLeftImage = new ImageIcon("src/main/resources/carVeryLeft.png").getImage();
        
        drivingAnimation = new Animation(drivingAnimationLocations, 50);

        drivingSlightlyRightAnimation = new Animation(drivingSlightlyRightAnimationLocations, 50);
        drivingRightAnimation = new Animation(drivingRightAnimationLocations, 50);
        drivingVeryRightAnimation = new Animation(drivingVeryRightAnimationLocations, 50);

        drivingSlightlyLeftAnimation = new Animation(drivingSlightlyLeftAnimationLocations, 50);
        drivingLeftAnimation = new Animation(drivingLeftAnimationLocations, 50);
        drivingVeryLeftAnimation = new Animation(drivingVeryLeftAnimationLocations, 50);

        // collider init
        carCollider = new Collider(playerWidth, playerHeight, playerX, playerY);

        start();
    }

    public void run()
    {
        while(true)
        {
            start = Instant.now(); // start of update
            updatePlayer(); // update player location, velocity and angle
            carCollider.update(playerX, playerY); // update collider
            panel.repaint(); // repaints accordingly to the updated values

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}

            finish = Instant.now(); // end of update
            deltaTime = Duration.between(start, finish).toMillis(); // capture the frame's duration
        }
    }

    private void updatePlayer() {
        // update the car's position according to its velocity
        playerX += Math.sin(Math.toRadians(angle)) * velocity * deltaTime;
        playerY -= Math.cos(Math.toRadians(angle)) * velocity * deltaTime;

        // update the car's velocity according to its state
        if(throttle) velocity += baseAcceleration * deltaTime;
        else if(breaks) velocity -= baseAcceleration * deltaTime * 10;
        else velocity -= baseAcceleration * deltaTime / 5;

        if(velocity < 0) velocity = 0;
        else if(velocity >= maxVelocity) velocity = maxVelocity;

        // move the car's angle accordingly to its velocity and state
        if(right) angle += baseAngleChange * deltaTime * (velocity > 0.015f ? 1 : 0) * (velocity < maxVelocity / 2.5f ? 3 : 1);
        else if(left) angle -= baseAngleChange * deltaTime * (velocity > 0.015f ? 1 : 0) * (velocity < maxVelocity / 2.5f ? 3 : 1);

        drivingAnimation.setInterval((int) velocity);

        drivingSlightlyRightAnimation.setInterval((int) velocity);
        drivingRightAnimation.setInterval((int) velocity);
        drivingVeryRightAnimation.setInterval((int) velocity);

        drivingSlightlyLeftAnimation.setInterval((int) velocity);
        drivingLeftAnimation.setInterval((int) velocity);
        drivingVeryLeftAnimation.setInterval((int) velocity);
        
        if(right || left) turnTime += deltaTime;
        else turnTime = 0;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Point p = new Point((int) (playerX+playerWidth/2), (int) (playerY+playerHeight/2));
        g2.rotate(Math.toRadians(angle), p.x,p.y); // rotate the car to angle degrees around point p

        // decide which animation to show
        if(velocity > 0){
            if(turnTime > 0){
                if(turnTime < 100 || velocity > maxVelocity / 2){
                    if(left){
                        outImage = drivingSlightlyLeftAnimation.getImage(deltaTime);
                    }

                    else{
                        outImage = drivingSlightlyRightAnimation.getImage(deltaTime);
                    }
                }

                else if(turnTime < 200 || velocity > velocity / 1.3f){
                    if(left){
                        outImage = drivingLeftAnimation.getImage(deltaTime);
                    }

                    else{
                        outImage = drivingRightAnimation.getImage(deltaTime);
                    }
                }

                else{
                    if(left){
                        outImage = drivingVeryLeftAnimation.getImage(deltaTime);
                    }

                    else{
                        outImage = drivingVeryRightAnimation.getImage(deltaTime);
                    }
                }
            }

            else{
                outImage = drivingAnimation.getImage(deltaTime);
            }
        }

        else {
            if(turnTime > 0){
                if(turnTime < 100){
                    if(left){
                        outImage = stationarySlightlyLeftImage;
                    }

                    else{
                        outImage = stationarySlightlyRightImage;
                    }
                }

                else if(turnTime < 200){
                    if(left){
                        outImage = stationaryLeftImage;
                    }

                    else{
                        outImage = stationaryRightImage;
                    }
                }

                else{
                    if(left){
                        outImage = stationaryVeryLeftImage;
                    }

                    else{
                        outImage = stationaryVeryRightImage;
                    }
                }
            }

            else{
                outImage = Stationary;
            }
        }

        g2.drawImage(outImage, (int) playerX, (int) playerY, playerWidth, playerHeight, null);
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

        // change player state according to unpressed keys
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
