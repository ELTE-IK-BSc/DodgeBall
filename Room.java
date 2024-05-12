public class Room {

  private int width;
  private int height;
  public Object[][] matrix;
  private int curentPlayrsNum;

  public Room(int width, int height) {
    this.width = width;
    this.height = height;
    matrix = new Object[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        matrix[i][j] = new Empty();
      }
    }
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public synchronized void printRoom() {
    System.out.print("\033[H\033[2J");
    System.out.print("\u001B[0;0H");
    for (int i = 0; i < this.width; i++) {
      if (i == 0) {
        System.out.print("+");
        for (int k = 0; k < this.width; k++) {
          System.out.print("-");
        }
        System.out.print("+\n");
      }

      System.out.print("|");
      for (int j = 0; j < this.height; j++) {
        System.out.print(matrix[i][j].toString());
      }
      System.out.print("|\n");

      if (i == this.width - 1) {
        System.out.print("+");
        for (int k = 0; k < this.width; k++) {
          System.out.print("-");
        }
        System.out.print("+");
      }
    }
  }

  public synchronized Object getObject(int x, int y) {
    if (x >= 0 && x < 5 && y >= 0 && y < 5) {
      return matrix[x][y];
    }
    return null;
  }

  public synchronized int getCurentPlayrsNum() {
    return curentPlayrsNum;
  }

  public synchronized void addObject(int x, int y, Object object) {
    matrix[x][y] = object;
    if (object instanceof Player) {
      curentPlayrsNum++;
    }
  }

  public synchronized void replaceObject(
    int fromx,
    int fromy,
    int tox,
    int toy,
    Object object
  ) {
    matrix[fromx][fromy] = new Empty();
    matrix[tox][toy] = object;
  }

  public synchronized void removeObject(int x, int y, Object object) {
    matrix[x][y] = object;
    if (object instanceof Player) {
      curentPlayrsNum--;
    }
  }
}
