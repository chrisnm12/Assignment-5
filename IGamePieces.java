import javalib.worldimages.*;
import java.awt.*;

public interface IGamePieces {
  WorldImage draw();
}

class Bullet implements IGamePieces {
  int radius;
  Color color;
  int speed;
  MyPosn position;
  Bullet(int radius, Color color, int speed, MyPosn position) {
    this.radius = radius;
    this.color = color;
    this.speed = speed;
    this.position = position;
  }

  public WorldImage draw() {
    return new Circle(new MyPosn(this.position.x, this.position.y), new MyPosn(0, this.speed), this.color, this.radius, 1).draw();
  }
}

class Ship implements IGamePieces {
  int radius;
  Color color;
  int speed;
  MyPosn position;
  Ship(int radius, Color color, int speed, MyPosn position) {
    this.radius = radius;
    this.color = color;
    this.speed = speed;
    this.position = position;
  }
  public WorldImage draw() {
    return new Circle(new MyPosn(this.position.x, this.position.y), new MyPosn(0, this.speed), this.color, this.radius, 0).draw();
  }
}

class StartingText implements IGamePieces {
  String text;
  int size;
  StartingText(String text, int size) {
    this.text = text;
    this.size = size;
  }
  public WorldImage draw() {
    return new TextImage(this.text, this.size, FontStyle.BOLD, Color.BLACK);
  }
}
