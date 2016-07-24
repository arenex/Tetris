package com.trying010;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class Android010_BbryTetrisActivity extends Activity
{
    DrawView drawView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        drawView = new DrawView(this);
        setContentView(drawView);
        drawView.requestFocus();
    }
}

class DrawView extends View implements OnTouchListener
{
    public View view = this;
    Point touched_at = new Point();
    Paint paint = new Paint();
    Bitmap mIcon;
    int keypressed = 0;

    int example;
    int x;
    int y;
    int[][] cell;
    int[][] color;
    int[][] block;
    int[][] block2;

    int[][] figure1 = 
    {{0,0,0,0},
     {0,1,1,1},
     {0,0,1,0},
     {0,0,0,0}};

    int[][] figure2 = 
    {{0,0,1,0},
     {0,1,1,0},
     {0,0,1,0},
     {0,0,0,0}};


    int[][] figure3 = 
    {{0,0,1,0},
     {0,1,1,1},
     {0,0,0,0},
     {0,0,0,0}};

    int[][] figure4 = 
    {{0,0,1,0},
     {0,0,1,1},
     {0,0,1,0},
     {0,0,0,0}};

    int rotation_index = 0;
    int horizontal_counter = 0;
    public int height;
    public int width;
    int touch_area_x_left;
    int touch_area_y_top;

    int blocksize;
    int leftborder;
    int rightborder;
    int topoffset;
    int blockborderoffset;
    int innerblocksize;
    int leftoffset;

    int colors[] = {
        0xffff0000,
        0xff00ff00,
        0xff0000ff,
        0xff00ffff,
        0xffffff00,
        0xffff00ff
    };

    int current_color = 0;
    Random rand = new Random();
    int timer;
    int collision = 0;
    int game_speed = 24;
    int type = 0;
    Figure figure = new Figure(0);

