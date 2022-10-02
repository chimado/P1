package org.chimado;

public class Collider {
    public float x1, y1, x2, y2;
    private int width, height;

    public Collider(int width, int height, float x, float y)
    {
        x1 = x;
        y1 = y;
        x2 = x + width;
        y2 = y + height;
        this.width = width;
        this.height = height;
    }

    // update the collider according to movement
    public void update(float x, float y)
    {
        x1 = x;
        y1 = y;
        x2 = x + width;
        y2 = y + height;
    }

    // getters
    public float getX1() {return x1;}
    public float getY1() {return y1;}
    public float getX2() {return x2;}
    public float getY2() {return y2;}

    // check if this object is colliding with another
    public boolean isColliding(Collider object)
    {
        if(object.getX1() > x1 && object.getX2() < x1 && object.getY1() > y1 && object.getY2() < y1 ||
                object.getX1() > x2 && object.getX2() < x2 && object.getY1() > y2 && object.getY2() < y2)
        {
            return true;
        }

        return false;
    }
}
