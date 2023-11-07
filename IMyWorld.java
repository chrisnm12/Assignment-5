import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;

public interface IMyWorld {
}
abstract class AWorld extends World {
  int bullets;
  AWorld(int bullets) {
    this.bullets = bullets;
  }
}

class StartingWorld extends AWorld {
  StartingWorld(int bullets) {
    super(bullets);
  }


  public World onTick() {
    return this;
  }


  public World onMouseClicked (Posn pos, String buttonname) {
    if (buttonname.equals("LeftButton")) {
      return new GameWorld(10);
    }  {
    return this;
    }
  }

  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("Start");
    IGamePieces bulletText = new StartingText("Bullets: " + this.bullets);
    WorldScene scene1 = scene.placeImageXY(text.draw(50), 250, 150);
    return scene1.placeImageXY(bulletText.draw(25), 70,280);
  }
}

class GameWorld extends AWorld {

  GameWorld(int bullets) {
    super(bullets);
  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("GAME");
    return scene.placeImageXY(text.draw(50),250, 150);
  }
}
interface IGamePieces {
  WorldImage draw(int size);
}
class Bullet implements IGamePieces {
  int radius;
  Color color;
  int speed;
  Bullet(int radius, Color color, int speed) {
    this.radius = radius;
    this.color = color;
    this.speed = speed;
  }
  public WorldImage draw(int size) {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color);
  }
}

class Ship implements IGamePieces {
  int radius;
  Color color;
  int speed;
  Ship(int radius, Color color, int speed) {
    this.radius = radius;
    this.color = color;
    this.speed = speed;
  }
  public WorldImage draw(int size) {
    return new CircleImage(this.radius, OutlineMode.SOLID, this.color);
  }
}

class StartingText implements IGamePieces {
    String text;
  StartingText(String text) {
    this.text = text;
  }
  public WorldImage draw(int size) {
    return new TextImage(this.text, size, FontStyle.BOLD, Color.BLACK);
  }
}





class ExamplesMyWorld {
  int ScreenHeight = 300;
  int ScreenWidth = 500;
  Bullet StartingBullet = new Bullet(2, Color.pink, 8);
  WorldImage Bullet = StartingBullet.draw(2);
  Ship Ship1 = new Ship((ScreenHeight / 30), Color.cyan, 4);
  WorldImage Ship = Ship1.draw(10);
  StartingText Text = new StartingText("Start");
  WorldImage Start = Text.draw(50);
/*
  boolean testDraw(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);
    return c.drawScene(s.placeImageXY(Start, 250, 250))
      && c.show();
  }
  */

  boolean testBigBang(Tester t) {
    World w = new StartingWorld(10);
    int worldWidth = 500;
    int worldHeight = 300;
    double tickRate = 1;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }

}