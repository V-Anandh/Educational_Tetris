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

  void clear() {
    for (int i = 0; i < cols; ++i)
      for (int j = 0; j < rows; ++j)
        colors[i][j] = 0;
  }

  void draw() {
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

  void fillSquare(int col, int row, color c) {
    if (col < 0 || col >= cols || row < 0 || row >= rows)
      return;
    noStroke();
    fill(c);
    rect(x + col*(myWidth/cols), y + row*(myHeight/rows), myWidth/cols, myHeight/rows);
  }

  void outlineSquare(int col, int row) {
    if (col < 0 || col >= cols || row < 0 || row >= rows)
      return;
    fill(outCol,60);
    noStroke();
    rect(x + col*(myWidth/cols), y + row*(myHeight/rows), myWidth/cols, myHeight/rows);
  }

  void endTurn() {
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

  boolean checkLines() {
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

  void eraseCleared() {
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

  boolean isOccupied(int x, int y) {
    if (y < 0 && x < cols && x >= 0) // allow movement/flipping to spaces above the board
      return false;
    return (x >= cols || x < 0 || y >= rows || colors[x][y] != 0);
  }
  void gridlines() {
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
