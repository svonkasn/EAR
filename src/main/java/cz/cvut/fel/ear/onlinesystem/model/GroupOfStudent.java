package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class GroupOfStudent extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "numberOfParalel", nullable = false)
  private Integer numberOfParalel;

  @OneToMany(mappedBy = "groupOfStudent")
  private Collection<User> users;

  @ManyToOne
  @JoinColumn(name = "course")
  private Course course;

  @ManyToMany(mappedBy = "termins")
  private Collection<Termin> termins;

  public Integer getNumberOfParalel() {
    return numberOfParalel;
  }

  public void setNumberOfParalel(Integer numberOfParalel) {
    this.numberOfParalel = numberOfParalel;
  }

  public Collection<User> getUsers() {
    return users;
  }

  public void setUsers(Collection<User> users) {
    this.users = users;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Collection<Termin> getTermins() {
    return termins;
  }

  public void setTermins(Collection<Termin> termins) {
    this.termins = termins;
  }
}
