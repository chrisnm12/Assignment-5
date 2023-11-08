import javalib.worldimages.*;

import java.awt.*;

public interface IGamePieces {
  WorldImage draw();
  IGamePieces update();
}

class MyPosn {
  int x;
  int y;

  MyPosn(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // Function to move the position of the bullet
  MyPosn move(int dy) {
    return new MyPosn(this.x, this.y - dy);
  }
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
  public Bullet move() {
    return new Bullet(this.radius, this.color, this.speed,
      this.position.move(this.speed));
  }

  public WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color);
  }

  public Bullet update() {
    return new Bullet(this.radius, this.color, this.speed,
      new MyPosn(this.position.x, this.position.y - this.speed));
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
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color);
  }

  public Ship update() {
    return new Ship(this.radius, this.color, this.speed,
      new MyPosn(this.position.x + this.speed, this.position.y));
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
  public StartingText update() {
    return null;
  }

}
