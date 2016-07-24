var touched_at = new Point();
var paint = null;
var keypressed = 0;
var example;
var x;
var y;
var cell;
var color;
var block;
var block2;

var figure1 = [
    [0, 0, 0, 0],
    [0, 1, 1, 1],
    [0, 0, 1, 0],
    [0, 0, 0, 0]
];

var figure2 = [
    [0, 0, 1, 0],
    [0, 1, 1, 0],
    [0, 0, 1, 0],
    [0, 0, 0, 0]
];

var figure3 = [
    [0, 0, 1, 0],
    [0, 1, 1, 1],
    [0, 0, 0, 0],
    [0, 0, 0, 0]
];

var figure4 = [
    [0, 0, 1, 0],
    [0, 0, 1, 1],
    [0, 0, 1, 0],
    [0, 0, 0, 0]
];

var rotation_index = 0;
var horizontal_counter = 0;
var height = 800;
var width = 480;

var blocksize;
var leftborder;
var rightborder;
var topoffset;
var blockborderoffset;
var innerblocksize;
var leftoffset;

var colors = ['#ff0000', '#00ff00', '#0000ff', '#00ffff', '#ffff00', '#ff00ff'];
var current_color = 0;
var rand;
var timer;
var collision = 0;
var game_speed = 24;
var type = 0;
var figure = new Figure(0);

function start_game()
{
    blocksize = ((Math.floor(height / 16)));
    leftborder = (Math.floor((width - (blocksize * 10)) / 2));
    rightborder = (width - (blocksize * 10)) - leftborder;
    topoffset = height - ((Math.floor(height / blocksize)) * blocksize);
    blockborderoffset = ((Math.floor(blocksize / 10)) + 1);
    innerblocksize = blocksize - 2 * blockborderoffset;
    leftoffset = leftborder - 4 * blocksize;

    example = 5;
    x = 7;
    y = 0;
    timer = 1;
    block = [];
    block2 = [];
    type = Math.floor(Math.random() * 7);
    block = figure.getArray(type, 0);
    cell = [];
    color = [];

    for (var i = 0; i < 21; i++)
    {
        cell[i] = [];
        color[i] = [];
        for (var j = 0; j < 19; j++)
        {
            cell[i][j] = 0;
            color[i][j] = '#000000';
        }
    }
    current_color = Math.floor(Math.random() * colors.length);

    for (var i = 0; i < 21; i++)
    {
        for (var j = 0; j < 19; j++)
        {
            cell[i][j] = 0;
        }
    }
    for (var i = 0; i < 17; i++)
    {
        cell[i][0 + 3] = 1;
        cell[i][11 + 3] = 1;
    }
    for (var i = 1; i < 11; i++)
    {
        cell[16][i + 3] = 1;
    }
    for (var i = 0; i < 21; i++)
    {
        for (var j = 0; j < 19; j++)
        {
            color[i][j] = '#000000';
        }
    }
    setInterval(step_game, 30);
}

