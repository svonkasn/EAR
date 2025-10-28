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

}
