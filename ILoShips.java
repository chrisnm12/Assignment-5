import javalib.worldimages.*;

public interface ILoShips {
  WorldImage draw();
  ILoShips process();

}
class MTLoShips implements ILoShips {
  public WorldImage draw() {
    return new EmptyImage();
  }
  public ILoShips process() {
    return this;
  }
}

class ConsLoShips implements ILoShips {
  Bullet first;
  ILoBullets rest;

  ConsLoShips(Bullet first, ILoBullets rest) {
    this.first = first;
    this.rest = rest;
  }

  public WorldImage draw() {
    return this.first.draw().overlayImages(this.rest.draw());
  }
  public ILoShips process() {
    return new ConsLoShips(this.first.update(), this.rest.process());
  }
}

