package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;


@Entity
public class Termin extends AbstractEntity{
  @Basic(optional = false)
  @Column(name = "startTime", nullable = false)
  private LocalDate startTime;
  @Basic(optional = false)
  @Column(name = "endTime", nullable = false)
  private LocalDate endTime;

  @ManyToOne
  @JoinColumn(name = "test")
  private Test test;

  @OneToMany(mappedBy = "studentTest")
  private List<StudentTest> studentTests;

  @ManyToMany
  @JoinTable(name = "gropuOfStudent",
  joinColumns = @JoinColumn(name = "group_id"),
  inverseJoinColumns = @JoinColumn(name = "termin_id"))
  private List<GroupOfStudent> groups;

  public LocalDate getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDate startTime) {
    this.startTime = startTime;
  }

  public LocalDate getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDate endTime) {
    this.endTime = endTime;
  }

  public Test getTest() {
    return test;
  }

  public void setTest(Test test) {
    this.test = test;
  }

  public List<StudentTest> getStudentTests() {
    return studentTests;
  }

  public void setStudentTests(List<StudentTest> studentTests) {
    this.studentTests = studentTests;
  }

  public List<GroupOfStudent> getGroups() {
    return groups;
  }

  public void setGroups(List<GroupOfStudent> groups) {
    this.groups = groups;
  }
}
