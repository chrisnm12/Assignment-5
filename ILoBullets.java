import javalib.worldimages.*;

public interface ILoBullets {
  WorldImage draw();
  ILoBullets process();

}
class MTLoBullets implements ILoBullets {
  public WorldImage draw() {
    return new EmptyImage();
  }
  public ILoBullets process() {
    return this;
  }
}

class ConsLoBullets implements ILoBullets {
  Bullet first;
  ILoBullets rest;

  ConsLoBullets(Bullet first, ILoBullets rest) {
    this.first = first;
    this.rest = rest;
  }

  public WorldImage draw() {
    return this.first.draw().overlayImages(this.rest.draw());
  }
  public ILoBullets process() {
    return new ConsLoBullets(this.first.update(), this.rest.process());
  }
}
