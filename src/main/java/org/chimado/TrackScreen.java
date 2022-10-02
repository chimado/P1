package org.chimado;
import java.awt.*;
import javax.swing.*;

public class TrackScreen extends JPanel{
    public static int width = 1920, height = 1080; // screen size
    static Collider topBorder, bottomBorder, leftBorder, rightBorder; // world border colliders
    Image track; // track image
    P1 p1; // game
    Car player; // a player

    public TrackScreen()
    {
        topBorder = new Collider(width, 2, 0, 0);
        bottomBorder = new Collider(width, 2, 0, height - 300);
        leftBorder = new Collider(2, height, 2, 0);
        rightBorder = new Collider(2, height, width, 0);

        //track = (new ImageIcon("src/main/resources/track.png")).getImage();
        p1 = new P1(this);
        player = new Car(this);

        addKeyListener(player);
        setFocusable(true);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //g.drawImage(track,0,0,getWidth(),getHeight(),null);
        player.draw(g);
    }

    public static void main(String[] args)
    {
        // set some technical values
        JFrame frame = new JFrame("P1");
        TrackScreen trackscreen = new TrackScreen();
        frame.add(trackscreen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setFocusable(false);
    }
}