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


void setup() {
  size(600, 690, P2D);
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

void draw() {
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

void loadNext() {
  curr = new Tetromino(next);
  next = shapes[(int)random(7)];
  currTime = 0;
}

void keyPressed() {
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

void startScreen() {
  background(0);
  text("EDUCATIONAL TETRIS", width/3-40, height/2 - 50);
  text("CLICK TO BEGIN", width/3-10, height/2  );

}
