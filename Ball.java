public class Ball extends Thread {

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
    return !(this.dirxCor == 0 && this.diryCor == 0);
  }

  public synchronized void throwBall(int x, int y) {
    this.dirxCor = x;
    this.diryCor = y;
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
      if (this.inMovement() && (newX < 0 && newX > 4 && newY < 0 && newY > 4)) {
        this.dirxCor = 0;
        this.diryCor = 0;
      }
      // dodge player
      Object obj = room.getObject(newX, newY);
      if (this.inMovement() && obj instanceof Player) {
        ((Player) obj).gameEnd();
        this.dirxCor = 0;
        this.diryCor = 0;
      }
      if (this.inMovement() && obj instanceof Empty) {
        // move forward
        room.replaceObject(xCor, yCor, newX, newY, this);
        this.xCor += newX;
        this.yCor += newY;
      }
    }
    room.removeObject(xCor, yCor, this);
  }

  @Override
  public String toString() {
    return "o";
  }
}