function step_game()
{
    if (keyboard.down || keypressed == 4)
    {
        game_speed = 1;
    }
    else
    {
        game_speed = 24;
    }
    if (timer % game_speed == 0)
    {
        collision = 0;
        for (var i = 0; i < 4; i++)
        {
            for (var j = 0; j < 4; j++)
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
            keypressed = 0;
            collision = 0;

            for (var i = 0; i < 4; i++)
            {
                for (var j = 0; j < 4; j++)
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
            current_color = Math.floor(Math.random() * colors.length);
            type = Math.floor(Math.random() * 7);
            rotation_index = 0;
            block = figure.getArray(type, 0);

            for (var i = 2; i < 16; i++)
            {
                horizontal_counter = 0;
                for (var j = 4; j < 14; j++)
                {
                    if (cell[i][j] == 1)
                    {
                        horizontal_counter++;
                    }
                }
                if (horizontal_counter == 10)
                {
                    for (var a = i; a > 1; a--)
                    {
                        for (var j = 4; j < 14; j++)
                        {

                            cell[a][j] = cell[a - 1][j];
                            color[a][j] = color[a - 1][j];
                        }
                    }
                }
            }
        }

        keyboard.up = false;
        keyboard.left = false;
        keyboard.right = false;
        invalidate();
    }
    timer++;
}

function onDraw()
{
    var g = myCanvas;
    g.ctxvar.fillStyle = "#000000";
    g.drawRect(new Rect(0, 0, width, height), paint);
    g.ctxvar.fillStyle = "#999999";
    g.drawRect(new Rect(0, 0, width, height), paint);
    g.ctxvar.fillStyle = "#000000";
    g.drawRect(new Rect(0, 0, leftborder, height), paint);
    g.drawRect(new Rect(width - rightborder, 0, rightborder + width - rightborder, height), paint);

    for (var i = 0; i < 4; i++)
    {
        for (var j = 0; j < 4; j++)
        {
            if (block[i][j] == 1)
            {
                g.ctxvar.fillStyle = "#000000";
                g.drawRect(new Rect(leftoffset + (x + j) * blocksize, topoffset + (y + i) * blocksize, blocksize + leftoffset + (x + j) * blocksize, blocksize + topoffset + (y + i) * blocksize), paint);
                g.ctxvar.fillStyle = colors[current_color];
                g.drawRect(new Rect(leftoffset + blockborderoffset + (x + j) * blocksize, topoffset + blockborderoffset + (y + i) * blocksize, innerblocksize + leftoffset + blockborderoffset + (x + j) * blocksize, innerblocksize + topoffset + blockborderoffset + (y + i) * blocksize), paint);
            }
        }
    }

    for (var i = 0; i < 21; i++)
    {
        for (var j = 0; j < 19; j++)
        {
            if (cell[i][j] == 1)
            {
                g.ctxvar.fillStyle = "#000000";
                g.drawRect(new Rect(leftoffset + j * blocksize, topoffset + i * blocksize, blocksize + leftoffset + j * blocksize, blocksize + topoffset + i * blocksize), paint);
                g.ctxvar.fillStyle = color[i][j];
                g.drawRect(new Rect(leftoffset + blockborderoffset + j * blocksize, topoffset + blockborderoffset + i * blocksize, innerblocksize + leftoffset + blockborderoffset + j * blocksize, innerblocksize + topoffset + blockborderoffset + i * blocksize), paint);
            }
        }
    }
}

function onTouch(xxx, yyy)
{
    if (keypressed != 4) game_speed = 24;
    var x_touch = xxx;
    var y_touch = yyy;

    if (keypressed == 0)
    {
        if (x_touch > 150 && x_touch < 480 - 150)
        {
            if (y_touch < 300)
            {
                keypressed = 1;
            }
        }
        if (x_touch > 0 && x_touch < 150)
        {
            keypressed = 2;
        }
        if (x_touch > 480 - 150 && x_touch < 480)
        {
            keypressed = 3;
        }
        if (x_touch > 150 && x_touch < 480 - 150 && keypressed < 2)
        {
            if (y_touch > 600)
            {
                keypressed = 4;
            }
        }
    }

    if (keyboard.up) keypressed = 1;
    if (keyboard.left) keypressed = 2;
    if (keyboard.right) keypressed = 3;
    if (keyboard.down) keypressed = 4;

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
        for (var i = 0; i < 4; i++)
        {
            for (var j = 0; j < 4; j++)
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
        for (var i = 0; i < 4; i++)
        {
            for (var j = 0; j < 4; j++)
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
            invalidate();
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
        for (var i = 0; i < 4; i++)
        {
            for (var j = 0; j < 4; j++)
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
            invalidate();
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
        keypressed = 4;
    }
    if (keypressed != 4) keypressed = 0;

    keyboard.up = false;
    keyboard.left = false;
    keyboard.right = false;

    return true;
}

function Figure(b)
{
    var drawing1 = [
        [0, 0, 0, 0],
        [0, 1, 1, 1],
        [0, 1, 0, 0],
        [0, 0, 0, 0]
    ];

    var drawing2 = [
        [0, 1, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 0, 0]
    ];

    var drawing3 = [
        [0, 0, 0, 1],
        [0, 1, 1, 1],
        [0, 0, 0, 0],
        [0, 0, 0, 0]
    ];

    var drawing4 = [
        [0, 0, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 1, 1],
        [0, 0, 0, 0]
    ];

    var drawing5 = [
        [0, 1, 1, 0],
        [0, 1, 1, 0],
        [0, 0, 0, 0],
        [0, 0, 0, 0]
    ];

    var drawing6 = [
        [0, 0, 0, 0],
        [0, 1, 1, 1],
        [0, 0, 0, 1],
        [0, 0, 0, 0]
    ];

    var drawing7 = [
        [0, 0, 1, 0],
        [0, 0, 1, 0],
        [0, 1, 1, 0],
        [0, 0, 0, 0]
    ];

    var drawing8 = [
        [0, 1, 0, 0],
        [0, 1, 1, 1],
        [0, 0, 0, 0],
        [0, 0, 0, 0]
    ];

    var drawing9 = [
        [0, 0, 1, 1],
        [0, 0, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 0, 0]
    ];
    var drawing10 = [
        [0, 0, 0, 0],
        [0, 1, 1, 0],
        [0, 0, 1, 1],
        [0, 0, 0, 0]
    ];

    var drawing11 = [
        [0, 0, 0, 1],
        [0, 0, 1, 1],
        [0, 0, 1, 0],
        [0, 0, 0, 0]
    ];
    var drawing12 = [
        [0, 0, 0, 0],
        [0, 0, 1, 1],
        [0, 1, 1, 0],
        [0, 0, 0, 0]
    ];

    var drawing13 = [
        [0, 0, 1, 0],
        [0, 0, 1, 1],
        [0, 0, 0, 1],
        [0, 0, 0, 0]
    ];

    var drawing15 = [
        [0, 0, 0, 0],
        [1, 1, 1, 1],
        [0, 0, 0, 0],
        [0, 0, 0, 0]
    ];

    var drawing16 = [
        [0, 1, 0, 0],
        [0, 1, 0, 0],
        [0, 1, 0, 0],
        [0, 1, 0, 0]
    ];
    var drawing17 = [
        [0, 0, 0, 0],
        [0, 1, 1, 1],
        [0, 0, 1, 0],
        [0, 0, 0, 0]
    ];

    var drawing18 = [
        [0, 0, 1, 0],
        [0, 1, 1, 0],
        [0, 0, 1, 0],
        [0, 0, 0, 0]
    ];

    var drawing19 = [
        [0, 0, 1, 0],
        [0, 1, 1, 1],
        [0, 0, 0, 0],
        [0, 0, 0, 0]
    ];

    var drawing20 = [
        [0, 0, 1, 0],
        [0, 0, 1, 1],
        [0, 0, 1, 0],
        [0, 0, 0, 0]
    ];

    this.attribute2 = b;

    Figure.prototype.getArray = function(type, rotation)
    {
        var a = drawing1;
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

function postInvalidate()
{
    invalidate();
}