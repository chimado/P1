package org.chimado;
import java.awt.*;
import javax.swing.*;

public class TrackScreen extends JPanel{
    Image track;
    P1 p1;
    Car player;

    public TrackScreen()
    {
        //track = (new ImageIcon("src/main/resources/track.png")).getImage();
        p1 = new P1(this);
        player = new Car(this);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {}

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
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame("P1");
        TrackScreen trackscreen = new TrackScreen();
        frame.add(trackscreen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenSize.width, screenSize.height);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setFocusable(false);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }
}