var myCanvas;

function start_application()
{
    myCanvas = new MyCanvas();
    start_game();
    mobile_on_resize();
    keyboard_init();
}

// from http://stackoverflow.com/questions/2450954/how-to-randomize-shuffle-a-javascript-array
Array.prototype.shuffle = function()
{
    var i = this.length, j, temp;
    if (i == 0) return this;
    while (--i)
    {
        j = Math.floor(Math.random() * (i + 1));
        temp = this[i];
        this[i] = this[j];
        this[j] = temp;
    }
    return this;
}

var total_images_loaded = 0;

function load_image(url, total_images)
{
    var image = new Image();
    image.onload = function()
    {
        total_images_loaded++;
        if (total_images_loaded == total_images)
        {
            start_application();
        }
    };
    image.src = url;
    return image;
}

function Point(x, y)
{
    if (x == null || y == null)
    {
        this.x = -1;
        this.y = -1;
    }
    else
    {
        this.x = x;
        this.y = y;
    }
}

var images_loaded = 0;

function init_load_pics()
{
    images_loaded++;
    if (images_loaded == 13) draw();
}

var board = new Image();
board.onload = function()
{
    init_load_pics()
};

function Rect(x, y, endx, endy)
{
    this.x = x;
    this.y = y;
    this.endx = endx;
    this.endy = endy;
}

function MyCanvas()
{
    var canvas = document.getElementById("myCanvas");
    var ctx = canvas.getContext("2d");
    canvas.addEventListener("mousedown", processClicks, false);
    this.ctxvar = ctx;

    MyCanvas.prototype.drawBitmap = function(arg1, arg2, arg3, arg4)
    {
        if (typeof arg3 === "rect")
        {
            ctx.drawImage(arg1, 0, 0, arg1.width, arg1.height, arg3.x, arg3.y - 0, arg3.endx - arg3.x, arg3.endy - arg3.y);
        }
        else
        {
            ctx.drawImage(arg1, arg2, arg3);
        }
    };

    MyCanvas.prototype.drawRect = function(arg3, arg4)
    {
        ctx.fillRect(arg3.x, arg3.y, arg3.endx - arg3.x, arg3.endy - arg3.y);
    };
}

function List()
{
    this.array = [];

    List.prototype.size = function()
    {
        return this.array.length;
    };

    List.prototype.get = function(i)
    {
        return this.array[i];
    };

    List.prototype.add = function(obj)
    {
        this.array.push(obj);
    };

    List.prototype.shuffle = function()
    {
        this.array.shuffle();
    };
}

function invalidate()
{
    draw();
}

function processClicks(event)
{
    var x = event.x | event.clientX;
    var y = event.y | event.clientY;
    var canvas = document.getElementById("myCanvas");
    x -= canvas.offsetLeft;
    y -= canvas.offsetTop;

    y = (y / new_canvas_height) * map_height;
    y = Math.floor(y);
    x = (x / new_canvas_width) * map_width;
    x = Math.floor(x);
    onTouch(x, y);
}

function draw()
{
    onDraw();
}

var keyboard = new Object();
keyboard.left = false;
keyboard.right = false;
keyboard.up = false;
keyboard.down = false;

function keyboard_init()
{
    document.onkeyup = function(e)
    {
        if (e.keyCode == '40')
        {
            keyboard.down = false;
            keypressed = 0;
        }
    }
    document.onkeydown = function(e)
    {
        keyboard.up = false;
        keyboard.down = false;
        keyboard.left = false;
        keyboard.right = false;

        if (e.keyCode == '38')
        {
            keyboard.up = true;
            onTouch(-100, -100);
        }
        else if (e.keyCode == '40')
        {
            keyboard.down = true;
            onTouch(-100, -100);
        }
        else if (e.keyCode == '37')
        {
            keyboard.left = true;
            onTouch(-100, -100);
        }
        else if (e.keyCode == '39')
        {
            keyboard.right = true;
            onTouch(-100, -100);
        }
    }
}