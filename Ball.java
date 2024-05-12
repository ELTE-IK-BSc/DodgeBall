import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Ball extends Thread {

  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

  public int waitms = 50;
  private Room room;
  private int xCor;
  private int yCor;
  private int dirxCor;
  private int diryCor;

  public Ball(int xCor, int yCor, Room room) {
    super();
    this.xCor = xCor;
    this.yCor = yCor;
    this.room = room;
    this.dirxCor = 0;
    this.diryCor = 0;
    room.addObject(xCor, yCor, this);
  }

  public synchronized boolean inMovement() {
    readLock.lock();
    try {
      return !(this.dirxCor == 0 && this.diryCor == 0);
    } finally {
      readLock.unlock();
    }
  }

  public synchronized void throwBall(int x, int y) {
    writeLock.lock();
    try {
      this.dirxCor = x;
      this.diryCor = y;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public void run() {
    while (this.room.getCurentPlayrsNum() > 1) {
      try {
        sleep(waitms);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      int newX = xCor + dirxCor;
      int newY = yCor + diryCor;
      // stop at the walls
      if (this.inMovement() && (newX < 0 || newX > 4 || newY < 0 || newY > 4)) {
        this.dirxCor = 0;
        this.diryCor = 0;
      }
      // dodge player
      if (this.inMovement()) {
        Object obj = room.getObject(newX, newY);
        synchronized (obj) {
          if (obj instanceof Player) {
            ((Player) obj).gameEnd();
            this.dirxCor = 0;
            this.diryCor = 0;
          } else {
            // move forward
            room.replaceObject(xCor, yCor, newX, newY, this);
            this.xCor = newX;
            this.yCor = newY;
          }
        }
      }
    }
    room.removeObject(xCor, yCor, this);
  }

  @Override
  public String toString() {
    return "o";
  }
}
