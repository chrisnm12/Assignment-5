
interface ILoCircle {
  ILoCircle moveAll();
  boolean isEmpty();
  Circle getFirst();
  ILoCircle getRest();
  ILoCircle removeOffscreen(int screenWidth, int screenHeight);
}

class MtLoCircle implements ILoCircle {
  public ILoCircle moveAll() {
    return this;
  }
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
}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }
  public ILoCircle moveAll() {
    return new ConsLoCircle(this.first.move(), this.rest.moveAll());
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
    // Check if the first circle is offscreen
    if (first.isCenterOffscreen(screenWidth, screenHeight)) {
      // Recursively remove offscreen circles from the rest of the list
      return rest.removeOffscreen(screenWidth, screenHeight);
    } else {
      // Keep the first circle and process the rest of the list
      return new ConsLoCircle(first, rest.removeOffscreen(screenWidth, screenHeight));
    }
  }
}
