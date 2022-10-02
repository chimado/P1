package org.chimado;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Animation{
    public int interval;
    private int frameDeltaTime = 0, currentFrame = 0;
    ArrayList<Image> images = new ArrayList<Image>();

    public Animation(ArrayList<String> locations, int interval) {
        this.interval = interval;

        for(String location : locations){
            images.add(new ImageIcon(location).getImage());
        }
    }

    public Image getImage(long deltaTime){
        frameDeltaTime += deltaTime;

        // if a set amount of time has passed change the frame
        if(frameDeltaTime >= interval){
            currentFrame++;

            if (currentFrame >= images.size()) currentFrame = 0;
        }
        return images.get(currentFrame);
    }

    public void setInterval(int interval){
        this.interval = interval;
    }
}
