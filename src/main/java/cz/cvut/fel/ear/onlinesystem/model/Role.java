package cz.cvut.fel.ear.onlinesystem.model;

public enum Role {
  ADMIN("admin"), STUDENT("student"), TEACHER("teacher");

  private final String name;
  Role(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Role{" +
      "name='" + name + '\'' +
      '}';
  }
}
