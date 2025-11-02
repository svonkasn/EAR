package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

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

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name= "group_id")
  private GroupOfStudent groupOfStudent;

  @OneToMany(mappedBy = "user")
  private Set<StudentTest> studentTests = new HashSet<>();

  @ManyToMany(mappedBy = "teachers")
  private Set<Course> coursesTaught = new HashSet<>();

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

  public Set<StudentTest> getStudentTests() {
    return studentTests;
  }

  public void setStudentTests(Set<StudentTest> studentTests) {
    this.studentTests = studentTests;
  }
}
