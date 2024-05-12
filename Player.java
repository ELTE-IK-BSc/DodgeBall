import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player extends Thread {

  private String name;
  private Room room;
  public int waitms = 100;
  private boolean active = true;

  public Player(String name, Room room) {
    super();
    this.name = name;
    this.room = room;
  }

  @Override
  public void run() {
    int xCor = 0;
    int yCor = 0;
    boolean placed = false;
    for (int i = 0; i < room.getWidth(); i++) {
      for (int j = 0; j < room.getHeight(); j++) {
        if (room.getObject(i, j) instanceof Empty && !placed) {
          placed = true;
          room.addObject(i, j, this);
          xCor = i;
          yCor = j;
        }
      }
    }
    while (active) {
      try {
        sleep(waitms);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      Random random = new Random();

      int randomX = random.nextInt((xCor + 1) - (xCor - 1) + 1) + (xCor - 1);
      int randomY = random.nextInt((yCor + 1) - (yCor - 1) + 1) + (yCor - 1);

      if (
        randomX >= room.getWidth() ||
        0 > randomX ||
        randomY >= room.getHeight() ||
        0 > randomY
      ) {
        continue;
      }
      if (!(room.getObject(randomX, randomY) instanceof Empty)) {
        continue;
      }

      room.replaceObject(xCor, yCor, randomX, randomY, this);
      xCor = randomX;
      yCor = randomY;

      if (room.getObject(xCor + 1, yCor + 1) instanceof Ball) {
        Ball ball = (Ball) room.getObject(xCor + 1, yCor + 1);
        if (!ball.inMovement()) {
          this.throwBallto(ball, xCor, yCor);
        }
      }
      if (room.getObject(xCor - 1, yCor - 1) instanceof Ball) {
        Ball ball = (Ball) room.getObject(xCor - 1, yCor - 1);
        if (!ball.inMovement()) {
          this.throwBallto(ball, xCor, yCor);
        }
      }
      if (room.getObject(xCor - 1, yCor + 1) instanceof Ball) {
        Ball ball = (Ball) room.getObject(xCor - 1, yCor + 1);
        if (!ball.inMovement()) {
          this.throwBallto(ball, xCor, yCor);
        }
      }
      if (room.getObject(xCor + 1, yCor - 1) instanceof Ball) {
        Ball ball = (Ball) room.getObject(xCor + 1, yCor - 1);
        if (!ball.inMovement()) {
          this.throwBallto(ball, xCor, yCor);
        }
      }
    }
    room.removeObject(xCor, yCor, this);
  }

  private synchronized void throwBallto(Ball ball, int x, int y) {
    ArrayList<Integer> axisX = new ArrayList<Integer>();
    ArrayList<Integer> axisY = new ArrayList<Integer>();
    for (int i = x - 1; i <= x + 1; i++) {
      axisX.add(i);
    }
    for (int j = y - 1; j <= y + 1; j++) {
      axisY.add(j);
    }
    Collections.shuffle(axisX);
    Collections.shuffle(axisY);

    int dirX = axisX.get(0);
    int dirY = axisY.get(0);
    while ((dirX != x && dirY != y) || (dirX == x && dirY == y - 1)) {
      Collections.shuffle(axisX);
      Collections.shuffle(axisY);

      dirX = axisX.get(0);
      dirY = axisY.get(0);
    }

    ball.throwBall(dirX, dirY);
  }

  public synchronized void gameEnd() {
    this.active = false;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
