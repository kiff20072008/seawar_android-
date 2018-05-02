package com.michail.reznikov.seawar;


public class Ceil {
    public Coordinates coordinates;
    public boolean is_death;
    Ceil (int x,int y,boolean is_death)
    {
        this.coordinates = new Coordinates(x,y);
        this.is_death=is_death;
    }
    public Ceil clone()
    {
        return new Ceil(this.coordinates.x,this.coordinates.y,this.is_death);
    }
}
