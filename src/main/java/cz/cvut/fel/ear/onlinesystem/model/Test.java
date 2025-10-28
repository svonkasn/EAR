package cz.cvut.fel.ear.onlinesystem.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Test extends AbstractEntity {
  @Basic(optional = false)
  @Column(name = "title", nullable = false)
  private String title;

  @Basic(optional = false)
  @Column(name = "description", nullable = false)
  private String description;

  @Basic(optional = false)
  @Column(name = "maxPoints", nullable = false)
  private Integer maxPoints;

  @Basic(optional = false)
  @Column(name = "maxAttempts", nullable = false)
  private Integer maxAttempts;

  @ManyToOne
  @JoinColumn(name = "teacher")
  private User teacher;

  @ManyToOne
  @JoinColumn(name = "course")
  private Course course;

  @OneToMany(mappedBy = "test")
  private List<Termin> termins;

}
