package com.michail.reznikov.seawar;

import java.util.Random;
import java.util.Vector;

class Field {
    private static boolean is_shoted=false;
    private static Ship ship;
    private static int curr_size=0;
    private static Coordinates temp_coordinates =new Coordinates(), coordinates = new Coordinates();
    private static Direction temp_dir=Direction.DOWN;

    Field()  {
        rnd=new Random(System.currentTimeMillis());
        field_player=new FieldCeil[10][10];
        field_computer=new FieldCeil[10][10];
        player_ships=new Vector<>(10);
        computer_ships = new Vector<>(10);
        for(int i=0;i<10;++i)
        {
            for(int j=0;j<10;++j)
            {
                field_player[i][j]=new FieldCeil();
                field_computer[i][j]=new FieldCeil();
            }
        }
    }


    private final Random rnd ;
    public final FieldCeil[][] field_player;
    public final FieldCeil[][] field_computer;
    public final Vector<Ship> player_ships;
    public final Vector<Ship> computer_ships;


    public void setField(boolean is_player,Ship ship,boolean is_initial)
    {
        FieldCeil[][] field = ((is_player)? field_player:field_computer);
        Coordinates offset = ship.getShipOffset();
        int temp_x,temp_y;
        for(int i=-1;i<ship.ship.size()+1;++i) {
            for (int j = -1; j < 2; ++j) {
                temp_x = ship.ship.elementAt(0).coordinates.x + i * offset.x + j * offset.y;
                temp_y = ship.ship.elementAt(0).coordinates.y + i * offset.y + j * offset.x;
                if (checkCoord(temp_x, temp_y)) {
                    if (is_initial) {
                        field[temp_y][temp_x].is_free = false;
                    } else {
                        field[temp_y][temp_x].is_shooted=true;
                    }
                }
            }
        }
    }
    public void initiliaze(boolean is_player) throws Exception {
        int temp_size=4;
        for(int i=0;i<10;++i)
        {
            if(i==1||i==3||i==6)
            {
                --temp_size;
            }
            if(is_player)
            {
                initiliazeShipsRandom(temp_size,true);
            }
            else
            {
                initiliazeShipsRandom(temp_size,false);
            }
        }


    }

    private void initiliazeShipsRandom(int size, boolean is_player) throws Exception {
        Vector<Ship> ships = ((is_player)? player_ships:computer_ships);
        Coordinates coordinates =new Coordinates();
        Direction dir;
        ships.addElement(new Ship(size));
        do {
            dir= Direction.values()[rnd.nextInt(4)];
            coordinates.x=rnd.nextInt(10);
            coordinates.y=rnd.nextInt(10);
        }while(!(ships.lastElement().setupShip(coordinates,dir)&&checkShipOverlaped(is_player,ships.lastElement())));
        setField(is_player,ships.lastElement(),true);
    }

    public boolean checkShipOverlaped(boolean is_player,Ship ship)
    {
        FieldCeil[][] field = ((is_player)? field_player:field_computer);
        for (Ceil it:ship.ship)
        {
            if (!field[it.coordinates.y][it.coordinates.x].is_free)
                return false;
        }
        return true;
    }

    public boolean checkDeadShips(boolean is_player)
    {
        Vector<Ship> vec = ((is_player) ? player_ships : computer_ships);
        for (Ship it : vec)
        {
            if (!it.isDeath())
            {
                return false;
            }
        }
        return true;
    }
    private boolean setTheShoot() {
        field_player[temp_coordinates.y][temp_coordinates.x].is_shooted=true;
        if(ship.shoot(temp_coordinates))
        {
            if(ship.isDeath())
            {
                setField(true,ship,false);
                is_shoted=false;
                return true;
            }
            ++curr_size;
            return true;
        }
        if(curr_size>1) {
            temp_coordinates = coordinates;
            temp_dir=temp_dir.opposite();
        }
        return false;
    }

    public boolean computerMove()
    {
        if(is_shoted)
        {
            switch (curr_size)
            {
                case 1:
                {
                    do {
                        temp_dir=Direction.values()[rnd.nextInt(4)];
                        temp_coordinates.x= coordinates.x+Ship.getOffset(temp_dir).x;
                        temp_coordinates.y= coordinates.y+Ship.getOffset(temp_dir).y;
                    }while((!checkCoord(temp_coordinates.x, temp_coordinates.y))||field_player[temp_coordinates.y][temp_coordinates.x].is_shooted);
                    return setTheShoot();
                }
                case 3:
                case 2:
                {
                    temp_coordinates.x+=Ship.getOffset(temp_dir).x;
                    temp_coordinates.y+=Ship.getOffset(temp_dir).y;
                    if(!checkCoord(temp_coordinates.x, temp_coordinates.y))
                    {
                        temp_coordinates = coordinates;
                        temp_dir=temp_dir.opposite();
                        return true;
                    }
                    return setTheShoot();
                }
            }
        }
        else
        {
            do {
                temp_coordinates.x=rnd.nextInt(10);
                temp_coordinates.y=rnd.nextInt(10);
            }while(field_player[temp_coordinates.y][temp_coordinates.x].is_shooted);
            field_player[temp_coordinates.y][temp_coordinates.x].is_shooted=true;
            for(int i=0;i<player_ships.size();++i)
            {
                if(player_ships.elementAt(i).shoot(temp_coordinates))
                {
                    if(player_ships.elementAt(i).isDeath())
                    {
                        setField(true,player_ships.elementAt(i),false);
                        is_shoted=false;
                        ship=null;
                        coordinates =null;
                        curr_size=1;
                        return true;
                    }
                    is_shoted=true;
                    ship=player_ships.elementAt(i);
                    coordinates = temp_coordinates.clone();
                    curr_size=1;

                    return true;
                }
            }
            return  false;
        }
        return false;
    }

    private boolean checkCoord(int x,int y)
    {
        return (!(x < 0 || y < 0 || x >=10 || y >= 10));
    }


}
