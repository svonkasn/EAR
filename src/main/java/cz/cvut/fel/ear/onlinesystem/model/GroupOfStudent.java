package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
public class GroupOfStudent extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "numberOfParallel", nullable = false)
  private Integer numberOfParallel;

  @OneToMany(mappedBy = "groupOfStudent")
  private Set<User> users = new HashSet<>();

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "course_id")
  private Course course;

  @OneToMany(mappedBy = "group")
  private Set<Test> tests = new HashSet<>();

  public Integer getNumberOfParallel() {
    return numberOfParallel;
  }

  public void setNumberOfParallel(Integer numberOfParallel) {
    this.numberOfParallel = numberOfParallel;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Set<Test> getTests() {
    return tests;
  }
  public void setTests(Set<Test> tests) {
    this.tests = tests;
  }
}
