import tester.*;
interface IFunc<A, R> {
  R apply(A arg);
}

interface IList<T> {
  <R> R accept(IListVisitor<T, R> visitor);
}
class MtLo<T> implements IList<T> {
  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitEmpty();
  }
}
class ConsLo<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsLo(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  public <R> R accept(IListVisitor<T, R> visitor) {
    return visitor.visitCons(this);
  }
}
interface IListVisitor<T, R> extends IFunc<IList<T>, R> {
  R visitEmpty();
  R visitCons(ConsLo<T> cons);
}

class Course {
  String name;
  IList<Course> prereqs;

  Course(String name, IList<Course> prereqs) {
    this.name = name;
    this.prereqs = prereqs;
  }
  boolean hasPrereq(String prereqName) {
    HasPrereq hasPrereq = new HasPrereq(prereqName);
    return hasPrereq.apply(this);
  }
  int getDeepestPathLength() {
    DeepestPathLength deepestPathLength = new DeepestPathLength();
    return deepestPathLength.apply(this.prereqs) + 1;
  }
}

class DeepestPathLength implements IListVisitor<Course, Integer> {
  public Integer apply(IList<Course> courses) {
    return courses.accept(this);
  }
  public Integer visitEmpty() {
    return 0;
  }
  public Integer visitCons(ConsLo<Course> cons) {
    int firstPathLength = cons.first.getDeepestPathLength();
    int restPathLength = cons.rest.accept(this);

    if (firstPathLength > restPathLength) {
      return firstPathLength;
    } else {
      return restPathLength;
    }
  }
}

class HasPrereq implements IPred<Course> {
  String prereqName;

  HasPrereq(String prereqName) {
    this.prereqName = prereqName;
  }
  public Boolean apply(Course course) {
    return course.prereqs.accept(new HasPrereqVisitor(this.prereqName));
  }
}

class HasPrereqVisitor implements IListVisitor<Course, Boolean> {
  String targetName;

  HasPrereqVisitor(String targetName) {
    this.targetName = targetName;
  }
  public Boolean visitEmpty() {
    return false;
  }

  public Boolean visitCons(ConsLo<Course> cons) {
    return cons.first.name.equals(this.targetName) || cons.rest.accept(this);
  }

  public Boolean apply(IList<Course> courses) {
    return courses.accept(this);
  }
}

interface IPred<X> extends IFunc<X, Boolean> {
}

class CourseExamples {
  Course courseA = new Course("A", new ConsLo<Course>(new Course("B", new MtLo<Course>()), new MtLo<Course>()));
  Course courseB = new Course("B", new MtLo<Course>());
  Course courseC = new Course("C", new ConsLo<Course>(courseA, new ConsLo<Course>(courseB, new MtLo<Course>())));

  boolean testGetDeepestPathLength1(Tester t) {
    return t.checkExpect(courseA.getDeepestPathLength(), 2);
  }
  boolean testGetDeepestPathLength2(Tester t) {
    return t.checkExpect(courseB.getDeepestPathLength(), 1);
  }

  boolean testGetDeepestPathLength3(Tester t) {
    return t.checkExpect(courseC.getDeepestPathLength(), 3);
  }

  boolean testHasPrereq1(Tester t) {
    return t.checkExpect(courseA.hasPrereq("B"), true);
  }

  boolean testHasPrereq2(Tester t) {
    return t.checkExpect(courseC.hasPrereq("D"), false);
  }

  boolean testHasPrereq3(Tester t) {
    return t.checkExpect(courseB.hasPrereq("B"), false);
  }
}
