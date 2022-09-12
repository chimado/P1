package org.chimado;
import java.awt.*;

public class P1 extends Thread{
    Color color;
    TrackScreen panel;

    public P1(TrackScreen p)
    {
        this.color=color;
        this.panel=p;
    }

    public void draw(Graphics g)
    {
    }

    // game loop
    public void run()
    {
        while(true)
        {
            try {
                Thread.sleep(7);
            } catch (InterruptedException e) {}
        }
    }
}