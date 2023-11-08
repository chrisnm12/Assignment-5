import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;

public interface IMyWorld {
}
abstract class AWorld extends World {
  int ammo;
  AWorld(int ammo) {
    this.ammo = ammo;
  }
}

class StartingWorld extends AWorld {
  StartingWorld(int ammo) {
    super(ammo);
  }
  public World onMouseClicked (Posn pos, String buttonname) {
    if (buttonname.equals("LeftButton")) {
      return new GameWorld(ammo);
    } else {
      return this;
    }
  }

  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("Click Start", 50);
    IGamePieces bulletText = new StartingText("Bullets: " + this.ammo, 25);
    WorldScene scene1 = scene.placeImageXY(text.draw(), 250, 150);
    return scene1.placeImageXY(bulletText.draw(), 70,280);
  }
}

class GameWorld extends AWorld {
  ILoBullets bullets;
  ILoShips ships;
  GameWorld(int ammo) {
    super(ammo);
    this.bullets = new MTLoBullets();
    this.ships = new MTLoShips();
  }
  public World onKeyEvent (String key) {
    if (this.ammo == 0) {
      return new EndWorld(0);
    } else if (key.equals(" ")) {
      this.ammo = this.ammo - 1;
      this.bullets = new ConsLoBullets(new Bullet(2, Color.pink, 8, new MyPosn(250, 299)), this.bullets);
    }
    return this;
  }
  @Override
  public World onTick() {
    this.bullets = this.bullets.process();

    if (this.ammo == 0) {
      return new EndWorld(0);
    }
    return this;
  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("Bullets: " + this.ammo, 25);
    IGamePieces ship = new Ship(10, Color.BLUE, 4, new MyPosn(250, 295));
    IGamePieces bullet = new Bullet(10, Color.pink, 8, new MyPosn(250, 299));
    WorldImage bulletsImage = this.bullets.draw();
    WorldScene scene1 = scene.placeImageXY(text.draw(),70, 280);
    WorldScene scene2 = scene1.placeImageXY(ship.draw(), 250, 295);
    return scene2.placeImageXY(bullet.draw(), 250, 299);
  }
}

class EndWorld extends AWorld {
  EndWorld(int ammo) {
    super(ammo);
  }
  // Have this scene differentiate based on whether the player wins or loses.
  @Override
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("Game Over", 50);
    return scene.placeImageXY(text.draw(),250, 150);
  }
}

class ExamplesMyWorld {
  int ScreenHeight = 300;
  int ScreenWidth = 500;
  Bullet StartingBullet = new Bullet(2, Color.pink, 8, new MyPosn(250, 299));
  WorldImage Bullet = StartingBullet.draw();
  Ship Ship1 = new Ship((ScreenHeight / 30), Color.cyan, 4, new MyPosn(0, 150));
  WorldImage Ship = Ship1.draw();
  StartingText Text = new StartingText("Start", 50);
  WorldImage Start = Text.draw();
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