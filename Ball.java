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
    return (dirxCor != 0) && (diryCor != 0);
  }

  public synchronized void throwBall(int x, int y) {
    this.dirxCor = x;
    this.diryCor = y;
  }

  @Override
  public void run() {
    while (this.room.getCurentPlayrsNum() > 1) {
      try {
        this.sleep(waitms);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (this.inMovement()) {}
    }
    room.removeObject(xCor, yCor, this);
  }

  @Override
  public String toString() {
    return "o";
  }
}
