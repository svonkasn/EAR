package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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

  @ManyToMany
  @JoinTable(
          name = "teacher_courses",
          joinColumns = @JoinColumn(name = "course_id"),
          inverseJoinColumns = @JoinColumn(name = "teacher_id")
  )
  private Set<User> teachers = new HashSet<>();

  @OneToMany(mappedBy = "course")
  private Set<Test> tests = new HashSet<>();

  @OneToMany(mappedBy = "course")
  private Set<GroupOfStudent> groups = new HashSet<>();

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

  public Set<Test> getTests() {
    return tests;
  }

  public void setTests(Set<Test> tests) {
    this.tests = tests;
  }

  public Set<User> getTeachers() {
    return teachers;
  }
  public void setTeachers(Set<User> teachers) {
    this.teachers = teachers;
  }
}
