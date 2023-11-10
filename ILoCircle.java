import javalib.worldimages.EmptyImage;
import javalib.worldimages.WorldImage;

public interface ILoCircle {
  ILoCircle moveAllBullets();
  ILoCircle moveAllShips();
  boolean isEmpty();
  Circle getFirst();
  ILoCircle getRest();
  ILoCircle removeOffscreen(int screenWidth, int screenHeight);
  WorldImage drawAll();
  ILoCircle removeHit(Circle other);
  ILoCircle append(ILoCircle other);
}

class MtLoCircle implements ILoCircle {
  public boolean isEmpty() {
    return true;
  }
  public Circle getFirst() {
    return null;
  }
  public ILoCircle getRest() {
    return null;
  }
  public ILoCircle removeOffscreen(int screenWidth, int screenHeight) {
    return this;
  }
  public WorldImage drawAll() {
    return new EmptyImage();
  }
  public ILoCircle removeHit(Circle other) {
    return this;
  }
  public ILoCircle moveAllBullets() {
    return this;
  }
  public ILoCircle moveAllShips() {
    return this;
  }
  public ILoCircle append(ILoCircle other) {
    return other;
  }
}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }
  public boolean isEmpty() {
    return false;
  }
  public Circle getFirst() {
    return this.first;
  }
  public ILoCircle getRest() {
    return this.rest;
  }
  public ILoCircle removeOffscreen(int screenWidth, int screenHeight) {
    if (first.isCenterOffscreen(screenWidth, screenHeight)) {
      return rest.removeOffscreen(screenWidth, screenHeight);
    } else {
      return new ConsLoCircle(first, rest.removeOffscreen(screenWidth, screenHeight));
    }
  }
  public WorldImage drawAll() {
    return this.first.draw().overlayImages(this.rest.drawAll());
  }
  public ILoCircle moveAllBullets() {
    return new ConsLoCircle(first.moveY().moveX(), rest.moveAllBullets());
  }
  public ILoCircle moveAllShips() {
    return new ConsLoCircle(first.moveX(), rest.moveAllShips());
  }
  public ILoCircle removeHit(Circle other) {
    if (other.isHit(this.first)) {
      return this.rest.removeHit(other);
    } else {
      return new ConsLoCircle(this.first, this.rest.removeHit(other));
    }
  }
  public ILoCircle append(ILoCircle other) {
    return new ConsLoCircle(this.first, this.rest.append(other));
  }
}

