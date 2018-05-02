package com.michail.reznikov.seawar;

import java.util.Vector;

class Ship
{
    public Vector<Ceil> ship;
    private Direction dir;

    private void updateCoord()
    {
        Coordinates offset = getOffset(this.dir);

        for(int i=1;i<ship.size();++i)
        {
            ship.elementAt(i).coordinates.x=ship.elementAt(0).coordinates.x+offset.x*i;
            ship.elementAt(i).coordinates.y=ship.elementAt(0).coordinates.y+offset.y*i;
        }

    }
    private boolean checkCoord()
    {
        updateCoord();
        for(Ceil it:ship)
        {
            if(it.coordinates.x>9||it.coordinates.y>9 || it.coordinates.x<0 ||it.coordinates.y<0)
            {
                return false;
            }
        }
        return true;
    }

    Ship(int size) throws Exception {
        if(size>4 || size<1)
        {
            throw new Exception("Wrong ship size");
        }
        ship = new Vector<>(size);
        for(int i=0;i<size;++i)
        {
            ship.add(new Ceil(0, 0,false));
        }
        this.dir= Direction.DOWN;
        if(!checkCoord())
        {
            throw new Exception("Wrong coordinates");
        }
    }
    public boolean isDeath()
    {
        for(Ceil it:ship)
        {
            if(!it.is_death)
            {
                return false;
            }
        }
        return true;
    }
    public void turnShip()
    {
        dir=dir.next();
        if(!checkCoord())
        {
            dir=dir.prev();
        }
        updateCoord();
    }
    public boolean shoot(Coordinates coordinates)
    {
        for(int i=0;i<ship.size();++i)
        {
            if(coordinates.isEquals(ship.elementAt(i).coordinates))
            {
                ship.elementAt(i).is_death=true;
                return true;
            }
        }
        return false;
    }
    public void moveShip(Direction dir)
    {
        Coordinates offset=getOffset(dir);

        ship.elementAt(0).coordinates.x+=offset.x;
        ship.elementAt(0).coordinates.y+=offset.y;
        if(!checkCoord())
        {
            ship.elementAt(0).coordinates.x-=offset.x;
            ship.elementAt(0).coordinates.y-=offset.y;
        }
        updateCoord();
    }
    public boolean setupShip(Coordinates coordinates, Direction dir)
    {
        ship.elementAt(0).coordinates = coordinates.clone();
        this.dir=dir;
        return checkCoord();

    }
    public Coordinates getShipOffset()
    {
        return getOffset(dir);

    }
    public static Coordinates getOffset(Direction dir)
    {
        int gorizontal_offset = ((dir == Direction.UP) || (dir == Direction.DOWN)) ? 0 : (dir == Direction.RIGHT ? 1 : -1);
        int vertikal_offset = ((dir == Direction.LEFT) || (dir == Direction.RIGHT)) ? 0 : (dir == Direction.DOWN ? 1 : -1);
        return new Coordinates(gorizontal_offset, vertikal_offset);

    }

}
