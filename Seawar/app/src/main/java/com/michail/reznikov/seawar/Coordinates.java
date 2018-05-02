package com.michail.reznikov.seawar;

public class Coordinates {
    public int x,y;
    Coordinates(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public Coordinates clone()
    {
        return new Coordinates(this.x,this.y);
    }
    public boolean isEquals(Coordinates coordinates)
    {
        return (this.x== coordinates.x && this.y== coordinates.y);
    }
    Coordinates() { this.x=this.y=0;}
}
