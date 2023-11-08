import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
class MyPosn extends Posn {

  // standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }

  // constructor to convert from a Posn to a MyPosn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // Method to add another MyPosn and return a new MyPosn
  MyPosn add(MyPosn other) {
    int newX = this.x + other.x;
    int newY = this.y + other.y;
    return new MyPosn(newX, newY);
  }

  // Method to check if the position is offscreen
  boolean isOffscreen(int screenWidth, int screenHeight) {
    return (x < 0 || x >= screenWidth || y < 0 || y >= screenHeight);
  }
}

public class Circle {
  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick
  Color color;
  int radius;

  Circle(MyPosn position, MyPosn velocity, Color color, int radius) {
    this.position = position;
    this.velocity = velocity;
    this.color = color;
    this.radius = radius;
  }

  Circle move() {
    MyPosn newPosition = position.add(velocity);
    return new Circle(newPosition, velocity, color, radius);
  }
  boolean isCenterOffscreen(int screenWidth, int screenHeight) {
    return position.isOffscreen(screenWidth, screenHeight);
  }
  WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color)
      .movePinhole(this.position.x, this.position.y);
  }
  void place(WorldScene scene) {
    scene.placeImageXY(this.draw(), this.position.x, this.position.y);
  }
  void placeAll(ILoCircle circles, WorldScene scene) {
    if (!circles.isEmpty()) {
      circles.getFirst().place(scene); // Place the first circle
      placeAll(circles.getRest(), scene); // Recursively place the rest of the circles
    }
  }
}


class CircleExamples {
  boolean testBigBang(Tester t) {
    World w = new World();
    int worldWidth = 500;
    int worldHeight = 300;
    double tickRate = 1;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }
}