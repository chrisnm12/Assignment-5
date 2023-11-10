import javalib.funworld.World;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.util.Random;

class MyPosn extends Posn {
  MyPosn(int x, int y) {
    super(x, y);
  }
  MyPosn(Posn p) {
    this(p.x, p.y);
  }
  boolean isOffscreen(int screenWidth, int screenHeight) {
    return (x < -250 || x > 250 || y < 0 || y > screenHeight);
  }
}

public class Circle {
  MyPosn position;
  MyPosn velocity;
  Color color;
  int radius;
  int n;
  Circle(MyPosn position, MyPosn velocity, Color color, int radius, int n) {
    this.position = position;
    this.velocity = velocity;
    this.color = color;
    this.radius = radius;
    this.n = n;
  }

  boolean isCenterOffscreen(int screenWidth, int screenHeight) {
    return position.isOffscreen(screenWidth, screenHeight);
  }

  WorldImage draw() {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color)
      .movePinhole(this.position.x, this.position.y);
  }

  Circle moveX() {
    MyPosn newPosition = new MyPosn(this.position.x + this.velocity.x, this.position.y);
    return new Circle(newPosition, this.velocity, this.color, this.radius, this.n);
  }

  Circle moveY() {
    MyPosn newPosition = new MyPosn(this.position.x, this.position.y + this.velocity.y);
    return new Circle(newPosition, this.velocity, this.color, this.radius, this.n);
  }

  public boolean isHit(Circle ship) {
    int dx = this.position.x - ship.position.x;
    int dy = this.position.y - ship.position.y;
    double distance = Math.sqrt(dx * dx + dy * dy);

    return distance < this.radius + ship.radius;
  }

  public Circle isHitAny(ILoCircle others) {
    if (others.isEmpty()) {
      return null;
    } else {
      Circle current = others.getFirst();
      if (this.isHit(current)) {
        return current;
      } else {
        return this.isHitAny(others.getRest());
      }
    }
  }
  ILoCircle explode() {
    return explodeHelper(0, new MtLoCircle());
  }
  ILoCircle explodeHelper(int i, ILoCircle explodedBullets) {
    if (i <= n && n < 5) {
      double angleIncrement = 360.0 / (n + 1);
      double angle = i * angleIncrement;
      double radians = Math.toRadians(angle);

      int xChange = (int) (Math.cos(radians) * 20);
      int yChange = (int) (Math.sin(radians) * 20);

      MyPosn newVelocity = new MyPosn(xChange, yChange);
      Circle explodedBullet = new Circle(this.position, newVelocity, this.color, this.grow(), this.n + 1);

      return explodeHelper(i + 1, explodedBullets.append(new ConsLoCircle(explodedBullet, new MtLoCircle())));
    } else {
      return explodedBullets;
    }
  }
  int grow() {
    if (this.radius < 12) {
      return this.radius + 2;
    } else {
      return this.radius;
    }
  }
}


class CircleExamples {
  boolean testBigBang(Tester t) {
    World w = new StartingWorld(10, new Random());
    int worldWidth = 500;
    int worldHeight = 300;
    double tickRate = 0.0357142857;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }
  // Ran out of time for tests unfortunately...
}