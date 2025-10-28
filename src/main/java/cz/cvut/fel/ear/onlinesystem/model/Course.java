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

}
