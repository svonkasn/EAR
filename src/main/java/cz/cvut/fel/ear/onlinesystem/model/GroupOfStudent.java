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
}
