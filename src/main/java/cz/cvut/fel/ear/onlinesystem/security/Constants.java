package cz.cvut.fel.ear.onlinesystem.security;

import cz.cvut.fel.ear.onlinesystem.model.Role;

public final class Constants {

  /**
   * Default user role.
   */
  public static final Role DEFAULT_ROLE = Role.STUDENT;

  /**
   * Username login form parameter.
   */
  public static final String USERNAME_PARAM = "username";

  private Constants() {
    throw new AssertionError();
  }
}

