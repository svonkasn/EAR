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

  @OneToMany(mappedBy = "test")
  private List<Test> tests;

  @OneToMany(mappedBy = "studentTest")
  private List<StudentTest> studentTests;

  @OneToMany(mappedBy = "groupOfStudent")
  private List<GroupOfStudent> groupOfStudents;

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

  public List<StudentTest> getStudentTests() {
    return studentTests;
  }

  public void setStudentTests(List<StudentTest> studentTests) {
    this.studentTests = studentTests;
  }

  public List<Test> getTests() {
    return tests;
  }

  public void setTests(List<Test> tests) {
    this.tests = tests;
  }

  public List<GroupOfStudent> getGroupOfStudents() {
    return groupOfStudents;
  }

  public void setGroupOfStudents(List<GroupOfStudent> groupOfStudents) {
    this.groupOfStudents = groupOfStudents;
  }
}
