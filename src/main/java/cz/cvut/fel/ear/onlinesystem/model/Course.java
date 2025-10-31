package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Course extends AbstractEntity{

  @Basic(optional=false)
  @Column(name = "code",unique=true )
  private String code;
  @Basic(optional=false)
  @Column(name = "name", nullable=false )
  private String name;
  @Basic(optional=false)
  @Column(name = "description", nullable=false )
  private String description;

  @ManyToMany(mappedBy = "user")
  private Collection<User> users;

  @OneToMany(mappedBy = "course")
  private Collection<Test> tests;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public void setUsers(Collection<User> users) {
    this.users = users;
  }

  public Collection<Test> getTests() {
    return tests;
  }

  public void setTests(Collection<Test> tests) {
    this.tests = tests;
  }
}
