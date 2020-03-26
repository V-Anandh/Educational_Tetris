import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Educational_Tetris extends PApplet {

/*
  Educational Tetris
 Author: Vivek Anandh
 Original Game Developer: Alexey Pajitnov
 Base Tetris Processing Code Inspired By: Karl Hiner
 Controls:
 Solve for L on the left equation to move left
 Solve for R on the left equation to move right
 DOWN or "+" or "=" to go down one block
 UP or "/" or "-" to flip
 SPACE - hard drop (drop immediately)
 */


final int LIGHTBLUE = color(116, 251, 253 );
final int ORANGE = color(243, 169, 60);
final int YELLOW = color(255, 253, 0);
final int PURPLE = color(156, 48, 245);
final int BLUE = color(0, 34, 244);
final int RED = color(233, 50, 35);
final int GREEN = color(117, 249, 76);

PImage photo;
Grid board, preview;
Tetromino curr;
Shape next;
Shape[] shapes = new Shape[7];
int timer = 50;
int currTime = 0;
int score = 0;
int lines = 0;
int level = 1;
final int SPEED_DECREASE = 2;
boolean game_over = false;
int realNumL, realNumR;
boolean play = false;


public void setup() {
  
  textSize(25);
   photo = loadImage("Start.png");
  shapes[0] = new Shape(4, new int[] {8, 9, 10, 11}, LIGHTBLUE); 
  shapes[1] = new Shape(3, new int[] {0, 3, 4, 5}, BLUE);  
  shapes[2] = new Shape(3, new int[] {2, 3, 4, 5}, ORANGE);  
  shapes[3] = new Shape(2, new int[] {0, 1, 2, 3}, YELLOW);  
  shapes[4] = new Shape(4, new int[] {5, 6, 8, 9}, GREEN);  
  shapes[5] = new Shape(3, new int[] {1, 3, 4, 5, }, PURPLE); 
  shapes[6] = new Shape(4, new int[] {4, 5, 9, 10}, RED);
  board = new Grid(20, 20, 321, 642, 20, 10);
  preview = new Grid(355, 20, 116, 58, 2, 4);
  next = shapes[(int)random(7)];
  loadNext();
}

public void draw() {
  if (play == false) {
    startScreen();
    board.clear();
    loadNext();
  if (mousePressed) {
    play = true;
  }  } if (play == true){
  realNumL = board.l2 - board.l1;
  realNumR = board.r2 - board.r1;
  if (realNumR<0) {
    board.r1 = (int)random(10);
  }
  if (realNumL<0) {
    board.l1 = (int)random(10);
  }
  if (realNumL == realNumR) {
    board.l1 = (int)random(10);
  }
  background(0);
  if (game_over) {
    text("GAME OVER\nSCORE: " + score, width/2 - 70, height/2 - 50);
    text("TO PLAY AGAIN, PRESS P", width/3 - 55, height/2 +30);
    if (key == 'p'){
    play = false;
    }
    return;
  }
  currTime++;
  if (currTime >= timer && board.animateCount == -1)
    curr.stepDown();
  preview.draw();
  board.draw();
  if (curr != null)
    curr.draw();
  fill(255);
  text("LEVEL\n" + level, width - 250, 120);
  text("LINES\n" + lines, width - 250, 200);
  text("SCORE\n" + score, width - 250, 280);
  text("LEFT EQUATION\n" + board.l1 + " +" + " L " + "= " + board.l2, width - 250, 360);
  text("RIGHT EQUATION\n" + board.r1 + " +" + " R " + "= " + board.r2, width - 250, 440);

  next.preview();
}
}

public void loadNext() {
  curr = new Tetromino(next);
  next = shapes[(int)random(7)];
  currTime = 0;
}

public void keyPressed() {
  int userNum = Character.getNumericValue(key); 
  if (curr == null || game_over) {
    return;
  }
  switch(keyCode) {
  case UP :  
    curr.rotate();  
    break;
  case '-' : 
    curr.rotate(); 
    break;
  case '+' :  
    curr.down(); 
    break;
  case '=' : 
    curr.down(); 
    break;   
  case DOWN : 
    curr.down(); 
    break;
  case ' ' : 
    curr.hardDown(); 
    break;
  }
  if (userNum == realNumR) {
    curr.right();
  }
  if (userNum == realNumL) {
    curr.left();
  }
}

public void startScreen() {
  background(0);
  text("EDUCATIONAL TETRIS", width/3-40, height/2 - 50);
  text("CLICK TO BEGIN", width/3-10, height/2  );

}
class Grid {
  int outCol;
  int r1 =(int)random(10), r2 =(int)random(10), l1 =(int)random(10), l2 =(int)random(10);
  int x, y;
  int myWidth, myHeight;
  int rows, cols;
  int[][] colors;
  ArrayList<Integer> clearedRows = new ArrayList<Integer>();
  int animateCount = -1;

  Grid(int x, int y, int w, int h, int rows, int cols) {
    this.x = x;
    this.y = y;
    myWidth = w;
    myHeight = h;
    this.rows = rows;
    this.cols = cols;
    colors = new int[cols][rows];
    for (int i = 0; i < cols; ++i)
      for (int j = 0; j < rows; ++j)
        colors[i][j] = 0;
  }

  public void clear() {
    for (int i = 0; i < cols; ++i)
      for (int j = 0; j < rows; ++j)
        colors[i][j] = 0;
  }

  public void draw() {
    stroke(255);
    strokeWeight(2);
    rect(x, y, myWidth, myHeight-1);
    for (int i = 0; i < cols; ++i)
      for (int j = 0; j < rows; ++j)
        fillSquare(i, j, colors[i][j]);
    // line clear animation
    if (animateCount >= 0) {
      int c = (animateCount < 255) ? animateCount : 255 - animateCount%255;
      if (clearedRows.size() == 4)
        c = color(0, c, c); 
      for (int row : clearedRows)
        for (int i = 0; i < cols; ++i)
          fillSquare(i, row, color(c, 200));
      animateCount += 10;
      if (animateCount > 2*255) {
        animateCount = -1;
        eraseCleared();
        loadNext();
      }
    }
  }

  public void fillSquare(int col, int row, int c) {
    if (col < 0 || col >= cols || row < 0 || row >= rows)
      return;
    noStroke();
    fill(c);
    rect(x + col*(myWidth/cols), y + row*(myHeight/rows), myWidth/cols, myHeight/rows);
  }

  public void outlineSquare(int col, int row) {
    if (col < 0 || col >= cols || row < 0 || row >= rows)
      return;
    fill(outCol,60);
    noStroke();
    rect(x + col*(myWidth/cols), y + row*(myHeight/rows), myWidth/cols, myHeight/rows);
  }

  public void endTurn() {
    for (int i = 0; i < curr.shape.matrix.length; ++i)
      for (int j = 0; j < curr.shape.matrix.length; ++j)
        if (curr.shape.matrix[i][j] && j + curr.y >= 0) 
          colors[i + curr.x][j + curr.y] = curr.getColor();
    if (checkLines()) {
      curr = null;
      animateCount = 0;
    } else
      loadNext();


    r1 =(int)random(10);
    r2 =(int)random(10);
    l1 =(int)random(10);
    l2 =(int)random(10);
  }

  public boolean checkLines() {
    clearedRows.clear();
    for (int j = 0; j < rows; ++j) {
      int count = 0;
      for (int i = 0; i < cols; ++i)
        if (isOccupied(i, j))
          count++;
      if (count >= cols)
        clearedRows.add(j);
    }
    if (clearedRows.isEmpty())
      return false;

    if (lines/10 < (lines + clearedRows.size())/10) {
      level++;
      timer -= SPEED_DECREASE;
    }
    lines += clearedRows.size();
    score += (1 << clearedRows.size() - 1)*100;
    return true;
  }

  public void eraseCleared() {
    for (int row : clearedRows) {
      for (int j = row - 1; j > 0; --j) {
        int[] rowCopy = new int[cols];
        for (int i = 0; i < cols; ++i)
          rowCopy[i] = colors[i][j];
        for (int i = 0; i < cols; ++i)
          colors[i][j + 1] = rowCopy[i];
      }
    }
  }

  public boolean isOccupied(int x, int y) {
    if (y < 0 && x < cols && x >= 0) // allow movement/flipping to spaces above the board
      return false;
    return (x >= cols || x < 0 || y >= rows || colors[x][y] != 0);
  }
  public void gridlines() {
    stroke(255);
    strokeWeight(2);
    line(52, 20, 52, 660);
    line(84, 20, 84, 660);
    line(116, 20, 116, 660);
    line(148, 20, 148, 660);
    line(180, 20, 180, 660);
    line(212, 20, 212, 660);
    line(244, 20, 244, 660);
    line(276, 20, 276, 660);
    line(308, 20, 308, 660);
    line(21, 628, 339, 628);
    line(21, 596, 339, 596);
    line(21, 564, 339, 564);
    line(21, 532, 339, 532);
    line(21, 500, 339, 500);
    line(21, 468, 339, 468);
    line(21, 436, 339, 436);
    line(21, 404, 339, 404);
    line(21, 372, 339, 372);
    line(21, 340, 339, 340);
    line(21, 308, 339, 308);
    line(21, 276, 339, 276);
    line(21, 244, 339, 244);
    line(21, 212, 339, 212);
    line(21, 180, 339, 180);
    line(21, 148, 339, 148);
    line(21, 116, 339, 116);
    line(21, 84, 339, 84);
    line(21, 52, 339, 52);
  }
}
class Shape {
  boolean[][] matrix;
  int c;
  
  Shape(int n, int[] blockNums, int c) {
    matrix = new boolean[n][n];
    for (int x = 0; x < n; ++x)
      for (int y = 0; y < n; ++y) 
        matrix[x][y] = false;
    for (int i = 0; i < blockNums.length; ++i)
      matrix[blockNums[i]%n][blockNums[i]/n] = true;
    this.c = c;
  }
  
  Shape(Shape other) {
    matrix = new boolean[other.matrix.length][other.matrix.length];
    for (int x = 0; x < matrix.length; ++x)
      for (int y = 0; y < matrix.length; ++y)
        matrix[x][y] = other.matrix[x][y];
    this.c = other.c;
  }
  
  public void preview() {
    int startJ = 1;  
    for (int i = 0; i < matrix.length; ++i)
      if (matrix[i][0])
        startJ = 0;
    for (int i = 0; i < matrix.length; ++i)
      for (int j = startJ; j < matrix.length; ++j)
        if (matrix[i][j])
          preview.fillSquare(i, j - startJ, c);
  }
}
class Tetromino {
  Shape shape;
  int x, y;
  int final_row;
  
  Tetromino(Shape shape) {
    this.shape = new Shape(shape);
    x = 3;
    y = -2;
    final_row = getFinalRow();
    game_over = !isLegal(this.shape.matrix, 3, -1);
  }
  
  public int getColor() { return shape.c; }
  
  public void left() {
    if (isLegal(shape.matrix, x - 1, y))
      x--;
    else if (isLegal(shape.matrix, x - 2, y))
      x -= 2;
    update();
  }
  
  public void right() {
    if (isLegal(shape.matrix, x + 1, y))
      x++;
    else if (isLegal(shape.matrix, x + 2, y))
      x += 2;
    update();
  }
  
  public void update() {
    final_row = getFinalRow();
    // reset the timer if player is at the bottom, for wiggle room before it locks
    if (y == final_row)
      currTime = -20;
  }
  
  // used when player presses down.
  public void down() {
    if (y >= final_row) {
      // if already at the bottom, down shortcuts to lock current and load next block
      board.endTurn();
    } else {
      stepDown();
      score += 1;  // get a point for manual down
    }
  }
  
  // used when automatically moving the block down.
  public void stepDown() {
    if (y >= final_row) {
      board.endTurn();
    } else {
      y++;
      currTime = 0;
    }
  }
  
  // move block all the way to the bottom
  public void hardDown() {
    score += (board.rows - y);
    y = final_row;
    board.endTurn();
  }
  
  public void rotate() {
    boolean[][] ret = new boolean[shape.matrix.length][shape.matrix.length];
    for (int x = 0; x < ret.length; ++x)
        for (int y = 0; y < ret.length; ++y)
            ret[x][y] = shape.matrix[y][ret.length - 1 - x];
    if (isLegal(ret, x, y)) {
      shape.matrix = ret;
      update();
    } else if (isLegal(ret, x + 1, y) || isLegal(ret, x + 2, y)) {
      shape.matrix = ret;
      right();
    } else if (isLegal(ret, x - 1, y) || isLegal(ret, x - 2, y)) {
      shape.matrix = ret;
      left();
    }
  }
  
  public int getFinalRow() {
    int start = max (0, y);
    for (int row = start; row <= board.rows; ++row)
      if (!isLegal(shape.matrix, x, row))
        return row - 1;
    return -1;
  }
  
  public boolean isLegal(boolean[][] matrix, int col, int row) {
    for (int i = 0; i < matrix.length; ++i)
      for (int j = 0; j < matrix.length; ++j)
        if (matrix[i][j] && board.isOccupied(col + i, row + j))
          return false;
    return true;
  }
  
  public void draw() {
    for (int i = 0; i < shape.matrix.length; ++i) {
      for (int j = 0; j < shape.matrix.length; ++j) {
        if (shape.matrix[i][j]) {
          board.fillSquare(x + i, y + j, shape.c);
          board.outCol = shape.c;
          board.outlineSquare(x + i, final_row + j);
          board.gridlines();          
        }
      }
    }
  }
  
}
  public void settings() {  size(600, 690, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Educational_Tetris" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