    public DrawView(Context context)
    {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        touch_area_x_left = (int)(((double)150/480)*width);
        touch_area_y_top = (int)(((double)300/800)*height);

        
        try
        {
            blocksize = ((height / 16));
            leftborder = ((width - (blocksize * 10)) / 2);
            rightborder = (width - (blocksize * 10)) - leftborder;
            topoffset = height - ((height / blocksize) * blocksize);
            blockborderoffset = ((blocksize / 10) + 1);
            innerblocksize = blocksize - 2 * blockborderoffset;
            leftoffset = leftborder - 4 * blocksize;

            example = 5;
            x = 7;
            y = 0;
            timer = 1;
            block = new int[4][4];
            block2 = new int[4][4];
            type = rand.nextInt(7);
            block = figure.getArray(type, 0);
            cell = new int[21][19];
            color = new int[21][19];
            current_color = rand.nextInt(colors.length);

            for (int i = 0; i < 21; i++)
            {
                for (int j = 0; j < 19; j++)
                {
                    cell[i][j] = 0;
                }
            }
            for (int i = 0; i < 17; i++)
            {
                cell[i][0 + 3] = 1;
                cell[i][11 + 3] = 1;
            }
            for (int i = 1; i < 11; i++)
            {
                cell[16][i + 3] = 1;
            }

            for (int i = 0; i < 21; i++)
            {
                for (int j = 0; j < 19; j++)
                {
                    color[i][j] = 0x0;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        new Thread()
        {
            public void run()
            {
                while (true)
                {
                    if (timer % game_speed == 0)
                    {
                        collision = 0;
                        for (int i = 0; i < 4; i++)
                        {
                            for (int j = 0; j < 4; j++)
                            {
                                if (cell[y + i + 1][x + j] + block[i][j] > 1)
                                {
                                    collision = 1;
                                }
                            }
                        }

                        if (collision == 0)
                        {
                            y++;
                        }
                        else
                        {
                            collision = 0;
                            for (int i = 0; i < 4; i++)
                            {
                                for (int j = 0; j < 4; j++)
                                {
                                    if (block[i][j] == 1)
                                    {
                                        cell[y + i][x + j] = block[i][j];
                                        color[y + i][x + j] = colors[current_color];
                                    }
                                }
                            }
                            x = 7;
                            y = 0;
                            current_color = rand.nextInt(colors.length);
                            type = rand.nextInt(7);
                            rotation_index = 0;
                            block = figure.getArray(type, 0);

                            for (int i = 2; i < 16; i++)
                            {
                                horizontal_counter = 0;
                                for (int j = 4; j < 14; j++)
                                {
                                    if (cell[i][j] == 1)
                                    {
                                        horizontal_counter++;
                                    }
                                }

                                if (horizontal_counter == 10)
                                {
                                    for (int a = i; a > 1; a--)
                                    {
                                        for (int j = 4; j < 14; j++)
                                        {
                                            cell[a][j] = cell[a - 1][j];
                                            color[a][j] = color[a - 1][j];
                                        }
                                    }
                                }
                            }
                        }
                        postInvalidate();
                    }
                    timer++;

                    try
                    {
                        Thread.sleep(50);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    public void redrawit()
    {
        invalidate();
    }

    public void onDraw(Canvas g)
    {
        paint.setColor(Color.BLACK);
        g.drawRect(new Rect(0, 0, width, height), paint);
        g.drawRect(new Rect(0, 0, width, height), paint);
        paint.setColor(Color.BLACK);
        g.drawRect(new Rect(0, 0, leftborder, height), paint);
        g.drawRect(new Rect(width - rightborder, 0, rightborder + width - rightborder, height), paint);
        paint.setColor(Color.BLACK);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (block[i][j] == 1)
                {
                    paint.setColor(Color.BLACK);
                    g.drawRect(new Rect(leftoffset + (x + j) * blocksize, topoffset + (y + i) * blocksize, blocksize + leftoffset + (x + j) * blocksize, blocksize + topoffset + (y + i) * blocksize), paint);
                    paint.setColor(colors[current_color]);
                    g.drawRect(new Rect(leftoffset + blockborderoffset + (x + j) * blocksize, topoffset + blockborderoffset + (y + i) * blocksize, innerblocksize + leftoffset + blockborderoffset + (x + j) * blocksize, innerblocksize + topoffset + blockborderoffset + (y + i) * blocksize), paint);
                }
            }
        }

        for (int i = 0; i < 21; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                if (cell[i][j] == 1)
                {
                    paint.setColor(Color.BLACK);
                    g.drawRect(new Rect(leftoffset + j * blocksize, topoffset + i * blocksize, blocksize + leftoffset + j * blocksize, blocksize + topoffset + i * blocksize), paint);
                    paint.setColor(color[i][j]);
                    g.drawRect(new Rect(leftoffset + blockborderoffset + j * blocksize, topoffset + blockborderoffset + i * blocksize, innerblocksize + leftoffset + blockborderoffset + j * blocksize, innerblocksize + topoffset + blockborderoffset + i * blocksize), paint);
                }
            }
        }
    }

    public boolean onTouch(View view, MotionEvent event)
    {
        int x_touch = (int) event.getX();
        int y_touch = (int) event.getY();
        
        if (keypressed == 0)
        {
            if (x_touch > touch_area_x_left && x_touch < width - touch_area_x_left)
            {
                if (y_touch < touch_area_y_top)
                {
                    keypressed = 1;
                }
            }
            if (x_touch > 0 && x_touch < touch_area_x_left)
            {
                keypressed = 2;
            }
            if (x_touch > width - touch_area_x_left && x_touch < width)
            {
                keypressed = 3;
            }
            if (x_touch > touch_area_x_left && x_touch < width - touch_area_x_left && keypressed < 2)
            {
                if (y_touch > touch_area_y_top*2)
                {
                    keypressed = 4;
                }
            }
        }

        if (keypressed == 1)
        {
            if (rotation_index == 0)
            {
                block2 = figure.getArray(type, 1);
            }
            else if (rotation_index == 1)
            {
                block2 = figure.getArray(type, 2);
            }
            else if (rotation_index == 2)
            {
                block2 = figure.getArray(type, 3);
            }
            else if (rotation_index == 3)
            {
                block2 = figure.getArray(type, 0);
            }
            collision = 0;
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cell[y + i][x + j] + block2[i][j] > 1)
                    {
                        collision = 1;
                    }
                }
            }
            if (collision == 0)
            {
                block = block2;
                if (rotation_index == 3)
                {
                    rotation_index = 0;
                }
                else
                {
                    rotation_index++;
                }
                postInvalidate();
            }
            else
            {
                collision = 0;
            }
            keypressed = 10;
        }
        if (keypressed == 2)
        {
            collision = 0;
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cell[y + i][x + j - 1] + block[i][j] > 1)
                    {
                        collision = 1;
                    }
                }
            }
            if (collision == 0)
            {
                x--;
                postInvalidate();
            }
            else
            {
                collision = 0;
            }
            keypressed = 10;
        }
        if (keypressed == 3)
        {
            collision = 0;
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cell[y + i][x + j + 1] + block[i][j] > 1)
                    {
                        collision = 1;
                    }
                }
            }
            if (collision == 0)
            {
                x++;
                postInvalidate();
            }
            else
            {
                collision = 0;
            }
            keypressed = 10;
        }
        if (keypressed == 4)
        {
            game_speed = 1;
            keypressed = 10;
        }
        if (event.getAction() == android.view.MotionEvent.ACTION_UP)
        {
            keypressed = 0;
            game_speed = 24;
        }
        return true;
    }
}

class Figure
{
    private int attribute2;

    int[][] drawing1 =
    {{0,0,0,0},
     {0,1,1,1},
     {0,1,0,0},
     {0,0,0,0}};

    int[][] drawing2 =
    {{0,1,1,0},
     {0,0,1,0},
     {0,0,1,0},
     {0,0,0,0}};   

