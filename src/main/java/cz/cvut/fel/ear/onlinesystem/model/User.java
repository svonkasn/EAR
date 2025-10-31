package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class User extends AbstractEntity{

  @Basic(optional = false)
  @Column(name = "username", nullable = false)

  private String username;
  @Basic(optional = false)
  @Column(name = "password", nullable = false)

  private String password;
  @Basic(optional = false)
  @Column(name = "email", nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;

  @ManyToOne
  @JoinColumn(name= "groupOfStudent")
  private GroupOfStudent groupOfStudent;

  @OneToMany(mappedBy = "studentsTest")
  private Collection<StudentTest> studentTests;

  @ManyToMany
  @JoinTable(name = "course",
  joinColumns = @JoinColumn(name = "teacher_id"),
  inverseJoinColumns = @JoinColumn(name = "course_id"))
  private Collection<Course> courses;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public GroupOfStudent getGroupOfStudent() {
    return groupOfStudent;
  }

  public void setGroupOfStudent(GroupOfStudent groupOfStudent) {
    this.groupOfStudent = groupOfStudent;
  }

  public Collection<StudentTest> getStudentTests() {
    return studentTests;
  }

  public void setStudentTests(Collection<StudentTest> studentTests) {
    this.studentTests = studentTests;
  }

  public Collection<Course> getCourses() {
    return courses;
  }

  public void setCourses(Collection<Course> courses) {
    this.courses = courses;
  }
}
