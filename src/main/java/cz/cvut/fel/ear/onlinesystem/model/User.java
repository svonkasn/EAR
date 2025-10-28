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

}
