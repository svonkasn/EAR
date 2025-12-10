package cz.cvut.fel.ear.onlinesystem.model;

public enum Role {
  ADMIN, STUDENT, TEACHER;

  public String getDisplayName() {
    return name().toLowerCase();
  }
}
