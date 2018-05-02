package com.michail.reznikov.seawar;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button[][] computer_field;
    private Button[][] player_field;
    private Button button_random,button_manual,button_up,button_down,button_right,button_left,button_apply,button_turn;
    private TextView takeout_field;
    private GridLayout computer_l,player_l;

    private Field field;

    private int curr_size;

    private SoundPool sp;
    private int soundIdShot;
    private int soundIdExplosion;

    private Curr_status curr_status;
    private WhoMove whoMove;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        findViews();

        setSounds();

        createButtons();

        buttonsSetInvisible(false);

        setButtonsOnClick();

        field=new Field();
        curr_size=4;
        curr_status=Curr_status.BEGIN;
        whoMove=WhoMove.PLAYER;
    }


    @Override
    public void onClick(View v) {

        switch (curr_status)
        {
            case GAME_OVER:
            {
                return;
            }
            case MANUAL_INITIATION:
            {
                manualInitiation(v);
                return;
            }
            case BEGIN: {
                begin(v);
                return;
            }
            case GAME:
            {
                playerMove(v);
                setFields();
                switch (whoMove)
                {
                    case PLAYER:
                    {
                        if(field.checkDeadShips(false))
                        {
                            takeout_field.setText(R.string.youwintext);
                            curr_status=Curr_status.GAME_OVER;
                            return;
                        }
                        return;
                    }
                    case COMPUTER:
                    {
                        sp.play(soundIdShot, 1, 1, 0, 0, 1);
                        do {
                            setFields();
                        }while(field.computerMove());
                        setFields();
                        whoMove=WhoMove.PLAYER;
                        if(field.checkDeadShips(true))
                        {
                            takeout_field.setText(R.string.youloosetext);
                            curr_status=Curr_status.GAME_OVER;
                            return;
                        }
                        takeout_field.setText(R.string.yourturnText);
                        return;
                    }
                }
            }
        }
    }

    private void setFields()
    {
        for(int i=0;i<10;++i)
        {
            for(int j=0;j<10;++j)
            {
                if(field.field_player[i][j].is_shooted)
                {
                    player_field[i][j].getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                }
                if(field.field_computer[i][j].is_shooted)
                {
                    computer_field[i][j].getBackground().setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
        for(Ship it:field.player_ships)
        {
            for(Ceil it2:it.ship)
            {
                if(it2.is_death)
                {
                    player_field[it2.coordinates.y][it2.coordinates.x].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    player_field[it2.coordinates.y][it2.coordinates.x].getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
        for(Ship it:field.computer_ships)
        {
            for(Ceil it2:it.ship)
            {
                if(it2.is_death)
                {
                    computer_field[it2.coordinates.y][it2.coordinates.x].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    computer_field[it2.coordinates.y][it2.coordinates.x].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                }
            }
        }
    }
    private void playerMove(View v)
    {
        int x,y;
        String[] arr= v.getTag().toString().split(" ");
        y=Integer.parseInt(arr[0]);
        x=Integer.parseInt(arr[1]);
        if(field.field_computer[y][x].is_shooted)
        {
            whoMove=WhoMove.PLAYER;
            return ;
        }

        field.field_computer[y][x].is_shooted=true;

        for(int i=0;i<field.computer_ships.size();++i)
        {
            if(field.computer_ships.elementAt(i).shoot(new Coordinates(x,y)))
            {
                whoMove=WhoMove.PLAYER;
                if(field.computer_ships.elementAt(i).isDeath())
                {
                    for(int j=0;j<field.computer_ships.elementAt(i).ship.size();++j)
                    {
                       sp.play(soundIdExplosion, 1, 1, 0, 0, 1);
                    }
                    takeout_field.setText(R.string.Shipisdeadtext);
                    field.setField(false,field.computer_ships.elementAt(i),false);
                    if(field.checkDeadShips(false))
                    {
                        setFields();
                        takeout_field.setText(R.string.youwintext);
                        curr_status=Curr_status.GAME_OVER;
                        return ;
                    }
                }
                else
                {
                    sp.play(soundIdExplosion, 1, 1, 0, 0, 1);
                    takeout_field.setText(R.string.youshoottezt);
                }
                setFields();
                return ;
            }
        }
        setFields();
        whoMove=WhoMove.COMPUTER;
    }

    private boolean initiliazePlayerShipsManual(View v)
    {
        resetPlayerField();
        switch (v.getId())
        {
            case R.id.but_up:
            {
                field.player_ships.lastElement().moveShip(Direction.UP);
                break;
            }
            case R.id.but_down:
            {
                field.player_ships.lastElement().moveShip(Direction.DOWN);
                break;
            }
            case R.id.but_left:
            {
                field.player_ships.lastElement().moveShip(Direction.LEFT);
                break;
            }
            case R.id.but_right:
            {
                field.player_ships.lastElement().moveShip(Direction.RIGHT);
                break;
            }
            case R.id.but_turn:
            {
                field.player_ships.lastElement().turnShip();
                break;
            }
            case R.id.but_apply:
            {
                boolean isOverlap=field.checkShipOverlaped(true,field.player_ships.lastElement());
                if(!isOverlap) {
                    takeout_field.setText(R.string.Shipsoverlapedtext);
                }
                setFields();
                return isOverlap;
            }
        }
        setFields();
        return false;
    }
    private void resetPlayerField()
    {
        for(int i=0;i<10;++i) {
            for (int j = 0; j < 10; ++j)
            {
                player_field[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private void begin(View v)
    {
        if(v.getId()==R.id.but_rand||v.getId()==R.id.but_manul)
        {
            button_random.setVisibility(View.INVISIBLE);
            button_manual.setVisibility(View.INVISIBLE);
            try {
                field.initiliaze(false);
                if(v.getId()==R.id.but_rand)
                {
                    field.initiliaze(true);
                    curr_status=Curr_status.GAME;
                    setFields();
                    takeout_field.setText(R.string.yourturnText);
                    return;
                }
                else
                {
                    try {
                        field.player_ships.addElement(new Ship(4));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    setFields();
                    computer_l.setVisibility(View.INVISIBLE);
                    buttonsSetInvisible(true);
                    curr_status=Curr_status.MANUAL_INITIATION;
                    return;
                }
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            setFields();
            takeout_field.setText(R.string.yourturnText);
        }
    }
    private void manualInitiation(View v)
    {
        if (initiliazePlayerShipsManual(v)) {
            field.setField(true, field.player_ships.lastElement(), true);

            if (field.player_ships.size() == 10) {
                buttonsSetInvisible(false);
                computer_l.setVisibility(View.VISIBLE);
                setFields();
                curr_status=Curr_status.GAME;
                return;
            }
            if (field.player_ships.size() == 1 || field.player_ships.size() == 3 || field.player_ships.size() == 6) {

                --curr_size;
            }
            try {
                if (field.player_ships.size() != 10) {
                    field.player_ships.addElement(new Ship(curr_size));

                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            setFields();
        }
    }

    private void findViews()
    {
        player_l = findViewById(R.id.layout1);
        computer_l = findViewById(R.id.layout2);
        takeout_field =findViewById(R.id.textout);
        button_manual = findViewById(R.id.but_manul);
        button_random = findViewById(R.id.but_rand);
        button_up = findViewById(R.id.but_up);
        button_down = findViewById(R.id.but_down);
        button_left = findViewById(R.id.but_left);
        button_right = findViewById(R.id.but_right);
        button_turn = findViewById(R.id.but_turn);
        button_apply = findViewById(R.id.but_apply);
    }

    private void createButtons()
    {
        player_field= new Button[10][];
        computer_field = new Button[10][];
        for(int i=0;i<10;++i)
        {
            player_field[i] = new Button[10];
            computer_field[i]=new Button[10];
        }

        for(int i=0;i<10;++i)
        {
            for(int j=0;j<10;++j)
            {
                player_field[i][j]=new Button(this);
                computer_field[i][j]=new Button(this);

                computer_field[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                player_field[i][j].getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(); // создаем параметры лаяута для кнопки
                lp.width = 0;
                lp.height = 0;
                lp.columnSpec = GridLayout.spec(j, 1f); // вес и позиция кнопки по горизонтали
                lp.rowSpec = GridLayout.spec(i, 1f); // и по вертикали

                player_l.addView(player_field[i][j],lp);
                computer_l.addView(computer_field[i][j],lp);

                computer_field[i][j].setOnClickListener(this);
                computer_field[i][j].setTag(i+" "+ j);
            }
        }
    }

    private void buttonsSetInvisible(boolean is_visible)
    {
        int visible = (is_visible)?View.VISIBLE:View.INVISIBLE;
        button_up.setVisibility(visible );
        button_down.setVisibility(visible );
        button_left.setVisibility(visible );
        button_right.setVisibility(visible );
        button_turn.setVisibility(visible );
        button_apply.setVisibility(visible );
    }

    private void setButtonsOnClick()
    {
        button_up.setOnClickListener(this);
        button_down.setOnClickListener(this);
        button_left.setOnClickListener(this);
        button_right.setOnClickListener(this);
        button_turn.setOnClickListener(this);
        button_apply.setOnClickListener(this);
        button_manual.setOnClickListener(this);
        button_random.setOnClickListener(this);
    }

    private void setSounds()
    {
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundIdShot = sp.load(this, R.raw.shot, 1);
        soundIdExplosion = sp.load(this, R.raw.explosion, 1);
    }

}
