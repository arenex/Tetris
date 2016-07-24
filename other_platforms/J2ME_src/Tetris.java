import java.util.Random;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class Tetris extends MIDlet
{
    private Display display;
    private SimpleSlidingCanvas canvas;

    public void startApp()
    {
        canvas = new SimpleSlidingCanvas();
        display = Display.getDisplay(this);
        display.setCurrent(canvas);
    }

    public void pauseApp()
    {}

    public void destroyApp(boolean unconditional)
    {
        notifyDestroyed();
    }
}
class SimpleSlidingCanvas extends Canvas implements Runnable
{
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

    int colors[] = {
        0xff0000,
        0x00ff00,
        0x0000ff,
        0x00ffff,
        0xffff00,
        0xff00ff
    };
    
    int current_color;
    Random rand = new Random();
    int timer;
    int collision = 0;
    int game_speed = 24;
    int type = 0;

    Figure figure = new Figure(0);

    public SimpleSlidingCanvas()
    {
        try
        {
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
            for (int i = 0; i < 19; i++)
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
            new Thread(this).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

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

                        for (int j = 3; j < 13; j++)
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
                                for (int j = 3; j < 13; j++)
                                {

                                    cell[a][j] = cell[a - 1][j];
                                    color[a][j] = color[a - 1][j];
                                }
                            }
                        }
                    }
                }
                repaint();
            }
            timer++;
            try
            {
                synchronized(this)
                {
                    wait(50L);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void paint(Graphics g)
    {
        g.setColor(0xffffff);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(0, 0, 0);

        g.fillRect(0, 0, 24, 131);
        g.fillRect(104, 0, 24, 131);

        g.setColor(0x000000);

        g.setColor(0x0);

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                if (block[i][j] == 1)
                {
                    g.setColor(0x0);
                    g.fillRect(-8 + (x + j) * 8, 3 + (y + i) * 8, 8, 8);
                    g.setColor(colors[current_color]);
                    g.fillRect(-8 + 1 + (x + j) * 8, 3 + 1 + (y + i) * 8, 6, 6);
                }
            }
        }

        for (int i = 0; i < 21; i++)
        {
            for (int j = 0; j < 19; j++)
            {
                if (cell[i][j] == 1)
                {
                    g.setColor(0x0);
                    g.fillRect(-8 + j * 8, 3 + i * 8, 8, 8);
                    g.setColor(color[i][j]);
                    g.fillRect(-7 + j * 8, 4 + i * 8, 6, 6);
                }
            }
        }
    }

    protected void keyPressed(int keyCode)
    {

        switch (getGameAction(keyCode))
        {
            case DOWN:

                game_speed = 1;

                break;

            case UP:

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
                    repaint();
                }
                else
                {
                    collision = 0;
                }

                break;

            case LEFT:
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
                }
                else
                {
                    collision = 0;
                }
                break;

            case GAME_C:
                x += 8;
                break;

            case GAME_D:
                y += 8;
                break;

            case RIGHT:
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
                }
                else
                {
                    collision = 0;
                }

        }
        repaint();
    }

    protected void keyReleased(int keyCode)
    {
        switch (getGameAction(keyCode))
        {
            case DOWN:
                game_speed = 24;
        }
    }
}

class ClassName
{
    private int attribute;
    public ClassName(int b)
    {
        this.attribute = b;
    }
    public int getMethod()
    {
        int a = 0;
        return a;
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