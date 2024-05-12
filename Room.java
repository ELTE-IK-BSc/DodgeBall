import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Room {

  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

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
    readLock.lock();
    try {
      return this.width;
    } finally {
      readLock.unlock();
    }
  }

  public int getHeight() {
    readLock.lock();
    try {
      return this.height;
    } finally {
      readLock.unlock();
    }
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
        System.out.println("+");
        System.out.println("Players: " + getCurentPlayrsNum());
      }
    }
  }

  public Object getObject(int x, int y) {
    if (x >= 0 && x < 5 && y >= 0 && y < 5) {
      readLock.lock();
      try {
        return matrix[x][y];
      } finally {
        readLock.unlock();
      }
    }
    return null;
  }

  public synchronized int getCurentPlayrsNum() {
    readLock.lock();
    try {
      return curentPlayrsNum;
    } finally {
      readLock.unlock();
    }
  }

  public void addObject(int x, int y, Object object) {
    writeLock.lock();
    try {
      matrix[x][y] = object;
      if (object instanceof Player) {
        curentPlayrsNum++;
      }
    } finally {
      writeLock.unlock();
    }
  }

  public void replaceObject(
    int fromx,
    int fromy,
    int tox,
    int toy,
    Object object
  ) {
    writeLock.lock();
    try {
      matrix[fromx][fromy] = new Empty();
      matrix[tox][toy] = object;
    } finally {
      writeLock.unlock();
    }
  }

  public void removeObject(int x, int y, Object object) {
    writeLock.lock();
    try {
      if (matrix[x][y] == object) {
        matrix[x][y] = new Empty();
        if (object instanceof Player) {
          curentPlayrsNum--;
        }
      }
    } finally {
      writeLock.unlock();
    }
  }
}
