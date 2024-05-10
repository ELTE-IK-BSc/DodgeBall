public class Game {

  public static int dimW = 5;
  public static int dimH = 5;
  public static int numofplayers = 10;
  public static int waitms = 50;

  public static void main(String[] args) {
    Room room = new Room(dimW, dimH);
    for (int i = 0; i < numofplayers; i++) {
      Player player = new Player(Character.toString((char) (65 + i)), room);
      player.start();
    }
    Ball ball = new Ball(3, 4, room);
    ball.start();

    while (room.getCurentPlayrsNum() > 1) {
      try {
        Thread.sleep(waitms);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      room.printRoom();
    }
    System.out.print("Gyoztes: ");
  }
}