    int[][] drawing3 =
    {{0,0,0,1},
     {0,1,1,1},
     {0,0,0,0},
     {0,0,0,0}};
       
    int[][] drawing4 =
    {{0,0,1,0},
     {0,0,1,0},
     {0,0,1,1},
     {0,0,0,0}};

    int[][] drawing5 =
    {{0,1,1,0},
     {0,1,1,0},
     {0,0,0,0},
     {0,0,0,0}};

    int[][] drawing6 =
    {{0,0,0,0},
     {0,1,1,1},
     {0,0,0,1},
     {0,0,0,0}};

    int[][] drawing7 =
    {{0,0,1,0},
     {0,0,1,0},
     {0,1,1,0},
     {0,0,0,0}};

    int[][] drawing8 =
    {{0,1,0,0},
     {0,1,1,1},
     {0,0,0,0},
     {0,0,0,0}};

    int[][] drawing9 =
    {{0,0,1,1},
     {0,0,1,0},
     {0,0,1,0},
     {0,0,0,0}};
    
    int[][] drawing10 =
    {{0,0,0,0},
     {0,1,1,0},
     {0,0,1,1},
     {0,0,0,0}};

    int[][] drawing11 =
    {{0,0,0,1},
     {0,0,1,1},
     {0,0,1,0},
     {0,0,0,0}};

    int[][] drawing12 =
    {{0,0,0,0},
     {0,0,1,1},
     {0,1,1,0},
     {0,0,0,0}};

    int[][] drawing13 =
    {{0,0,1,0},
     {0,0,1,1},
     {0,0,0,1},
     {0,0,0,0}};

    int[][] drawing15 =
    {{0,0,0,0},
     {1,1,1,1},
     {0,0,0,0},
     {0,0,0,0}};

    int[][] drawing16 =
    {{0,1,0,0},
     {0,1,0,0},
     {0,1,0,0},
     {0,1,0,0}};
    
    int[][] drawing17 =
    {{0,0,0,0},
     {0,1,1,1},
     {0,0,1,0},
     {0,0,0,0}};

    int[][] drawing18 = 
    {{0,0,1,0},
     {0,1,1,0},
     {0,0,1,0},
     {0,0,0,0}};

    int[][] drawing19 = 
    {{0,0,1,0},
     {0,1,1,1},
     {0,0,0,0},
     {0,0,0,0}};

    int[][] drawing20 = 
    {{0,0,1,0},
     {0,0,1,1},
     {0,0,1,0},
     {0,0,0,0}};

    public Figure(int b)
    {
        this.attribute2 = b;
    }

    public int[][] getArray(int type, int rotation)
    {
        int[][] a = drawing1;

        if (type == 0)
        {
            if (rotation == 0)
            {
                a = drawing1;
            }
            else if (rotation == 1)
            {
                a = drawing2;
            }
            else if (rotation == 2)
            {
                a = drawing3;
            }
            else if (rotation == 3)
            {
                a = drawing4;
            }
        }
        else if (type == 1)
        {
            if (rotation == 0)
            {
                a = drawing5;
            }
            else if (rotation == 1)
            {
                a = drawing5;
            }
            else if (rotation == 2)
            {
                a = drawing5;
            }
            else if (rotation == 3)
            {
                a = drawing5;
            }
        }
        else if (type == 2)
        {
            if (rotation == 0)
            {
                a = drawing6;
            }
            else if (rotation == 1)
            {
                a = drawing7;
            }
            else if (rotation == 2)
            {
                a = drawing8;
            }
            else if (rotation == 3)
            {
                a = drawing9;
            }
        }
        else if (type == 3)
        {
            if (rotation == 0)
            {
                a = drawing10;
            }
            else if (rotation == 1)
            {
                a = drawing11;
            }
            else if (rotation == 2)
            {
                a = drawing10;
            }
            else if (rotation == 3)
            {
                a = drawing11;
            }
        }
        else if (type == 4)
        {
            if (rotation == 0)
            {
                a = drawing12;
            }
            else if (rotation == 1)
            {
                a = drawing13;
            }
            else if (rotation == 2)
            {
                a = drawing12;
            }
            else if (rotation == 3)
            {
                a = drawing13;
            }
        }
        else if (type == 5)
        {
            if (rotation == 0)
            {
                a = drawing15;
            }
            else if (rotation == 1)
            {
                a = drawing16;
            }
            else if (rotation == 2)
            {
                a = drawing15;
            }
            else if (rotation == 3)
            {
                a = drawing16;
            }
        }
        else if (type == 6)
        {
            if (rotation == 0)
            {
                a = drawing17;
            }
            else if (rotation == 1)
            {
                a = drawing18;
            }
            else if (rotation == 2)
            {
                a = drawing19;
            }
            else if (rotation == 3)
            {
                a = drawing20;
            }
        }
        return a;
    }
}