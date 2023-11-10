import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;
import java.util.Random;

import java.awt.*;

abstract class AWorld extends World {
  int ammo;
  AWorld(int ammo) {
    this.ammo = ammo;
  }
}

class StartingWorld extends AWorld {
  Random rand;
  StartingWorld() {
    this(10, new Random());
  }
  StartingWorld(int initialAmmo, Random rand) {
    super(initialAmmo);
    this.rand = rand;
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
  Random rand;
  ILoCircle bullets;
  ILoCircle ships;
  int shipSpawnRate;
  int tickCount;
  int score;
  GameWorld(int ammo) {
    this(ammo, new Random());
  }
  GameWorld(int ammo, Random rand) {
    super(ammo);
    this.rand = rand;
    this.bullets = new MtLoCircle();
    this.ships = new MtLoCircle();
    this.shipSpawnRate = 15;
    this.tickCount = 0;
    this.score = 0;
  }
  public World onKeyEvent(String key) {
    if (key.equals(" ")) {
      if (this.ammo > 0) {
        this.ammo = this.ammo - 1;
        this.bullets = new ConsLoCircle(new Circle(new MyPosn(0, 0), new MyPosn(0, 8), Color.pink, 2, 1), this.bullets);
      }
    }
    return this;
  }
  @Override
  public World onTick() {
    this.bullets = this.bullets.moveAllBullets();
    this.ships = this.ships.moveAllShips();
    this.tickCount = this.tickCount + 1;

    this.removeHitShips();
    this.bullets = this.bullets.removeOffscreen(750, 300);
    this.ships = this.ships.removeOffscreen(750, 300);

    if (tickCount % shipSpawnRate == 0) {
      this.ships = this.addShip();
    }
    return this.checkForEnd();
  }

  public ILoCircle addShip() {
    Random rand = new Random();
    int upperY = 250;
    int lowerY = 70;
    int randX = rand.nextInt(2);
    int randY = lowerY + rand.nextInt(upperY - lowerY + 1);

    int xPosition;
    MyPosn newVelocity;
    if (randX == 0) {
      xPosition = -250;
      newVelocity = new MyPosn(4, 0);
    } else {
      xPosition = 250;
      newVelocity = new MyPosn(-4, 0);
    }
    return new ConsLoCircle(new Circle(new MyPosn(xPosition, randY), newVelocity, Color.cyan, 10, 0), this.ships);
  }
  void removeHitShips() {
    removeHitShipsHelper(this.ships);
  }
  void removeHitShipsHelper(ILoCircle ships) {
    if (!ships.isEmpty()) {
      Circle currentShip = ships.getFirst();
      ILoCircle restOfShips = ships.getRest();

      Circle hitBullet = currentShip.isHitAny(this.bullets);
      if (hitBullet != null) {
        this.ships = this.ships.removeHit(currentShip);
        this.bullets = this.bullets.removeHit(hitBullet);

        ILoCircle explodedBullets = hitBullet.explode();
        this.bullets = this.bullets.append(explodedBullets);

        this.incrementScore();
      }
      removeHitShipsHelper(restOfShips);
    }
  }

  public void incrementScore() {
    this.score += 1;
  }

  public World checkForEnd() {
    if (this.ammo <= 0 && this.bullets.isEmpty()) {
      return new EndWorld(this.ammo, new Random(), this.score);
    } else {
      return this;
    }
  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("Bullets: " + this.ammo, 25);
    IGamePieces scoreText = new StartingText("Score: " + this.score, 25);
    IGamePieces ship = new Ship(5, Color.BLUE, 4, new MyPosn(0, 0));
    IGamePieces bullet = new Bullet(2, Color.pink, 8, new MyPosn(0, 0));
    WorldImage bullets = this.bullets.drawAll();
    WorldImage ships = this.ships.drawAll();
    WorldScene scene1 = scene.placeImageXY(text.draw(),70, 280);
    WorldScene scene2 = scene1.placeImageXY(scoreText.draw(), 430, 280);
    WorldScene scene3 = scene2.placeImageXY(ship.draw(), 250, 295);
    WorldScene scene4 = scene3.placeImageXY(ships, 250, 300);
    return scene4.placeImageXY(bullets, 250, 299);
  }
}

class EndWorld extends AWorld {
  Random rand;
  int endScore;
  EndWorld(int ammo, Random rand, int endScore) {
    super(ammo);
    this.rand = rand;
    this.endScore = endScore;
  }

  @Override
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();
    IGamePieces text = new StartingText("Game Over", 50);
    IGamePieces scoreText = new StartingText("Final Score: " + this.endScore, 25);
    WorldScene scene1 = scene.placeImageXY(text.draw(),250, 150);
    return scene1.placeImageXY(scoreText.draw(),250, 200);
  }
}
